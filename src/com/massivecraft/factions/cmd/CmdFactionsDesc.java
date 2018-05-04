package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsDescriptionChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeNullable;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsDesc extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDesc()
	{
		// Aliases
		this.addAliases("descricao", "description");

		// Parametros (necessario)
		this.addParameter(TypeNullable.get(TypeString.get()), "novaDesc", "erro", true);

		// Requisições
		this.addRequirements(ReqHasFaction.get());

		// Descrição do comando
		this.setDesc("§6 desc §e<desc> §8-§7 Altera a descrição da facção.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{	
		// Argumentos
		for (String newDescription: this.getArgs()) {
		
			
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("§cVocê precisar ser capitão ou superior para poder alterar a descrição da facção.");
			return;
		}
			
		// Verificando se os argumentos não são nulos
		if (!this.argIsSet(0)) 
		{
			msender.msg("§cArgumentos insuficientes, use /f desc <descrição>");
			return;
		}
		
		// Evento
		EventFactionsDescriptionChange event = new EventFactionsDescriptionChange(sender, msenderFaction, newDescription);
		event.run();
		if (event.isCancelled()) return;
		newDescription = event.getNewDescription().replace("&", "§");

		// Aplicando evento
		msenderFaction.setDescription(newDescription);
		
		// Informando a facção
		for (MPlayer follower : msenderFaction.getMPlayers())
		{
			follower.msg("§e%s §edefiniu a descrição da facção para:\n§7'§f%s§7'", msender.getRole().getPrefix() + msender.getName(), msenderFaction.getDescriptionDesc());
		}
	}
	}
}
