package com.massivecraft.factions.engine;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;

public class EngineKdr  extends Engine{
	
	private static EngineKdr i = new EngineKdr();
	public static EngineKdr get() { return i; }
	
	public static int getPlayerKills(MPlayer mp) {
		double kills = mp.getKills();
		return (int) kills;
	}
	
	public static int getPlayerDeaths(MPlayer mp) {
		double deaths = mp.getDeaths();
		return (int) deaths;
	}
	
	public static String getPlayerKdr(MPlayer mp) {
		Double kdr = mp.getKdr();
		String kdr2f = String.format("%.2f", kdr);
		if (kdr2f == null) {
			return "0.00";
		} else {
			return kdr2f;
		}
	}
	
	public static int getFacKills(Faction f) {
		int fackills = 0;
		List<MPlayer> mps = f.getMPlayers();
		for (int i = 0; i < mps.size(); i++) {
			MPlayer mp = mps.get(i);
			fackills += mp.getKills();
		} 
		return fackills;
	}
	
	public static int getFacDeaths(Faction f) {
		int facdeaths = 0;
		List<MPlayer> mps = f.getMPlayers();
		for (int i = 0; i < mps.size(); i++) {
			MPlayer mp = mps.get(i);
			facdeaths += mp.getDeaths();
		} 
		return facdeaths;
	}
	
	public static double getFacKdr(Faction f) {
		double fdeaths = getFacDeaths(f);
		int fkills = getFacKills(f);
		if (fdeaths == 0) {
			return fkills;
		} else {
			double fkdr = fkills/fdeaths;
			return fkdr;
		}
	}

	@EventHandler
	public void aoMorrer(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Entity en = p.getKiller();
		if (en instanceof Player) {
			MPlayer matador = MPlayer.get(p.getKiller());
			MPlayer difunto = MPlayer.get(p);
			matador.setKills(matador.getKills() + 1);
			difunto.setDeaths(difunto.getDeaths() + 1);
		}
	}
}
