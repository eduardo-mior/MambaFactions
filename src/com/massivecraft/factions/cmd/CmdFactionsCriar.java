package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasntFaction;
import com.massivecraft.factions.cmd.type.TypeFactionNameStrict;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.store.MStore;

public class CmdFactionsCriar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsCriar()
	{
		// Aliases
		this.addAliases("new", "create");
		
		// Parametros (necessario)
		this.addParameter(TypeFactionNameStrict.get(), "nome");
		
		// Requisições
		this.addRequirements(ReqHasntFaction.get());

		// Descrição do comando
		this.setDesc("§6 criar §e<nome> §8-§7 Cria uma nova facção.");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		String newName = this.readArg();
		
		// Pre-Generate Id (pré-criação do id da facção)
		String factionId = MStore.createId();
		
		// Evento
		EventFactionsCreate createEvent = new EventFactionsCreate(sender, factionId, newName);
		createEvent.run();
		if (createEvent.isCancelled()) return;
		
		// Aplicando o evento
		Faction faction = FactionColl.get().create(factionId);
		faction.setName(newName);
		
		msender.setRole(Rel.LEADER);
		msender.setFaction(faction);
		
		EventFactionsMembershipChange joinEvent = new EventFactionsMembershipChange(sender, msender, faction, MembershipChangeReason.CREATE);
		joinEvent.run();
		// NOTA: O factions cria uma facção por existe o JoinEvent, para colocar o sender dentro da facção.
		
		// Informando o sender
		msg("§aFacção §f%s§a criada com sucesso!", newName);
		setDefaultPermissions(faction);
	}
	
	private void setDefaultPermissions(Faction faction) {
		faction.getPermitted(MPerm.getPermBuild()).remove(Rel.ALLY);
		faction.getPermitted(MPerm.getPermBuild()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermBuild()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermBuild()).remove(Rel.NEUTRAL);
		faction.getPermitted(MPerm.getPermBuild()).remove(Rel.RECRUIT);
		faction.getPermitted(MPerm.getPermContainer()).remove(Rel.ALLY);
		faction.getPermitted(MPerm.getPermContainer()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermContainer()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermContainer()).remove(Rel.NEUTRAL);
		faction.getPermitted(MPerm.getPermContainer()).remove(Rel.RECRUIT);
		faction.getPermitted(MPerm.getPermHome()).remove(Rel.ALLY);
		faction.getPermitted(MPerm.getPermHome()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermHome()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermHome()).remove(Rel.NEUTRAL);
		faction.getPermitted(MPerm.getPermHome()).add(Rel.RECRUIT);
		faction.getPermitted(MPerm.getPermDoor()).add(Rel.RECRUIT);
		faction.getPermitted(MPerm.getPermDoor()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermDoor()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermDoor()).remove(Rel.NEUTRAL);
		faction.getPermitted(MPerm.getPermButton()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermButton()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermButton()).remove(Rel.NEUTRAL);
		faction.getPermitted(MPerm.getPermLever()).remove(Rel.ENEMY);
		faction.getPermitted(MPerm.getPermLever()).remove(Rel.TRUCE);
		faction.getPermitted(MPerm.getPermLever()).remove(Rel.NEUTRAL);
	}
}
