package me.falconseeker.deaths;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.packets.PacketUtil;
import me.falconseeker.thepit.utils.Messages;

public class InstantDeath implements Listener {
	  
	private ThePit main;
	private PacketUtil packets;
	private FileConfiguration config;
	private Messages messages;
	private HashMap<UUID, Integer> map = new HashMap<>();
	
	public InstantDeath(ThePit main) {
		this.main = main;
		this.messages  = main.getMessages();
		this.packets = main.getPackets();
		this.config = main.getConfig();
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
    public void onVoid(PlayerMoveEvent e)
	  {
	    Player player = e.getPlayer();
	    int yheight = 1;

	    if ((int) player.getLocation().getY() <= yheight)
	    {
	      Player p = e.getPlayer();
	      p.setHealth(20.0D);
	      p.setExhaustion(0.0F);
	      p.setAllowFlight(false);
	      p.setFoodLevel(20);
	      p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 50, 1000));
	      p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 50, 1000));
	      Location spawnPoint = p.getWorld().getSpawnLocation();
	      p.teleport(spawnPoint);
	      p.sendMessage(messages.respawn);
	    }
	  }
	  @EventHandler
	  public void onDeath(PlayerDeathEvent e)
	  {
		  if (!(e.getEntity() instanceof Player)) return;
		  
	      Player p = e.getEntity();
	      map.put(p.getUniqueId(), p.getTotalExperience());
	      e.getDrops().clear();
	   	  p.spigot().respawn();
	      p.setHealth(20.0D);
	      p.setExhaustion(0.0F);
	      p.setFoodLevel(20);
	      p.setFireTicks(0);	      
	    if (config.getBoolean("Titles.Enabled"))
	    {
	      String title = ChatColor.translateAlternateColorCodes('&', config.getString("Titles.Title"));
	      String subtitle = ChatColor.translateAlternateColorCodes('&', config.getString("Titles.Subtitle"));
	      int fadeInOut = 1;
	      int Stay = 60;
	      packets.sendTitle(p, Integer.valueOf(fadeInOut), Integer.valueOf(Stay), Integer.valueOf(fadeInOut), title, subtitle);
		  packets.sendActionBar(p, ChatColor.translateAlternateColorCodes('&', config.getString("Titles.Actionbar")));
	    }
	    new BukkitRunnable() {
			
	    @Override
	      public void run()
	      {
	          p.setGameMode(GameMode.SURVIVAL);
	          Location spawnPoint = p.getWorld().getSpawnLocation();
	          p.teleport(spawnPoint);
		      p.setTotalExperience(map.get(p.getUniqueId()));

	      }	      
	    }.runTaskLater(main, 1L);	  
	 }
}
