package com.massivecraft.factions.engine;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.TemporaryBoard;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.factions.event.EventFactionsEnteredInAttack;
import com.massivecraft.factions.event.EventFactionsFinishAttack;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;

public class EngineSobAtaque extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineSobAtaque i = new EngineSobAtaque();
	public static EngineSobAtaque get() { return i; }
	
	// -------------------------------------------- //
	// TEMPORARY INFO
	// -------------------------------------------- //
	
	public static Map<Chunk, Long> underattack = new ConcurrentHashMap<>();
	public static Map<Chunk, Location> infoattack = new ConcurrentHashMap<>();
	public static Set<Faction> facs = new HashSet<>();
	
	// -------------------------------------------- //
	// ADD FACTION IN ATACK
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExplode(EntityExplodeEvent e)
	{
		// Pegando a chunk
		Chunk c = e.getLocation().getChunk();
		
		// Verificando se a chunk já esta em ataque
		if (underattack.containsKey(c)) {
			underattack.replace(c, System.currentTimeMillis() + 300000L);
			return;
		}
		
		// Pegando o PS
		PS ps = PS.valueOf(e.getLocation());
				
		// Pegando a facção e verificando se elá é uma facção padrão
		Faction faction = BoardColl.get().getFactionAt(ps);
		if (faction.getId().equals(Factions.ID_SAFEZONE)) return;
		else if (faction.getId().equals(Factions.ID_WARZONE)) return;
		else if (faction.getId().equals(Factions.ID_NONE)) return;
		
		// Verificando se o claim não é temporario
		else if (TemporaryBoard.get().isTemporary(ps)) return;
		
		// Colocando a chunk em ataque
		underattack.put(c, System.currentTimeMillis() + 300000L);
		infoattack.put(c, e.getLocation());
		
		// Verificação se a facção já esta em ataque
		if (!faction.isInAttack()) 
		{
			EventFactionsEnteredInAttack event = new EventFactionsEnteredInAttack(faction, e);
			event.run();
			faction.setInAttack(true);
			facs.add(faction);
			EngineFly.disableFlyFaction(faction);
		}
	}
	
	// -------------------------------------------- //
	// REMOVE CHUNK IN ATTACK
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void terras(EventFactionsChunksChange e)
	{
		MPlayer mp = e.getMPlayer();
		Faction faction = mp.getFaction();
		for (Entry<PS, Faction> entry : e.getOldChunkFaction().entrySet()) 
		{
			Faction atual = entry.getValue();
			if (!atual.isNone() && atual.isInAttack()) 
			{
				PS ps = entry.getKey();
				Chunk c = PS.asBukkitChunk(ps);
				if (underattack.containsKey(c)) 
				{
					underattack.remove(c);
					infoattack.remove(c);
					for (Chunk chunk : infoattack.keySet()) 
					{
						Faction at = BoardColl.get().getFactionAt(PS.valueOf(chunk));
						if (at.getId().equals(atual.getId())) return;
					}
				}
				facs.remove(atual);
				faction.setInAttack(false);
			}
		}
	}
	
	// -------------------------------------------- //
	// BLOCK REMOVE SPAWNERS AND BEACONS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void aoQuebrar(BlockBreakEvent e) {
		Block b = e.getBlock();
		
		if (b.getType() == Material.MOB_SPAWNER) {
			if (BoardColl.get().getFactionAt(PS.valueOf(b)).isInAttack()) {
				e.getPlayer().sendMessage("§cVocê não pode remover §lgeradores §cenquanto a facção estiver sob ataque!");
				e.setCancelled(true);
				return;
			}
		}
		
		if (b.getType() == Material.BEACON) {
			if (BoardColl.get().getFactionAt(PS.valueOf(b)).isInAttack()) {
				e.getPlayer().sendMessage("§cVocê não pode remover §lsinalizadores §cenquanto a facção estiver sob ataque!");
				e.setCancelled(true);
				return;
			}
		}
	}
	
	// -------------------------------------------- //
	// METHODS TO HANDLE
	// -------------------------------------------- //
	
	public void remove(Chunk c, Faction faction) {
		underattack.remove(c);
		infoattack.remove(c);
		for (Chunk chunk : infoattack.keySet()) {
			Faction at = BoardColl.get().getFactionAt(PS.valueOf(chunk));
			if (at.getId().equals(faction.getId())) return;
		}
		facs.remove(faction);
		faction.setInAttack(false);
		EventFactionsFinishAttack event = new EventFactionsFinishAttack(faction);
		event.run();
	}
	
}