package com.massivecraft.factions.engine;

import java.util.Map.Entry;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.TemporaryBoard;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;

public class EngineTemporaryBoard extends Engine 
{	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static EngineTemporaryBoard i = new EngineTemporaryBoard();
	public static EngineTemporaryBoard get() { return i; }

	// -------------------------------------------- //
	// REMOVE TEMPORARY BOARDS IN EVENTS
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onUnclaim(EventFactionsChunksChange e) {
		for (Entry<PS, Faction> entry : e.getOldChunkFaction().entrySet()) {
			Faction atual = entry.getValue();
			if (!atual.isNone()) {
				PS ps = entry.getKey();
				if (TemporaryBoard.get().isTemporary(ps)) {
					TemporaryBoard.get().delete(ps, atual);
				}
			}
		}
	}

}