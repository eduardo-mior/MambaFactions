package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsMotdChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeNullable;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsMotd extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMotd()
	{
		// Aliases
        this.addAliases("mensagem");
        
		// Parametros (não necessario)
		this.addParameter(TypeNullable.get(TypeString.get()), "novaMotd", "erro", true);
		
		// Requisições
		this.addRequirements(ReqHasFaction.get());
		
		// Descrição do comando
		this.setDesc("§6 motd §e[mensagem] §8-§7 Altera ou mostra a mensagem da facção.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("§cVocê precisar ser capitão ou superior para poder alterar a motd da facção.");
			return;
		}
		
		// Lendo os argumanetos e verificando se o argumento é nulo
		if (!this.argIsSet(0)) 
		{
			msender.msg("§cArgumentos insuficientes, use /f motd <mensagem>");
			return;
		}
		
		// Argumentos
		String target = this.readArg();

		target = target.trim();
		target = Txt.parse(target);

		// Evento
		EventFactionsMotdChange event = new EventFactionsMotdChange(sender, msenderFaction, target);
		event.run();
		if (event.isCancelled()) return;
		target = event.getNewMotd().replace("§", "&");
		
		// Aplicando o evento
		msenderFaction.setMotd(target);
		
		// Informando os players
		for (MPlayer follower : msenderFaction.getMPlayers())
		{
			follower.msg("§e%s §edefiniu a motd da facção para:\n§7'§f%s§7'", msender.getRole().getPrefix() + msender.getName(), msenderFaction.getMotdDesc());
		}
	}
	
}
