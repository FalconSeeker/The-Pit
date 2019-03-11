package me.falconseeker.extraevents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.packets.PacketUtil;
import me.falconseeker.thepit.utils.Messages;
import me.falconseeker.thepit.utils.XTags;

public class Streaks implements Listener {
	
	private Map<String, Integer> killstreak = new HashMap<String, Integer>();
	private ThePit main;
	private Messages messages;
	private FileConfiguration config;
	private PacketUtil packets;
	
	public Streaks(ThePit main) {
		this.main = main;
		this.config = main.getConfig();
		this.packets = main.getPackets();
		this.messages = main.getMessages();
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	public Map<String, Integer> getkillstreak() {
		return killstreak;
	}
	
	public void killReward(Player p, int k) {
		Bukkit.broadcastMessage(messages.killstreak);
		for (int degree = 0; degree < 360; degree++) {
			double radians = Math.toRadians(degree);
			double x = Math.cos(radians);
			double z = Math.sin(radians);
			p.getLocation().add(x, 0.0D, z);
			p.getLocation().getWorld().playEffect(p.getLocation(),
			Effect.valueOf(config.getString("Effects.Streaks")), 1);
			p.getLocation().subtract(x, 0.0D, z);
		}
			String Title = config.getString("Titles.StreakTitle5Kills");
			String Subtitle = config.getString("Titles.StreakSubtitle5Kills");
			packets.sendTitle(p, 1, 5, 1, Title, Subtitle);
	}

	@EventHandler
	public void playerdeath(PlayerDeathEvent ev) {
		Player k = ev.getEntity();
		if ((k.getKiller() instanceof Player)) {
			Player p = k.getKiller();
			
			addtokillstreak(p);
			main.getConfig().set("Kills." + k.getName(), String.valueOf(Integer.parseInt("Kills." + k.getName()) + 1));
			killstreak.put(k.getName(), Integer.valueOf(0));
	          ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS, 1);
	          ItemMeta itemMeta = item.getItemMeta();
	          LeatherArmorMeta lch = (LeatherArmorMeta) item.getItemMeta();

	          Random ran = new Random();
	          
	          switch(ran.nextInt(20)) {
	          case 1:
	        	  //RED
		          lch.setColor(Color.fromRGB(255, 0, 0));
		          lch.setDisplayName(ChatColor.RED + "Fresh Pants");
		          break;
	          case 2:
	        	  //GREEN
		          lch.setColor(Color.fromRGB(0, 255, 0));
		          lch.setDisplayName(ChatColor.GREEN + "Fresh Pants");
		          break;
	          case 3:
	        	  //YELLOW
		          lch.setColor(Color.fromRGB(255, 255, 0));
		          lch.setDisplayName(ChatColor.YELLOW + "Fresh Pants");

	          	  break;
	          case 4:
	        	  //ORANGE - TWEAK THIS LATER
		          lch.setColor(Color.fromRGB(255, 155, 0));
		          lch.setDisplayName(ChatColor.GOLD + "Fresh Pants");			          item.setItemMeta(lch);

	          	  break;
	          case 5:
	        	  //BLUE - TWEAK THIS LATER
		          lch.setColor(Color.fromRGB(0, 20, 255));
	          	  lch.setDisplayName(ChatColor.AQUA + "Fresh Pants");
	          	  break;
	          default:
	        	  //BLUE - TWEAK THIS LATER
		          lch.setColor(Color.fromRGB(0, 20, 255));
	          	  lch.setDisplayName(ChatColor.AQUA + "Fresh Pants");
	          	  break; 
	          	  
	          }
	          List<String> l = new ArrayList<String>();
	              l.add(ChatColor.GRAY + "Kept on death.");
	              lch.setLore(l);
	              item.setItemMeta(lch);
	              p.setItemInHand(item);
	              p.getItemInHand().setItemMeta(itemMeta);
	          ItemStack s =  XTags.setItemTag(item, "Tier 1", "Tier");
	          ItemStack t =  XTags.setItemTag(s, "true", "Enchantable");

	          p.getInventory().setItemInHand(XTags.setItemTag(t, 1000, "Cost"));

	          p.updateInventory();
	          
	          p.sendMessage(ChatColor.GREEN + "Item Received");
		}
	}

	public void addtokillstreak(Player killer) {
		String name = killer.getName();
		if (killstreak.containsKey(name)) {
			int kills = ((Integer) killstreak.get(name)).intValue();
			kills++;
			killstreak.put(name, Integer.valueOf(kills));
			Location loc = killer.getLocation();
			if (kills == 100) {
				for (int degree = 0; degree < 360; degree++) {
					double radians = Math.toRadians(degree);
					double x = Math.cos(radians);
					double z = Math.sin(radians);
					loc.add(x, 0.0D, z);
					loc.getWorld().playEffect(killer.getLocation(), Effect.valueOf(config.getString("Effects.Streaks")), 1);
					loc.subtract(x, 0.0D, z);
				}
			}
		}
	}
}