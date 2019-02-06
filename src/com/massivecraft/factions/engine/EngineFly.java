package com.massivecraft.factions.engine;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.MUtil;

public class EngineFly extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineFly i = new EngineFly();
	public static EngineFly get() { return i; }
	
	// -------------------------------------------- //
	// FLY INFO
	// -------------------------------------------- //
	
	public static Set<Player> flys = new HashSet<>();
	
	// -------------------------------------------- //
	// FLY
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (flys.contains(p)) {
			flys.remove(p);
			p.setAllowFlight(false);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.getAllowFlight()) {
			if (p.hasPermission("factions.voar.bypass") || MPlayer.get(p).isOverriding()) return;
			else flys.add(p);
		}
	}

	// -------------------------------------------- //
	// UTIL METHODS
	// -------------------------------------------- //
	
	public static void disableFly(Player p)
	{
		flys.remove(p);
		p.setAllowFlight(false);
		p.sendMessage("§cVocê saiu dos territórios das facção portanto seu modo voar foi automaticamente desabilitado.");
	}
	
	public static void disableFly(EntityDamageByEntityEvent e) 
	{
		// Verificando se o defensor é um player
		Entity edefender = e.getEntity();
		if (MUtil.isntPlayer(edefender)) return;
		
		Entity edamager = e.getDamager();
		// Verificando se o atacante é um player ou uma flecha
		if (MUtil.isPlayer(edamager) || edamager instanceof Arrow) {
			Player defender = (Player) edefender;
			if (defender.getAllowFlight()) {
				flys.remove(defender);
				defender.setAllowFlight(false);
				defender.sendMessage("§cVocê foi atingido por um inimigo portanto seu modo voar foi automaticamente desabilitado.");
			}
		}
	}
	
	public static void disableFlyFaction(Faction f)
	{
		for (Player p : f.getOnlinePlayers()) {
			if (!p.hasPermission("factions.voar.bypass") && p.getAllowFlight()) {
				flys.remove(p);
				p.setFlying(false);
				p.setAllowFlight(false);
				p.sendMessage("§cSua facção entrou em ataque portanto seu modo voar foi automaticamente desabilitado.");
			}
		}
	}
	
}
