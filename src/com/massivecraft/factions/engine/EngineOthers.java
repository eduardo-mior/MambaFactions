package com.massivecraft.factions.engine;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.OthersUtil;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;

public class EngineOthers extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineOthers i = new EngineOthers();
	public static EngineOthers get() { return i; }
	
	// -------------------------------------------- //
	// TEMPORARY INFO
	// -------------------------------------------- //
	
	private static List<String> LIST = new ArrayList<>();
	
	// -------------------------------------------- //
	// ANNOUNCE WHEN KILL PLAYER
	// -------------------------------------------- //
	
	@EventHandler(ignoreCancelled = true)
	public void aoMorrerAnunciarMorte(PlayerDeathEvent e) {
	    if (MConf.get().anunciarMorteAoMorrer) {
	    	Player p = e.getEntity();
	    	Entity en = p.getKiller();
	    	if (en instanceof Player) {
	    		Player k = p.getKiller().getPlayer();
	    		MPlayer mp = MPlayer.get(p);
	    		MPlayer mk = MPlayer.get(k);
	    		if (mp == null || mk == null) return;
	    		String facp = mp.getFaction().isNone() ? "" : "§3[" + mp.getRole().getPrefix() + mp.getFaction().getName() + "§3] ";
	    		String fack = mk.getFaction().isNone() ? "" : "§3[" + mk.getRole().getPrefix() + mk.getFaction().getName() + "§3] ";
	    		for (Player target : OthersUtil.getPlayersNearby(p, MConf.get().distanciaDoAnuncioEmBlocos)) {
	    			target.sendMessage(facp + p.getName() + "§c foi morto por " + fack + k.getName());
	    		}
	    	}
		}
	}
	
	// -------------------------------------------- //
	// BLOCK SPAWNERS OFF FACTIONS LANDS 
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void aoColocarSpawner(BlockPlaceEvent e) {
		if (e.getBlockPlaced().getType() == Material.MOB_SPAWNER) {
			if (MConf.get().bloquearSpawnersForaDoClaim) {
				PS ps = PS.valueOf(e.getBlockPlaced());
				Faction f = BoardColl.get().getFactionAt(ps);
				if (f.isNone()) {
					e.setCancelled(true);
					e.getPlayer().sendMessage("§cHey! Os geradores só podem ser usados em locais protegidos por sua facção.");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void naoSpawnar(CreatureSpawnEvent e) {
		if (MConf.get().bloquearSpawnersForaDoClaim) {
			if (e.getSpawnReason() == SpawnReason.SPAWNER) {
				PS ps = PS.valueOf(e.getLocation());
				Faction f = BoardColl.get().getFactionAt(ps);
				if (f.isNone()) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	// -------------------------------------------- //
	// ANNOUNCE WHEN JOIN OR QUIT OF SERVER
	// -------------------------------------------- //
	
	@EventHandler(ignoreCancelled = true)
	public void aoLogar(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		MPlayer mp = MPlayer.get(p);
		Faction f = mp.getFaction();
		if (!f.isNone()) {
			f.msg("§a" + mp.getRole().getPrefix() + mp.getName() + "§a entrou no servidor.");
		}
	}
	
	@EventHandler
	public void aoDeslogar(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		MPlayer mp = MPlayer.get(p);
		Faction f = mp.getFaction();
		if (!f.isNone()) {
			f.msg("§c" + mp.getRole().getPrefix() + mp.getName() + "§c saiu do servidor.");
		}
	}
	
	// -------------------------------------------- //
	// BLOCK TELEPORT TO INVALID LOCATIONS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommandEvent(PlayerCommandPreprocessEvent e) {
	
		Player p = e.getPlayer();
		String cmd = e.getMessage().toLowerCase();
		String[] args = cmd.split(" ");	  
	  		
		if (args[0].contains("sethome") || args[0].contains("createhome")) {
			MPlayer mp = MPlayer.get(p);
			Faction faction = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation()));	        
			if (!faction.isNone() && !faction.getMPlayers().contains(mp) && !faction.getRelationTo(mp.getFaction()).equals(Rel.ALLY)) {
				p.sendMessage("§cVocê não tem permissão para definir uma home neste local.");		
				e.setCancelled(true);
			}
		}
		
		if (args[0].contains("voltar") || args[0].contains("return") || args[0].contains("home") || args[0].contains("back")) {
			LIST.add(p.getName());
			LIST.add(p.getName());
			new BukkitRunnable() {
				@Override
				public void run() {
					if (LIST.contains(p.getName())) {
						LIST.remove(p.getName()); 
						if (LIST.contains(p.getName())) {
							LIST.remove(p.getName()); 
						}
					}
				}
			}.runTaskLater(Factions.get(), 20 * 8);
			return;
		}	
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void aoTeleportarComComandoBackOuHome(PlayerTeleportEvent e) {
				
		Player p = e.getPlayer();
        MPlayer mp = MPlayer.get(p);
            
		if (e.getCause() == TeleportCause.COMMAND || e.getCause() == TeleportCause.UNKNOWN  || e.getCause() == TeleportCause.PLUGIN ) {
	        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(e.getTo()));
			if (!faction.getMPlayers().contains(mp) && !faction.getRelationTo(mp.getFaction()).equals(Rel.ALLY)) {
				if (LIST.contains(p.getName())) {
					if (faction.getId().equals(Factions.ID_NONE) || faction.getId().equals(Factions.ID_WARZONE) || faction.getId().equals(Factions.ID_SAFEZONE)) {
						return;
					} else {
						e.setCancelled(true);
						LIST.remove(p.getName());
						p.sendMessage("§cVocê não pode se teleportar para este local pois ele esta protegido pela facção §f" + faction.getName() + "§c.");
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void enderPearlClipping(PlayerTeleportEvent event)
	{
		if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;

		// this exploit works when the target location is within 0.31 blocks or so of a door or glass block or similar...
		Location target = event.getTo();
		Location from = event.getFrom();

		// blocks who occupy less than 1 block width or length wise need to be handled differently
		Material mat = event.getTo().getBlock().getType();
		if (
				((mat == Material.THIN_GLASS || mat == Material.IRON_FENCE) && clippingThrough(target, from, 0.65))
			 || ((mat == Material.FENCE || mat == Material.NETHER_FENCE) && clippingThrough(target, from, 0.45))
			)
		{			
			event.setTo(from);
			return;
		}

		// simple fix otherwise: ender pearl target locations are standardized to be in the center (X/Z) of the target block, not at the edges
		target.setX(target.getBlockX() + 0.5);
		target.setZ(target.getBlockZ() + 0.5);
		event.setTo(target);
	}
	
	public static boolean clippingThrough(Location target, Location from, double thickness)
	{
		return
		(
			(from.getX() > target.getX() && (from.getX() - target.getX() < thickness))
		 || (target.getX() > from.getX() && (target.getX() - from.getX() < thickness))
		 || (from.getZ() > target.getZ() && (from.getZ() - target.getZ() < thickness))
		 || (target.getZ() > from.getZ() && (target.getZ() - from.getZ() < thickness))
		);
	}
}
