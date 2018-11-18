package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsVerTerras extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsVerTerras()
	{
		// Aliases
		this.addAliases("sc");
		
		// Descrição
		this.setDesc("§6 sc,verterras §8-§7 Mostra as delimitações das terras.");

		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
		
		// Parametros (não necessario)
		this.addParameter(TypeString.get(), "on/off", "erro", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Boolean old = msender.isSeeingChunk();
		Boolean target = readBoolean(old);
		
		// Verificando se o player digitou um argumento correto
		if (target == null) {
			msg("§cComando incorreto, use /f sc [on/off]");
			return;
		}
		
		// Descrição da ação
		String desc = target ? "§2ativada": "§cdesativada";

		// Verificando se o player já esta com modo ver terras ativado/desativado
		if (target == old) {
			msg("§aA visualização das delimitações das terras já está %s§a.", desc);
			return;
		}
		
		// Setando o modo verterras como ativado/desativado
		msender.setSeeingChunk(target);
		
		// Informando o sender
		msg("§aVisualização das delimitações das terras %s§a.", desc);
	}

}