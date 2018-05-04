package com.massivecraft.factions;

import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.Colorized;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.collections.MassiveSet;
import org.bukkit.ChatColor;


import java.util.Collections;
import java.util.Set;

public enum Rel implements Colorized, Named
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	ENEMY(
		"um inimigo", "inimigos", "§euma facção §cinimiga", "facção inimiga",
		"Inimigo"
	) { @Override public ChatColor getColor() { return MConf.get().colorEnemy; } },
	
	NEUTRAL(
		"um neutro", "neutros", "§euma facção §fneutra", "facção neutra",
		"Neutro"
	) { @Override public ChatColor getColor() { return MConf.get().colorNeutral; } },
	
	ALLY(
		"um aliado", "aliados", "§euma facção §baliada", "facção aliada",
		"Aliado"
	) { @Override public ChatColor getColor() { return MConf.get().colorAlly; } },
	
	TRUCE(
			"um trégua", "tréguas", "§euma facção em §8trégua", "facção em trégua",
			""
		) { @Override public ChatColor getColor() { return MConf.get().colorTruce; } },
	
	RECRUIT(
		"um recruta da sua facção", "recrutas da sua facção", "", "",
		"Recruta"
	) { @Override public String getPrefix() { return MConf.get().prefixRecruit; } },
	
	MEMBER(
		"um membro da sua facção", "membros da sua facção", "sua facção", "suas facções",
		"Membro"
	) { @Override public String getPrefix() { return MConf.get().prefixMember; } },
	
	OFFICER(
		"um capitão da sua facção", "capitões da sua facção", "", "",
		"Capitão", "Capitao"
	) { @Override public String getPrefix() { return MConf.get().prefixOfficer; } },
	
	LEADER(
		"líder da sua facção", "lider da sua facção", "", "",
		"Líder", "Lider", "Dono"
	) { @Override public String getPrefix() { return MConf.get().prefixLeader; } },
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public int getValue() { return this.ordinal(); }
	
	private final String descPlayerOne;
	public String getDescPlayerOne() { return this.descPlayerOne; }
	
	private final String descPlayerMany;
	public String getDescPlayerMany() { return this.descPlayerMany; }
	
	private final String descFactionOne;
	public String getDescFactionOne() { return this.descFactionOne; }
	
	private final String descFactionMany;
	public String getDescFactionMany() { return this.descFactionMany; }
	
	private final Set<String> names;
	public Set<String> getNames() { return this.names; }
	@Override public String getName() { return this.getNames().iterator().next(); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	Rel(String descPlayerOne, String descPlayerMany, String descFactionOne, String descFactionMany, String... names)
	{
		this.descPlayerOne = descPlayerOne;
		this.descPlayerMany = descPlayerMany;
		this.descFactionOne = descFactionOne;
		this.descFactionMany = descFactionMany;
		this.names = Collections.unmodifiableSet(new MassiveSet<>(names));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ChatColor getColor()
	{
		return MConf.get().colorMember;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public boolean isAtLeast(Rel rel)
	{
		return this.getValue() >= rel.getValue();
	}
	
	public boolean isAtMost(Rel rel)
	{
		return this.getValue() <= rel.getValue();
	}
	
	public boolean isLessThan(Rel rel)
	{
		return this.getValue() < rel.getValue();
	}
	
	public boolean isMoreThan(Rel rel)
	{
		return this.getValue() > rel.getValue();
	}
	
	public boolean isRank()
	{
		return this.isAtLeast(Rel.RECRUIT);
	}
	
	// Used for friendly fire.
	public boolean isFriend()
	{
		return this.isAtLeast(TRUCE);
	}
	
	public String getPrefix()
	{
		return "";
	}
	
}
