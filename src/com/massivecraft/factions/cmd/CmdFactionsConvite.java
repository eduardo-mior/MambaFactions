package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsConvite extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsConvite() 
	{
		// Aliases
	    this.addAliases("convidar", "i", "adicionar", "invite", "convites");
		    
		// Descrição
		this.setDesc("§6 convite §8-§7 Gerencia os convites da facção.");
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsConviteAdd cmdFactionsConviteAdd = new CmdFactionsConviteAdd();
	public CmdFactionsConviteDel cmdFactionsConviteDel = new CmdFactionsConviteDel();
	public CmdFactionsConviteListar cmdFactionsConviteListar = new CmdFactionsConviteListar();
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //	
	
	@Override
	public void perform() throws MassiveException
	{
		// Verificando se o player possui facção
		if (!msender.hasFaction() && msender.isPlayer()) {
			if (msender.getInvitations().isEmpty()) {
				msg("§cVocê não possui convites de facções pendentes.");
				return;
			} else {
				EngineMenuGui.get().abrirMenuConvitesRecebidos(msender);
				return;
			}
		}
		
		// Verificando se possui permissão
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msg("§cVocê precisar ser capitão ou superior para poder gerenciar os convites da facção.");
			return;
		}
		
		// Verificando se é um player para abrir o menu gui
		if (msender.isPlayer()) {
			EngineMenuGui.get().abrirMenuConvites(me);
			return;
		}
	}
}
