package com.massivecraft.factions.task;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.engine.EngineFly;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.ps.PS;

public class TaskFlyManager extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	 
	private static TaskFlyManager i = new TaskFlyManager();
	public static TaskFlyManager get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public long getDelayMillis()
	{
		return 1500L;
	}
	
	@Override
	public boolean isSync() {
		return false;
	}
	
	private static Set<Player> remove = new HashSet<>();
	private static Set<String> worlds = MConf.get().mundosComFlyAtivado;
	
	@Override
	public void invoke(long now)
	{
		for (Player p : EngineFly.flys)
		{
			if (!p.getAllowFlight()) {
				remove.add(p);
			} else {
				if (!worlds.contains(p.getWorld().getName())) {
					MPlayer mplayer = MPlayer.get(p);
					if (mplayer.hasFaction()) {
						PS ps = PS.valueOf(p.getLocation());	
						if (!BoardColl.get().getFactionAt(ps).equals(mplayer.getFaction())) {
							remove.add(p);
						}
					} else {
						remove.add(p);
					}
				}
			}
		}
		if (!remove.isEmpty()) {
			Bukkit.getScheduler().runTask(Factions.get(), new Runnable() {
				public void run() {
					for (Player p : remove) {
						p.setAllowFlight(false);
						p.sendMessage("§cVocê saiu dos territórios das facção portanto seu modo voar foi automaticamente desabilitado.");
					}
					EngineFly.flys.removeAll(remove);
					remove.clear();
				}
			});
		}
	}
	
}