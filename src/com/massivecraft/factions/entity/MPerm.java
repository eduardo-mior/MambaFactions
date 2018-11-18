package com.massivecraft.factions.entity;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import com.massivecraft.factions.AccessStatus;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.TerritoryAccess;
import com.massivecraft.factions.event.EventFactionsCreatePerms;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.Prioritized;
import com.massivecraft.massivecore.Registerable;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.predicate.PredicateIsRegistered;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public class MPerm extends Entity<MPerm> implements Prioritized, Registerable, Named
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static transient String ID_BUILD = "build";
	public final static transient String ID_DOOR = "door";
	public final static transient String ID_BUTTON = "button";
	public final static transient String ID_LEVER = "lever";
	public final static transient String ID_CONTAINER = "container";
	
	public final static transient String ID_TERRITORY = "territory";
	public final static transient String ID_HOME = "home";
	public final static transient String ID_CLAIMNEAR = "claimnear";

	public final static transient int PRIORITY_BUILD = 1000;
	public final static transient int PRIORITY_DOOR = 2000;
	public final static transient int PRIORITY_BUTTON = 3000;
	public final static transient int PRIORITY_LEVER = 4000;
	public final static transient int PRIORITY_CONTAINER = 5000;
	
	public final static transient int PRIORITY_TERRITORY = 6000;
	public final static transient int PRIORITY_HOME = 7000;
	public final static transient int PRIORITY_CLAIMNEAR = 8000;
	
	// -------------------------------------------- //
	// META: CORE
	// -------------------------------------------- //
	
	public static MPerm get(Object oid)
	{
		return MPermColl.get().get(oid);
	}
	
	public static List<MPerm> getAll()
	{
		return getAll(false);
	}
	
	public static List<MPerm> getAll(boolean isAsync)
	{
		setupStandardPerms();
		new EventFactionsCreatePerms().run();
		
		return MPermColl.get().getAll(PredicateIsRegistered.get(), ComparatorSmart.get());
	}
	
	public static void setupStandardPerms()
	{
		getPermBuild();
		getPermDoor();
		getPermButton();
		getPermLever();
		getPermContainer();
		
		getPermTerritory();
		getPermHome();
		getPermClaimnear();
	}
	
	public static MPerm getPermBuild() { return getCreative(PRIORITY_BUILD, ID_BUILD, ID_BUILD, "editar o terreno", MUtil.set(Rel.LEADER, Rel.OFFICER, Rel.MEMBER), true); }
	public static MPerm getPermDoor() { return getCreative(PRIORITY_DOOR, ID_DOOR, ID_DOOR, "usar portas", MUtil.set(Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY), true); }
	public static MPerm getPermButton() { return getCreative(PRIORITY_BUTTON, ID_BUTTON, ID_BUTTON, "usar botões", MUtil.set(Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY), true); }
	public static MPerm getPermLever() { return getCreative(PRIORITY_LEVER, ID_LEVER, ID_LEVER, "usar alavancas", MUtil.set(Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY), true); }
	public static MPerm getPermContainer() { return getCreative(PRIORITY_CONTAINER, ID_CONTAINER, ID_CONTAINER, "usar containers", MUtil.set(Rel.LEADER, Rel.OFFICER, Rel.MEMBER), true); }
	
	public static MPerm getPermHome() { return getCreative(PRIORITY_HOME, ID_HOME, ID_HOME, "se teleportar para a home", MUtil.set(Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY), false); }
	public static MPerm getPermTerritory() { return getCreative(PRIORITY_TERRITORY, ID_TERRITORY, ID_TERRITORY, "administrar territórios", MUtil.set(Rel.LEADER, Rel.OFFICER), false); }
	public static MPerm getPermClaimnear() { return getCreative(PRIORITY_CLAIMNEAR, ID_CLAIMNEAR, ID_CLAIMNEAR, "claimar perto", MUtil.set(Rel.LEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT), false); }
	
	public static MPerm getCreative(int priority, String id, String name, String desc, Set<Rel> standard, boolean territory)
	{
		MPerm ret = MPermColl.get().get(id, false);
		if (ret != null)
		{
			ret.setRegistered(true);
			return ret;
		}
		
		ret = new MPerm(priority, name, desc, standard, territory);
		MPermColl.get().attach(ret, id);
		ret.setRegistered(true);
		ret.sync();
		
		return ret;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public MPerm load(MPerm that)
	{
		this.priority = that.priority;
		this.name = that.name;
		this.desc = that.desc;
		this.standard = that.standard;
		this.territory = that.territory;
		
		return this;
	}
	
	// -------------------------------------------- //
	// TRANSIENT FIELDS (Registered)
	// -------------------------------------------- //
	
	private transient boolean registered = false;
	public boolean isRegistered() { return this.registered; }
	public void setRegistered(boolean registered) { this.registered = registered; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// The sort priority. Low values appear first in sorted lists.
	// 1 is high up, 99999 is far down.
	// Standard Faction perms use "thousand values" like 1000, 2000, 3000 etc to allow adding new perms inbetween.
	// So 1000 might sound like a lot but it's actually the priority for the first perm.
	private int priority = 0;
	@Override public int getPriority() { return this.priority; }
	public MPerm setPriority(int priority) { this.priority = priority; this.changed(); return this; }
	
	// The name of the perm. According to standard it should be fully lowercase just like the perm id.
	// In fact the name and the id of all standard perms are the same.
	// I just added the name in case anyone feel like renaming their perms for some reason.
	// Example: "build"
	private String name = "defaultName";
	@Override public String getName() { return this.name; }
	public MPerm setName(String name) { this.name = name; this.changed(); return this; }
	
	// The perm function described as an "order".
	// The desc should match the format:
	// "You are not allowed to X."
	// "You are not allowed to edit the terrain."
	// Example: "edit the terrain"
	private String desc = "defaultDesc";
	public String getDesc() { return this.desc; }
	public MPerm setDesc(String desc) { this.desc = desc; this.changed(); return this; }
	
	// What is the standard (aka default) perm value?
	// This value will be set for factions from the beginning.
	// Example: ... set of relations ...
	private Set<Rel> standard = new LinkedHashSet<>();
	public Set<Rel> getStandard() { return this.standard; }
	public MPerm setStandard(Set<Rel> standard) { this.standard = standard; this.changed(); return this; }
	
	// Is this a territory perm meaning it has to do with territory construction, modification or interaction?
	// True Examples: build, container, door, lever etc.
	// False Examples: name, invite, home, sethome, etc.
	private boolean territory = false;
	public boolean isTerritory() { return this.territory; }
	public MPerm setTerritory(boolean territory) { this.territory = territory; this.changed(); return this; }

	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MPerm()
	{
		// No argument constructor for GSON
	}
	
	public MPerm(int priority, String name, String desc, Set<Rel> standard, boolean territory)
	{
		this.priority = priority;
		this.name = name;
		this.desc = desc;
		this.standard = standard;
		this.territory = territory;
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	public String createDeniedMessage(MPlayer mplayer, Faction hostFaction)
	{
		// Null Check
		if (mplayer == null) throw new NullPointerException("mplayer");
		if (hostFaction == null) throw new NullPointerException("hostFaction");
		
		String ret = Txt.parse("%s§c não deixa você §c%s§c.", hostFaction.describeTo(mplayer), this.getDesc());
		
		Player player = mplayer.getPlayer();
		if (player != null && Perm.ADMIN.has(player))
		{
			ret += Txt.parse("\n§eVocê pode burlar isso usando o comando §6/f admin§e" );
		}
		
		return ret;
	}
	
	public boolean has(Faction faction, Faction hostFaction)
	{
		// Null Check
		if (faction == null) throw new NullPointerException("faction");
		if (hostFaction == null) throw new NullPointerException("hostFaction");
		
		Rel rel = faction.getRelationTo(hostFaction);
		return hostFaction.isPermitted(this, rel);
	}
	
	public boolean has(MPlayer mplayer, Faction hostFaction, boolean verboose)
	{
		// Null Check
		if (mplayer == null) throw new NullPointerException("mplayer");
		if (hostFaction == null) throw new NullPointerException("hostFaction");
		
		if (mplayer.isOverriding()) return true;
		
		Rel rel = mplayer.getRelationTo(hostFaction);
		if (hostFaction.isPermitted(this, rel)) return true;
		
		if (verboose) mplayer.message(this.createDeniedMessage(mplayer, hostFaction));
		
		return false;
	}
	
	public boolean has(MPlayer mplayer, PS ps, boolean verboose)
	{
		// Null Check
		if (mplayer == null) throw new NullPointerException("mplayer");
		if (ps == null) throw new NullPointerException("ps");
		
		if (mplayer.isOverriding()) return true;
		
		TerritoryAccess ta = BoardColl.get().getTerritoryAccessAt(ps);
		Faction hostFaction = ta.getHostFaction();
		
		if (this.isTerritory())
		{
			AccessStatus accessStatus = ta.getTerritoryAccess(mplayer);
			if (accessStatus != AccessStatus.STANDARD)
			{
				if (verboose && accessStatus == AccessStatus.DECREASED)
				{
					mplayer.message(this.createDeniedMessage(mplayer, hostFaction));
				}
				
				return accessStatus.hasAccess();
			}
		}
		
		return this.has(mplayer, hostFaction, verboose);
	}

	// -------------------------------------------- //
	// UTIL: ASCII
	// -------------------------------------------- //
	
	public static String getStateHeaders()
	{
		String ret = "";
		for (Rel rel : Rel.values())
		{
			ret += rel.getColor().toString();
			ret += rel.toString().substring(0, 3);
			ret += " ";
		}
		
		return ret;
	}

	// -------------------------------------------- //
	// UTIL: RESET
	// -------------------------------------------- //
	
	public static void resetDefaultPermissions(Faction faction) {
		faction.getPermitted(MPerm.getPermBuild()).remove(Rel.ALLY);
		faction.getPermitted(MPerm.getPermBuild()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermBuild()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermBuild()).remove(Rel.NEUTRAL);
		faction.getPermitted(MPerm.getPermBuild()).remove(Rel.RECRUIT);
		faction.getPermitted(MPerm.getPermContainer()).remove(Rel.ALLY);
		faction.getPermitted(MPerm.getPermContainer()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermContainer()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermContainer()).remove(Rel.NEUTRAL);
		faction.getPermitted(MPerm.getPermContainer()).remove(Rel.RECRUIT);
		faction.getPermitted(MPerm.getPermHome()).remove(Rel.ALLY);
		faction.getPermitted(MPerm.getPermHome()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermHome()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermHome()).remove(Rel.NEUTRAL);
		faction.getPermitted(MPerm.getPermHome()).add(Rel.RECRUIT);
		faction.getPermitted(MPerm.getPermDoor()).add(Rel.RECRUIT);
		faction.getPermitted(MPerm.getPermDoor()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermDoor()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermDoor()).remove(Rel.NEUTRAL);
		faction.getPermitted(MPerm.getPermButton()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermButton()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermButton()).remove(Rel.NEUTRAL);
		faction.getPermitted(MPerm.getPermLever()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermLever()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermLever()).remove(Rel.NEUTRAL);
	}
}
