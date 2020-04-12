package pl.WindSkull.LoginPlugin;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.avaje.ebean.EbeanServer;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mengcraft.simpleorm.EbeanHandler;
import com.mengcraft.simpleorm.EbeanManager;

@SuppressWarnings("deprecation")
public class Core extends JavaPlugin implements Listener
{
	//private HashMap<InetSocketAddress, BukkitTask> lastSesions = new  HashMap<>();
	public static HashMap<Player,Boolean> loginSession = new HashMap<>();
	public static HashMap<Player,Boolean> loginStatus = new HashMap<>();
	
	private Cache<InetAddress,Boolean> lastSesions = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();
	/*
	 * private final Cache<String, Object> pool = CacheBuilder.newBuilder()
	 * .expireAfterAccess(5, TimeUnit.HOURS) .build();
	 */
	
	
	private EbeanServer eserver;
	
	

	@Override
	public void onEnable() 
	{
		super.onEnable();
		
		//Enable ebean
		EbeanManager manager = getServer().getServicesManager()
				.getRegistration(EbeanManager.class)
				.getProvider();
		EbeanHandler handler = manager.getHandler(this);
		if (handler.isNotInitialized()) 
		{
			handler.define(PlayerDataBase.class);
			try 
			{
				handler.initialize();
			} 
			catch(Exception e) 
			{
				System.out.println(e.getMessage());
				return;
			}
		}
		handler.reflect();
		handler.install();
		eserver = handler.getServer();
		
		
		saveResource("langconf.yml", false);
		LangManager.loadMessages(this);
		//Register commands
		this.getCommand("login").setExecutor(new LoginCommand(eserver));
		this.getCommand("register").setExecutor(new RegisterCommand(eserver));
		this.getCommand("passchange").setExecutor(new ChangePasswordCommand(eserver));
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent e)
	{
		Player p = e.getPlayer();
		Bukkit.getScheduler().runTaskLater(this,() -> {p.setWalkSpeed(0f);p.setFlySpeed(0f);},4);
		p.setWalkSpeed(0f);
		p.setFlySpeed(0f);
		try
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE-1, 150));
		}
		catch(NullPointerException ex)
		{
			System.out.println(ex);
		}
		
		
		PlayerDataBase pdb = eserver.find(PlayerDataBase.class).where().eq(PlayerDataBase.PLAYER_NAME, e.getPlayer().getName()).findUnique();
		/*if(lastSesions.getIfPresent(p.getAddress().getAddress()))
		{
			lastSesions.invalidate(p.getAddress());
			Core.loginSession.remove(p);
			Core.loginStatus.put(p, true);
			PlayerDataBase.writeToPlayer(pdb,p);
			Misc.sendAfterTime(this, p, LangManager.getMessage(LangManager.PLAYER_LOGED), 4l);
		}
		else
		{*/
			if(pdb == null)
			{
				Misc.sendAfterTime(this, p, LangManager.getMessage(LangManager.REGISTER_MESSAGE), 4l);
				loginSession.put(p, false);
			}
			else
			{
				System.out.println(LangManager.getMessage(LangManager.LOGIN_MESSAGE));
				Misc.sendAfterTime(this, p, LangManager.getMessage(LangManager.LOGIN_MESSAGE), 4l);
				loginSession.put(p, true);
			}
		//}
	}
	
	@EventHandler
	public void onPlayerExit(PlayerQuitEvent e)
	{
		savePlayer(e.getPlayer());
	}
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e)
	{
		savePlayer(e.getPlayer());
	}
	
	
	
	
	private void savePlayer(Player p)
	{
		final InetSocketAddress addres = p.getAddress();
		
		PlayerDataBase pdb = eserver.find(PlayerDataBase.class).where().eq(PlayerDataBase.PLAYER_NAME, p.getName()).findUnique();
		if(pdb == null) { System.out.print("Player Data Base Is NULL!!!!!!!!!!!!");return;}
		PlayerDataBase.readFromPlayer(pdb,p);
		eserver.save(pdb);
		p.teleport(p.getWorld().getSpawnLocation());
		p.getInventory().clear();
		lastSesions.put(addres.getAddress(), true);
	}
	
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			if(loginSession.containsKey(e.getEntity()))
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChatCommand(PlayerCommandPreprocessEvent  e)
	{
		if(loginSession.containsKey(e.getPlayer()))
		{
			String message = e.getMessage().toLowerCase();
			if(!message.startsWith("/login") && !message.startsWith("/register"))
			{
				e.setCancelled(true);
				e.getPlayer().sendMessage(loginSession.get(e.getPlayer()) ? LangManager.getMessage(LangManager.LOGIN_MESSAGE) : LangManager.getMessage(LangManager.REGISTER_MESSAGE));
			}	
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMessage(PlayerChatEvent e)
	{
		if(loginSession.containsKey(e.getPlayer()))
		{
			String message = e.getMessage().toLowerCase();
			if(!message.startsWith("/login") && !message.startsWith("/register"))
			{
				e.setCancelled(true);
				e.getPlayer().sendMessage(loginSession.get(e.getPlayer()) ? LangManager.getMessage(LangManager.LOGIN_MESSAGE) : LangManager.getMessage(LangManager.REGISTER_MESSAGE));
			}	
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		if(loginSession.containsKey(e.getPlayer())) e.setCancelled(true);
	}
}
