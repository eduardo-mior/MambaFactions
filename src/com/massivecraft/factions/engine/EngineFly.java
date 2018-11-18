package com.massivecraft.factions.engine;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;

public class EngineFly extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineFly i = new EngineFly();
	public static EngineFly get() { return i; }

	// -------------------------------------------- //
	// MOVE DETECT
	// -------------------------------------------- //
	
	@EventHandler(ignoreCancelled = true)
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (p.isFlying() && !p.hasPermission("factions.voar.bypass")) {
			MPlayer mplayer = MPlayer.get(p);
			if (mplayer.hasFaction()) {
				PS ps = PS.valueOf(p.getLocation());	
				if (!BoardColl.get().getFactionAt(ps).equals(mplayer.getFaction())) {
					disableFly(p);
				}
			} else {
				disableFly(p);
			}
		}
	}

	// -------------------------------------------- //
	// UTIL METHODS
	// -------------------------------------------- //
	
	public static void disableFly(Player p )
	{
		p.setFlying(false);
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
				defender.sendMessage("§cVocê foi atingido por um inimigo portanto seu modo voar foi automaticamente desabilitado.");
				defender.setFlying(false);
				defender.setAllowFlight(false);
			}
		}
	}
	
	public static void disableFlyFaction(Faction f)
	{
		for (Player p : f.getOnlinePlayers()) {
			if (!p.hasPermission("factions.voar.bypass") && p.getAllowFlight()) {
				p.sendMessage("§cSua facção entrou em ataque portanto seu modo voar foi automaticamente desabilitado.");
				p.setFlying(false);
				p.setAllowFlight(false);
			}
		}
	}
	
}
