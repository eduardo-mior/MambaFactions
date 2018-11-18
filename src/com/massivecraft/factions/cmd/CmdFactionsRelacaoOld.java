package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
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
		this.addParameter(TypeString.get(), "facção", "erro", true);

		// Visibilidade do comando
		this.setVisibility(Visibility.INVISIBLE);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		// Verficiando se os argumentos são validos
		if (!this.argIsSet()) {
			msg("§cArgumentos insuficientes, use /f " + this.relName + " <facção>");
			return;
		}
		
		// Verificando se o player possui permissão
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("§cVocê precisar ser capitão ou superior para poder gerenciar as relações da facção.");
			return;
		}
		
		// Aplicando o evento
		CmdFactions.get().cmdFactionsRelacao.execute(sender, MUtil.list(
			this.arg(),
			this.relName
		));
	}

}
