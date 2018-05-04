package com.massivecraft.factions.entity;

import java.util.List;

import com.massivecraft.factions.event.EventFactionsCreateFlags;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.Prioritized;
import com.massivecraft.massivecore.Registerable;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.predicate.PredicateIsRegistered;
import com.massivecraft.massivecore.store.Entity;

public class MFlag extends Entity<MFlag> implements Prioritized, Registerable, Named
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static transient String ID_POWERLOSS = "powerloss";
	public final static transient String ID_PVP = "pvp";
	public final static transient String ID_FRIENDLYFIRE = "friendlyfire";
	public final static transient String ID_EXPLOSIONS = "explosions";
	public final static transient String ID_PERMANENT = "permanent";
	public final static transient String ID_PEACEFUL = "peaceful";

	public final static transient String ID_INFPOWER = "infpower";
	
	public final static transient int PRIORITY_POWERLOSS = 3_000;
	public final static transient int PRIORITY_PVP = 4_000;
	public final static transient int PRIORITY_FRIENDLYFIRE = 5_000;
	public final static transient int PRIORITY_EXPLOSIONS = 6_000;
	public final static transient int PRIORITY_PERMANENT = 7_000;
	public final static transient int PRIORITY_PEACEFUL = 8_000;
	public final static transient int PRIORITY_INFPOWER = 9_000;
	
	// -------------------------------------------- //
	// META: CORE
	// -------------------------------------------- //
	
	public static MFlag get(Object oid)
	{
		return MFlagColl.get().get(oid);
	}
	
	public static List<MFlag> getAll()
	{
		return getAll(false);
	}
	
	public static List<MFlag> getAll(boolean isAsync)
	{
		setupStandardFlags();
		new EventFactionsCreateFlags(isAsync).run();
		return MFlagColl.get().getAll(PredicateIsRegistered.get(), ComparatorSmart.get());
	}
	
	public static void setupStandardFlags()
	{
		getFlagPowerloss();
		getFlagPvp();
		getFlagFriendlyire();
		getFlagPermanent();
		getFlagPeaceful();
		getFlagInfpower();
	}
	
	public static MFlag getFlagPowerloss() { return getCreative(PRIORITY_POWERLOSS, ID_POWERLOSS, ID_POWERLOSS, "Ao morrer neste território, você ira perder poder? ", true); }
	public static MFlag getFlagPvp() { return getCreative(PRIORITY_PVP, ID_PVP, ID_PVP, "O pvp podera acontecer no território da facção?", true); }
	public static MFlag getFlagFriendlyire() { return getCreative(PRIORITY_FRIENDLYFIRE, ID_FRIENDLYFIRE, ID_FRIENDLYFIRE, "Membros da facção e aliados poderão se bater no território da facção?", false); }
	public static MFlag getFlagPermanent() { return getCreative(PRIORITY_PERMANENT, ID_PERMANENT, ID_PERMANENT, "A facção não pode ser deletada (permanente)?", false); }
	public static MFlag getFlagPeaceful() { return getCreative(PRIORITY_PEACEFUL, ID_PEACEFUL, ID_PEACEFUL, "A facção esta em trégua com todos?", false); }
	public static MFlag getFlagInfpower() { return getCreative(PRIORITY_INFPOWER, ID_INFPOWER, ID_INFPOWER, "A facção possui poder ifinito?", false); }
	
	public static MFlag getCreative(int priority, String id, String nome, String desc, boolean padrao)
	{
		MFlag ret = MFlagColl.get().get(id, false);
		if (ret != null)
		{
			ret.setRegistered(true);
			return ret;
		}
		
		ret = new MFlag(priority, nome, desc, padrao);
		MFlagColl.get().attach(ret, id);
		ret.setRegistered(true);
		ret.sync();
		
		return ret;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public MFlag load(MFlag that)
	{
		this.priority = that.priority;
		this.nome = that.nome;
		this.desc = that.desc;
		this.padrao = that.padrao;
		
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
	// Standard Faction flags use "thousand values" like 1000, 2000, 3000 etc to allow adding new flags inbetween.
	// So 1000 might sound like a lot but it's actually the priority for the first flag.
	private int priority = 0;
	@Override public int getPriority() { return this.priority; }
	public MFlag setPriority(int priority) { this.priority = priority; this.changed(); return this; }
	
	// The name of the flag. According to standard it should be fully lowercase just like the flag id.
	// In fact the name and the id of all standard flags are the same.
	// I just added the name in case anyone feel like renaming their flags for some reason.
	// Example: "monsters"
	private String nome = "defaultNome";
	public String getNome() { return this.nome; }
	public MFlag setNome(String nome) { this.nome = nome; this.changed(); return this; }
	
	// The flag function described as a question.
	// Example: "Can monsters spawn in this territory?"
	private String desc = "defaultDesc";
	public String getDesc() { return this.desc; }
	public MFlag setDesc(String desc) { this.desc = desc; this.changed(); return this; }
	
	// What is the standard (aka default) flag value?
	// This value will be set for factions from the beginning.
	// Example: false (per default monsters do not spawn in faction territory)
	private boolean padrao = true;
	public boolean isPadrao() { return this.padrao; }
	public MFlag setPadrao(boolean padrao) { this.padrao = padrao; this.changed(); return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MFlag()
	{
		// No argument constructor for GSON
	}
	
	public MFlag(int priority, String nome, String desc, boolean padrao)
	{
		this.priority = priority;
		this.nome = nome;
		this.desc = desc;
		this.padrao = padrao;
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
