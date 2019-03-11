package me.falconseeker.extraevents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.XTags;

public class Villagers implements Listener {

	private ThePit main;
	
	public Villagers(ThePit main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public void onRightClick(PlayerInteractEntityEvent event) {
      Player player = event.getPlayer();
      Entity ent = event.getRightClicked();

          if (ent.getType() != EntityType.VILLAGER) return;
          
          event.setCancelled(true);
          player.sendMessage("true");

          switch(ent.getCustomName()){
        	  case "Perks":
        		  player.closeInventory();
            	  main.getPerks().openInventory(player);
        		  break;
       	  case "Statistics": case "Prestige":
        		  break;
        	  case "Shop":
        		  main.getShop().openInventory(player);
        		  break;
          }
        	  return;
        }
	@EventHandler
	public void j(PlayerJoinEvent e) {
		e.getPlayer().spigot().setCollidesWithEntities(false);
	} 	
}
