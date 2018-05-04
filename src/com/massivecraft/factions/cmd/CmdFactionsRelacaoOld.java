package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.util.MUtil;

public class CmdFactionsRelacaoOld extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public final String relName;

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRelacaoOld(String rel)
	{
		// Fields
		this.relName = rel.toLowerCase();
		this.setSetupEnabled(false);

		// Aliases
		this.addAliases(relName);

		// Parametros (necessario)
		this.addParameter(TypeFaction.get(), "facção", true);

		// Visibilidade do comando
		this.setVisibility(Visibility.INVISIBLE);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Faction faction = this.readArg();
		
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("§cVocê precisar ser capitão ou superior para poder gerenciar as relações da facção.");
			return;
		}
		
		// Aplicando o evento
		CmdFactions.get().cmdFactionsRelacao.cmdFactionsRelacaoDefinir.execute(sender, MUtil.list(
			faction.getId(),
			this.relName
		));
	}

}
