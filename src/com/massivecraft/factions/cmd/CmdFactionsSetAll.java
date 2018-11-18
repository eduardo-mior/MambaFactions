package com.massivecraft.factions.cmd;

import java.util.Set;

import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsSetAll extends CmdFactionsSetXAll
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSetAll(boolean claim)
	{
		// Super
		super(claim);
		
		// Aliases
		this.addAliases("all");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Set<PS> getChunks() throws MassiveException
	{	
		// Verificando se o player possui permissão 
		if (!MPerm.getPermTerritory().has(msender, msenderFaction, true)) throw new MassiveException();
					
		// Verificando se o argumento foi definido ou se o sender é o console
		if ((this.argIsSet() && this.argAt(0).equalsIgnoreCase("confirmar")) || !msender.isPlayer()) 	
		{
			Set<PS> chunks = BoardColl.get().getChunks(msenderFaction);
			this.setFormatOne("§a%s§a %s todas as chunks da facção (§d%d§a).");
			this.setFormatMany("§a%s§a %s todas as chunks da facção (§d%d§a).");
			
			// Verificando se a facção possui terras para abandonar
			if (chunks.size() == 0) throw new MassiveException().setMsg("§cA sua facção não possui terras para abandonar.");
			
			return chunks;
		} 
		
		// Caso o argumento não seja informado então é aberto o menu de confirmação
		else 
		{
			// Verificando se a facção possui terras para abandonar
			if (msenderFaction.getLandCount() < 1) throw new MassiveException().setMsg("§cA sua facção não possui terras para abandonar.");
			
			EngineMenuGui.get().abrirMenuAbandonarTerras(me);
			throw new MassiveException();
		} 		
	}
	
}