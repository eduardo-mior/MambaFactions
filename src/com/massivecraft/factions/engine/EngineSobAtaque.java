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
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;

public class EngineSobAtaque extends Engine{

	private static EngineSobAtaque i = new EngineSobAtaque();
	public static EngineSobAtaque get() { return i; }
	
	public static ConcurrentHashMap<Chunk, Long> underattack;
	public static ConcurrentHashMap<Faction, Integer> factionattack;
	public static ConcurrentHashMap<Chunk, Location> infoattack;
	static { EngineSobAtaque.underattack = new ConcurrentHashMap<Chunk, Long>(); }
	static { EngineSobAtaque.factionattack = new ConcurrentHashMap<Faction, Integer>(); }
	static { EngineSobAtaque.infoattack = new ConcurrentHashMap<Chunk, Location>(); }
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e){
		Faction faction = BoardColl.get().getFactionAt(PS.valueOf(e.getLocation()));
		
		Chunk c = e.getLocation().getChunk();
		
		if (underattack.containsKey(c)) return;
		else if (faction.getId().equals(Factions.ID_SAFEZONE)) return;
		else if (faction.getId().equals(Factions.ID_WARZONE)) return;
		else if (faction.getId().equals(Factions.ID_NONE)) return;
		
		underattack.put(c, System.currentTimeMillis());
		factionattack.put(faction, 0);
		infoattack.put(c, e.getLocation());
	}
	
	public void aumentarSegundos(Chunk c) {
		underattack.replace(c, underattack.get(c) + 1);
	}
	
	public void remover(Chunk c, Faction f) {
		underattack.remove(c);
		factionattack.remove(f); 
		infoattack.remove(c);
	}
	
	public long getTime(Chunk c) {
		return EngineSobAtaque.underattack.get(c);
	}
	
	@EventHandler
	public void desfazer(EventFactionsDisband e) {
		if(factionattack.get(e.getFaction()) != null) {
			e.setCancelled(true);
			e.getSender().sendMessage("§cVocê não pode desfazer sua facção enquanto estiver sobre ataque!");
			return;
		}
	}
	
	@EventHandler
	public void abandonarTerras(InventoryOpenEvent e) {
		if (e.getInventory().getName().equals("§8Abandonar todas as terras")) {
			Player p = (Player) e.getPlayer();
			MPlayer mp = MPlayer.get(p);
			Faction fac = mp.getFaction();
			if (factionattack.containsKey(fac)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void aoExecutarComando(PlayerCommandPreprocessEvent e) {
		
		Player p = e.getPlayer();
		MPlayer mp = MPlayer.get(p);
		Faction fac = mp.getFaction();
		
		if(factionattack.containsKey(fac)) {
	        String cmd = e.getMessage().toLowerCase();
			if (cmd.startsWith("/f unclaim") || cmd.startsWith("/f desproteger") ||cmd.startsWith("/f abandonar")) { 
				e.setCancelled(true);
				p.sendMessage("§cVocê não pode controlar territórios enquanto estiver sobre ataque!");
				return;
			}
			
			else if (cmd.startsWith("/f nome") || cmd.startsWith("/f name") || cmd.startsWith("/f renomear") || cmd.startsWith("/f rename")) { 
				e.setCancelled(true);
				p.sendMessage("§cVocê não pode alterar o nome da facção enquanto estiver sobre ataque!");
				return;
			}
			
			else if (cmd.startsWith("/f sair") || cmd.startsWith("/f leave") || cmd.startsWith("/f deixar") || cmd.startsWith("/f abandonar")) { 
				if (mp.getFaction().getMPlayers().size() == 1) {
					e.setCancelled(true);
					p.sendMessage("§cVocê não pode sair da sua facção pois você é o ultimo membro da facção e a facção esta sobre ataque!");
					return;
				}
			}
		}
	}
	
	public void terras(EventFactionsChunksChange e) {
		if(factionattack.get(e.getNewFaction()) != null) {
			e.setCancelled(true);
			e.getSender().sendMessage("§cVocê não pode controlar territórios enquanto estiver sobre ataque!");
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBreakSpawner(BlockBreakEvent e) {
		if(!e.getBlock().getType().equals(Material.MOB_SPAWNER)) {
			return;
		}
		
		Player p = e.getPlayer();
		MPlayer mp = MPlayer.get(p);
		if(!mp.hasFaction()) {
			return;
		}
		
		Faction fac = BoardColl.get().getFactionAt(PS.valueOf(e.getBlock()));
		
		if(factionattack.get(fac) != null) {
			p.sendMessage("§cVocê não pode remover §lgeradores §cenquanto estiver sob ataque!");
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBreakBeacon(BlockBreakEvent e) {
		if(!e.getBlock().getType().equals(Material.BEACON)) {
			return;
		}
		
		Player p = e.getPlayer();
		MPlayer mp = MPlayer.get(p);
		if(!mp.hasFaction()) {
			return;
		}
		Faction fac = BoardColl.get().getFactionAt(PS.valueOf(e.getBlock()));
		
		if(factionattack.get(fac) != null) {
			p.sendMessage("§cVocê não pode remover §lsinalizadores §cenquanto estiver sob ataque!");
			e.setCancelled(true);
			return;
		}
	}
	
}