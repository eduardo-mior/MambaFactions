package com.massivecraft.factions.cmd;

import java.util.Collections;
import java.util.Set;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanOn;
import com.massivecraft.massivecore.ps.PS;


public class CmdFactionsSetAuto extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private boolean claim = true;
	public boolean isClaim() { return this.claim; }
	public void setClaim(boolean claim) { this.claim = claim; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSetAuto(boolean claim)
	{
		// Fields
		this.setClaim(claim);
		this.setSetupEnabled(false);
		
		// Parametros (não necessario)
		this.addParameter(TypeBooleanOn.get(), "ativar", "desativar");
		
		// Aliases
		this.addAliases("auto");

		// Requirements
		this.addRequirements(RequirementIsPlayer.get());
		
		// Descrição do comando
		this.setDesc("§6 claim auto §e[on/off] §8-§7 Conquista as terras automaticamente enquanto você anda.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{	
		
		boolean old = msender.getAutoClaimFaction() == null ? false : true;
		boolean target = this.readArg(!old);
		
		if (old == false && target == false) {
			msender.msg("§aO modo auto conquistar terras já está §cdesativado§a.");
		} else if (old == true && target == true) {
			msender.msg("§aO modo auto conquistar terras já está §2ativado§a.");
		} else {
		
		// Args
		final Faction newFaction;
		if (claim)
		{
			newFaction = msenderFaction;
		}
		else
		{
			newFaction = FactionColl.get().getNone();
		}
		
		// Disable?
		if (newFaction == null || newFaction == msender.getAutoClaimFaction())
		{
			msender.setAutoClaimFaction(null);
			msg("§aModo auto conquistar terras §cdesativado§a.");
			return;
		}
		
		// MPerm Preemptive Check
		if (newFaction.isNormal() && ! MPerm.getPermTerritory().has(msender, newFaction, true)) return;
		
		// Apply / Inform
		msender.setAutoClaimFaction(newFaction);
		msg("§aModo auto conquistar terras §2ativado§a.");
		
		// Chunks
		final PS chunk = PS.valueOf(me.getLocation()).getChunk(true);
		Set<PS> chunks = Collections.singleton(chunk);		
		
		// Apply / Inform
		msender.tryClaim(newFaction, chunks);
	}
	}
}
