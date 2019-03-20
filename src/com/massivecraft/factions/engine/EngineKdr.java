package com.massivecraft.factions.engine;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;

public class EngineKdr extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineKdr i = new EngineKdr();
	public static EngineKdr get() { return i; }

	// -------------------------------------------- //
	// UPDATE KILLS AND DEATHS
	// -------------------------------------------- //

	@EventHandler(ignoreCancelled = true)
	public void aoMorrer(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Entity en = p.getKiller();
		if (en instanceof Player) {
			MPlayer matador = MPlayer.get(p.getKiller());
			MPlayer difunto = MPlayer.get(p);
			if (matador != null) 
				matador.setKills(matador.getKills() + 1);
			if (difunto != null)
				difunto.setDeaths(difunto.getDeaths() + 1);
		}
	}
}