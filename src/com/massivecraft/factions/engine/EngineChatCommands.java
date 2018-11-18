package com.massivecraft.factions.engine;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
import com.massivecraft.factions.util.OthersUtil;
import com.massivecraft.massivecore.Engine;

public class EngineChatCommands extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineChatCommands i = new EngineChatCommands();
	public static EngineChatCommands get() { return i; }
	
	// -------------------------------------------- //
	// ENGINE INFO
	// -------------------------------------------- //
	
	public static HashMap<Player, Command> USING = new HashMap<>();	
	private static HashMap<Player, String> CONFIRM = new HashMap<>();
	
	// -------------------------------------------- //
	// UPDATE KILLS AND DEATHS
	// -------------------------------------------- //

	@SuppressWarnings("incomplete-switch")
	@EventHandler(ignoreCancelled = true)
	public void aoUsarChat(AsyncPlayerChatEvent e) 
	{
		// Pegando o player
		Player p = e.getPlayer();
		
		// Verificando se ele esta usando algum comando no chat
		if (USING.containsKey(p)) 
		{
			// Cancelando o evto
			e.setCancelled(true);
			
			// Pegando o comando que ele esta usando e pegando a mensagem
			final Command command = USING.get(p);
			final String message = e.getMessage().trim();
			
			// Verificando se o player quer cancelar a ação
			if (message.toLowerCase().equals("cancelar"))
			{
				// Remove os players da base de dados e informando
				USING.remove(p);
				CONFIRM.remove(p);
				p.sendMessage(command.error);
				return;
			}
			
			// Verificando se o player quer confirmar a ação
			if (CONFIRM.containsKey(p)) 
			{
				// Remove os players da base de dados
				String entry = CONFIRM.get(p);
				USING.remove(p);
				CONFIRM.remove(p);
				
				// Verificando se o player quer confirmar ou não
				if (message.toLowerCase().equals("não") || message.toLowerCase().equals("nao"))
				{
					p.sendMessage(command.error);
					return;
				}
				
				else if (message.toLowerCase().equals("sim")) 
				{
					// Executando a ação de acordo com o comando
					switch(command)
					{
						case CREATE:
						{
							CmdFactions.get().cmdFactionsCriar.execute(p, Arrays.asList(entry));
							return;
						}
						case INVITE: 
						{
							CmdFactions.get().cmdFactionsConvite.cmdFactionsConviteAdd.execute(p, Arrays.asList(entry));
							return;
						}
					}
				}
				
				else return;
			}
			
			// Caso ele não queira cancelar e nem confirmar então executamos normalmente
			switch(command)
			{
				case CREATE: 
				{
					tryCreateFaction(message, p);
					return;
				}
				case INVITE: 
				{
					tryInvitePlayer(message, p);
					return;
				}
				case RELATION: 
				{
					tryChangeRelation(message, p);
					break;
				}
			}
		}
	}
	
	// Método para tentar criar uma facção a partir da mensagem digitada
	private void tryCreateFaction(String newName, Player p) 
	{
		// Verificando se o nome da facção é valido
		p.sendMessage(" ");
		if (!OthersUtil.isValidFactionsName(newName, p)) {
			p.sendMessage(" ");
			return;
		}
		
		// Confirmando se esse vai ser realmente o nome da facção etc...
		p.sendMessage("§eVocê realmente deseja criar a facção §f[" + newName + "§f]§e?");
		p.sendMessage("§7Responda '§asim§7' ou '§cnão§7'.\n §a");
		CONFIRM.put(p, newName);
	}
	
	// Método para tentar convidar um player a partir da mensagem digitada
	private void tryInvitePlayer(String playerName, Player p)
	{
		// Verificando se o player e o alvo são os mesmos
		if (p.getName().equalsIgnoreCase(playerName)) 
		{
			p.sendMessage("§cVocê não pode enviar um convite para você mesmo.");
			return;
		}
		
		// Pegando o player e verificando se ele é valido
		MPlayer mp = MPlayerColl.get().getByName(playerName);
		if (mp == null) 
		{
			p.sendMessage("\n§cNenhum player encontrado com o nome '" + playerName + "'.\n §c");
			return;
		}
		
		// Confirmando se ele realmente vai querer enviar o convite etc...
		p.sendMessage("§eVocê realmente deseja convidar §f" + playerName + "§e para sua facção?");
		p.sendMessage("§7Responda '§asim§7' ou '§cnão§7'.\n §a");
		CONFIRM.put(p, playerName);
	}
	
	// Método para tentar alterar a relação da facção a partir da mensagem digitada
	private void tryChangeRelation(String factionName, Player p) 
	{
		// Pegando a facção pelo nome
		Faction f = FactionColl.get().getByName(factionName);
		
		// Verificando se a facção é valida
		if (f == null) 
		{
			p.sendMessage("\n§cNenhuma facção encontrada com o nome '" + factionName + "'.\n §c");
			return;
		}
	
		// Verificação se a facção informada não é a mesma do player
		MPlayer mp = MPlayer.get(p);
		if (mp.getFaction().getName().equalsIgnoreCase(factionName)) 
		{
			p.sendMessage("\n§cVocê não pode definir uma relação com sua própria facção.\n §c");
			return;
		}
		
		// Removendo o player da base de dados e abrindo o menu
		USING.remove(p);
		EngineMenuGui.get().abrirMenuDefinirRelacao(mp, f);
	}
}

enum Command {
	
	CREATE("§cVocê cancelou a criação da facção."), 
	INVITE("§cVocê cancelou o envio do convite."),
	RELATION("§cVocê cancelou a alteração da relação.");
	
	String error;
	
	Command(String error) {
		this.error = error;
	}
	
}