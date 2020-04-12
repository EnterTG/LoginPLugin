package pl.WindSkull.LoginPlugin;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

public class LangManager {

	
	public static final String SHORT_PASSWORD = "shortPassword";
	public static final String PLAYER_LOGED = "playerLoged";
	public static final String PLAYER_REGISTERED = "playerRegistered";
	public static final String PLAYER_NOT_REGISTERED = "playerNotRegistered";
	public static final String PASSWORDS_NOT_EQUAL = "passwordNotEqual";
	public static final String ERROR_ON_SAVE = "errorOnSave";
	public static final String REGISTER_MESSAGE = "registerMessage";
	public static final String LOGIN_MESSAGE = "loginMessage";
	public static final String PASSWORD_CHANGED = "passwordChanged";
	public static final String PASSWORD_WRONG = "passwordWrong";
	
	
	public static HashMap<String, String> messages = new HashMap<>();
	
	public static String getMessage(String key)
	{
		if(messages.containsKey(key)) return ChatColor.translateAlternateColorCodes('&', messages.get(key));
		else return "Message error";
	}
	
	public static void loadMessages(Core c)
	{
		FileConfiguration fc = new YamlConfiguration();;
		try {
			fc.load(c.getDataFolder().getAbsolutePath() + "\\langconf.yml");
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		loadDefault(fc);
		loadMessage(SHORT_PASSWORD,fc.getString(SHORT_PASSWORD));
		loadMessage(PLAYER_LOGED,fc.getString(PLAYER_LOGED));
		loadMessage(PLAYER_REGISTERED,fc.getString(PLAYER_REGISTERED));
		loadMessage(PLAYER_NOT_REGISTERED,fc.getString(PLAYER_NOT_REGISTERED));
		loadMessage(PASSWORDS_NOT_EQUAL,fc.getString(PASSWORDS_NOT_EQUAL));
		loadMessage(ERROR_ON_SAVE,fc.getString(ERROR_ON_SAVE));
		loadMessage(REGISTER_MESSAGE,fc.getString(REGISTER_MESSAGE));
		loadMessage(LOGIN_MESSAGE,fc.getString(LOGIN_MESSAGE));
		loadMessage(PASSWORD_CHANGED,fc.getString(PASSWORD_CHANGED));
		loadMessage(PASSWORD_WRONG,fc.getString(PASSWORD_WRONG));
	}
	
	private static void loadMessage(String key,String s)
	{
		System.out.println("Key: " + key + " value: " +s);
		messages.put(key, s);
	}
	private static void loadDefault(FileConfiguration fc)
	{
		addDefault(fc,SHORT_PASSWORD);
		addDefault(fc,PLAYER_LOGED);
		addDefault(fc,PLAYER_REGISTERED);
		addDefault(fc,PLAYER_NOT_REGISTERED);
		addDefault(fc,PASSWORDS_NOT_EQUAL);
		addDefault(fc,ERROR_ON_SAVE);
	}
	
	private static void addDefault(FileConfiguration fc,String s)
	{
		fc.addDefault(SHORT_PASSWORD, "Default: " + s);
	}
	
	
}
