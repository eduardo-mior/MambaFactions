package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsTop extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsTop() 
	{
		// Aliases
	    this.addAliases("rank", "ranking");
	    
		// Descrição
		this.setDesc("§6 top §8-§7 Abre o menu do Rank das facções.");
		
		/*
		 * A unica razão deste comando estar aqui
		 * é porque nós queriamos mostrar o comando /f top
		 * na lista de comandos do /f ajuda
		 * 
		 * Você pode ignorar isso ou apagar se quiser :D
		 */
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException {}
}
