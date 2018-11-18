package com.massivecraft.factions.engine;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EngineCanCombatHappen extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineCanCombatHappen i = new EngineCanCombatHappen();
	public static EngineCanCombatHappen get() { return i; }
	
	// -------------------------------------------- //
	// CAN COMBAT DAMAGE HAPPEN
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void canCombatDamageHappen(EntityDamageByEntityEvent event)
	{
		if (this.canCombatDamageHappenBOOLEAN(event)) {
			EngineFly.disableFly(event);
			return;
		}
		event.setCancelled(true);

		Entity damager = event.getDamager();
		if (damager instanceof Arrow) damager.remove();
	}

	// mainly for flaming arrows; don't want allies or people in safe zones to be ignited even after damage event is cancelled
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void canCombatDamageHappen(EntityCombustByEntityEvent event)
	{
		EntityDamageByEntityEvent sub = new EntityDamageByEntityEvent(event.getCombuster(), event.getEntity(), EntityDamageEvent.DamageCause.FIRE, 0D);
		if (this.canCombatDamageHappenBOOLEAN(sub)) return;
		event.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void canCombatDamageHappen(PotionSplashEvent event)
	{
		// If a harmful potion is splashing ...
		if (!MUtil.isHarmfulPotion(event.getPotion())) return;
		
		ProjectileSource projectileSource = event.getPotion().getShooter();
		if (! (projectileSource instanceof Entity)) return;
		
		Entity thrower = (Entity)projectileSource;

		// ... scan through affected entities to make sure they're all valid targets.
		for (LivingEntity affectedEntity : event.getAffectedEntities())
		{
			EntityDamageByEntityEvent sub = new EntityDamageByEntityEvent(thrower, affectedEntity, EntityDamageEvent.DamageCause.CUSTOM, 0D);
			if (this.canCombatDamageHappenBOOLEAN(sub)) continue;
			
			// affected entity list doesn't accept modification (iter.remove() is a no-go), but this works
			event.setIntensity(affectedEntity, 0.0);
		}
	}
	
	public boolean canCombatDamageHappenBOOLEAN(EntityDamageByEntityEvent event)
	{		
		// Verificando se o defensor é 1 player
		Entity edefender = event.getEntity();
		if (MUtil.isntPlayer(edefender)) return true;
		Player defender = (Player)edefender;
		
		// ... and the attacker is someone else ...
		Entity eattacker = MUtil.getLiableDamager(event);
		
		// (we check null here since there may not be an attacker)
		// (lack of attacker situations can be caused by other bukkit plugins)
		if (eattacker != null && eattacker.equals(edefender)) return true;
		
		// Verificando se o atacante é 1 player
		if (MUtil.isntPlayer(eattacker)) return true;
		Player attacker = (Player)eattacker;
		MPlayer mattacker = MPlayer.get(attacker);
		
		// ... fast evaluate if the attacker is overriding ...
		if (mattacker != null && mattacker.isOverriding()) return true;
		
		// Pegando a facção onde o PvP esta ocorrendo e o defensor 
		MPlayer mdefender = MPlayer.get(edefender);
		PS defenderPs = PS.valueOf(defender.getLocation());
		Faction defenderPsFaction = BoardColl.get().getFactionAt(defenderPs);
		
		// Verificando se o PvP amigo esta ativado
		if (defenderPsFaction.getFlag(MFlag.getFlagFriendlyire())) return true;
		// Verificando se o PvP esta tivado
		if (defenderPsFaction.getFlag(MFlag.getFlagPvp()) == false) return false;
		// Verificando se algum deles esta na zona livre
		if (mattacker.hasFaction() == false || mdefender.hasFaction() == false) return true;
		// Verificando se as facções são aliadas
		if (mattacker.getFaction().getRelationTo(mdefender.getFaction()).equals(Rel.ALLY)) return false; 
		// Verificando se os players são da mesma facção
		if (mattacker.getFaction().getMPlayers().contains(mdefender)) return false;
		
		return true;
	}

}
