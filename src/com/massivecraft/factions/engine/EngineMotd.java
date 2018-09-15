package com.massivecraft.factions.engine;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.mixin.MixinActual;
import com.massivecraft.massivecore.mixin.MixinMessage;

public class EngineMotd extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMotd i = new EngineMotd();
	public static EngineMotd get() { return i; }

	// -------------------------------------------- //
	// MOTD
	// -------------------------------------------- //

	// Can't be cancelled
	@EventHandler(priority = EventPriority.MONITOR)
	public void motdMonitor(PlayerJoinEvent event)
	{
		// Gather info ...
		Player player = event.getPlayer();
		MPlayer mplayer = MPlayer.get(player);
		Faction faction = mplayer.getFaction();

		// ... if there is a motd ...
		if ( ! faction.hasMotd()) return;

		// ... and this is an actual join ...
		if ( ! MixinActual.get().isActualJoin(event)) return;

		// ... then prepare the messages ...
		final List<Object> messages = faction.getMotdMessages();
		
		MixinMessage.get().messageOne(player, messages);
	}
	
}
