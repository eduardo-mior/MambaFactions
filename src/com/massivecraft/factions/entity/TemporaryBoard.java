package com.massivecraft.factions.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.massivecraft.factions.Factions;
import com.massivecraft.massivecore.ps.PS;

public class TemporaryBoard 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TemporaryBoard i = new TemporaryBoard();
	public static TemporaryBoard get() { return i; }
	
	private static final Map<PS, BukkitTask> TEMPORARY = new HashMap<>();
	
	// -------------------------------------------- //
	// CREATE 
	// -------------------------------------------- //
	
	public void create(PS ps, Faction faction) {
		BoardColl.get().setTempFactionAt(ps, faction);
		TEMPORARY.put(coords(ps), new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				delete(ps, faction);
			}
		}.runTaskLaterAsynchronously(Factions.get(), 36000L));
	}
	
	// -------------------------------------------- //
	// DELETE
	// -------------------------------------------- //
	
	public void delete(PS ps, Faction faction) 
	{
		BoardColl.get().removeTempAt(faction, ps);
		TEMPORARY.get(coords(ps)).cancel();
		TEMPORARY.remove(coords(ps));
	}
	
	public void delete(PS ps) 
	{
		TEMPORARY.get(coords(ps)).cancel();
		TEMPORARY.remove(coords(ps));
	}
	
	// -------------------------------------------- //
	// CHECK
	// -------------------------------------------- //
	
	public boolean isTemporary(PS ps) 
	{
		return TEMPORARY.containsKey(coords(ps));
	}
	
	// -------------------------------------------- //
	// IS IT BETTER TO ONLY WORK WITH THE COORDS?
	// -------------------------------------------- //
	
	private static PS coords(PS ps) 
	{
		return ps.getChunkCoords(true);
	}
}