package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsDescriptionChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsDesc extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDesc()
	{
		// Aliases
		this.addAliases("descricao", "description");

		// Descrição
		this.setDesc("§6 desc §e<desc> §8-§7 Altera a descrição da facção.");

		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeString.get(), "descricao", "erro", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{					
		// Verificando se o player possui permissão
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msg("§cVocê precisar ser capitão ou superior para poder alterar a descrição da facção.");
			return;
		}
				
		// Verificando se os argumentos não são nulos
		if (!this.argIsSet()) {
			msg("§cArgumentos insuficientes, use /f desc <descrição>");
			return;
		}
		
		// Argumentos
		String newDescription = Txt.parse(this.arg()).replace('&', '§');
		
		// Verificando se a descrição antiga não é igual anova
		if (msenderFaction.getDescriptionDesc().equals(newDescription)) {
			msg("§cA descrição da facção já é '" + newDescription + "§c'.");
			return;
		}
			
		// Evento
		EventFactionsDescriptionChange event = new EventFactionsDescriptionChange(sender, msenderFaction, newDescription);
		event.run();
		if (event.isCancelled()) return;
		newDescription = event.getNewDescription();
	
		// Aplicando evento
		msenderFaction.setDescription(newDescription);
			
		// Informando a facção
		for (MPlayer mp : msenderFaction.getMPlayersWhereOnline(true)) {
			mp.msg("§e%s§e definiu a descrição da facção para:\n§7§l'§f%s§7§l'", msender.getRole().getPrefix() + msender.getName(), msenderFaction.getDescriptionDesc());
		}
	}
}
