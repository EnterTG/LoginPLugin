package pl.WindSkull.LoginPlugin;

import java.io.IOException;
import java.sql.Timestamp;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name="players")
public class PlayerDataBase {


	@Id
	private int id;
	public static final String ID = "id";
	
	@Version
	public Timestamp lastUpdate;
	
	@NotNull
	private String playerName;
	public static final String PLAYER_NAME = "playerName";
	//Player encrypted password
	@NotNull
	private String playerPassword;
	public static final String PLAYER_PASSWORD = "playerPassword";
	//Player possition 
	@NotNull
	private double x;
	public static final String PLAYER_LOCATION_X = "x";
	@NotNull
	private double y;
	public static final String PLAYER_LOCATION_Y = "y";
	@NotNull
	private double z;
	public static final String PLAYER_LOCATION_Z = "z";
	@NotNull
	private float pitch;
	public static final String PLAYER_LOCATION_PITCH = "pitch";
	@NotNull
	private float yaw;
	public static final String PLAYER_LOCATION_YAW = "yaw";
	@NotEmpty
	private String worldName;
	public static final String PLAYER_LOCATION_WORLDNAME = "worldName";
	
	@Column(length=12000) @NotEmpty
	private String playerInventory;
	public static final String PLAYER_INVENTORY = "playerInventory";
	
	@NotEmpty
	private String playerArmor;
	public static final String PLAYER_ARMOR = "playerArmor";
	
	public static void writeToPlayer( @Nonnull PlayerDataBase pdb, @Nonnull Player p)
	{
		Location loc = new Location(Bukkit.getWorld(pdb.getWorldName()), pdb.getX(), pdb.getY(), pdb.getZ());
		Preconditions.checkNotNull(loc);
		loc.setPitch(pdb.getPitch());
		loc.setYaw(pdb.getYaw());
		p.teleport(loc);
		p.setWalkSpeed(0.2f);
		p.setFlySpeed(0.2f);
		p.removePotionEffect(PotionEffectType.JUMP);
		try 
		{
			p.getInventory().setContents(BukkitSerialization.itemStackArrayFromBase64(pdb.getPlayerInventory()));
			p.getInventory().setArmorContents(BukkitSerialization.itemStackArrayFromBase64(pdb.getPlayerArmor()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void readFromPlayer(@Nonnull PlayerDataBase pdb, @Nonnull Player p)
	{
		Location loc = p.getLocation();
		pdb.setX(loc.getX());
		pdb.setY(loc.getY());
		pdb.setZ(loc.getZ());
		pdb.setYaw(loc.getYaw());
		pdb.setPitch(loc.getPitch());
		pdb.setWorldName(loc.getWorld().getName());
		pdb.setPlayerName(p.getName());
		pdb.setPlayerInventory(BukkitSerialization.toBase64(p.getInventory()));
		pdb.setPlayerArmor(BukkitSerialization.itemStackArrayToBase64(p.getInventory().getArmorContents()));
	}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param playerName the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * @return the playerPassword
	 */
	public String getPlayerPassword() {
		return playerPassword;
	}

	/**
	 * @param playerPassword the playerPassword to set
	 */
	public void setPlayerPassword(String playerPassword) {
		this.playerPassword = playerPassword;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * @return the pitch
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * @param pitch the pitch to set
	 */
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	/**
	 * @return the yaw
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * @param yaw the yaw to set
	 */
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	/**
	 * @return the worldName
	 */
	public String getWorldName() {
		return worldName;
	}

	/**
	 * @param worldName the worldName to set
	 */
	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	/**
	 * @return the playerInventory
	 */
	public String getPlayerInventory() {
		return playerInventory;
	}

	/**
	 * @param playerInventory the playerInventory to set
	 */
	public void setPlayerInventory(String playerInventory) {
		this.playerInventory = playerInventory;
	}

	/**
	 * @return the playerArmor
	 */
	public String getPlayerArmor() {
		return playerArmor;
	}

	/**
	 * @param playerArmor the playerArmor to set
	 */
	public void setPlayerArmor(String playerArmor) {
		this.playerArmor = playerArmor;
	}

	/**
	 * @return the lastUpdate
	 */
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
