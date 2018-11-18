package com.massivecraft.factions.cmd;

import java.util.List;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.command.Visibility;
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
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeString.get(), "mensagem", "erro", true);

        // Visibilidade do comando
        this.setVisibility(Visibility.INVISIBLE);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{							
		// Verficiando se os argumentos são validos
		if (!this.argIsSet()) {
			msg("§cComando incorreto, use /. <mensagem>");
			return;
		}
		
		// Argumento (mensagem)
		String msg = this.arg().replace('&', '§');
		msg = ("§a[f] §f" + msender.getRole().getPrefix() + msender.getName() + "§a: " + msg);
		
		// Mensagem para os membros da facção
		for (MPlayer mp : msenderFaction.getMPlayersWhereOnline(true)) {
			mp.msg(msg);
		}
	}
}
