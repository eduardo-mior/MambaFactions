package com.massivecraft.factions.cmd;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeRelation;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.EventFactionsRelationChange;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.mson.Mson;

public class CmdFactionsRelacaoDefinir extends FactionsCommand
{
	{
		
	// Aliases
    this.addAliases("set", "setar");
    
	// Descrição do comando
	this.setDesc("§6 relacao definir §e<facção> §8-§7 Define uma relação.");
	
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsRelacaoDefinir()
	{
		// Parametros (necessario)
		this.addParameter(TypeFaction.get(), "facção");
		this.addParameter(TypeRelation.get(), "relação", "abrirMenu");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Faction otherFaction = this.readArg();
		Rel newRelation = this.readArg();
		
		
		// Verificando se a facção target é a mesma facção do msender
		if (otherFaction == msenderFaction)
		{
			throw new MassiveException().setMsg("§cVocê não pode definir uma relação com sua própria facção.");
		}
		
		// Menu gui /f relação
		if (newRelation == null && msender.isPlayer())
		{
			Player p = msender.getPlayer();
			String factionNome = otherFaction.getName();
			Inventory inv = Bukkit.createInventory(null, 27, "§8Relação com " + factionNome);
			
			if (msenderFaction.getRelationTo(otherFaction) == Rel.ALLY) {
			inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§bDefinir aliança com " + factionNome).setLore("§cSua a facção já é §caliada da " + factionNome + "§c.").setLeatherArmorColor(Color.AQUA).toItemStack());
			} else if (msenderFaction.getRelationWish(otherFaction) == Rel.ALLY || otherFaction.getRelationWish(msenderFaction) == Rel.ALLY) {
			inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§bDefinir aliança com " + factionNome).setLore("§eSua facção já possui um", "§econvite de aliança pendente", "§ecom a §f" + factionNome + "§e!", "", "§fClique para ver a lista de", "§fconvites de aliança pendentes.").setLeatherArmorColor(Color.AQUA).toItemStack());
			} else {
			inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§bDefinir aliança com " + factionNome).setLore("§fClique para definir a facção","§7" + factionNome + "§f como facção §baliada§f.").setLeatherArmorColor(Color.AQUA).toItemStack());
			}
			
			if (msenderFaction.getRelationWish(otherFaction) == Rel.NEUTRAL) {
			inv.setItem(13, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§fDefinir neutralidade com " + factionNome).setLore("§cSua a facção já é neutra com a " + factionNome + ".").setLeatherArmorColor(Color.WHITE).toItemStack());
			} else {
			inv.setItem(13, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§fDefinir neutralidade com " + factionNome).setLore("§fClique para definir a facção","§7" + factionNome + "§f como facção §fneutra§f.").setLeatherArmorColor(Color.WHITE).toItemStack()); }
			
			if (msenderFaction.getRelationWish(otherFaction) == Rel.ENEMY) {
			inv.setItem(15, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§cDefinir rivalidade com " + factionNome).setLore("§cSua a facção já é §crival da " + factionNome + ".").setLeatherArmorColor(Color.RED).toItemStack());
			} else {
			inv.setItem(15, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§cDefinir rivalidade com " + factionNome).setLore("§fClique para definir a facção","§7" + factionNome + "§f como facção §cinimiga§f.").setLeatherArmorColor(Color.RED).toItemStack()); }

			p.openInventory(inv);
			return;
		}
		
		if (newRelation == null && msender.isConsole()) 
		{
			throw new MassiveException().setMsg("§cUtilize /f relacao definir <faccao> <relacao>");
		}
		
		if (newRelation == Rel.TRUCE) 
		{
			throw new MassiveException().setMessage("§cDesculpe mas o sistema de facções em trégua foi desabilitado neste servidor.");
		}
		
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("§cVocê precisar ser capitão ou superior para poder gerenciar as relações da facção.");
			return;
		}
		
		// Verificando se a relação da facção target é a mesma da facção do msender
		if (msenderFaction.getRelationWish(otherFaction) == newRelation)
		{
			throw new MassiveException().setMsg("§eA sua facção já é %s§e da §f%s§e.", newRelation.getDescFactionOne(), otherFaction.getName());
		}
		
		// Evento
		EventFactionsRelationChange event = new EventFactionsRelationChange(sender, msenderFaction, otherFaction, newRelation);
		event.run();
		if (event.isCancelled()) return;
		newRelation = event.getNewRelation();

		// Enviando pedido (aliados)
		msenderFaction.setRelationWish(otherFaction, newRelation);
		Rel currentRelation = msenderFaction.getRelationTo(otherFaction, true);

		// Definindo a relação sem precisar de confirmação
		if (newRelation == currentRelation)
		{
			otherFaction.msg("§f%s§e agora é %s§e.", msenderFaction.getName(), newRelation.getDescFactionOne());
			msenderFaction.msg("§f%s§e agora é %s§e.", otherFaction.getName(), newRelation.getDescFactionOne());
		}
		
		// Informando que a facção deseja ser aliado
		else
		{
			MassiveCommand command = CmdFactions.get().cmdFactionsRelacao.cmdFactionsRelacaoDefinir;
			String colorOne = newRelation.getColor() + newRelation.getDescFactionOne();

			// Mson && Json
			Mson factionsRelationshipChange = mson(
				Mson.parse("§f%s§e deseja se tornar %s§e.", msenderFaction.getName(), colorOne),
				Mson.SPACE,
				mson("§e[ACEITAR]").command(command, msenderFaction.getName(), newRelation.name())
			);
			
			otherFaction.sendMessage(factionsRelationshipChange);
			msenderFaction.msg("§f%s§e foi informada de que a sua facção deseja se tornar %s§e.", otherFaction.getName(), colorOne);
		}
		
		// Aplicando o evento
		msenderFaction.changed();
	}
	
}
