package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsAdmin extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsAdmin()
	{
		// Aliases
		this.addAliases("adm");

		// Parametros (não necessario)
		this.addParameter(TypeBooleanYes.get(), "on/off", "flip");

		// Descrição do comando
		this.setDesc("§6 admin §e[on/off] §8-§7 Entra e sai do modo admin.");
		
        // Visibilidade do comando
        this.setVisibility(Visibility.SECRET);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		boolean old = msender.isOverriding();
		boolean target = this.readArg(!old);
		String desc = Txt.parse(target ? "§2habilitado" : "§cdesabilitado");

		if (target == old)
		{
			msg("§aVocê já está com o modo admin %s§a.", desc);
			return;
		}
		
		// Aplicando o evento
		msender.setOverriding(target);		
		
		// Informando os players players		
		msender.msg(Txt.parse("§aModo admin " + desc + "§a."));
	}
}
