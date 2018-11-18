package com.massivecraft.factions.engine;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.ps.PS;

public class EngineChunkChange extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static EngineChunkChange i = new EngineChunkChange();
	public static EngineChunkChange get() { return i; }

	// -------------------------------------------- //
	// CHUNK CHANGE: ALLOWED
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onChunksChange(EventFactionsChunksChange event)
	{
		// For security reasons we block the chunk change on any error since an error might block security checks from happening.
		try
		{
			onChunksChangeInner(event);
		}
		catch (Throwable throwable)
		{
			event.setCancelled(true);
			throwable.printStackTrace();
		}
	}

	public void onChunksChangeInner(EventFactionsChunksChange event)
	{
		// Pegando o MPlyaer
		final MPlayer mplayer = event.getMPlayer();

		// Verificando se o MPlayer é um admin
		if (mplayer.isOverriding()) return;
		
		// Pegando a facção que esta claimando o local, as chunks claimadas e as facções das chunks
		final Faction newFaction = event.getNewFaction();
		final Set<PS> chunks = event.getChunks();
		final Map<Faction, Set<PS>> currentFactionChunks = event.getOldFactionChunks();
		final Set<Faction> currentFactions = currentFactionChunks.keySet();
		
		// Verificando se a facção tem poder infinito
		if (newFaction.getFlag(MFlag.getFlagInfpower())) return;
		
		// Verificando se o player tem permissão para usar o comando
		if (!MPerm.getPermTerritory().has(mplayer, newFaction, true)) {
			event.setCancelled(true);
			return;
		}		

		// Verificando se o player esta realmente claimando
		if (newFaction.isNormal()) {
			
			// Verificando se a facção possui o minimo de membros requeridos
			if (newFaction.getMPlayers().size() < MConf.get().claimsRequireMinFactionMembers) {
				mplayer.msg("§cA sua facção precisa ter no minimo %s membros para poder conquistar territórios.", MConf.get().claimsRequireMinFactionMembers);
				event.setCancelled(true);
				return;
			}
			
			// Verificando se a compra de terrenos esta habilitada naquele mundo
			for (PS chunk : chunks) {
				String worldId = chunk.getWorld();
				if (!MConf.get().worldsClaimingEnabled.contains(worldId)) {
					mplayer.msg("§cA compra de territórios esta desabilitada neste mundo.");
					event.setCancelled(true);
					return;
				}
			}
			
			// Verificando se a facção não claimou além do limite permitido
			int totalLandCount = newFaction.getLandCount() + chunks.size();
			if (MConf.get().claimedLandsMax > 0 && totalLandCount > MConf.get().claimedLandsMax) {
				mplayer.msg("§cLimite máximo de terras atingido ("+MConf.get().claimedLandsMax+"§c)! Você não pode mais conquistar territórios.");
				event.setCancelled(true);
				return;
			}
			
			// Verificando se a facção tem poder necessario para claimar as terras
			if (totalLandCount > newFaction.getPowerRounded()) {
				mplayer.msg("§cA sua facção não tem poder suficiente para poder conquistar mais territórios.");
				event.setCancelled(true);
				return;
			}
			
			// Verificando se a facção já claimou além do número de mundos permitidos
			if (MConf.get().claimedWorldsMax > 0) {
				Set<String> oldWorlds = newFaction.getClaimedWorlds();
				Set<String> newWorlds = PS.getDistinctWorlds(chunks);
				Set<String> worlds = new MassiveSet<>();
				worlds.addAll(oldWorlds);
				worlds.addAll(newWorlds);
				if (!oldWorlds.containsAll(newWorlds) && worlds.size() > MConf.get().claimedWorldsMax) {
					String worldsMax = MConf.get().claimedWorldsMax == 1 ? "mundo diferente." : "mundos diferentes.";
					mplayer.msg("§cVocê só pode conquistar terras em %d %s", MConf.get().claimedWorldsMax, worldsMax);
					event.setCancelled(true);
					return;
				}
			}
			
			// Verificando se os claims precisam estar conectados, e verificando se a facção já claimou algo naquele mundo
			if (MConf.get().claimsMustBeConnected && newFaction.getLandCountInWorld(chunks.iterator().next().getWorld()) > 0) {
				
				// Verificando se os claims não estão conectados
				if (!BoardColl.get().isAnyConnectedPs(chunks, newFaction)) {
					
					// Verificando se o sistema de proteger terras de inimigos mesmo não estando conectadas esta ativado
					if (!MConf.get().claimsCanBeUnconnectedIfOwnedByOtherFaction) {
						mplayer.msg("§cVocê só poder conquistar terras que estejam conectadas às suas.");
						event.setCancelled(true);
						return;
					}
					
					// Verificando se alguma das facções claimadas é normal
					boolean containsNormalFaction = BoardColl.containsNormalFaction(currentFactions);
					if (!containsNormalFaction) {
						mplayer.msg("§cVocê só poder conquistar terras que estejam conectadas às suas ou que sejam pertencentes a outras facções.");
						event.setCancelled(true);
						return;
					}
				}
			}
			
			// Percorrendo todas as chunks e facções claimandas
			for (Entry<Faction, Set<PS>> entry : currentFactionChunks.entrySet()) {
				Faction oldFaction = entry.getKey();
				Set<PS> oldChunks = entry.getValue();

				// Verificando se a facção não é a zona livre
				// Caso a facção não seja a zona livre, então o terreno esta sendo dominado de uma facção para outra
				if (!oldFaction.isNone()) {

					// Verificando se a facção não possui relações de trégua ou aliança
					if (newFaction.getRelationTo(oldFaction) == Rel.ALLY) {
						mplayer.msg("§cVocê não pode conquistar este território devido a sua relação com o atual dono do território. ");
						event.setCancelled(true);
						return;
					}
	
					// Verificando se a facção inimiga esta realmente com poder baixo
					if (oldFaction.getPowerRounded() > oldFaction.getLandCount() - oldChunks.size()) {
						mplayer.msg("§eA facção §f[%s§f]§e é dona deste território e é forte o bastante para mantê-lo.", oldFaction.getName());
						event.setCancelled(true);
						return;
					}
	
					// Verificando se a facção começou a dominar pela borda
					if (!BoardColl.get().isAnyBorderPs(chunks)) {
						mplayer.msg("§cVocê deve começar a dominar as terras pelas bordas não pelo meio.");
						event.setCancelled(true);
						return;
					}
				}
			}
			
			// Pegando todas as chunks próximas
			Set<PS> nearbyChunks = BoardColl.getNearbyChunks(chunks, MConf.get().claimMinimumChunksDistanceToOthers);
			nearbyChunks.removeAll(chunks);
			
			// Pegando todas as facções próximas e pegando a permissão para claimar próximo
			Set<Faction> nearbyFactions = BoardColl.getDistinctFactions(nearbyChunks);
			nearbyFactions.remove(FactionColl.get().getNone());
			nearbyFactions.remove(newFaction);
			MPerm claimnear = MPerm.getPermClaimnear();
			
			// Percorrendo todos as chunks claimadas
			for (Entry<Faction, Set<PS>> entry : currentFactionChunks.entrySet()) {
				
				// Verificando se a facção antiga não é a zona livre
				Faction oldFaction = entry.getKey();
				if (oldFaction.isNone()) {
					
					// Percorrendo todas as facções próximas
					for (Faction nearbyFaction : nearbyFactions) {
	
						// Verificando se a facção tem permissão para claimar próximo
						if (!claimnear.has(newFaction, nearbyFaction)) {
							mplayer.message(claimnear.createDeniedMessage(mplayer, nearbyFaction));
							event.setCancelled(true);
							return;
						}
					}
				}
			}
		}
	}
}