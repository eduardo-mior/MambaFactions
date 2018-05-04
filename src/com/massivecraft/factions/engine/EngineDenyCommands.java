package com.massivecraft.factions.engine;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class EngineDenyCommands extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineDenyCommands i = new EngineDenyCommands();
	public static EngineDenyCommands get() { return i; }

	// -------------------------------------------- //
	// DENY COMMANDS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void denyCommands(PlayerCommandPreprocessEvent event)
	{
		// If a player is trying to run a command ...
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		MPlayer mplayer = MPlayer.get(player);
		
		// ... and the player is not overriding ...
		if (mplayer.isOverriding()) return;
		
		// ... clean up the command ...
		String command = event.getMessage();
		command = Txt.removeLeadingCommandDust(command);
		command = command.toLowerCase();
		command = command.trim();
		
		// ... if there is a faction at the players location we fetch the relation now ...
		PS ps = PS.valueOf(player.getLocation()).getChunk(true);
		Faction factionAtPs = BoardColl.get().getFactionAt(ps);
		Rel factionAtRel = null;
		
		if (factionAtPs != null && ! factionAtPs.isNone())
		{
			factionAtRel = factionAtPs.getRelationTo(mplayer);
		}
		
		// ... if there is no relation here then there are no further checks ...
		if (factionAtRel == null) return;
		
		List<String> deniedCommands = MConf.get().denyCommandsTerritoryRelation.get(factionAtRel);
		if (deniedCommands == null) return;
		if ( ! MUtil.containsCommand(command, deniedCommands)) return;
		
		mplayer.msg("§cVocê não pode usar '§c%s§c' em territórios inimigos.", command);
		event.setCancelled(true);
	}
	
}
