package com.massivecraft.factions.cmd;

import java.util.LinkedHashMap;

import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.engine.EngineKdr;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Progressbar;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
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
        
		// Parametros (não necessario)
		this.addParameter(TypeMPlayer.get(), "player", "você");

		// Descrição do comando
		this.setDesc("§6 p,perfil §e<player> §8-§7 Mostra os status do player.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		MPlayer mplayer = this.readArg(msender);
		
		// Verificando se o player tem facção
		final boolean hasfac = mplayer.hasFaction();
		
		// Criando o titulo da mensagem
		message(Txt.titleize("§ePefil de §e" + mplayer.getName()));
		
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
		
		// Mostrando o cargo do player
		if (mplayer.hasPowerBoost())
		{
			double powerBoost = mplayer.getPowerBoost();
			String powerBoostType = (powerBoost > 0 ? "§2bônus§e" : "§4penalidade§e");
			msg("§6Boost de Poder: §e%f §e(%s)", powerBoost, powerBoostType);
		}
				
		// Mostrando se o player esta online
		msg("§6Status: " + (mplayer.isOnline() ? "§2Online" : "§cOffline"));
		
		long ultimoLoginMillis = mplayer.getLastActivityMillis() - System.currentTimeMillis();
		LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(ultimoLoginMillis, TimeUnit.getAllButMillis()), 3);
		String ultimoLogin = TimeDiffUtil.formatedMinimal(ageUnitcounts, "§e");
		msg("§6Último login: §e" + ultimoLogin + "§e atrás");
		
		
		// Mostrado o kdr e os stats de pvp 
		msg("§6Abates / Mortes / Kdr: §e" + EngineKdr.getPlayerKills(mplayer) + "§e/" + EngineKdr.getPlayerDeaths(mplayer) + "§e/" + EngineKdr.getPlayerKdr(mplayer));
	}
}
