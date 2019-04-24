package com.massivecraft.factions.engine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.TemporaryBoard;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.massivecore.Engine;

public class EngineDisband extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineDisband i = new EngineDisband();
	public static EngineDisband get() { return i; }

	// -------------------------------------------- //
	// UPDATE CACHE AND REFERENCES OF FACTION
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDisaband(EventFactionsDisband e) {
		Faction faction = e.getFaction();
		faction.getEnemies().forEach(target -> target.removeEnemy(faction));
		faction.getAliadosPendentesEnviados().forEach(target -> target.removePendingRelation(faction));
		faction.getInvitations().keySet().forEach(target -> MPlayer.get(target).removeInvitation(faction));
		faction.getTempClaims().forEach(target -> TemporaryBoard.get().delete(target));
	}
	
}