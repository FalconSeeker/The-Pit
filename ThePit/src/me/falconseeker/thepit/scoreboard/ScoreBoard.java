package me.falconseeker.thepit.scoreboard;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.clip.placeholderapi.PlaceholderAPI;
import me.falconseeker.thepit.ThePit;

public class ScoreBoard implements Listener {
	
  private ThePit main = ThePit.getPlugin(ThePit.class);
  
 @EventHandler
  public void onScoreboardUpdate(ScoreboardEvent event){
	    List<String> text = main.getConfig().getStringList("Scoreboard.scores");
	    for (String s : text) {
	    	event.writeLine(main.getMethods().place(s, event.getPlayer()));
	    }
    event.setScoreboardName(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Scoreboard.title")));
    event.setFooter(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Scoreboard.footer")));
  }
  }