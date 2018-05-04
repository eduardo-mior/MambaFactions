package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;

public class CmdFactionsRelacao extends FactionsCommand
{
	{

	// Aliases
    this.addAliases("relação", "relation", "rel");

	// Requisições
	this.addRequirements(ReqHasFaction.get());
    
	// Descrição do comando
	this.setDesc("§6 relacao §8-§7 Gerencia as relações da facção.");
	
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public CmdFactionsRelacaoDefinir cmdFactionsRelacaoDefinir = new CmdFactionsRelacaoDefinir();
	public CmdFactionsRelacaoListar cmdFactionsRelacaoListar = new CmdFactionsRelacaoListar();

}
