package me.falconseeker.extraevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.falconseeker.thepit.ThePit;

public class HealthBar implements Listener {
	
	private ThePit main;
	
	public HealthBar(ThePit main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	  protected int getPlayerHealth(Player p)
	  {
	    return (int)p.getHealth() / 2;
	  }
	  
	  protected int getPlayerHealthPercentage(Player p)
	  {
	    int health = getPlayerHealth(p);
	    return (int)(health * 5.0D / 5.0D);
	  }
	  
	  public void sendheathy(Player target, Player receiver)
	  {
	    String str = "";
	    for (int i = 0; i < getPlayerHealthPercentage(target); i++) {
	      str = str + ChatColor.DARK_RED.toString() + "?";
	    }
	    for (int i = 0; i < 10 - getPlayerHealth(target); i++) {
	      str = str + ChatColor.BLACK.toString() + "?";
	    }
	    main.getPackets().sendActionBar(receiver, ChatColor.GRAY + target.getDisplayName() + " " + str);
	  }
	  @EventHandler
	  public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e)
	  { 
		  if (!(e.getEntity() instanceof Player)) return;
	      if (!(e.getDamager() instanceof Player)) return;
	        Player target = (Player) e.getDamager();
	        sendheathy((Player) e.getEntity(), target);
	   }
	  
}
