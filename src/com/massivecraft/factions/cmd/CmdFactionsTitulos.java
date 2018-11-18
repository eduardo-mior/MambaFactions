package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.requirement.RequirementTitlesAvailable;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mixin.MixinTitle;

public class CmdFactionsTitulos extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsTitulos()
	{
		// Aliases
		this.addAliases("tt", "territorytitles");
		
		// Descrição
		this.setDesc("§6 tt,titulos §8-§7 Mostra os titulos dos território.");

		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(RequirementTitlesAvailable.get());
		
		// Parametros (não necessario)
		this.addParameter(TypeString.get(), "on/off", "erro", true);
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
		Boolean old = msender.isTerritoryInfoTitles();
		Boolean target = readBoolean(old);
		
		// Verificando se o player digitou um argumento correto
		if (target == null) {
			msg("§cComando incorreto, use /f tt [on/off]");
			return;
		}
		
		// Descrição da ação
		String desc = target ? "§2ativada": "§cdesativada";

		// Verificando se o player já esta com a visualização ativada/desativada
		if (target == old) {
			msg("§aA visualização dos titulos dos territórios já está %s§a.", desc);
			return;
		}
		
		// Setando a visualização como ativado/desativado
		msender.setTerritoryInfoTitles(target);
		
		// Informando os players players
		msg("§aVisualização dos titulos dos territórios %s§a.", desc);
	}
	
}