package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineSobAtaque;

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
		
		// Verificando se a facção não esta sob ataque
		if (EngineSobAtaque.factionattack.containsKey(msenderFaction.getName())) {
			msender.message("§cVocê não pode abandonar a sua facção enquanto ela estiver sobre ataque!");
			return;
		}
		
		// Saindo da facção
		msender.leave();
	}
	
}
