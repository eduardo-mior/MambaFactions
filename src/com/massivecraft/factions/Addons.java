package com.massivecraft.factions;

import java.util.Map.Entry;

import org.bukkit.Bukkit;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

public class Addons {
	
	public static void load() 
	{
		Bukkit.getScheduler().runTaskAsynchronously(Factions.get(), new Runnable()
		{
			@Override
			public void run()
			{
				final BoardColl board = BoardColl.get();
				for (Faction faction : FactionColl.get().getAll())
				{
					// Updating the temporary claims
					for (PS ps : faction.getTempClaims()) 
					{
						board.removeAt(ps);
					}
					faction.clearTempClains();
					
					// Updating the invites
					for (String mplayerId : faction.getInvitations().keySet()) 
					{
						MPlayer mp = MPlayer.get(mplayerId);
						if (mp != null) mp.addInvitation(faction.getId());
					}
					
					// Updating the pending relations
					for (Entry<String, Rel> entry : faction.getRelationWishes().entrySet())
					{
						Faction otherFaction = Faction.get(entry.getKey());
						
						if (otherFaction == null) continue;
						
						Rel myRel = entry.getValue();
						Rel otherRel = otherFaction.getRelationWish(faction);
						
						if (otherRel == Rel.ALLY && myRel == Rel.ALLY) continue;
						if (otherRel == Rel.ALLY) faction.addPendingRelation(otherFaction);
						if (myRel == Rel.ALLY) otherFaction.addPendingRelation(faction);
					}
				}
			}
		});
	}

}