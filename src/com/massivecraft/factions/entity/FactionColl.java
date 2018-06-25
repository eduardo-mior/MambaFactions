package com.massivecraft.factions.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.util.Txt;

public class FactionColl extends Coll<Faction>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static FactionColl i = new FactionColl();
	public static FactionColl get() { return i; }

	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
	}
	
	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //
	
	@Override
	public void setActive(boolean active)
	{
		super.setActive(active);
		
		if (!active) return;
		
		this.createSpecialFactions();
	}
	
	// -------------------------------------------- //
	// SPECIAL FACTIONS
	// -------------------------------------------- //
	
	public void createSpecialFactions()
	{
		this.getNone();
		this.getSafezone();
		this.getWarzone();
	}
	
	public Faction getNone()
	{
		String id = Factions.ID_NONE;
		Faction faction = this.get(id);
		if (faction != null) return faction;
		
		faction = this.create(id);
		
		faction.setName(Factions.NAME_NONE_DEFAULT);
		faction.setDescription("É perigoso sair sózinho!");
		
		faction.setFlag(MFlag.getFlagPermanent(), true);
		faction.setFlag(MFlag.getFlagInfpower(), true);
		faction.setFlag(MFlag.getFlagPeaceful(), false);
		faction.setFlag(MFlag.getFlagPowerloss(), true);
		faction.setFlag(MFlag.getFlagPvp(), true);
		faction.setFlag(MFlag.getFlagFriendlyire(), false);
		
		faction.setPermittedRelations(MPerm.getPermBuild(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY, Rel.TRUCE, Rel.NEUTRAL, Rel.ENEMY);
		faction.setPermittedRelations(MPerm.getPermDoor(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY, Rel.TRUCE, Rel.NEUTRAL, Rel.ENEMY);
		faction.setPermittedRelations(MPerm.getPermContainer(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY, Rel.TRUCE, Rel.NEUTRAL, Rel.ENEMY);
		faction.setPermittedRelations(MPerm.getPermButton(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY, Rel.TRUCE, Rel.NEUTRAL, Rel.ENEMY);
		faction.setPermittedRelations(MPerm.getPermLever(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY, Rel.TRUCE, Rel.NEUTRAL, Rel.ENEMY);
		
		return faction;
	}
	
	public Faction getSafezone()
	{
		String id = Factions.ID_SAFEZONE;
		Faction faction = this.get(id);
		if (faction != null) return faction;
		
		faction = this.create(id);
		
		faction.setName(Factions.NAME_SAFEZONE_DEFAULT);
		faction.setDescription("Livre de monstros e PvP!");
		
		faction.setFlag(MFlag.getFlagPermanent(), true);
		faction.setFlag(MFlag.getFlagInfpower(), true);
		faction.setFlag(MFlag.getFlagPeaceful(), true);
		faction.setFlag(MFlag.getFlagPowerloss(), false);
		faction.setFlag(MFlag.getFlagPvp(), false);
		faction.setFlag(MFlag.getFlagFriendlyire(), false);
		
		faction.setPermittedRelations(MPerm.getPermBuild(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT);
		faction.setPermittedRelations(MPerm.getPermDoor(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY, Rel.TRUCE, Rel.NEUTRAL, Rel.ENEMY);
		faction.setPermittedRelations(MPerm.getPermContainer(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY, Rel.TRUCE, Rel.NEUTRAL, Rel.ENEMY);
		faction.setPermittedRelations(MPerm.getPermButton(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT);
		faction.setPermittedRelations(MPerm.getPermLever(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT);
		
		return faction;
	}
	
	public Faction getWarzone()
	{
		String id = Factions.ID_WARZONE;
		Faction faction = this.get(id);
		if (faction != null) return faction;
		
		faction = this.create(id);
		
		faction.setName(Factions.NAME_WARZONE_DEFAULT);
		faction.setDescription("Não é um lugar seguro para se estar!");
		
		faction.setFlag(MFlag.getFlagPermanent(), true);
		faction.setFlag(MFlag.getFlagInfpower(), true);
		faction.setFlag(MFlag.getFlagPeaceful(), true);
		faction.setFlag(MFlag.getFlagPowerloss(), true);
		faction.setFlag(MFlag.getFlagPvp(), true);
		faction.setFlag(MFlag.getFlagFriendlyire(), false);
		
		faction.setPermittedRelations(MPerm.getPermBuild(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT);
		faction.setPermittedRelations(MPerm.getPermDoor(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY, Rel.TRUCE, Rel.NEUTRAL, Rel.ENEMY);
		faction.setPermittedRelations(MPerm.getPermContainer(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY, Rel.TRUCE, Rel.NEUTRAL, Rel.ENEMY);
		faction.setPermittedRelations(MPerm.getPermButton(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT);
		faction.setPermittedRelations(MPerm.getPermLever(), Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT);
		
		return faction;
	}
	
	// -------------------------------------------- //
	// GET FACTION BY NAME
	// -------------------------------------------- //
	
	@Override
	public Faction getByName(String name)
	{
		String compStr = MiscUtil.getComparisonString(name);
		for (Faction faction : this.getAll())
		{
			if (faction.getComparisonName().equals(compStr))
			{
				return faction;
			}
		}
		return null;
	}

	// -------------------------------------------- //
	// PREDICATE LOGIC
	// -------------------------------------------- //

	public Map<Rel, List<String>> getRelationNames(Faction faction, Set<Rel> rels)
	{
		// Create
		Map<Rel, List<String>> ret = new LinkedHashMap<>();
		MFlag flagPeaceful = MFlag.getFlagPeaceful();
		boolean peaceful = faction.getFlag(flagPeaceful);
		for (Rel rel : rels)
		{
			ret.put(rel, new ArrayList<String>());
		}

		// Fill
		for (Faction fac : FactionColl.get().getAll())
		{
			if (fac.getFlag(flagPeaceful)) continue;

			Rel rel = fac.getRelationTo(faction);
			List<String> names = ret.get(rel);
			if (names == null) continue;

			String name = fac.describeTo(faction, true);
			names.add(name);
		}

		// Replace TRUCE if peaceful
		if (!peaceful) return ret;

		List<String> names = ret.get(Rel.TRUCE);
		if (names == null) return ret;

		ret.put(Rel.TRUCE, Collections.singletonList(MConf.get().colorTruce.toString() + Txt.parse("<italic>*todos*")));

		// Return
		return ret;
	}

}
