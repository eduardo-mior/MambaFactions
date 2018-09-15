package com.massivecraft.factions.engine;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsEnteredInAttack;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;

public class EngineSobAtaque extends Engine{

	private static EngineSobAtaque i = new EngineSobAtaque();
	public static EngineSobAtaque get() { return i; }
	
	public static ConcurrentHashMap<Chunk, Long> underattack;
	public static ConcurrentHashMap<String, Faction> factionattack;
	public static ConcurrentHashMap<Chunk, Location> infoattack;
	static { EngineSobAtaque.underattack = new ConcurrentHashMap<Chunk, Long>(); }
	static { EngineSobAtaque.factionattack = new ConcurrentHashMap<String, Faction>(); }
	static { EngineSobAtaque.infoattack = new ConcurrentHashMap<Chunk, Location>(); }
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e){
		Faction faction = BoardColl.get().getFactionAt(PS.valueOf(e.getLocation()));
		
		Chunk c = e.getLocation().getChunk();
		
		if (underattack.containsKey(c)) return;
		else if (faction.getId().equals(Factions.ID_SAFEZONE)) return;
		else if (faction.getId().equals(Factions.ID_WARZONE)) return;
		else if (faction.getId().equals(Factions.ID_NONE)) return;
		
		boolean already = factionattack.containsKey(faction.getName());
		EventFactionsEnteredInAttack event = new EventFactionsEnteredInAttack(faction, e.getLocation(), already, e);
		event.run();
		if (event.isCancelled()) return;
		underattack.put(c, System.currentTimeMillis());
		factionattack.put(faction.getName(), faction);
		infoattack.put(c, e.getLocation());
		desligarFlyDosPlayers(faction);
	}
	
	private void desligarFlyDosPlayers(Faction f) {
		for (Player p : f.getOnlinePlayers()) {
			if (!p.hasPermission("factions.voar.bypass") && p.getAllowFlight()) {
				p.sendMessage("§cSua facção entrou em ataque portanto seu modo voar foi automaticamente desabilitado.");
				p.setAllowFlight(false);
				p.setFlying(false);
			}
		}
	}
	
	public void aumentarSegundos(Chunk c) {
		underattack.replace(c, underattack.get(c) + 1);
	}
	
	public void remover(Chunk c, Faction f) {
		underattack.remove(c);
		factionattack.remove(f.getName()); 
		infoattack.remove(c);
	}
	
	public long getTime(Chunk c) {
		return EngineSobAtaque.underattack.get(c);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void desfazer(EventFactionsDisband e) {
		if (factionattack.containsKey(e.getFaction().getName())) {
			e.setCancelled(true);
			e.getSender().sendMessage("§cVocê não pode desfazer a sua facção enquanto ela estiver sobre ataque!");
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void aoExecutarComando(PlayerCommandPreprocessEvent e) {
		
		Player p = e.getPlayer();
		MPlayer mp = MPlayer.get(p);
		Faction fac = mp.getFaction();
		
		if (factionattack.containsKey(fac.getName())) {
	        String cmd = e.getMessage().toLowerCase();
			if (cmd.startsWith("/f unclaim") || cmd.startsWith("/f desproteger") ||cmd.startsWith("/f abandonar")) { 
				e.setCancelled(true);
				p.sendMessage("§cVocê não pode controlar territórios enquanto estiver sobre ataque!");
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void terras(EventFactionsChunksChange e) {
		MPlayer mp = e.getMPlayer();
		if (factionattack.containsKey(mp.getFaction().getName())) {
			e.getSender().sendMessage("§cVocê não pode controlar territórios enquanto estiver sobre ataque!");
			e.setCancelled(true);
			return;
		}
		for (PS ps : e.getChunks()) {
			Chunk c = PS.asBukkitChunk(ps);
			if (underattack.containsKey(c)) {
				underattack.remove(c);
				infoattack.remove(c);
				Faction atual = BoardColl.get().getFactionAt(ps);
				for (Chunk chunk : infoattack.keySet()) {
					Faction at = BoardColl.get().getFactionAt(PS.valueOf(chunk));
					if (at.equals(atual)) return;
				}
				factionattack.remove(atual.getName());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakSpawner(BlockBreakEvent e) {
		
		if (e.getBlock().getType() == Material.MOB_SPAWNER) {
			Player p = e.getPlayer();
			MPlayer mp = MPlayer.get(p);
			if(!mp.hasFaction()) {
				return;
			}
			
			Faction fac = BoardColl.get().getFactionAt(PS.valueOf(e.getBlock()));
			
			if (factionattack.containsKey(fac.getName())) {
				p.sendMessage("§cVocê não pode remover §lgeradores §cenquanto estiver sob ataque!");
				e.setCancelled(true);
				return;
			}
		}
		
		if (e.getBlock().getType() == Material.BEACON) {
			Player p = e.getPlayer();
			MPlayer mp = MPlayer.get(p);
			if(!mp.hasFaction()) {
				return;
			}
			
			Faction fac = BoardColl.get().getFactionAt(PS.valueOf(e.getBlock()));
			
			if (factionattack.containsKey(fac.getName())) {
				p.sendMessage("§cVocê não pode remover §lsinalizadores §cenquanto estiver sob ataque!");
				e.setCancelled(true);
				return;
			}
		}
	}
}