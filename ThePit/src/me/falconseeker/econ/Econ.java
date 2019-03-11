package me.falconseeker.econ;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.Messages;

public class Econ implements CommandExecutor {
	
	private ThePit main;
	private Messages messages;
	
	public Econ(ThePit main){
	  this.main = main;
	  this.messages = main.getMessages();
	}

	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("gold")) {
			if (!(sender.hasPermission("falconseeker.gold"))) {
				sender.sendMessage(messages.noperms);
				return true;
			}
			if (!main.getConfig().getBoolean("Econ-Enabled")) {
				return true;
			}
			switch(args.length) {
			case 0:
				sender.sendMessage(ChatColor.RED + "Try /gold <Player> <Amount>"); return true;
			case 1: 
				sender.sendMessage(ChatColor.RED + "Try /gold <Player> <Amount>"); return true;
			case 2:
				@SuppressWarnings("deprecation") Player target = (Player) Bukkit.getOfflinePlayer(args[0]);
				if (target == null) {
					sender.sendMessage(ChatColor.RED + "Invalid Target!"); return true;
				}
				int amount = Integer.valueOf(args[1]).intValue();
				sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "GOLD PICKUP!" + ChatColor.GRAY + " Given "
						+ ChatColor.YELLOW + amount + "g " + ChatColor.GRAY + "to " + args[0]);
				main.getMethods().setGold(target, amount); return true;
				
			}
			return true;
			}
		return true;
	}
}
