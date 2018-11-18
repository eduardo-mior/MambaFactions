package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.command.type.primitive.TypeString;

public abstract class CmdFactionsSetXAll extends CmdFactionsSetX
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSetXAll(boolean claim)
	{
		// Super
		super(claim);
		
		// Parameters
		this.addParameter(TypeString.get(), "all", "all");
	}
}