package pl.WindSkull.LoginPlugin;

import javax.annotation.Nonnull;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.avaje.ebean.EbeanServer;


public class RegisterCommand implements CommandExecutor{

	
	private EbeanServer eserver;
	public RegisterCommand(EbeanServer es) 
	{
		eserver = es;
	}
	
	
	@Override
	public boolean onCommand(@Nonnull CommandSender arg0, @Nonnull Command arg1, @Nonnull String arg2, @Nonnull String[] arg3) 
	{
		
		if(!(arg0 instanceof Player)) return false;
		
		Player p = (Player) arg0;
		if(!Core.loginSession.containsKey(p))
			return Misc.trueMessage(p, LangManager.getMessage(LangManager.PLAYER_LOGED));
		if(Core.loginSession.get(p))
			return Misc.trueMessage(p, LangManager.getMessage(LangManager.PLAYER_REGISTERED));
		
		if(arg3.length < 1)
			return Misc.trueMessage(p, LangManager.getMessage(LangManager.SHORT_PASSWORD));
		
		if(!arg3[0].equals(arg3[1]))
			return Misc.trueMessage(p, LangManager.getMessage(LangManager.PASSWORDS_NOT_EQUAL));
		
		PlayerDataBase pdb = new PlayerDataBase();
		PlayerDataBase.readFromPlayer(pdb,p);
		try 
		{
			pdb.setPlayerPassword(LoginCommand.encrypt(p.getName(), arg3[0]));
			eserver.save(pdb);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return Misc.trueMessage(p, LangManager.getMessage(LangManager.ERROR_ON_SAVE));
		}
		
		return false;
	}


}
