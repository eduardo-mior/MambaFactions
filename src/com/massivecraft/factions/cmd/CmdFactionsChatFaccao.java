package com.massivecraft.factions.cmd;

import java.util.List;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.TypeNullable;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsChatFaccao extends FactionsCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdFactionsChatFaccao i = new CmdFactionsChatFaccao() { public List<String> getAliases() { return MConf.get().aliasesChatFaccao; } };
	public static CmdFactionsChatFaccao get() { return i; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsChatFaccao()
	{
		// Requisições
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeNullable.get(TypeString.get()), "mensagem", "erro", true);

        // Visibilidade do comando
        this.setVisibility(Visibility.INVISIBLE);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Transformando todos os argumentos em 1 string
		for (String msg: this.getArgs()) {
				
			Faction f = msender.getFaction();
							
			// Verficiando se os argumentos são validos
			if (!this.argIsSet(0))
			{
				msender.message("§cArgumentos insuficientes, use /. <mensagem>");
				return;
			}
			
			// Mensagem para os membros da facção
			for(MPlayer mp : f.getMPlayersWhereOnline(true)) {
				mp.message(("§a[f] §f" + msender.getRole().getPrefix() + msender.getName() + "§a: " + msg).replace('&', '§'));
				
			}
		}
	}
}
