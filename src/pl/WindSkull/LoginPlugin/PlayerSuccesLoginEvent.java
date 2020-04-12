package pl.WindSkull.LoginPlugin;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class PlayerSuccesLoginEvent extends Event 
{

	
	private static final HandlerList HANDLERS = new HandlerList();
	private Player player;
	
	public PlayerSuccesLoginEvent(Player p) {
		this.player = p;
	}
	
	@Override
	public @Nonnull HandlerList getHandlers() {
		
		return HANDLERS;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

}
