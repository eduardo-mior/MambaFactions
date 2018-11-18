package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsAdmin extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsAdmin()
	{
		// Aliases
		this.addAliases("adm");

		// Descrição
		this.setDesc("§6 admin §e[on/off] §8-§7 Entra e sai do modo admin.");
		
		// Parametros (não necessario)
		this.addParameter(TypeString.get(), "on/off", "erro", true);
		
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
		Boolean old = msender.isOverriding();
		Boolean target = readBoolean(old);
		
		// Verificando se o player digitou um argumento correto
		if (target == null) {
			msg("§cComando incorreto, use /f admin [on/off]");
			return;
		}
		
		// Descrição da ação
		String desc = target ? "§2ativado": "§cdesativado";

		// Verificando se o player já esta com modo admin habilitado.
		if (target == old) {
			msg("§aVocê já está com o modo admin %s§a.", desc);
			return;
		}
		
		// Aplicando o evento
		msender.setOverriding(target);		
		
		// Informando os players players
		msender.msg("§aModo admin " + desc + "§a.");
	}
}
