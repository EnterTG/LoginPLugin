package pl.WindSkull.LoginPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Misc {


	public static boolean falseMessage(Player p,String message)
	{
		return message(p,message,false);
	}
	
	public static boolean trueMessage(Player p,String message)
	{
		return message(p,message,true);
	}
	
	public static boolean message(Player p,String message,boolean b)
	{
		p.sendMessage(message);
		return b;
	}
	
	public static void sendAfterTime(Core c,Player p,String message,long time)
	{
		Bukkit.getScheduler().runTaskLater(c, () -> p.sendMessage(message), time);
	}
	
}
