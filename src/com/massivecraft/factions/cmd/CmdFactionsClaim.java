package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.MUtil;

public class CmdFactionsClaim extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsClaim() 
	{
		// Aliases
	    this.addAliases("conquistar", "dominar");
	    
		// Descrição
		this.setDesc("§6 proteger §8-§7 Conquista territórios para a sua facção.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(RequirementIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsSetOne cmdFactionsClaimOne = new CmdFactionsSetOne(true);
	public CmdFactionsSetAuto cmdFactionsClaimAuto = new CmdFactionsSetAuto(true);
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		CmdFactions.get().cmdFactionsClaim.cmdFactionsClaimOne.execute(sender, MUtil.list());
	}
}
