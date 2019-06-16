package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.MUtil;

public class CmdFactionsUnclaim extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	public CmdFactionsUnclaim() 
	{
		// Aliases
	    this.addAliases("desproteger", "abandonar", "contestar");
	
		// Descrição
		this.setDesc("§6 abandonar §8-§7 Abandona territórios da sua facção.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsSetOne cmdFactionsUnclaimOne = new CmdFactionsSetOne(false);
	public CmdFactionsSetAll cmdFactionsUnclaimAll = new CmdFactionsSetAll(false);
	

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		CmdFactions.get().cmdFactionsUnclaim.cmdFactionsUnclaimOne.execute(sender, MUtil.list());
	}
}