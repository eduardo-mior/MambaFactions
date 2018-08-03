package com.massivecraft.factions.engine;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;

public class EngineFly extends Engine{
	
	private static EngineFly i = new EngineFly();
	public static EngineFly get() { 
		systemFly = MConf.get().sistemaDeVoarNoClaim; 
		return i; 
	}
	
	private static boolean systemFly;
	
	@EventHandler
	public void playerMove(PlayerMoveEvent e) {
		if (systemFly) {
			if (e.getPlayer().getAllowFlight() && !e.getPlayer().hasPermission("factions.voar.bypass")) {
				MPlayer mplayer = MPlayer.get(e.getPlayer());
				PS ps = PS.valueOf(e.getPlayer().getLocation());
				if (!mplayer.hasFaction() || !BoardColl.get().getFactionAt(ps).equals(mplayer.getFaction())) {
					e.getPlayer().setAllowFlight(false);
					e.getPlayer().sendMessage("§cVocê saiu dos territórios das facção portanto seu modo voar foi automaticamente desabilitado.");
				}
			}
		}
	}
	
	public static void disableFly(EntityDamageByEntityEvent e) {
		if (systemFly) {
			Entity edefender = e.getEntity();
			if (MUtil.isntPlayer(edefender)) return;
			Player defender = (Player)edefender;
			if (defender.getAllowFlight()) {
				defender.sendMessage("§cVocê foi atingido por um inimigo portanto seu modo voar foi automaticamente desabilitado.");
				defender.setAllowFlight(false);
			}
		}
	}
}
