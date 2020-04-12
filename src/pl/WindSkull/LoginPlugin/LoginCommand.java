package pl.WindSkull.LoginPlugin;

import java.security.Key;
import java.security.spec.KeySpec;

import javax.annotation.Nonnull;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.avaje.ebean.EbeanServer;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public class LoginCommand implements CommandExecutor{

	
	private EbeanServer eserver;
	public LoginCommand(EbeanServer es) 
	{
		eserver = es;
	}
	
	@Override
	public boolean onCommand(@Nonnull CommandSender arg0, @Nonnull Command arg1, @Nonnull String arg2, @Nonnull String[] arg3) 
	{
		if(arg0 instanceof Player)
		{
			
			Player p = (Player) arg0;
			if(Core.loginStatus.containsKey(p)) return false;
			PlayerDataBase pdb = eserver.find(PlayerDataBase.class).where().eq(PlayerDataBase.PLAYER_NAME, p.getName()).findUnique();
			
			if(pdb == null)
				return Misc.trueMessage(p, LangManager.getMessage(LangManager.PLAYER_NOT_REGISTERED));
			
			String password = arg3[0];
			try 
			{
				if(p.getName().equalsIgnoreCase(decrypt(pdb.getPlayerPassword(), password)))
				{
					Core.loginSession.remove(p);
					Core.loginStatus.put(p, true);
					PlayerDataBase.writeToPlayer(pdb,p);
					return Misc.trueMessage(p, LangManager.getMessage(LangManager.PLAYER_LOGED));
				}
				else
					return Misc.trueMessage(p, LangManager.getMessage(LangManager.PASSWORD_WRONG));
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

	
	
	
	private static final String ALGORITHM = "AES";

	public static String encrypt(@Nonnull String valueToEnc,@Nonnull String password) throws Exception {
		Key key = generateKey(password);
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = c.doFinal(valueToEnc.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encValue);
		return encryptedValue;
	}

	public static String decrypt(@Nonnull String encryptedValue,@Nonnull String password) throws Exception {
		Key key = generateKey(password);
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}
	private static byte[] salt = new byte[] {0,0,0,0,0,0,0,0} ;
	private static SecretKey generateKey(String password) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		return secret;
		/*
		 * Key key = new SecretKeySpec(password.getBytes(), ALGORITHM); return key;
		 */
	}
}
