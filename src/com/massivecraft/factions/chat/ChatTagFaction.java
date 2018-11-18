package com.massivecraft.factions.chat;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.command.CommandSender;

public class ChatTagFaction extends ChatTag
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatTagFaction() { super("faction"); }
	private static ChatTagFaction i = new ChatTagFaction();
	public static ChatTagFaction get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getReplacement(CommandSender sender)
	{
		// Get entities
		MPlayer usender = MPlayer.get(sender);
		
		// No "force"
		Faction faction = usender.getFaction();
		if (faction.isNone()) return "";
		
		return "§7[" + usender.getRole().getPrefix() + faction.getName() + "§7] §f";
	}

}
