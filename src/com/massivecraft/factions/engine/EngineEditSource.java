package com.massivecraft.factions.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
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
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;

public class EngineEditSource  extends Engine{
	
	private static EngineEditSource i = new EngineEditSource();
	public static EngineEditSource get() { return i; }
	
	private List<String> lista = new ArrayList<>();
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
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
	public void aoMorrerAnunciarMorte(PlayerDeathEvent e) {
	    if (MConf.get().anunciarMorteAoMorrer) {
	    	Player p = e.getEntity();
	    	Entity en = p.getKiller();
	    	if (en instanceof Player) {
	    		Player k = p.getKiller().getPlayer();
	    		MPlayer mp = MPlayer.get(p);
	    		MPlayer mk = MPlayer.get(k);
	    		String facp = mp.getFaction().isNone() ? "" : "§3[" + mp.getRole().getPrefix() + mp.getFaction().getName() + "§3] ";
	    		String fack = mk.getFaction().isNone() ? "" : "§3[" + mk.getRole().getPrefix() + mk.getFaction().getName() + "§3] ";
	    		for (Player target : getPlayersNearby(p)) {
	    			target.sendMessage("§3" + facp + p.getName() + "§c foi morto por §3" + fack + k.getName());
	    		}
	    	}
		}
	}
	
	private List<Player> getPlayersNearby(Player player) {
		List<Player> players = new ArrayList<Player>();
		int d2 = MConf.get().distanciaDoAnuncioEmBlocos * MConf.get().distanciaDoAnuncioEmBlocos;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2) {
				players.add(p);
			}
		}
		return players;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void aoColocarSpawner(BlockPlaceEvent e) {
		if (MConf.get().bloquearSpawnersForaDoClaim) {
			if (e.getBlockPlaced().getType() == Material.MOB_SPAWNER) {
				PS ps = PS.valueOf(e.getBlockPlaced());
				Faction f = BoardColl.get().getFactionAt(ps);
				if (f.isNone()) {
					e.setCancelled(true);
					e.getPlayer().sendMessage("§cHey! Os geradores só podem ser usados em locais protegidos por sua facção.");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
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
	
	@EventHandler
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
		
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommandEvent(PlayerCommandPreprocessEvent e) {
	
		Player p = e.getPlayer();
		String cmd = e.getMessage().toLowerCase();
		String[] args = cmd.split(" ");	  
	  		
		if (args[0].contains("sethome") || args[0].contains("setcasa") || args[0].contains("createhome")) {
			MPlayer mp = MPlayer.get(p);
			BoardColl coll = BoardColl.get();
			Faction faction = coll.getFactionAt(PS.valueOf(e.getPlayer().getLocation()));	        
			if (!faction.isNone() && !faction.getMPlayers().contains(mp) && !faction.getRelationTo(mp.getFaction()).equals(Rel.ALLY)) {
				e.setCancelled(true);
				p.sendMessage("§cVocê não tem permissão para definir uma home neste local.");		
			}
		}
		
		if (args[0].contains("voltar") || args[0].contains("casa") || args[0].contains("return") || args[0].contains("home") || args[0].contains("back")) {
			lista.add(p.getName());
			lista.add(p.getName());
			new BukkitRunnable() {
				@Override
				public void run() {
					if (lista.contains(p.getName())) {
						lista.remove(p.getName()); 
						if (lista.contains(p.getName())) {
							lista.remove(p.getName()); 
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
	        BoardColl coll = BoardColl.get();
	        Faction faction = coll.getFactionAt(PS.valueOf(e.getTo()));
			if (!faction.getMPlayers().contains(mp) && !faction.getRelationTo(mp.getFaction()).equals(Rel.ALLY)) {
				if (lista.contains(p.getName())) {
					if (faction.getId().equals(Factions.ID_NONE) || faction.getId().equals(Factions.ID_WARZONE) || faction.getId().equals(Factions.ID_SAFEZONE)) {
						return;
					} else {
						e.setCancelled(true);
						lista.remove(p.getName());
						p.sendMessage("§cVocê não pode se teleportar para este local pois ele esta protegido pela facção §f" + faction.getName() + "§c.");
					}
				}
			}
		}
	}
	
	public static List<Faction> getAliadosPendentesEnviados(Faction f) {
		List<Faction> aliadosPendentesEnviados = new ArrayList<>();
        Map<String, Rel> relations = f.getRelationWishes();
		for (Entry<String, Rel> relation : relations.entrySet()){
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
		for (Faction faction : factions) {
	        Map<String, Rel> relations = faction.getRelationWishes();
			for (Entry<String, Rel> relation : relations.entrySet()) {
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
	
	
	public static List<Faction> getAliados(Faction f) {
		List<Faction> aliados = new ArrayList<>();
		Set<String> relations = f.getRelationWishes().keySet();
		
		for (String id : relations) {
			Faction fac = Faction.get(id);
			if (fac != null && fac.getRelationTo(f).equals(Rel.ALLY)) aliados.add(fac);
		}
		return aliados;
	}
	
	public static List<String> fplayers(Faction f) {
		
		List<String> players = new ArrayList<>();
		String pon = "";

		for (int i = 0; i < f.getOnlinePlayers().size(); i++) {
			Player p = f.getOnlinePlayers().get(i);
			MPlayer mp = MPlayer.get(p);
			pon += "§7 " + mp.getRole().getPrefix() + mp.getName();
		}
		
		int tamanho = pon.length();
		if (tamanho < 40) {
			players.add("§7"+ pon.substring(0, tamanho)); 
			return players; }
		
		else if (tamanho < 80) {
			players.add("§7"+pon.substring(0,40));
			players.add("§7"+pon.substring(40, tamanho));
			return players; }
		
		else if (tamanho < 120) {
			players.add("§7"+pon.substring(0,40));
			players.add("§7"+pon.substring(40,80));
			players.add("§7"+pon.substring(80, tamanho)); 
			return players; }
		
		else if (tamanho < 160) {
			players.add("§7"+pon.substring(0,40));
			players.add("§7"+pon.substring(40,80));
			players.add("§7"+pon.substring(80, 120)); 
			players.add("§7"+pon.substring(120, tamanho)); 
			return players; }
		
		else if (tamanho < 200) {
			players.add("§7"+pon.substring(0,40));
			players.add("§7"+pon.substring(40,80));
			players.add("§7"+pon.substring(80, 120)); 
			players.add("§7"+pon.substring(120, 160)); 
			players.add("§7"+pon.substring(160, tamanho)); 
			return players; }
		
		else if (tamanho < 240) {
			players.add("§7"+pon.substring(0,40));
			players.add("§7"+pon.substring(40,80));
			players.add("§7"+pon.substring(80, 120)); 
			players.add("§7"+pon.substring(120, 160)); 
			players.add("§7"+pon.substring(160, 200));
			players.add("§7"+pon.substring(200, tamanho)); 
			return players; }
		
		else {
			players.add("§7§oMuitos players online.");
			return players; 
		}
	}
	
	public static List<String> fmotd(Faction f) {
		
		String factionmotd = f.getMotdDesc();
		int factionmotdtamanho = factionmotd.length();
		List<String> motd = new ArrayList<>();
		
		if (f.hasMotd() == false) {
			motd.add("§7§o'Mensagem do dia indefinida.'");
			return motd; }
		
		else if (f.hasMotd() && factionmotdtamanho < 40) {
			motd.add("§7'§b"+ factionmotd.substring(0, factionmotdtamanho) + "§7'"); 
			return motd; }
		
		else if (f.hasMotd() && factionmotdtamanho < 110) {
			motd.add("§7'§b"+factionmotd.substring(0,40));
			motd.add("§b"+factionmotd.substring(50, factionmotdtamanho) + "§7'");
			return motd; }
		
		else {
			motd.add("§7A frase da motd é muito grande!");
			motd.add("§7Para visualiza-la use '§f/f motd§7'");
			return motd; 
		}
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
