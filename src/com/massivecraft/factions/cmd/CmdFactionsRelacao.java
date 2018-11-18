package com.massivecraft.factions.cmd;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.event.EventFactionsRelationChange;
import com.massivecraft.factions.util.OthersUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsRelacao extends FactionsCommand
{
	// -------------------------------------------- //
	// COSTANTS
	// -------------------------------------------- //

	public static final Set<Rel> RELEVANT_RELATIONS = new MassiveSet<>(Rel.ALLY, Rel.ENEMY);
	public static final String SEPERATOR = Txt.parse("§f: ");

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRelacao()
	{
		// Aliases
	    this.addAliases("relação", "relation", "rel");
	    
		// Descrição
		this.setDesc("§6 relacao §8-§7 Gerencia as relações da facção.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeString.get(), "facção", "erro");
		this.addParameter(TypeString.get(), "facção", "erro", true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //	
	
	@Override
	public void perform() throws MassiveException
	{	
		// Gambiara para o /f relacao listar
		if (performListFactions()) return;
		
		// Verificando se o sender é um player
		if (!msender.isPlayer()) {
			if (!this.argIsSet(0) || !this.argIsSet(1)) {
				msg("§cComando incorreto, use /f relacao <facção> <relacao>");
				return;
			}
		}
		
		// Verificando se é um player para abrir o menu gui
		if (!this.argIsSet(0)) {
			if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
				EngineMenuGui.get().abrirMenuVerRelacoes(me);
				return;
			} else {
				EngineMenuGui.get().abrirMenuGerenciarRelacoes(me);
				return;
			}
		}
		
		// Verificando se o player possui permissão
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msg("§cVocê precisar ser capitão ou superior para poder gerenciar as relações da facção.");
			return;
		}
				
		// Verificando se a facção target é a mesma facção do msender
		String name = this.argAt(0);
		if (msenderFaction.getName().equalsIgnoreCase(name)) {
			msg("§cVocê não pode definir uma relação com sua própria facção.");
			return;
		}
		
		// Argumentos
		Faction otherFaction = readFaction(name);
		
		// Menu gui /f relação
		if (!this.argIsSet(1)) {
			EngineMenuGui.get().abrirMenuDefinirRelacao(msender, otherFaction);
			return;
		}
		
		// Argumentos
		Rel newRelation = readRelation(this.argAt(1));
		Rel atualRelation = msenderFaction.getRelationWish(otherFaction);
		
		// Verificando se a relação da facção target é a mesma da facção do msender
		if (atualRelation == newRelation) {
			if (newRelation == Rel.ALLY && atualRelation != Rel.ALLY) {
				msg("§eA sua facção já possui um convite de aliança pendente com a §f[%s§f]§e.", otherFaction.getName());
				return;
			} else {
				msg("§eA sua facção já é %s§e da §f[%s§f]§e.", newRelation.getDescFactionOne(), otherFaction.getName());
				return;
			}
		}
		
		// Verificando se a nova relação é de aliança 
		if (newRelation == Rel.ALLY) 
		{
			// Verificando se a do sender não passou o limite de aliados
			if (msenderFaction.getAllys().size() >= MConf.get().factionAllyLimit) {
				msg("§cA sua facção já antingiu o limite máximo de aliados permitidos por facção (%s).", MConf.get().factionAllyLimit);
				return;
			}
			
			// Verificando se a do target não passou o limite de aliados
			if (otherFaction.getAllys().size() >= MConf.get().factionAllyLimit) {
				msg("§cA a facção §f[%s§f]§c já antingiu o limite máximo de aliados permitidos por facção (%s).", otherFaction.getName(), MConf.get().factionAllyLimit);
				return;
			}
			
			// Verificando se a facção não passou do limite de convites pendentes
			if (otherFaction.getRelationWish(msenderFaction) != Rel.ALLY && OthersUtil.getAliadosPendentesEnviados(msenderFaction).size() >= 21) {
				msg("§cA sua facção já atingiu o limite máximo de alianças pendentes por facção (21).");
				return;
			}
			
			if (otherFaction.getRelationWish(msenderFaction) != Rel.ALLY && otherFaction.getPendingRelations().size() >= 21) {
				msg("§cA a facção §f[%s§f]§c já antingiu o limite máximo de alianças pendetes por facção (21).", otherFaction.getName());
				return;
			}
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
		if (newRelation == currentRelation)	{
			otherFaction.msg("§f[%s§f]§e definiu sua facção como %s§e.", msenderFaction.getName(), newRelation.getDescFactionOne());
			msenderFaction.msg("§f[%s§f]§e agora é %s§e.", otherFaction.getName(), newRelation.getDescFactionOne());
			
			if (newRelation == Rel.ALLY) {
				otherFaction.removePendingRelation(msenderFaction);
				msenderFaction.removePendingRelation(otherFaction);
			}
		}
		
		// Informando que a facção deseja ser aliado
		else
		{
			String colorOne = newRelation.getColor() + newRelation.getDescFactionOne();
			String relation =  newRelation.getName().toLowerCase();

			// Mson && Json
			Mson factionsRelationshipChange = mson(
				Mson.parse("§f[%s§f]§e deseja se tornar %s§e.", msenderFaction.getName(), colorOne),
				Mson.SPACE,
				mson("§e[ACEITAR]").command("/f relacao " + msenderFaction.getName() + " " + relation)
			);
			
			otherFaction.sendMessage(factionsRelationshipChange);
			msenderFaction.msg("§f[%s§f]§e foi informada de que a sua facção deseja se tornar %s§e.", otherFaction.getName(), colorOne);
	
			// Sistema de relações pendentes
			Rel my = msenderFaction.getRelationWish(otherFaction);
			Rel other = otherFaction.getRelationWish(msenderFaction);
			if (my == Rel.ALLY && other != Rel.ALLY) {
				otherFaction.addPendingRelation(msenderFaction);
			} else if (other == Rel.ALLY && my != Rel.ALLY) {
				msenderFaction.addPendingRelation(otherFaction);
			}
		}
		
		// Aplicando o evento
		msenderFaction.changed();
	}

	private boolean performListFactions() throws MassiveException 
	{
		if (!this.argIsSet(0)) return false;
		String arg = this.argAt(0).toLowerCase();
		if (!arg.equals("list") && !arg.equals("listar")) return false;
		
		// Argumentos (não necessario)
		int page = this.argIsSet(1) ? readInt() : 1;

		// Pager Create
		final Faction faction = msenderFaction;
		final Pager<String> pager = new Pager<>(this, "", page, new Stringifier<String>()
		{
			@Override
			public String toString(String item, int index)
			{
				return item;
			}
		});

		Bukkit.getScheduler().runTaskAsynchronously(Factions.get(), new Runnable()
		{
			@Override
			public void run()
			{
				// Prepare Items
				List<String> relNames = new MassiveList<>();
				for (Entry<Rel, List<String>> entry : FactionColl.get().getRelationNames(faction, RELEVANT_RELATIONS).entrySet())
				{
					Rel relation = entry.getKey();
					String coloredName = relation.getColor().toString() + relation.getName();

					for (String name : entry.getValue())
					{
						relNames.add(coloredName + SEPERATOR + name);
					}
				}

				// Pager Title
				pager.setTitle(Txt.parse("§eRelações da Facção §2(%d)", relNames.size()));

				// Pager Items
				pager.setItems(relNames);

				// Pager Message
				pager.message();
			}
		});
		return true;
	}	
}
