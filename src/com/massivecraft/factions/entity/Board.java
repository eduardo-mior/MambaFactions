package com.massivecraft.factions.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.RelationParticipator;
import com.massivecraft.factions.TerritoryAccess;
import com.massivecraft.factions.engine.EngineSobAtaque;
import com.massivecraft.factions.util.AsciiCompass;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

public class Board extends Entity<Board> implements BoardInterface
{
	public static final transient Type MAP_TYPE = new TypeToken<Map<PS, TerritoryAccess>>(){}.getType();
	private static final transient String SQUARE = "\u2588";
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static Board get(Object oid)
	{
		return BoardColl.get().get(oid);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public Board load(Board that)
	{
		this.map = that.map;
		
		return this;
	}
	
	@Override
	public boolean isDefault()
	{
		if (this.map == null) return true;
		if (this.map.isEmpty()) return true;
		return false;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// TODO: Make TerritoryAccess immutable.
	
	private ConcurrentSkipListMap<PS, TerritoryAccess> map;
	public Map<PS, TerritoryAccess> getMap() { return Collections.unmodifiableMap(this.map); }
	public Map<PS, TerritoryAccess> getMapRaw() { return this.map; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Board()
	{
		this.map = new ConcurrentSkipListMap<>();
	}
	
	public Board(Map<PS, TerritoryAccess> map)
	{
		this.map = new ConcurrentSkipListMap<>(map);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: BOARD
	// -------------------------------------------- //
	
	// GET
	
	@Override
	public TerritoryAccess getTerritoryAccessAt(PS ps)
	{
		if (ps == null) return null;
		ps = ps.getChunkCoords(true);
		TerritoryAccess ret = this.map.get(ps);
		if (ret == null || ret.getHostFaction() == null) ret = TerritoryAccess.valueOf(Factions.ID_NONE);
		return ret;
	}
	
	@Override
	public Faction getFactionAt(PS ps)
	{
		if (ps == null) return null;
		TerritoryAccess ta = this.getTerritoryAccessAt(ps);
		return ta.getHostFaction();
	}
	
	// SET
	
	@Override
	public void setTerritoryAccessAt(PS ps, TerritoryAccess territoryAccess)
	{
		ps = ps.getChunkCoords(true);
		
		if (territoryAccess == null || (territoryAccess.getHostFactionId().equals(Factions.ID_NONE) && territoryAccess.isDefault()))
		{	
			this.map.remove(ps);
		}
		else
		{
			this.map.put(ps, territoryAccess);
		}
		
		this.changed();
	}
	
	@Override
	public void setFactionAt(PS ps, Faction faction)
	{
		TerritoryAccess territoryAccess = null;
		if (faction != null)
		{
			territoryAccess = TerritoryAccess.valueOf(faction.getId());
		}
		this.setTerritoryAccessAt(ps, territoryAccess);
	}
	
	// REMOVE
	
	@Override
	public void removeAt(PS ps)
	{
		this.setTerritoryAccessAt(ps, null);
	}
	
	@Override
	public void removeAll(Faction faction)
	{
		String factionId = faction.getId();
		
		for (Entry<PS, TerritoryAccess> entry : this.map.entrySet())
		{
			TerritoryAccess territoryAccess = entry.getValue();
			if ( ! territoryAccess.getHostFactionId().equals(factionId)) continue;
			
			PS ps = entry.getKey();
			this.removeAt(ps);
		}
	}
	
	// CHUNKS
	
	@Override
	public Set<PS> getChunks(Faction faction)
	{
		return this.getChunks(faction.getId());
	}
	
	@Override
	public Set<PS> getChunks(String factionId)
	{
		Set<PS> ret = new HashSet<>();
		for (Entry<PS, TerritoryAccess> entry : this.map.entrySet())
		{
			TerritoryAccess ta = entry.getValue();
			if (!ta.getHostFactionId().equals(factionId)) continue;
			
			PS ps = entry.getKey();
			ps = ps.withWorld(this.getId());
			ret.add(ps);
		}
		return ret;
	}
	
	@Override
	public Map<Faction, Set<PS>> getFactionToChunks()
	{
		Map<Faction, Set<PS>> ret = new MassiveMap<>();
		
		for (Entry<PS, TerritoryAccess> entry : this.map.entrySet())
		{
			// Get Faction
			TerritoryAccess ta = entry.getValue();
			Faction faction = ta.getHostFaction();
			if (faction == null) continue;
			
			// Get Chunks
			Set<PS> chunks = ret.get(faction);
			if (chunks == null)
			{
				chunks = new MassiveSet<>();
				ret.put(faction, chunks);
			}
			
			// Add Chunk
			PS chunk = entry.getKey();
			chunk = chunk.withWorld(this.getId());
			chunks.add(chunk);
		}
		
		return ret;
	}
	
	// COUNT
	
	@Override
	public int getCount(Faction faction)
	{
		return this.getCount(faction.getId());
	}
	
	@Override
	public int getCount(String factionId)
	{
		int ret = 0;
		for (TerritoryAccess ta : this.map.values())
		{
			if (!ta.getHostFactionId().equals(factionId)) continue;
			ret += 1;
		}
		return ret;
	}
	
	@Override
	public Map<Faction, Integer> getFactionToCount()
	{
		Map<Faction, Integer> ret = new MassiveMap<>();
		
		for (Entry<PS, TerritoryAccess> entry : this.map.entrySet())
		{
			// Get Faction
			TerritoryAccess ta = entry.getValue();
			Faction faction = ta.getHostFaction();
			if (faction == null) continue;
			
			// Get Count
			Integer count = ret.get(faction);
			if (count == null)
			{
				count = 0;
			}
			
			// Add Chunk
			ret.put(faction, count + 1);
		}
		
		return ret;
	}
	
	// CLAIMED
	
	@Override
	public boolean hasClaimed(Faction faction)
	{
		return this.hasClaimed(faction.getId());
	}
	
	@Override
	public boolean hasClaimed(String factionId)
	{
		for (TerritoryAccess ta : this.map.values())
		{
			if ( ! ta.getHostFactionId().equals(factionId)) continue;
			return true;
		}
		return false;
	}
	
	// NEARBY DETECTION
		
	// Is this coord NOT completely surrounded by coords claimed by the same faction?
	// Simpler: Is there any nearby coord with a faction other than the faction here?
	@Override
	public boolean isBorderPs(PS ps)
	{
		ps = ps.getChunk(true);
		
		PS nearby = null;
		Faction faction = this.getFactionAt(ps);
		
		nearby = ps.withChunkX(ps.getChunkX() +1);
		if (faction != this.getFactionAt(nearby)) return true;
		
		nearby = ps.withChunkX(ps.getChunkX() -1);
		if (faction != this.getFactionAt(nearby)) return true;
		
		nearby = ps.withChunkZ(ps.getChunkZ() +1);
		if (faction != this.getFactionAt(nearby)) return true;
		
		nearby = ps.withChunkZ(ps.getChunkZ() -1);
		if (faction != this.getFactionAt(nearby)) return true;
		
		return false;
	}

	@Override
	public boolean isAnyBorderPs(Set<PS> pss)
	{
		for (PS ps : pss)
		{
			if (this.isBorderPs(ps)) return true;
		}
		return false;
	}

	// Is this coord connected to any coord claimed by the specified faction?
	@Override
	public boolean isConnectedPs(PS ps, Faction faction)
	{
		ps = ps.getChunk(true);
		
		PS nearby = null;
		TemporaryBoard board = TemporaryBoard.get();
		
		nearby = ps.withChunkX(ps.getChunkX() +1);
		if (!board.isTemporary(nearby) && faction == this.getFactionAt(nearby)) return true;
		
		nearby = ps.withChunkX(ps.getChunkX() -1);
		if (!board.isTemporary(nearby) && faction == this.getFactionAt(nearby)) return true;
		
		nearby = ps.withChunkZ(ps.getChunkZ() +1);
		if (!board.isTemporary(nearby) && faction == this.getFactionAt(nearby)) return true;
		
		nearby = ps.withChunkZ(ps.getChunkZ() -1);
		if (!board.isTemporary(nearby) && faction == this.getFactionAt(nearby)) return true;
		
		return false;
	}
	
	@Override
	public boolean isAnyConnectedPs(Set<PS> pss, Faction faction)
	{
		for (PS ps : pss)
		{
			if (this.isConnectedPs(ps, faction)) return true;
		}
		return false;
	}
		
	@Override
	public List<Mson> getMap(Player p, RelationParticipator observer, PS centerPs, double inDegrees, int width, int height)
	{
		centerPs = centerPs.getChunkCoords(true);
		
		List<Mson> ret = new ArrayList<>();
		
		ret.add(Mson.NEWLINE);
		
		int halfWidth = width / 2;
		int halfHeight = height / 2;
		width = halfWidth * 2 + 1;
		height = halfHeight * 2 + 1;
				
		PS topLeftPs = centerPs.plusChunkCoords(-halfWidth, -halfHeight);
		
		// Get the compass
		List<String> asciiCompass = AsciiCompass.getAsciiCompass(inDegrees);
		
		// Trabalhando com a borda do mundo
		World world = p.getWorld();
		Location center = world.getWorldBorder().getCenter();
		double size = world.getWorldBorder().getSize() / 2.0D;
		int cXP = new Location(world, center.getX() + size, 0, 0).getChunk().getX();
		int cXN = new Location(world, center.getX() - size, 0, 0).getChunk().getX();
		int cZP = new Location(world, 0, 0, center.getZ() + size).getChunk().getZ();
		int cZN = new Location(world, 0, 0, center.getZ() - size).getChunk().getZ();

		// For each row
		for (int dz = 0; dz < height; dz++)
		{
			// Draw and add that row
			List<Mson> row = new ArrayList<>();
			for (int dx = 0; dx < width; dx++)
			{
				
				if (dx == halfWidth && dz == halfHeight) {
					row.add(Mson.mson(ChatColor.YELLOW + SQUARE));
					continue;
				}
				
				PS herePs = topLeftPs.plusChunkCoords(dx, dz);
				Faction hereFaction = this.getFactionAt(herePs);
				int cX = herePs.getChunkX(true);
				int cZ = herePs.getChunkZ(true);
				
				if (cX > cXP) {
					row.add(Mson.mson(ChatColor.BLACK + SQUARE));
					continue;
				}
				
				else if (cX < cXN) {
					row.add(Mson.mson(ChatColor.BLACK + SQUARE));
					continue;
				}
				
				else if (cZ > cZP) {
					row.add(Mson.mson(ChatColor.BLACK + SQUARE));
					continue;
				}
				
				else if (cZ < cZN) {
					row.add(Mson.mson(ChatColor.BLACK + SQUARE));
					continue;
				}
				
				else if (hereFaction.isNone()) {
					row.add(Mson.mson(ChatColor.GRAY + SQUARE));
					continue;
				}

				else if (isInAttack(world, herePs))	{
					row.add(Mson.mson(ChatColor.LIGHT_PURPLE + SQUARE).tooltip(hereFaction.getColorTo(observer).toString() + hereFaction.getName()));
					continue;
				}
				
				else if (TemporaryBoard.get().isTemporary(herePs)) {
					row.add(Mson.mson(ChatColor.BLUE + SQUARE).tooltip(hereFaction.getColorTo(observer).toString() + hereFaction.getName()));
					continue;
				}
				
				else {
					row.add(Mson.mson(hereFaction.getColorTo(observer).toString() + SQUARE).tooltip(hereFaction.getColorTo(observer).toString() + hereFaction.getName()));
					continue;
				}
				
			}
			
			// Add the compass
			     if (dz == 3)  row.add(Mson.mson(" " + asciiCompass.get(0)).bold(true));
			else if (dz == 4)  row.add(Mson.mson(" " + asciiCompass.get(1)).bold(true));
			else if (dz == 5)  row.add(Mson.mson(" " + asciiCompass.get(2)).bold(true));
			else if (dz == 7)  row.add(Mson.mson(" " + MConf.get().colorAlly         + SQUARE + " §fAliada"));
			else if (dz == 8)  row.add(Mson.mson(" " + MConf.get().colorNeutral      + SQUARE + " §fNeutra"));
			else if (dz == 9)  row.add(Mson.mson(" " + MConf.get().colorEnemy        + SQUARE + " §fInimiga"));
			else if (dz == 10) row.add(Mson.mson(" " + ChatColor.GRAY                + SQUARE + " §fZona Livre"));
			else if (dz == 11) row.add(Mson.mson(" " + MConf.get().colorNoPVP        + SQUARE + " §fZona Protegida"));
			else if (dz == 12) row.add(Mson.mson(" " + MConf.get().colorFriendlyFire + SQUARE + " §fZona de Guerra"));
			else if (dz == 13) row.add(Mson.mson(" " + ChatColor.YELLOW              + SQUARE + " §fSua posição"));
			else if (dz == 14) row.add(Mson.mson(" " + MConf.get().colorMember       + SQUARE + " §fSua facção"));
			else if (dz == 15) row.add(Mson.mson(" " + ChatColor.LIGHT_PURPLE        + SQUARE + " §fSob ataque"));
			else if (dz == 16) row.add(Mson.mson(" " + ChatColor.BLUE                + SQUARE + " §fTerreno temporário"));
			
			ret.add(Mson.mson(row));
		}
		return ret;
	}
	
	// Método para verificar se a facção esta sob ataque
	private boolean isInAttack(World world, PS ps) {
		Chunk chunk = world.getChunkAt(ps.getChunkX(), ps.getChunkZ());
		return EngineSobAtaque.underattack.containsKey(chunk);
	}
}
