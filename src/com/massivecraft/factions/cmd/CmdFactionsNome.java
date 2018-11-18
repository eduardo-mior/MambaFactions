package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.event.EventFactionsNameChange;
import com.massivecraft.factions.util.OthersUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsNome extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsNome()
	{
		// Aliases
        this.addAliases("name", "renomear", "rename");
        
		// Descrição
		this.setDesc("§6 nome §e<nome> §8-§7 Altera o nome da facção.");

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
		// Verificando se o player possui permissão
		if (!(msender.getRole() == Rel.LEADER || msender.isOverriding())) {
			msg("§cApenas o líder da facção pode alterar o nome da facção.");
			return;
		}
		
		// Verificando se a facção não esta sob ataque
		if (msenderFaction.isInAttack()) {
			msg("§cVocê não pode alterar o nome da facção enquanto ela estiver sobre ataque!");
			return;
		}
		
		// Verficiando se os argumentos são validos
		if (!this.argIsSet()) {
			msg("§cArgumentos insuficientes, use /f nome <nome>");
			return;
		}
		
		// Argumentos
		String newName = this.arg();
		
		// Verificando se o novo nome não é igual o atual
		if (newName.equals(msenderFaction.getName())) {
			msg("§cO nome da sua facção já é '" + newName + "'.");
			return;
		}
		
		// Método para verificar se o nome da facção é valido
		if (!OthersUtil.isValidFactionsName(newName, me)) return;
				
		// Evento
		EventFactionsNameChange event = new EventFactionsNameChange(sender, msenderFaction, newName);
		event.run();
		if (event.isCancelled()) return;
		newName = event.getNewName();

		// Applicando evento
		msenderFaction.setName(newName);

		// Informando a facção
		msenderFaction.msg("§e%s§e definiu o nome da facção para §f[%s§f]§e.", msender.getRole().getPrefix() + msender.getName(), newName);
	}
	
}
