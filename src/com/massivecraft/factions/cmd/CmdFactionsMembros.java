package com.massivecraft.factions.cmd;

import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsMembros extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMembros()
	{
		// Aliases
		this.addAliases("status", "s");
		
		// Descrição
		this.setDesc("§6 membros §e<facção> §8-§7 Mostra a lista de membros da facção.");
		
		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
		
		// Parametros (não ecessario)
		this.addParameter(TypeString.get(), "outra facção", "sua facção", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Pegando a facção
		Faction faction = readFaction();
		
		// Verificando se o player é da facção é a zona livre
		if (faction.isNone()) {
			msg("§cArgumentos insuficientes, use /f membros <facção>");
			return;
		}
		
		// Verificando se a facção tem muitos membros
		if (faction.getMPlayers().size() > 29) {
			msg("§cA facção §f"+ faction.getName() +"§c possui muitos membros portanto o Menu não podera ser aberto.");
			return;
		}
		
		// Verificando se esta sem membros
		if (faction.getMPlayers().size() == 0) {
			msg("§cA facção §f" + faction.getName() + "§c não possui membros!");
			return;
		}
		
		EngineMenuGui.get().abrirMenuMembrosDaFaccao(msender, faction);
	}
}