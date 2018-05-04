package com.massivecraft.factions.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.comparator.ComparatorMPlayerRole;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsFactionShowAsync;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.PriorityLines;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

public class EngineShow extends Engine
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String BASENAME = "factions";
	public static final String BASENAME_ = BASENAME+"_";
	
	public static final String SHOW_ID_FACTION_ID = BASENAME_ + "id";
	public static final String SHOW_ID_FACTION_DESCRIPTION = BASENAME_ + "description";
	public static final String SHOW_ID_FACTION_AGE = BASENAME_ + "age";
	public static final String SHOW_ID_FACTION_POWER = BASENAME_ + "power";
	public static final String SHOW_ID_FACTION_STATS = BASENAME_ + "stats";
	public static final String SHOW_ID_FACTION_FOLLOWERS = BASENAME_ + "followers";
	public static final String SHOW_ID_FACTION_ALIADOS = BASENAME_ + "allys";
	public static final String SHOW_ID_FACTION_INIMIGOS = BASENAME_ + "enemies";
	
	public static final int SHOW_PRIORITY_FACTION_ID = 1000;
	public static final int SHOW_PRIORITY_FACTION_DESCRIPTION = 2000;
	public static final int SHOW_PRIORITY_FACTION_AGE = 3000;
	public static final int SHOW_PRIORITY_FACTION_POWER = 5000;
	public static final int SHOW_PRIORITY_FACTION_STATS = 6000;
	public static final int SHOW_PRIORITY_FACTION_FOLLOWERS = 9000;
	public static final int SHOW_PRIORITY_FACTION_ALIADOS = 10000;
	public static final int SHOW_PRIORITY_FACTION_INIMIGOS = 11000;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineShow i = new EngineShow();
	public static EngineShow get() { return i; }

	// -------------------------------------------- //
	// FACTION SHOW
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onFactionShow(EventFactionsFactionShowAsync event)
	{
		final int tableCols = 4;
		final CommandSender sender = event.getSender();
		final MPlayer mplayer = event.getMPlayer();
		final Faction faction = event.getFaction();
		final boolean normal = faction.isNormal();
		final Map<String, PriorityLines> idPriorityLiness = event.getIdPriorityLiness();
		String none = Txt.parse("§7§oNinguém");

		// ID
		if (mplayer.isOverriding())
		{
			show(idPriorityLiness, SHOW_ID_FACTION_ID, SHOW_PRIORITY_FACTION_ID, "ID", faction.getId());
		}

		// DESCRIPTION
		show(idPriorityLiness, SHOW_ID_FACTION_DESCRIPTION, SHOW_PRIORITY_FACTION_DESCRIPTION, "Descrição", faction.getDescriptionDesc());
		
		// SECTION: NORMAL
		if (normal)
		{
			// AGE
			long ageMillis = faction.getCreatedAtMillis() - System.currentTimeMillis();
			LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(ageMillis, TimeUnit.getAllButMillis()), 3);
			String ageDesc = TimeDiffUtil.formatedMinimal(ageUnitcounts, "§e");
			show(idPriorityLiness, SHOW_ID_FACTION_AGE, SHOW_PRIORITY_FACTION_AGE, "§6Criada há", ageDesc + "§e atrás");

			// POWER
			double powerBoost = faction.getPowerBoost();
			String boost = (powerBoost == 0.0) ? "" : (powerBoost > 0.0 ? " (bônus: " : " (penalidade: ") + powerBoost + ")";
			String powerDesc = Txt.parse("%d/%d/%d%s", faction.getLandCount(), faction.getPowerRounded(), faction.getPowerMaxRounded(), boost);
			show(idPriorityLiness, SHOW_ID_FACTION_POWER, SHOW_PRIORITY_FACTION_POWER, "Terras / Poder / Poder Máximo", powerDesc);
		
			// STATS
			double kdr = EngineKdr.getFacKdr(faction);
			int kills = EngineKdr.getFacKills(faction);
			int deaths = EngineKdr.getFacDeaths(faction);
			String statsDesc = Txt.parse("%d/%d/%.2f", kills, deaths,kdr);
			show(idPriorityLiness, SHOW_ID_FACTION_STATS, SHOW_PRIORITY_FACTION_STATS, "Abates / Mortes / Kdr", statsDesc);
			
			String aliados = "";
			String rival = "";
			Collection<Faction> facs = FactionColl.get().getAll();
			for(Faction f : facs) {
				if (faction.getRelationTo(f).equals(Rel.ALLY)) {
					aliados += "§8, " + f.getName(faction);
				}
			}
			for(Faction f : facs) {
				if (faction.getRelationTo(f).equals(Rel.ENEMY)) {
					rival +=  "§8, " + f.getName(faction);
				}
			}
				
			if (aliados.equals("")) {
				aliados = "....§7§oNenhum";
			}
			if (rival.equals("")) {
				rival = "....§7§oNenhum";
			}
			rival = rival.length() > 250 ? ("§7§oMuitos inimigos! Use /f relação listar " + faction.getName()) : rival;

			
			show(idPriorityLiness, SHOW_ID_FACTION_ALIADOS, SHOW_PRIORITY_FACTION_ALIADOS, "Aliados", aliados.substring(4,aliados.length()));
			show(idPriorityLiness, SHOW_ID_FACTION_INIMIGOS, SHOW_PRIORITY_FACTION_INIMIGOS, "Inimigos", rival.substring(4,rival.length()));
		}

		// FOLLOWERS
		List<String> followerLines = new ArrayList<>();

		List<String> followerNamesOnline = new ArrayList<>();
		List<String> followerNamesOffline = new ArrayList<>();

		List<MPlayer> followers = faction.getMPlayers();
		Collections.sort(followers, ComparatorMPlayerRole.get());
		for (MPlayer follower : followers)
		{
			if (follower.isOnline(sender))
			{
				if (normal) {
				followerNamesOnline.add("§f" + follower.getRole().getPrefix() + "§f" + follower.getDisplayName(mplayer));
				} else {
				followerNamesOnline.add("§f" + follower.getDisplayName(mplayer));
				}
			}
			else if (normal)
			{
				// For the non-faction we skip the offline members since they are far to many (infinite almost)
				followerNamesOffline.add(follower.getRole().getPrefix() + follower.getDisplayName(mplayer));
			}
		}
		
		String headerOnline = Txt.parse("§6Membros Online (%s):", followerNamesOnline.size());
		followerLines.add(headerOnline);
		if (followerNamesOnline.isEmpty())
		{
			followerLines.add(none);
		}
		else
		{
			followerLines.addAll(table(followerNamesOnline, tableCols));
		}

		if (normal)
		{
			String headerOffline = Txt.parse("§6Membros Offline (%s):", followerNamesOffline.size());
			followerLines.add(headerOffline);
			if (followerNamesOffline.isEmpty())
			{
				followerLines.add(none);
			}
			else
			{
				followerLines.addAll(table(followerNamesOffline, tableCols));
			}
		}
		idPriorityLiness.put(SHOW_ID_FACTION_FOLLOWERS, new PriorityLines(SHOW_PRIORITY_FACTION_FOLLOWERS, followerLines));
	}

	public static String show(String key, String value)
	{
		return Txt.parse("§6%s: §e%s", key, value);
	}

	public static PriorityLines show(int priority, String key, String value)
	{
		return new PriorityLines(priority, show(key, value));
	}

	public static void show(Map<String, PriorityLines> idPriorityLiness, String id, int priority, String key, String value)
	{
		idPriorityLiness.put(id, show(priority, key, value));
	}

	public static List<String> table(List<String> strings, int cols)
	{
		List<String> ret = new ArrayList<>();

		StringBuilder row = new StringBuilder();
		int count = 0;

		Iterator<String> iter = strings.iterator();
		while (iter.hasNext())
		{
			String string = iter.next();
			row.append(string);
			count++;

			if (iter.hasNext() && count != cols)
			{
				row.append(Txt.parse(" §e| "));
			}
			else
			{
				ret.add(row.toString());
				row = new StringBuilder();
				count = 0;
			}
		}

		return ret;
	}

}
