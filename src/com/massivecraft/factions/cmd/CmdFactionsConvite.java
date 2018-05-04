package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;

public class CmdFactionsConvite extends FactionsCommand
{
	
	{
	// Aliases
    this.addAliases("convidar", "i", "adicionar", "invite");
	    
	// Descrição do comando
	this.setDesc("§6 convite §8-§7 Gerencia os convites da facção.");
	
	// Requisições
	this.addRequirements(ReqHasFaction.get());
	
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsConviteAdd cmdFactionsConviteAdd = new CmdFactionsConviteAdd();
	public CmdFactionsConviteDel cmdFactionsConviteDel = new CmdFactionsConviteDel();
	public CmdFactionsConviteListar cmdFactionsConviteListar = new CmdFactionsConviteListar();

}
