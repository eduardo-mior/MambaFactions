package com.massivecraft.factions.entity;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.massivecraft.factions.FactionsIndex;
import com.massivecraft.factions.FactionsParticipator;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.RelationParticipator;
import com.massivecraft.factions.event.EventFactionsChunkChangeType;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.factions.util.RelationUtil;
import com.massivecraft.massivecore.mixin.MixinSenderPs;
import com.massivecraft.massivecore.mixin.MixinTitle;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSFormatHumanSpace;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.annotations.SerializedName;

public class MPlayer extends SenderEntity<MPlayer> implements FactionsParticipator
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //

	public static MPlayer get(Object oid)
	{
		return MPlayerColl.get().get(oid);
	}

	// -------------------------------------------- //
	// LOAD
	// -------------------------------------------- //

	@Override
	public MPlayer load(MPlayer that)
	{
		this.setLastActivityMillis(that.lastActivityMillis);
		this.setFactionId(that.factionId);
		this.setRole(that.role);
		this.setPowerBoost(that.powerBoost);
		this.setPower(that.power);
		this.setMapAutoUpdating(that.mapAutoUpdating);
		this.setOverriding(that.overriding);
		this.setTerritoryInfoTitles(that.territoryInfoTitles);
		this.setKills(that.kills);
		this.setDeaths(that.deaths);
		return this;
	}
	
	// -------------------------------------------- //
	// IS DEFAULT
	// -------------------------------------------- //

	@Override
	public boolean isDefault()
	{
		// Last activity millis is data we use for clearing out cleanable players. So it does not in itself make the player data worth keeping.
		if (this.hasFaction()) return false;
		// Role means nothing without a faction.
		// Title means nothing without a faction.
		if (this.hasPowerBoost()) return false;
		if (this.getPowerRounded() != (int) Math.round(MConf.get().defaultPlayerPower)) return false;
		// if (this.isMapAutoUpdating()) return false; // Just having an auto updating map is not in itself reason enough for database storage.
		if (this.isOverriding()) return false;
		if (this.isTerritoryInfoTitles() != MConf.get().territoryInfoTitlesDefault) return false;

		return true;
	}

	// -------------------------------------------- //
	// UPDATE FACTION INDEXES
	// -------------------------------------------- //

	@Override
	public void postAttach(String id)
	{
		FactionsIndex.get().update(this);
	}

	@Override
	public void preDetach(String id)
	{
		FactionsIndex.get().update(this);
	}
	
	@Override
	public void preClean()
	{
		if (this.getRole() == Rel.LEADER)
		{
			this.getFaction().promoteNewLeader();
		}
		
		this.leave();
	}

	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	// In this section of the source code we place the field declarations only.
	// Each field has it's own section further down since just the getter and setter logic takes up quite some place.

	// The last known time of explicit player activity, such as login or logout.
	// This value is most importantly used for removing cleanable players.
	// For that reason it defaults to the current time.
	// Really cleanable players will be considered newly active when upgrading Factions from 2.6 --> 2.7.
	// There is actually more than one reason we store this data ourselves and don't use the OfflinePlayer#getLastPlayed.
	// 1. I don't trust that method. It's been very buggy or even completely broken in previous Bukkit versions.
	// 2. The method depends on the player.dat files being present.
	// Server owners clear those files at times, or move their database data around between different servers.
	private long lastActivityMillis = System.currentTimeMillis();

	// This is a foreign key.
	// Each player belong to a faction.
	// Null means default.
	private String factionId = null;

	// What role does the player have in the faction?
	// Null means default.
	private Rel role = null;

	// Player usually do not have a powerboost. It defaults to 0.
	// The powerBoost is a custom increase/decrease to default and maximum power.
	// Note that player powerBoost and faction powerBoost are very similar.
	private Double powerBoost = null;

	// Each player has an individual power level.
	// The power level for online players is occasionally updated by a recurring task and the power should stay the same for offline players.
	// For that reason the value is to be considered correct when you pick it. Do not call the power update method.
	// Null means default.
	private Double power = null;

	// Has this player requested an auto-updating ascii art map?
	// Null means false
	private Boolean mapAutoUpdating = null;

	// Is this player overriding?
	// Null means false
	@SerializedName(value = "usingAdminMode")
	private Boolean overriding = null;

	// Does this player use titles for territory info?
	// Null means default specified in MConf.
	private Boolean territoryInfoTitles = null;
	
	// The kills and deaths of the player
	// The KDR is calculated so it does not need to be saved 
	private Integer kills = null;
	private Integer deaths = null;
	
	// The invitations of de factions
	private transient Set<String> invitations = new HashSet<>();
	
	// The Faction this player is currently autoclaiming for.
	// Null means the player isn't auto claiming.
	// NOTE: This field will not be saved to the database ever.
	private transient WeakReference<Faction> autoClaimFaction = new WeakReference<>(null);

	public Faction getAutoClaimFaction()
	{
		if (this.isFactionOrphan()) return null;
		Faction ret = this.autoClaimFaction.get();
		if (ret == null) return null;
		if (ret.detached()) return null;
		return ret;
	}
	public void setAutoClaimFaction(Faction autoClaimFaction) { this.autoClaimFaction = new WeakReference<>(autoClaimFaction); }

	// Does the player have /f seechunk activated?
	// NOTE: This field will not be saved to the database ever.
	private transient boolean seeingChunk = false;
	public boolean isSeeingChunk() { return this.seeingChunk; }
	public void setSeeingChunk(boolean seeingChunk) { this.seeingChunk = seeingChunk; }

	// -------------------------------------------- //
	// CORE UTILITIES
	// -------------------------------------------- //

	public void resetFactionData()
	{
		// The default neutral faction
		this.setFactionId(null);
		this.setRole(null);
		this.setAutoClaimFaction(null);
	}

	// -------------------------------------------- //
	// FIELD: lastActivityMillis
	// -------------------------------------------- //

	public long getLastActivityMillis()
	{
		return this.lastActivityMillis;
	}

	public void setLastActivityMillis(long lastActivityMillis)
	{
		// Clean input
		long target = lastActivityMillis;

		// Detect Nochange
		if (MUtil.equals(this.lastActivityMillis, target)) return;

		// Apply
		this.lastActivityMillis = target;

		// Mark as changed
		this.changed();
	}

	public void setLastActivityMillis()
	{
		this.setLastActivityMillis(System.currentTimeMillis());
	}
	
	@Override
	public boolean shouldBeCleaned(long now)
	{
		return this.shouldBeCleaned(now, this.lastActivityMillis);
	}
	
	// -------------------------------------------- //
	// FIELD: factionId
	// -------------------------------------------- //
	
	private Faction getFactionInternal()
	{
		String effectiveFactionId = this.convertGet(this.factionId, MConf.get().defaultPlayerFactionId);
		return Faction.get(effectiveFactionId);
	}
	
	public boolean isFactionOrphan()
	{
		return this.getFactionInternal() == null;
	}

	@Deprecated
	public String getFactionId()
	{
		return this.getFaction().getId();
	}

	// This method never returns null
	public Faction getFaction()
	{
		Faction ret;
		
		ret = this.getFactionInternal();
		
		// Adopt orphans
		if (ret == null)
		{
			ret = FactionColl.get().getNone();
		}
		
		return ret;
	}
	
	public boolean hasFaction()
	{
		return !this.getFaction().isNone();
	}

	// This setter is so long because it search for default/null case and takes
	// care of updating the faction member index
	public void setFactionId(String factionId)
	{
		// Before
		String beforeId = this.factionId;

		// After
		String afterId = factionId;

		// NoChange
		if (MUtil.equals(beforeId, afterId)) return;

		// Apply
		this.factionId = afterId;

		// Index
		FactionsIndex.get().update(this);

		// Mark as changed
		this.changed();
	}

	public void setFaction(Faction faction)
	{
		this.setFactionId(faction.getId());
	}

	// -------------------------------------------- //
	// FIELD: role
	// -------------------------------------------- //

	public Rel getRole()
	{
		if (this.isFactionOrphan()) return Rel.RECRUIT;
		
		if (this.role == null) return MConf.get().defaultPlayerRole;
		return this.role;
	}

	public void setRole(Rel role)
	{
		// Clean input
		Rel target = role;

		// Detect Nochange
		if (MUtil.equals(this.role, target)) return;

		// Apply
		this.role = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: powerBoost
	// -------------------------------------------- //

	@Override
	public double getPowerBoost()
	{
		Double ret = this.powerBoost;
		if (ret == null) ret = 0D;
		return ret;
	}

	@Override
	public void setPowerBoost(Double powerBoost)
	{
		// Clean input
		Double target = powerBoost;
		if (target == null || target == 0) target = null;

		// Detect Nochange
		if (MUtil.equals(this.powerBoost, target)) return;

		// Apply
		this.powerBoost = target;

		// Mark as changed
		this.changed();
	}

	public boolean hasPowerBoost()
	{
		return this.getPowerBoost() != 0D;
	}

	// -------------------------------------------- //
	// FIELD: power
	// -------------------------------------------- //

	// MIXIN: RAW

	public double getPowerMaxUniversal()
	{
		return this.getPowerMax();
	}

	public double getPowerMax()
	{
		return MConf.get().powerMax + this.getPowerBoost();
	}

	public double getPowerMin()
	{
		return MConf.get().powerMin;
	}

	public double getPowerPerHour()
	{
		return MConf.get().powerPerHour;
	}

	public double getPowerPerDeath()
	{
		return MConf.get().powerPerDeath;
	}

	// MIXIN: FINER

	public double getLimitedPower(double power)
	{
		power = Math.max(power, this.getPowerMin());
		power = Math.min(power, this.getPowerMax());

		return power;
	}

	public int getPowerMaxRounded()
	{
		return (int) Math.round(this.getPowerMax());
	}

	public int getPowerMinRounded()
	{
		return (int) Math.round(this.getPowerMin());
	}

	public int getPowerMaxUniversalRounded()
	{
		return (int) Math.round(this.getPowerMaxUniversal());
	}

	// RAW

	@Deprecated
	public double getDefaultPower()
	{
		return MConf.get().defaultPlayerPower;
	}

	public double getPower()
	{
		Double ret = this.power;
		if (ret == null) ret = MConf.get().defaultPlayerPower;
		ret = this.getLimitedPower(ret);
		return ret;
	}

	public void setPower(Double power)
	{
		// Clean input
		Double target = power;

		// Detect Nochange
		if (MUtil.equals(this.power, target)) return;

		// Apply
		this.power = target;

		// Mark as changed
		this.changed();
	}

	// FINER

	public int getPowerRounded()
	{
		return (int) Math.round(this.getPower());
	}

	// -------------------------------------------- //
	// FIELD: mapAutoUpdating
	// -------------------------------------------- //

	public boolean isMapAutoUpdating()
	{
		if (this.mapAutoUpdating == null) return false;
		if (this.mapAutoUpdating == false) return false;
		return true;
	}

	public void setMapAutoUpdating(Boolean mapAutoUpdating)
	{
		// Clean input
		Boolean target = mapAutoUpdating;
		if (MUtil.equals(target, false)) target = null;

		// Detect Nochange
		if (MUtil.equals(this.mapAutoUpdating, target)) return;

		// Apply
		this.mapAutoUpdating = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: overriding
	// -------------------------------------------- //

	public boolean isOverriding()
	{
		if (this.overriding == null) return false;
		if (this.overriding == false) return false;

		if (!this.hasPermission(Perm.ADMIN, true))
		{
			this.setOverriding(false);
			return false;
		}

		return true;
	}

	public void setOverriding(Boolean overriding)
	{
		// Clean input
		Boolean target = overriding;
		if (MUtil.equals(target, false)) target = null;

		// Detect Nochange
		if (MUtil.equals(this.overriding, target)) return;

		// Apply
		this.overriding = target;

		// Mark as changed
		this.changed();
	}
	
	// -------------------------------------------- //
	// FIELD: Deaths
	// -------------------------------------------- //
	
	public void setDeaths(Integer deaths) 
	{
		Integer target = deaths;
		
		// Detect Nochange
		if (MUtil.equals(this.deaths, target)) return;

		// Apply
		this.deaths = target;

		// Mark as changed
		this.changed();
	}

	public Integer getDeaths() 
	{
		Integer deaths = this.deaths;
		if (deaths == null) deaths = 0;
		return deaths;
	}
	
	// -------------------------------------------- //
	// FIELD: Kills
	// -------------------------------------------- //
	
	public void setKills(Integer kills)
	{
		Integer target = kills;
		
		// Detect Nochange
		if (MUtil.equals(this.kills, target)) return;

		// Apply
		this.kills = target;

		// Mark as changed
		this.changed();
	}
	
	public Integer getKills()
	{
		Integer kills = this.kills;
		if (kills == null) kills = 0;
		return kills;
	}
	
	// -------------------------------------------- //
	// FIELD: Kdr
	// -------------------------------------------- //
	
	public double getKdr() 
	{
		double kills = this.getKills();
		double deaths = this.getDeaths();
		if (deaths == 0) {
			return kills;
		} else {
			return kills/deaths;
		}
	}
	
	public String getKdrRounded() 
	{
		return String.format("%.2f", this.getKdr());
	}
	
	// -------------------------------------------- //
	// INVITATIONS
	// -------------------------------------------- //
	
	public Set<String> getInvitations()
	{
		return this.invitations;
	}
	
	public void addInvitation(Faction faction) 
	{
		// Apply
		this.invitations.add(faction.getId());
	}
	
	public void removeInvitation(Faction faction) 
	{		
		// Apply
		this.invitations.remove(faction.getId());
	}
	
	public void clearInvitations() 
	{
		// Apply
		this.invitations.clear();
	}
	
	// -------------------------------------------- //
	// FIELD: territoryInfoTitles
	// -------------------------------------------- //

	public boolean isTerritoryInfoTitles()
	{
		if (!MixinTitle.get().isAvailable()) return false;
		if (this.territoryInfoTitles == null) return MConf.get().territoryInfoTitlesDefault;
		return this.territoryInfoTitles;
	}

	public void setTerritoryInfoTitles(Boolean territoryInfoTitles)
	{
		// Clean input
		Boolean target = territoryInfoTitles;
		if (MUtil.equals(target, MConf.get().territoryInfoTitlesDefault)) target = null;

		// Detect Nochange
		if (MUtil.equals(this.territoryInfoTitles, target)) return;

		// Apply
		this.territoryInfoTitles = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// TITLE, NAME, FACTION NAME AND CHAT
	// -------------------------------------------- //

	public String getFactionName()
	{
		Faction faction = this.getFaction();
		if (faction.isNone()) return "";
		return faction.getName();
	}

	// Base concatenations:

	public String getNameAndSomething(String color, String something)
	{
		String ret = "";
		ret += color;
		ret += this.getRole().getPrefix();
		if (something != null && something.length() > 0)
		{
			ret += something;
			ret += " ";
			ret += color;
		}
		ret += this.getName();
		return ret;
	}

	public String getNameAndFactionName()
	{
		return this.getNameAndSomething("", this.getFactionName());
	}

	// -------------------------------------------- //
	// RELATION AND RELATION COLORS
	// -------------------------------------------- //

	@Override
	public String describeTo(RelationParticipator observer, boolean ucfirst)
	{
		return RelationUtil.describeThatToMe(this, observer, ucfirst);
	}

	@Override
	public String describeTo(RelationParticipator observer)
	{
		return RelationUtil.describeThatToMe(this, observer);
	}

	@Override
	public Rel getRelationTo(RelationParticipator observer)
	{
		return RelationUtil.getRelationOfThatToMe(this, observer);
	}

	@Override
	public Rel getRelationTo(RelationParticipator observer, boolean ignorePeaceful)
	{
		return RelationUtil.getRelationOfThatToMe(this, observer, ignorePeaceful);
	}

	@Override
	public ChatColor getColorTo(RelationParticipator observer)
	{
		return RelationUtil.getColorOfThatToMe(this, observer);
	}

	// -------------------------------------------- //
	// TERRITORY
	// -------------------------------------------- //

	public boolean isInOwnTerritory()
	{
		PS ps = MixinSenderPs.get().getSenderPs(this.getId());
		if (ps == null) return false;
		return BoardColl.get().getFactionAt(ps) == this.getFaction();
	}

	public boolean isInEnemyTerritory()
	{
		PS ps = MixinSenderPs.get().getSenderPs(this.getId());
		if (ps == null) return false;
		return BoardColl.get().getFactionAt(ps).getRelationTo(this) == Rel.ENEMY;
	}
	
	// -------------------------------------------- //
	// ACTIONS
	// -------------------------------------------- //

	public void leave()
	{
		Faction myFaction = this.getFaction();

		boolean permanent = myFaction.getFlag(MFlag.getFlagPermanent());

		// Event
		EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(this.getSender(), this, myFaction, MembershipChangeReason.LEAVE);
		membershipChangeEvent.run();
		if (membershipChangeEvent.isCancelled()) return;

		if (myFaction.isNormal())
		{
			for (MPlayer mplayer : myFaction.getMPlayersWhereOnline(true))
			{
				mplayer.msg("�f%s�e abandonou a fac��o.", this.describeTo(mplayer).replace("Voc�", "�eVoc�"));
			}
		}

		this.resetFactionData();

		if (myFaction.isNormal() && !permanent && myFaction.getMPlayers().isEmpty())
		{
			EventFactionsDisband eventFactionsDisband = new EventFactionsDisband(this.getSender(), myFaction);
			eventFactionsDisband.run();
			if (!eventFactionsDisband.isCancelled())
			{
				// Remove this faction
				this.msg("�eA fac��o �f[%s�f]�e foi desfeita pois voc� abandonou a fac��o e voc� era o ultimo membro da fa�c�o.", myFaction.getName());
				myFaction.detach();
			}
		}
	}

	// NEW
	public boolean tryClaim(Faction newFaction, Collection<PS> pss)
	{
		return this.tryClaim(newFaction, pss, null, null);
	}

	public boolean tryClaim(Faction newFaction, Collection<PS> pss, String formatOne, String formatMany)
	{
		// Args
		if (formatOne == null) formatOne = "�a%s�a %s �d%d �achunk�a.";
		if (formatMany == null) formatMany = "�a%s�a %s �d%d �achunks pr�ximas�a.";

		final Set<PS> chunks = PS.getDistinctChunks(pss);

		// NoChange
		// We clean the chunks further by removing what does not change.
		// This is also very suggested cleaning of EventFactionsChunksChange input.
		Iterator<PS> iter = chunks.iterator();
		while (iter.hasNext())
		{
			PS chunk = iter.next();
			Faction oldFaction = BoardColl.get().getFactionAt(chunk);
			if (newFaction == oldFaction) iter.remove();
		}
		if (chunks.isEmpty())
		{
			if (newFaction == this.getFaction()) 
			{
				msg(("�eSua fac��o j� � dona deste territ�rio."));
			}
			else 
			{
				msg(("�eSua fac��o n�o � dona deste territ�rio."));
			}
			return true;
		}

		// Event
		// NOTE: We listen to this event ourselves at LOW.
		// NOTE: That is where we apply the standard checks.
		CommandSender sender = this.getSender();
		EventFactionsChunksChange event = new EventFactionsChunksChange(sender, chunks, newFaction);
		event.run();
		if (event.isCancelled()) return false;

		// Apply
		for (PS chunk : chunks)
		{
			BoardColl.get().setFactionAt(chunk, newFaction);
		}

		// Inform
		for (Entry<Faction, Set<PS>> entry : event.getOldFactionChunks().entrySet())
		{
			final Faction oldFaction = entry.getKey();
			final Set<PS> oldChunks = entry.getValue();
			final PS oldChunk = oldChunks.iterator().next();
			final EventFactionsChunkChangeType type = EventFactionsChunkChangeType.get(oldFaction, newFaction, this.getFaction());

			String chunkString = oldChunk.toString(PSFormatHumanSpace.get());
			String typeString = type.past;
			boolean format = oldChunks.size() == 1;

			// Verificando se uma terra est� sendo dominada...
			if (!oldFaction.isNone() && !newFaction.isNone()) 
			{
				for (MPlayer informee : oldFaction.getMPlayers())
				{
					informee.msg("�eSua fac��o teve �d%d�e " + (format ? "terreno dominado" : " terrenos dominados") + " pela �f[%s�f]�e.", oldChunks.size(), newFaction.getName());
				}	
					
				for (MPlayer informee : newFaction.getMPlayers())
				{
					informee.msg((format ? formatOne : formatMany), name(informee), typeString, oldChunks.size(), chunkString);
					informee.msg("�f%s�e --> �a%s", oldFaction.getName(), "�a" + newFaction.describeTo(informee));
				}
			}
			
			// Caso contrario um CLAIM ou um UNCLAIM normal esta ocorrendo...
			else
			{
				Set<MPlayer> informees = getClaimInformees(this, newFaction, oldFaction);
				for (MPlayer informee : informees)
				{
					informee.msg((format ? formatOne : formatMany), name(informee), typeString, oldChunks.size(), chunkString);
					informee.msg("�f%s�e --> �a%s", oldFaction.getName(), newFaction.isNone() ? "�2" + newFaction.describeTo(informee) : "�a" + newFaction.describeTo(informee));
				}
			}
			
		}
		// Success
		return true;
	}

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	private String name(MPlayer mp) {
		if (this.getId().equals(mp.getId())) return "�aVoc�";
		else return "�a" + this.getRole().getPrefix() + this.getName();
	}

	private Set<MPlayer> getClaimInformees(MPlayer msender, Faction... factions)
	{
		Set<MPlayer> ret = new HashSet<>();

		if (msender != null) ret.add(msender);

		for (Faction faction : factions)
		{
			if (faction == null) continue;
			if (faction.isNone()) continue;
			ret.addAll(faction.getMPlayers());
		}

		return ret;
	}

}
