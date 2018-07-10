package com.massivecraft.factions.cmd;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.Invitation;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.store.EntityInternalMap;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;

public class CmdFactionsConviteListar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsConviteListar()
	{
		// Aliases
	    this.addAliases("ver", "list");
		
		// Descrição do comando
		this.setDesc("§6 convite listar §8-§7 Mostra a lista de convites pendentes.");

	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //	
	
	@Override
	public void perform() throws MassiveException
	{		
		// Variables
		Faction faction = msenderFaction;
		Player p = msender.getPlayer();
		final long now = System.currentTimeMillis();
		int tamanho = 54;
		int flecha = 49;
		
		//Pegando os convites;
		EntityInternalMap<Invitation> invitations = faction.getInvitations();
		
		//Tamano do menu GUI
		if (invitations.size() <= 5) {
			tamanho = 36;
			flecha = 31;
		} else if (invitations.size() > 5 && invitations.size() <= 10) {
			tamanho = 45;
			flecha = 40;
		} else { 
			tamanho = 54;
			flecha = 49;
		}
			
		//Criando o menu GUI
		Inventory inv = Bukkit.createInventory(null, tamanho, "§1§2§3§8Convites pendentes (" + invitations.size() + "§8)");
		inv.setItem(flecha, new ItemBuilder(Material.ARROW).setName("§1§2§cVoltar").toItemStack());
		
		//Fazendo um loop pelos convites;
		int n = 1;
		int slot = 11;
		for(Entry<String, Invitation> invitation : invitations.entrySet()){
			
		    //Pegando jogador convidado;
		    MPlayer invited = MPlayer.get(invitation.getKey());
		    
		    //Pegando jogador que convidou;
		    MPlayer inviter = MPlayer.get(invitation.getValue().getInviterId());
		    
		    //Pegando o nome do jogador convidado
		    String invitedName = invited.getName();
		    
		    //Pegando o nome do jogador que convidou
		    String inviterName = inviter != null ? inviter.getName() : "§7§0Desconhecido";
		    
			String inviteTime = "§7§o0 minutos";
			if (invitation.getValue().getCreationMillis() != null)
			{
				long millis = now - invitation.getValue().getCreationMillis();
				LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(millis, TimeUnit.getAllButMillis()), 2);
				inviteTime = TimeDiffUtil.formatedMinimal(ageUnitcounts);
			}

		    //Criando o item no menu gui
			inv.setItem(slot, new ItemBuilder(Material.PAPER).setName("§eConvite #"+n).setLore("§fPlayer que foi convidado: §7" + invitedName, "§fPlayer que convidou: §7" + inviter.getRole().getPrefix() + inviterName, "§fConvite enviado há " + inviteTime + "§f atrás.", "", "§fBotão direito: §7Remover convite", "§fBotão Esquerdo: §7Informações do player").toItemStack());
		    
		    //Criando o loop no menu e no item do menu gui
			slot+= slot == 15 || slot == 24 ? + 5 : + 1;
			n++;
		}
		p.openInventory(inv);
	}
}
