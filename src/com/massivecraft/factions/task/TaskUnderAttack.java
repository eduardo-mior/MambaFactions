package com.massivecraft.factions.task;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import com.massivecraft.factions.util.ColorScrollPlus;
import com.massivecraft.factions.util.ColorScrollPlus.ScrollType;
import com.massivecraft.factions.engine.EngineSobAtaque;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.nms.NmsChat;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.TimeUnit;

public class TaskUnderAttack extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
    final ColorScrollPlus cs = new ColorScrollPlus("§4", "Sua facção está sob ataque!", "§c", "§4", "§4", false, false, ColorScrollPlus.ScrollType.FORWARD);
	 
	private static TaskUnderAttack i = new TaskUnderAttack();
	public static TaskUnderAttack get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public long getDelayMillis()
	{
		return (long) (0.25 * TimeUnit.MILLIS_PER_SECOND);
	}
	
	@Override
	public void invoke(long now)
	{
		  if(cs.getScrollType() == ScrollType.FORWARD) {
              if(cs.getPosition() >= cs.getString().length()) {
                  cs.setScrollType(ScrollType.BACKWARD);
              }
              
          } else if(cs.getPosition() <= -1) {
                  cs.setScrollType(ScrollType.FORWARD);
          }
		
		List<Faction> facunder = new ArrayList<>();
				
		for(Chunk chunk : EngineSobAtaque.underattack.keySet()) {
			Faction fac = BoardColl.get().getFactionAt(PS.valueOf(chunk));
			
			if (fac == null) {
				return;
			}
			
			if(!facunder.contains(fac)) {
				facunder.add(fac);	
			}
			
			EngineSobAtaque.get().aumentarSegundos(chunk);
			
			if(System.currentTimeMillis() - EngineSobAtaque.get().getTime(chunk) > 5 * TimeUnit.MILLIS_PER_MINUTE) {
				EngineSobAtaque.get().remover(chunk, fac);
			}
		}
		
		for(Faction f : facunder) {
			for (Player p : f.getOnlinePlayers()){
				NmsChat.get().sendActionbarMsg(p, cs.next());
			}
		}	
	}
	
}
