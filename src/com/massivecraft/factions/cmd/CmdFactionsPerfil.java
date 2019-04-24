package com.massivecraft.factions.cmd;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Progressbar;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsPerfil extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPerfil()
	{
		// Aliases
        this.addAliases("p", "player");

		// Descrição
		this.setDesc("§6 p,perfil §e<player> §8-§7 Mostra os status do player.");
		
		// Parametros (não necessario)
		this.addParameter(TypeString.get(), "outro player", "você", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		MPlayer mplayer = readMPlayer();
		
		// Verificando se o player tem facção
		final boolean hasfac = mplayer.hasFaction();
		
		// Criando o titulo da mensagem
		message(Txt.titleize(mplayer.getId().equals(msender.getId()) ? "§eSeu Perfil" : "§ePerfil de §e" + mplayer.getName()));
		
		// Criando a profressbar do powr do player
		double progressbarQuota = 0;
		double playerPowerMax = mplayer.getPowerMax();
		if (playerPowerMax != 0)
		{
			progressbarQuota = mplayer.getPower() / playerPowerMax;
		}
		
		int progressbarWidth = (int) Math.round(mplayer.getPowerMax() / mplayer.getPowerMaxUniversal() * 100);
		msg("§6Poder: §e%s", Progressbar.HEALTHBAR_CLASSIC.withQuota(progressbarQuota).withWidth(progressbarWidth).render());
				
		// Poder dotal e poder atual do player
		msg("§6Poder Total: §e%.2f§e/§e%.2f", mplayer.getPower(), mplayer.getPowerMax());
				
		// Mostrando a facção do player
		msg("§6Facção: §e" + (hasfac ? mplayer.getFactionName() : "§7§oNenhuma"));
		
		msg("§6Cargo: §e" + (hasfac ? (mplayer.getRole().getPrefix() + mplayer.getRole().getName()) : "§7§oNenhum"));
		
		// Se o player possui poder máximo
		if (mplayer.hasPowerBoost())
		{
			msg("§6Poder máximo: §e%f", mplayer.getPowerBoost());
		}
		
		// Mostrando se o player esta online
		msg("§6Status: " + (mplayer.isOnline() ? "§2Online" : "§cOffline"));
		
		// Mostrando a data e o horario do ultimo login
		String ultimoLogin = new SimpleDateFormat("dd/MM/yyyy 'às' hh:mm").format(new Date(mplayer.getLastActivityMillis()));
		msg("§6Último login: §e" + ultimoLogin);
		
		// Mostrado o kdr e os stats de pvp 
		msg("§6Abates / Mortes / Kdr: §e" + mplayer.getKills() + "§e/" + mplayer.getDeaths() + "§e/" + mplayer.getKdrRounded());
	}
}
