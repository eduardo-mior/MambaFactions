package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsTransferir extends FactionsCommand 
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsTransferir() 
	{
		// Aliases
		this.addAliases("lider", "liderança", "lideranca");
		
		// Descrição
		this.setDesc("§6 transferir §e<player> §8-§7 Transfere a liderança da facção.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeString.get(), "player", "erro", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException 
	{
		// Verificando se o sender é lider da facção
		if (msender.getRole() != Rel.LEADER) {
			msg("§cApenas o líder da facção pode promover uma novo líder.");
			return;
		}
		
		// Verficiando se os argumentos são validos
		if (!this.argIsSet()) {
			msg("§cArgumentos insuficientes, use /f transferir <player>");
			return;
		}
		
		// Verificando se o sender e o target são a mesma pessoa
		String name = this.arg();
		if (msender.getName().equalsIgnoreCase(name)) {
			msg("§cVocê não pode transferir a liderança para você mesmo");
			return;
		}

		// Argumentos
		MPlayer target = readMPlayer(name);
		Faction facSender = msender.getFaction();
		Faction facTarget = target.getFaction();

		// Verificando se o target é da mesma facão que o sender
		if (facSender != facTarget) {
			msg("§cEste jogador não esta na sua facção.");
			return;
		}
		
		// Aplicando o evento
		msender.setRole(Rel.OFFICER);
		target.setRole(Rel.LEADER);
		
		// Informando o sender e o target
		facSender.msg("§e" + msender.getName() + "§e transferiu a lideração da facção para §f" + target.getName() + "§e.");
	}
}
