package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeFactionNameLenient;
import com.massivecraft.factions.engine.EngineSobAtaque;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.EventFactionsNameChange;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsNome extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsNome()
	{
		// Parameters
		this.addParameter(TypeFactionNameLenient.get(), "novoNome");

		// Aliases
        this.addAliases("name", "renomear", "rename");

		// Requisições
		this.addRequirements(ReqHasFaction.get());
        
		// Descrição do comando
		this.setDesc("§6 nome §e<nome> §8-§7 Altera o nome da facção.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verificando se o player possui permissão
		if (!(msender.getRole() == Rel.LEADER || msender.isOverriding())) {
			msender.message("§cApenas o líder da facção pode alterar o nome da facção.");
			return;
		}
		
		// Verificando se a facção não esta sob ataque
		if (EngineSobAtaque.factionattack.containsKey(msenderFaction.getName())) {
			msender.message("§cVocê não pode alterar o nome da sua facção enquanto ela estiver sobre ataque!");
			return;
		}
		
		// Argumentos
		String newName = this.readArg();
		Faction faction = msenderFaction;
		
		// Evento
		EventFactionsNameChange event = new EventFactionsNameChange(sender, faction, newName);
		event.run();
		if (event.isCancelled()) return;
		newName = event.getNewName();

		// Applicando evento
		faction.setName(newName);

		// Informando a facção
		faction.msg("§e%s§e definiu o nome da facção para §f%s§e.", msender.getRole().getPrefix() + msender.getName(), newName);
	}
	
}
