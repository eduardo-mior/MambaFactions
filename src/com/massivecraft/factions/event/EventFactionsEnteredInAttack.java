package com.massivecraft.factions.event;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.massivecraft.factions.entity.Faction;

public class EventFactionsEnteredInAttack extends EventFactionsAbstract
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Faction target;
	public Faction getFaction() { return this.target; }
	
	private final Location location;
	public Location getLocation() { return this.location; }
	
	private final EntityExplodeEvent event;
	public EntityExplodeEvent getEvent() { return this.event; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventFactionsEnteredInAttack(Faction target, Location location, EntityExplodeEvent event)
	{
		this.target = target;
		this.location = location;
		this.event = event;
	}

}
