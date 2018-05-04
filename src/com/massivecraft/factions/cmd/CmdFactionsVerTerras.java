package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanOn;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsVerTerras extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsVerTerras()
	{
		// Aliases
		this.addAliases("sc");
		
		// Parametros (não necessario)
		this.addParameter(TypeBooleanOn.get(), "mostrar", "esconder");

		// Requisições
		this.addRequirements(RequirementIsPlayer.get());
		
		// Descrição do comando
		this.setDesc("§6 sc,verterras §8-§7 Mostra as delimitações das terras.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		boolean old = msender.isSeeingChunk();
		boolean target = this.readArg(!old);
		String targetDesc = Txt.parse(target ? "§2ativada": "§cdesativada");
		
		// Verificando se o player ja esta com o modo verterras ativado
		if (target == old)
		{
			msg("§aA visualização das delimitações das terras já está %s§a.", targetDesc);
			return;
		}
		
		// Setando o modo verterras como ativado/desativado
		msender.setSeeingChunk(target);
		
		// Informando o msender
		msg("§aVisualização das delimitações das terras %s§a.", targetDesc);
	}

}
