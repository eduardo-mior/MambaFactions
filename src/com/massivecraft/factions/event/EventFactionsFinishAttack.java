package com.massivecraft.factions.event;

import org.bukkit.event.HandlerList;

import com.massivecraft.factions.entity.Faction;

public class EventFactionsFinishAttack extends EventFactionsAbstract
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
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventFactionsFinishAttack(Faction target)
	{
		this.target = target;
	}

}
