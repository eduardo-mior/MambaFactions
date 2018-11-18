package com.massivecraft.factions.cmd;

import java.util.List;
import java.util.Set;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsChatAliados extends FactionsCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdFactionsChatAliados i = new CmdFactionsChatAliados() { public List<String> getAliases() { return MConf.get().aliasesChatAliados; } };
	public static CmdFactionsChatAliados get() { return i; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsChatAliados()
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
			msg("§cComando incorreto, use /a <mensagem>");
			return;
		}
			
		// Lista de aliados
		Set<Faction> aliados = msenderFaction.getAllys();
			
		// Verificar se a facção possui aliados
		if (aliados.size() == 0) {
			msg("§cSua facção não possui aliados!");
			return;
		}
		
		// Argumento (mensagem)
		String msg = this.arg().replace('&', '§');
		msg = ("§b[a] §7[" + msenderFaction.getName() + "§7] §f" + msender.getRole().getPrefix() + msender.getName() + "§b: " + msg);
			
		// Mensagem para os aliados
		for (Faction ally : aliados) {
			for (MPlayer mp : ally.getMPlayersWhereOnline(true)) {
				mp.msg(msg);
			}
		}
	
		// Mensagem para os membros da facção
		for (MPlayer mp : msenderFaction.getMPlayersWhereOnline(true)) {
			mp.msg(msg);
		}
	}
}
