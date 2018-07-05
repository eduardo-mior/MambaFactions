package com.massivecraft.factions.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;

public class EngineEditSource  extends Engine{
	
	private static EngineEditSource i = new EngineEditSource();
	public static EngineEditSource get() { return i; }
	
	public HashMap<Player, Integer> lista = new HashMap<Player, Integer>();
	
	@EventHandler
	public void comandoClaim(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().toLowerCase();
		if (cmd.equalsIgnoreCase("/f claim") || cmd.equalsIgnoreCase("/f proteger") || cmd.equalsIgnoreCase("/f conquistar") || cmd.equalsIgnoreCase("/f dominar")) { 
			e.setMessage("/f claim one");
		}
		
		else if (cmd.startsWith("/f entrar zona de guerra") || cmd.startsWith("/f join zona de guerra")) {
			e.setMessage("/f join warzone");
		}
		
		else if (cmd.startsWith("/f join zona protegida") || cmd.startsWith("/f entrar zona protegida")) {
			e.setMessage("/f join safezone");
		}
	}
	
	@EventHandler
	public void aoLogar(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		MPlayer mp = MPlayer.get(p);
		Faction f = mp.getFaction();
		if (!f.isNone() && mp.hasFaction()) {
			f.msg("§a"+mp.getRole().getPrefix()+mp.getName()+"§a entrou no servidor.");
		}
	}
	
	@EventHandler
	public void aoDeslogar(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		MPlayer mp = MPlayer.get(p);
		Faction f = mp.getFaction();
		if (!f.isNone() && mp.hasFaction()) {
			f.msg("§c"+mp.getRole().getPrefix()+mp.getName()+"§c saiu do servidor.");
		}
	}
		
	@EventHandler
	public void onCommandEvent(PlayerCommandPreprocessEvent e) {
	
	  Player p = e.getPlayer();
	  String cmd = e.getMessage().toLowerCase();
      String[] arg1 = null;
	  arg1 = cmd.split(" ");
	  
	  if (arg1[0].contains("voltar") || arg1[0].contains("casa") || arg1[0].contains("return") || arg1[0].contains("home") || arg1[0].contains("back")) {
		 if(!lista.containsKey(p)) {
			lista.put(p, 0);
	        new BukkitRunnable() {
	            @Override
	            public void run() {
	            	if (lista.containsKey(p)) {
	            		lista.remove(p); }
	            }
	        }.runTaskLater(Factions.get(), 20 * 20);
	        return;
			}
		}

	    else if (arg1[0].contains("sethome") || arg1[0].contains("setcasa") || arg1[0].contains("createhome")) {
	        MPlayer mp = MPlayer.get(p);
	        BoardColl coll = BoardColl.get();
	        Faction faction = coll.getFactionAt(PS.valueOf(e.getPlayer().getLocation()));
			if (faction != null) {
				if (!(faction.isNone() || faction.getMPlayers().contains(mp))) {
					e.setCancelled(true);
					p.sendMessage("§cVocê não tem permissão para definir uma home neste local.");
				}
			} 
		} 
	    
	    else if (cmd.startsWith("/f sethome") || cmd.startsWith("/f definirhome") || cmd.startsWith("/f definirbase")) {
	        MPlayer mp = MPlayer.get(p);
	        BoardColl coll = BoardColl.get();
	        Faction faction = coll.getFactionAt(PS.valueOf(e.getPlayer().getLocation()));
			if (faction != null) {
				if (!(faction.getMPlayers().contains(mp))) {
					e.setCancelled(true);
					p.sendMessage("§cVocê só pode definir a home da facção dentro dos territórios da sua facção.");
				}
			}
		}
	}
	
	@EventHandler (priority=EventPriority.LOW)
	public void aoTeleportarComComandoBackOuHome(PlayerTeleportEvent e) {
				
		Player p = e.getPlayer();
        MPlayer mp = MPlayer.get(p);
            
		if (e.getCause() == TeleportCause.COMMAND || e.getCause() == TeleportCause.UNKNOWN  || e.getCause() == TeleportCause.PLUGIN ) {
	        BoardColl coll = BoardColl.get();
	        Faction faction = coll.getFactionAt(PS.valueOf(e.getTo()));
			if (!(faction.isNone() || faction.getMPlayers().contains(mp)) && faction != null) {
				if (lista.containsKey(p)) {
					lista.remove(p);
					e.setCancelled(true);
					p.sendMessage("§cVocê não pode se teleportar para este local pois ele esta protegido pela facção §f" + faction.getName() + "§c.");
				}
			}
		}
	}
	
	public static List<Faction> getAliadosPendentesEnviados(Faction f) {
		List<Faction> aliadosPendentesEnviados = new ArrayList<>();
        Map<String, Rel> relations = f.getRelationWishes();
		for(Entry<String, Rel> relation : relations.entrySet()){
			Faction ally = Faction.get(relation.getKey());
			if (ally != null) {
				if (relation.getValue() == Rel.ALLY) {
					if (ally.getRelationWish(f) != Rel.ALLY) {
						aliadosPendentesEnviados.add(ally);
					}
				}
			}
		}
		return aliadosPendentesEnviados;
	}
	
	public static List<Faction> getAliadosPendentesRecebidos(Faction f) {
		List<Faction> aliadosPendentesRecebidos = new ArrayList<>();
		Collection<Faction> factions = FactionColl.get().getAll();
		for(Faction faction : factions) {
	        Map<String, Rel> relations = faction.getRelationWishes();
			for(Entry<String, Rel> relation : relations.entrySet()) {
				if (relation.getValue() == Rel.ALLY) {
					Faction ally = Faction.get(relation.getKey());
					if (ally == f) {
						if (!(f.getRelationWishes().containsKey(faction.getId()))) {
							aliadosPendentesRecebidos.add(faction);
						}
					}
				}
			}
		}
		return aliadosPendentesRecebidos;
	}
	
	
	public static List<String> fplayers(Faction f) {
		
		List<String> players = new ArrayList<>();
		String pon = "";
		int tamanho = pon.length();

		if (f.getOnlinePlayers().size() > 0) {
			for (final Player todos : f.getOnlinePlayers()) {
				final MPlayer mps = MPlayer.get(todos);
				pon += pon + "§7" + mps.getRole().getPrefix() + mps.getName();
			}
			players.add(pon);
		}
		
		
		if (f.getOnlinePlayers().size() != 0 && tamanho < 35) {
			players.add("§7"+ pon.substring(0, tamanho)); 
			return players; }
		
		else if (f.getOnlinePlayers().size() != 0 && tamanho < 70 && tamanho >= 35 ) {
			players.add("§7"+pon.substring(0,35));
			players.add("§7"+pon.substring(35, tamanho));
			return players; }
		
		else if (f.getOnlinePlayers().size() != 0 && tamanho < 105 && tamanho >= 70 && tamanho >= 35) {
			players.add("§7"+pon.substring(0,35));
			players.add("§7"+pon.substring(35,70));
			players.add("§7"+pon.substring(70, tamanho)); 
			return players; }
		
		else if (f.getOnlinePlayers().size() != 0 && tamanho < 140 && tamanho >= 105 && tamanho >= 70 && tamanho >= 35) {
			players.add("§7"+pon.substring(0,35));
			players.add("§7"+pon.substring(35,70));
			players.add("§7"+pon.substring(70, 105)); 
			players.add("§7"+pon.substring(105, tamanho)); 
			return players; }
		
		else if (f.getOnlinePlayers().size() != 0 && tamanho < 175 && tamanho >= 140 && tamanho >= 105 && tamanho >= 70 && tamanho >= 35) {
			players.add("§7"+pon.substring(0,35));
			players.add("§7"+pon.substring(35,70));
			players.add("§7"+pon.substring(70, 105)); 
			players.add("§7"+pon.substring(105, 140)); 
			players.add("§7"+pon.substring(140, tamanho)); 
			return players; }
		
		else if (f.getOnlinePlayers().size() != 0 && tamanho < 210 && tamanho >= 175 && tamanho >= 140 && tamanho >= 105 && tamanho >= 70 && tamanho >= 35) {
			players.add("§7"+pon.substring(0,35));
			players.add("§7"+pon.substring(35,70));
			players.add("§7"+pon.substring(70, 105)); 
			players.add("§7"+pon.substring(105, 140)); 
			players.add("§7"+pon.substring(140, 175));
			players.add("§7"+pon.substring(175, tamanho)); 
			return players; }
		
		else if (f.getOnlinePlayers().size() != 0 && tamanho < 245 && tamanho >= 175 && tamanho >= 140 && tamanho >= 105 && tamanho >= 70 && tamanho >= 35) {
			players.add("§7"+pon.substring(0,35));
			players.add("§7"+pon.substring(35,70));
			players.add("§7"+pon.substring(70, 105)); 
			players.add("§7"+pon.substring(105, 140)); 
			players.add("§7"+pon.substring(140, 175)); 
			players.add("§7"+pon.substring(175, 210)); 
			players.add("§7"+pon.substring(210, tamanho)); 
			return players; }
		
		else {
			players.add("§7§oNinguém.");
			return players; 
		}
	}
	
	public static List<String> fmotd(Faction f) {
		
		final String factionmotd = f.getMotdDesc();
		int factionmotdtamanho = factionmotd.length();
		List<String> motd = new ArrayList<>();
		
		if (f.hasMotd() == false) {
			motd.add("§7§o'Mensagem do dia indefinida.'");
			return motd; }
		
		else if (f.hasMotd() == true && factionmotdtamanho < 35) {
			motd.add("§7'§b"+ factionmotd.substring(0, factionmotdtamanho) + "§7'"); 
			return motd; }
		
		else if (f.hasMotd() == true && factionmotdtamanho < 70 && factionmotdtamanho >= 35 ) {
			motd.add("§7'§b"+factionmotd.substring(0,35));
			motd.add("§b"+factionmotd.substring(35, factionmotdtamanho) + "§7'");
			return motd; }
		
		else if (f.hasMotd() == true && factionmotdtamanho < 100 && factionmotdtamanho >= 70 && factionmotdtamanho >= 35) {
			motd.add("§7'§b"+factionmotd.substring(0,35));
			motd.add("§b"+factionmotd.substring(35,70));
			motd.add("§b"+factionmotd.substring(70, factionmotdtamanho) + "§7'"); 
			return motd; }
		
		else {
			motd.add("§7A frase da motd é muito grande!");
			motd.add("§7Para visualiza-la use '§f/f motd§7'");
			return motd; 
		}
	}
	
	public static List<Faction> getAliados(Faction f) {
		List<Faction> aliados = new ArrayList<>();
		
		for(Faction faction : FactionColl.get().getAll()) {
			if (f.getRelationTo(faction).equals(Rel.ALLY)) {
				if (faction.getRelationTo(f).equals(Rel.ALLY)) {
					aliados.add(faction);
				}
			}
		}
		
		return aliados;
	}
	
	public static Faction getFactionByName(String name)
	{
		String compStr = MiscUtil.getComparisonString(name);
		for (Faction faction : FactionColl.get().getAll())
		{
			if (faction.getComparisonName().equals(compStr))
			{
				return faction;
			}
		}
		return null;
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
