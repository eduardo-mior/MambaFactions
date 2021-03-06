package com.massivecraft.factions.cmd;

import java.util.Set;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.event.EventFactionsRelationChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsRelacao extends FactionsCommand
{
	// -------------------------------------------- //
	// COSTANTS
	// -------------------------------------------- //

	public static final Set<Rel> RELEVANT_RELATIONS = new MassiveSet<>(Rel.ALLY, Rel.ENEMY);
	public static final String SEPERATOR = Txt.parse("�f: ");

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRelacao()
	{
		// Aliases
	    this.addAliases("rela��o", "relation", "rel");
	    
		// Descri��o
		this.setDesc("�6 relacao �8-�7 Gerencia as rela��es da fac��o.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeString.get(), "fac��o", "erro");
		this.addParameter(TypeString.get(), "fac��o", "erro", true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //	
	
	@Override
	public void perform() throws MassiveException
	{	
		// Gambiara para o /f relacao listar
		if (performListFactions()) return;
		
		// Verificando se o sender � um player
		if (!msender.isPlayer()) {
			if (!this.argIsSet(0) || !this.argIsSet(1)) {
				msg("�cComando incorreto, use /f relacao <fac��o> <relacao>");
				return;
			}
		}
		
		// Verificando se � um player para abrir o menu gui
		if (!this.argIsSet(0)) {
			if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
				EngineMenuGui.get().abrirMenuVerRelacoes(msender);
				return;
			} else {
				EngineMenuGui.get().abrirMenuGerenciarRelacoes(msender);
				return;
			}
		}
		
		// Verificando se o player possui permiss�o
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msg("�cVoc� precisar ser capit�o ou superior para poder gerenciar as rela��es da fac��o.");
			return;
		}
				
		// Verificando se a fac��o target � a mesma fac��o do msender
		String tag = this.argAt(0);
		if (msenderFaction.getName().equalsIgnoreCase(tag)) {
			msg("�cVoc� n�o pode definir uma rela��o com sua pr�pria fac��o.");
			return;
		}
		
		// Argumentos
		Faction otherFaction = readFaction(tag);
		
		// Menu gui /f rela��o
		if (!this.argIsSet(1)) {
			EngineMenuGui.get().abrirMenuDefinirRelacao(msender, otherFaction);
			return;
		}
		
		// Argumentos
		Rel newRelation = readRelation(this.argAt(1));
		Rel atualRelation = msenderFaction.getRelationWish(otherFaction);
		
		// Verificando se a rela��o da fac��o target � a mesma da fac��o do msender
		if (atualRelation == newRelation) {
			if (newRelation == Rel.ALLY && atualRelation != Rel.ALLY) {
				msg("�eA sua fac��o j� possui um convite de alian�a pendente com a �f[%s�f] �e.", otherFaction.getName());
				return;
			} else {
				msg("�eA sua fac��o j� � %s�e da �f[%s�f] �e.", newRelation.getDescFactionOne(), otherFaction.getName());
				return;
			}
		}
		
		// Verificando se a nova rela��o � de alian�a 
		if (newRelation == Rel.ALLY) 
		{
			// Verificando se a do sender n�o passou o limite de aliados
			if (msenderFaction.getAllys().size() >= MConf.get().factionAllyLimit) {
				msg("�cA sua fac��o j� antingiu o limite m�ximo de aliados permitidos por fac��o (%s).", MConf.get().factionAllyLimit);
				return;
			}
			
			// Verificando se a do target n�o passou o limite de aliados
			if (otherFaction.getAllys().size() >= MConf.get().factionAllyLimit) {
				msg("�cA a fac��o �f[%s�f] �c j� antingiu o limite m�ximo de aliados permitidos por fac��o (%s).", otherFaction.getName(), MConf.get().factionAllyLimit);
				return;
			}
			
			// Verificando se a fac��o n�o passou do limite de convites pendentes
			if (otherFaction.getRelationWish(msenderFaction) != Rel.ALLY && msenderFaction.getAliadosPendentesEnviados().size() >= 21) {
				msg("�cA sua fac��o j� atingiu o limite m�ximo de alian�as pendentes por fac��o (21).");
				return;
			}
			
			if (otherFaction.getRelationWish(msenderFaction) != Rel.ALLY && otherFaction.getPendingRelations().size() >= 21) {
				msg("�cA a fac��o �f[%s�f] �c j� antingiu o limite m�ximo de alian�as pendetes por fac��o (21).", otherFaction.getName());
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

		// Definindo a rela��o sem precisar de confirma��o
		if (newRelation == currentRelation)	{
			otherFaction.msg("�f[%s�f] �e definiu sua fac��o como %s�e.", msenderFaction.getName(), newRelation.getDescFactionOne());
			msenderFaction.msg("�f[%s�f] �e agora � %s�e.", otherFaction.getName(), newRelation.getDescFactionOne());
			
			// Sistema de rela��es pendentes
			if (newRelation == Rel.ALLY) {
				otherFaction.removePendingRelation(msenderFaction);
				msenderFaction.removePendingRelation(otherFaction);
			}
			if (atualRelation == Rel.ALLY && newRelation != Rel.ALLY) {
				msenderFaction.addPendingRelation(otherFaction);
			}
			
			// Cache do sistema de inimigos
			if (newRelation == Rel.ENEMY) {
				otherFaction.addEnemy(msenderFaction);
				msenderFaction.addEnemy(otherFaction);
			} else {
				otherFaction.removeEnemy(msenderFaction);
				msenderFaction.removeEnemy(otherFaction);
			}
		}
		
		else
		{
			String colorOne = newRelation.getColor() + newRelation.getDescFactionOne();
			String relation =  newRelation.getName().toLowerCase();

			// Mson && Json
			Mson factionsRelationshipChange = mson(
				Mson.parse("�f[%s�f] �e deseja se tornar %s�e.", msenderFaction.getName(), colorOne),
				Mson.SPACE,
				mson("�e[ACEITAR]").command("/f relacao " + msenderFaction.getName() + " " + relation)
			);
			
			otherFaction.sendMessage(factionsRelationshipChange);
			msenderFaction.msg("�f[%s�f] �e foi informada de que a sua fac��o deseja se tornar %s�e.", otherFaction.getName(), colorOne);
	
			// Sistema de rela��es pendentes
			Rel my = msenderFaction.getRelationWish(otherFaction);
			Rel other = otherFaction.getRelationWish(msenderFaction);
			if (my == Rel.ALLY && other != Rel.ALLY) {
				otherFaction.addPendingRelation(msenderFaction);
			} else if (other == Rel.ALLY && my != Rel.ALLY) {
				msenderFaction.addPendingRelation(otherFaction);
			}
			// Sistema de cache de inimigos
			if (my == Rel.ENEMY || other == Rel.ENEMY) {
				otherFaction.addEnemy(msenderFaction);
				msenderFaction.addEnemy(otherFaction);
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
		
        int nrelations = msenderFaction.getEnemies().size() + msenderFaction.getAllys().size() + msenderFaction.getSemiAllys().size();
        if (nrelations <= 0) {
        	msg("�cSua fac��o n�o possui rela��es definidas.");
        	return true;
        }
        
		if (msender.isPlayer()) {
			EngineMenuGui.get().abrirMenuRelacoesListar(msender, 1);
		}
		
		return true;
	}
}
