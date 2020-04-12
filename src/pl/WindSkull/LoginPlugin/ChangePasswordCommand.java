package pl.WindSkull.LoginPlugin;

import javax.annotation.Nonnull;
import javax.crypto.BadPaddingException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.avaje.ebean.EbeanServer;

public class ChangePasswordCommand implements CommandExecutor{

	private EbeanServer eserver;
	public ChangePasswordCommand(EbeanServer es) 
	{
		eserver = es;
	}
	@Override
	public boolean onCommand(@Nonnull CommandSender arg0, @Nonnull Command arg1, @Nonnull String arg2,
			@Nonnull String[] arg3) 
	{
		if(arg0 instanceof Player)
		{
			Player p = (Player) arg0;
			if(arg3.length < 2)
				return false;
			
			PlayerDataBase pdb = eserver.find(PlayerDataBase.class).where().eq(PlayerDataBase.PLAYER_NAME, p.getName()).findUnique();
			if(pdb == null)
				return Misc.trueMessage(p, LangManager.getMessage(LangManager.PLAYER_NOT_REGISTERED));
			
			String password = arg3[0];
			try 
			{
				if(p.getName().equalsIgnoreCase(LoginCommand.decrypt(pdb.getPlayerPassword(), password)))
				{
					if(!arg3[1].equals(arg3[2]))
						return Misc.trueMessage(p, LangManager.getMessage(LangManager.PASSWORDS_NOT_EQUAL));
					
					pdb.setPlayerPassword(LoginCommand.encrypt(p.getName(), arg3[1]));
					eserver.save(pdb);
					
					return Misc.trueMessage(p, LangManager.getMessage(LangManager.PASSWORD_CHANGED));
				}
			}
			catch (BadPaddingException ex)
			{
				return Misc.trueMessage(p, LangManager.getMessage(LangManager.PASSWORD_WRONG));
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return false;
	}

}
