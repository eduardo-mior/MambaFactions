package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.requirement.RequirementTitlesAvailable;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanOn;
import com.massivecraft.massivecore.mixin.MixinTitle;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsTitulos extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsTitulos()
	{
		// Aliases
		this.addAliases("tt", "territorytitles");

		// Parametros (não necessario)
		this.addParameter(TypeBooleanOn.get(), "on|off", "toggle");

		// Requisições
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(RequirementTitlesAvailable.get());
		
		// Descrição do comando
		this.setDesc("§6 tt,titulos §8-§7 Mostra os titulos dos território.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Visibility getVisibility()
	{
		// Isto esconde o comando caso o player estiver usando uma versão que não suporta titles
		if ( ! MixinTitle.get().isAvailable()) return Visibility.INVISIBLE;
		return super.getVisibility();
	}
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		boolean before = msender.isTerritoryInfoTitles();
		boolean after = this.readArg(!before);
		String desc = Txt.parse(after ? "§2ativada": "§cdesativada");
		
		// Verificando se o player ja esta com o modo title ativado
		if (after == before)
		{
			msg("§aA visualização dos titulos dos territórios já está %s§a.", desc);
			return;
		}
		
		// Setando o modo title como ativado/desativado
		msender.setTerritoryInfoTitles(after);
		
		// Informando o msender
		msg("§aVisualização dos titulos dos territórios %s§a.", desc);
	}
	
}
