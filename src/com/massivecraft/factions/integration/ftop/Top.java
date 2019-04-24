package com.massivecraft.factions.integration.ftop;

import java.util.Collection;
import com.massivecraft.factions.entity.Faction;

import br.com.kickpost.ftop.FTop;

public class Top {
	
	public static boolean isEnabled()
	{
		return FTop.get().isEnabled();
	}
	
	public static FTop getTop()
	{
		return FTop.get();
	}
	
	public static int getTopPosition(Faction f)
	{
		return getTop().getTopPosition(f);
	}

	public static Collection<Faction> getTopFactions()
	{
		return getTop().getTopFactions();
	}
	
}