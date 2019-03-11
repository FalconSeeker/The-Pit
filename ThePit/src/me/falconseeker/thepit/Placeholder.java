package me.falconseeker.thepit;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.falconseeker.extraevents.CombatLog;
import me.falconseeker.thepit.utils.Methods;
import net.md_5.bungee.api.ChatColor;

public class Placeholder extends PlaceholderExpansion {

	private ThePit main = ThePit.getPlugin(ThePit.class);
	private Methods methods = main.getMethods();
	
    public Placeholder(ThePit thePit) {
    	 this.main = thePit;
    }
	public String getIdentifier() {
        return "thepit";
    }
    public String getPlugin() {
        return null;
    }
    public String getAuthor() {
        return "FalconSeeker";
    }
    public String getVersion() {
        return "version2";
    }
    public String onPlaceholderRequest(Player player, String identifier) {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String date = format.format(now);
        if(identifier.equalsIgnoreCase("onlines")){
            return String.valueOf(Bukkit.getOnlinePlayers().size());
        }
        if(player == null){
            return "";
        }
        if(identifier.equalsIgnoreCase("name")){
            return player.getName();
        }
        if(identifier.equalsIgnoreCase("level")){
            return methods.getChatLevel(player);
        }
        if(identifier.equalsIgnoreCase("date")){
            return date;
        }
        if(identifier.equalsIgnoreCase("xp")){
            return String.valueOf(player.getExpToLevel());
        }
        if(identifier.equalsIgnoreCase("stat")){
            return methods.getCombat(player);
        }
        if(identifier.equalsIgnoreCase("gold")){
            return String.valueOf(methods.getGold(player));
        }
 
        return null;
    }
    
}

 