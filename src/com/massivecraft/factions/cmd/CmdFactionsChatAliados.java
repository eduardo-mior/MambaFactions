package com.massivecraft.factions.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.TypeNullable;
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
				msender.message("§cArgumentos insuficientes, use /a <mensagem>");
				return;
			}
			
			// Lista de aliados
			List<Faction> aliados = new ArrayList<>();
			
			// Obter lista de aliados
			Set<String> relations = f.getRelationWishes().keySet();;
			
			for (String id : relations) {
				Faction fac = Faction.get(id);
				if (fac != null && fac.getRelationTo(f).equals(Rel.ALLY)) aliados.add(fac);
			}
			
			// Verificar se a facção possui aliados
			if (aliados.size() == 0) {
				msender.msg("§cSua facção não possui aliados!");
				return;
			}
			
			// Mensagem para os aliados
			for(Faction ally : aliados) {
				for(MPlayer mp : ally.getMPlayersWhereOnline(true)) {
					mp.message(("§b[a] §7["  + f.getName() + "§7] §f" + msender.getRole().getPrefix() + msender.getName() + "§b: " + msg).replace('&', '§'));
				}
			}
	
			// Mensagem para os membros da facção
	        for(MPlayer mp : f.getMPlayersWhereOnline(true)) {
	        	mp.message(("§b[a] §7["+ f.getName() + "§7] §f" + msender.getRole().getPrefix()  + msender.getName() + "§b: " + msg).replace('&', '§'));
			}
		}
	}
}
