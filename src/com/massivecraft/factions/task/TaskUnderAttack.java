package com.massivecraft.factions.task;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import com.massivecraft.factions.engine.EngineSobAtaque;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.util.ColorScrollPlus;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.nms.NmsChat;
import com.massivecraft.massivecore.ps.PS;

public class TaskUnderAttack extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
    private static final ColorScrollPlus cs = new ColorScrollPlus("§c", "Sua facção está sob ataque!", "§4", "§c", "§c");
	 
	private static TaskUnderAttack i = new TaskUnderAttack();
	public static TaskUnderAttack get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public long getDelayMillis()
	{
		return 250L;
	}
	
	@Override
	public boolean isSync() {
		return false;
	}
	
	@Override
	public void invoke(long now)
	{
		for (Chunk chunk : EngineSobAtaque.underattack.keySet())
		{						
			if (System.currentTimeMillis() > EngineSobAtaque.underattack.get(chunk))
			{
				Faction fac = BoardColl.get().getFactionAt(PS.valueOf(chunk));
				EngineSobAtaque.get().remove(chunk, fac);
			}
		}
		
		for (Faction f : EngineSobAtaque.facs)
		{
			String next = cs.next();
			for (Player p : f.getOnlinePlayers())
			{
				NmsChat.get().sendActionbarMsg(p, next);
			}
		}
	}
	
}