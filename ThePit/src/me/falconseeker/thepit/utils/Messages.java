package me.falconseeker.thepit.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import me.clip.placeholderapi.PlaceholderAPI;
import me.falconseeker.thepit.ThePit;

public class Messages {
	
	private ThePit main = ThePit.getPlugin(ThePit.class);
	private FileConfiguration config = main.getConfig();

	public Messages(ThePit main) {
		this.main = main;
	}	
	
	//MESSAGES
	public String noperms = config.getString("Messages.NoPermission");
	public String killstreak = config.getString("Messages.KillStreak");
	public String respawn = config.getString("Messages.Respawn");
	public String unknown = config.getString("Messages.Unknown");
	public String reload = config.getString("Messages.Reload");
	public List<String> help = config.getStringList("Messages.Help");

	public String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public Collection<? extends String> color(List<String> stringList) {
		List<String> strings = new ArrayList<>();
		for (String string : stringList) {
		    strings.add(ChatColor.translateAlternateColorCodes('&', string));
		}
		return strings;
	}

}
