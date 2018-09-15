package com.massivecraft.factions.engine;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;

public class EngineFly extends Engine{
	
	private static EngineFly i = new EngineFly();
	public static EngineFly get() { return i; }
	
	
	@EventHandler(ignoreCancelled = true)
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (p.isFlying() && !p.hasPermission("factions.voar.bypass")) {
			MPlayer mplayer = MPlayer.get(p);
			if (!mplayer.hasFaction()) {
				p.setFlying(false);
				p.setAllowFlight(false);
				p.sendMessage("§cVocê saiu dos territórios das facção portanto seu modo voar foi automaticamente desabilitado.");
			} else {
				PS ps = PS.valueOf(p.getLocation());	
				if (!BoardColl.get().getFactionAt(ps).equals(mplayer.getFaction())) {
					p.setFlying(false);
					p.setAllowFlight(false);
					p.sendMessage("§cVocê saiu dos territórios das facção portanto seu modo voar foi automaticamente desabilitado.");
				}
			}
		}
	}
	
	public static void disableFly(EntityDamageByEntityEvent e) {
		Entity edefender = e.getEntity();
		if (MUtil.isntPlayer(edefender)) return;
		Player defender = (Player)edefender;
		if (defender.getAllowFlight()) {
			defender.sendMessage("§cVocê foi atingido por um inimigo portanto seu modo voar foi automaticamente desabilitado.");
			defender.setAllowFlight(false);
		}
	}
	
}
