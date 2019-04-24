package com.massivecraft.factions.engine;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerToRecipientChat;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;

public class EngineChat extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineChat i = new EngineChat();
	public static EngineChat get() { return i; }
	public EngineChat()
	{
		this.setPlugin(Factions.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if ( ! active) return;
		
		Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, this, MConf.get().chatSetFormatAt, new SetFormatEventExecutor(), Factions.get(), true);
		Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, this, MConf.get().chatParseTagsAt, new ParseTagsEventExecutor(), Factions.get(), true);
		Bukkit.getPluginManager().registerEvent(EventMassiveCorePlayerToRecipientChat.class, this, EventPriority.NORMAL, new ParseRelcolorEventExecutor(), Factions.get(), true);
	}
	
	// -------------------------------------------- //
	// SET FORMAT
	// -------------------------------------------- //
	
	private class SetFormatEventExecutor implements EventExecutor
	{
		@Override
		public void execute(Listener listener, Event event) throws EventException
		{
			try
			{
				if (!(event instanceof AsyncPlayerChatEvent)) return;
			((AsyncPlayerChatEvent)event).setFormat(MConf.get().chatSetFormatTo);
			}
			catch (Throwable t)
			{
				throw new EventException(t);
			}
		}
	}
	
	
	// -------------------------------------------- //
	// PARSE TAGS
	// -------------------------------------------- //

	private class ParseTagsEventExecutor implements EventExecutor
	{
		@Override
		public void execute(Listener listener, Event event) throws EventException
		{
			try
			{
				if (!(event instanceof AsyncPlayerChatEvent)) return;
				parseTags((AsyncPlayerChatEvent)event);
			}
			catch (Throwable t)
			{
				throw new EventException(t);
			}
		}
	}

	public static void parseTags(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		
		String format = event.getFormat();
		format = format(format, player);
		event.setFormat(format);
	}
	
	// -------------------------------------------- //
	// PARSE RELCOLOR
	// -------------------------------------------- //
	
	private class ParseRelcolorEventExecutor implements EventExecutor
	{
		@Override
		public void execute(Listener listener, Event event) throws EventException
		{
			try
			{
				if (!(event instanceof EventMassiveCorePlayerToRecipientChat)) return;
				parseRelcolor((EventMassiveCorePlayerToRecipientChat)event);
			}
			catch (Throwable t)
			{
				throw new EventException(t);
			}
		}
	}

	public static void parseRelcolor(EventMassiveCorePlayerToRecipientChat event)
	{
		String format = event.getFormat();
		format = format(format, event.getSender());
		event.setFormat(format);
	}
	
	private static String format(String msg, CommandSender sender)
	{		
		// Get entities
		MPlayer usender = MPlayer.get(sender);
		
		// No "force"
		Faction faction = usender.getFaction();
		if (faction.isNone()) return msg.replace("{faction}", "");
		
		return msg.replace("{faction}","§7[" + usender.getRole().getPrefix() + faction.getName() + "§7] §f");
	}
	
}
