package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FactionsParticipator;
import com.massivecraft.factions.Perm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeNullable;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import com.massivecraft.massivecore.util.Txt;

public abstract class CmdFactionsPoderAbstract extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	protected CmdFactionsPoderAbstract(Type<? extends FactionsParticipator> parameterType, String parameterName)
	{
		// Parametros (não necessario)
		this.addParameter(parameterType, parameterName);
		this.addParameter(TypeNullable.get(TypeDouble.get()), "quantidade", "ver");
		
        // Visibilidade do comando
        this.setVisibility(Visibility.SECRET);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		/*
		 * Este comando é um pouco complexo de editar e também é pouco usado.
		 * Você pode pular esta parte ou simplesmente deletar este comando.
		 */
		
		// Parameters
		FactionsParticipator factionsParticipator = this.readArg();
		Double powerBoost = this.readArg(factionsParticipator.getPowerBoost());
		
		// Try set the powerBoost
		boolean updated = this.trySet(factionsParticipator, powerBoost);
		
		// Inform
		this.informPowerBoost(factionsParticipator, powerBoost, updated);
	}
	
	private boolean trySet(FactionsParticipator factionsParticipator, Double powerBoost) throws MassiveException
	{
		// Trying to set?
		if (!this.argIsSet(1)) return false;
		
		// Check set permissions
		if (!Perm.PODER_SET.has(sender, true)) throw new MassiveException();
		
		// Set
		factionsParticipator.setPowerBoost(powerBoost);
		
		// Return
		return true;
	}
	
	private void informPowerBoost(FactionsParticipator factionsParticipator, Double powerBoost, boolean updated)
	{
		// Prepare
		String participatorDescribe = factionsParticipator.describeTo(msender, false);
		powerBoost = powerBoost == null ? factionsParticipator.getPowerBoost() : powerBoost;
		String powerDescription = Txt.parse(Double.compare(powerBoost, 0D) >= 0 ? "§aum §2bônus" : "§auma §4penalidade");
		String when = updated ? "agora " : "";
		
		// Create message
		String messagePlayer = Txt.parse("§a%s§a %spossui§a %s§a de poder de §d%.2f§a.", participatorDescribe, when, powerDescription, powerBoost);
		
		// Inform
		msender.message(messagePlayer);
	}
	
}
