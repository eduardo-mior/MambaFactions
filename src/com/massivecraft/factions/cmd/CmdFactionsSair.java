package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;

public class CmdFactionsSair extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSair()
	{
		// Aliases
        this.addAliases("leave", "deixar");
        
		// Requisições
		this.addRequirements(ReqHasFaction.get());

		// Descrição do comando
		this.setDesc("§6 sair §8-§7 Abandona a sua facção atual.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		msender.leave();
	}
	
}
