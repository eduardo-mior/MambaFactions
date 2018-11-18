package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsSair extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSair()
	{
		// Aliases
        this.addAliases("leave", "deixar");

		// Descrição
		this.setDesc("§6 sair §8-§7 Abandona a sua facção atual.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (não necessario)
		this.addParameter(TypeString.get(), "confirmação", "null", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Verificando se a facção não esta sob ataque
		if (msenderFaction.isInAttack()) {
			msg("§cVocê não pode abandonar a facção enquanto ela estiver sobre ataque!");
			return;
		}

		// Verificando se o player é o lider da facção
		if (msender.getRole() == Rel.LEADER) {
			msg("§cVocê é o lider da facção, portanto não pode abandona-la. Caso queira desfaze-la use /f desfazer.");
			return;
		}
		
		// Caso não haja o argumento "confirmar" então é aberto um menu de confirmação
		if ((!this.argIsSet() || !this.arg().equalsIgnoreCase("confirmar")) && msender.isPlayer()) {
			EngineMenuGui.get().abrirMenuAbandonarFaccao(msender);
			return;
		}

		// Saindo da facção
		msender.leave();
	}
	
}