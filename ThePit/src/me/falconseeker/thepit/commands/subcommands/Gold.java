package me.falconseeker.thepit.commands.subcommands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.falconseeker.commands.CommandModule;
import me.falconseeker.thepit.ThePit;

public class Gold extends CommandModule
{
	private ThePit main;
	
    public Gold(ThePit main)
    {
        //It defines the arg1, min args, and max args.
        super("gold", 1, 2);
        
        this.main = main;
    }
   
    //The method that "runs" the command.
    public void run(CommandSender sender, String[] args)
    {
    	Player p = (Player) sender;
    	
    	if (args[1].equalsIgnoreCase("setloc")) {
    		List<Location> l = (List<Location>) main.getConfig().getList("Gold-Locations");
    		l.add(p.getLocation());
    		main.getConfig().set("Gold-Locations", l);
    		main.saveConfig();
            sender.sendMessage("You did it right!");
    		return;
    	}
        //Send the player saying it worked.
        sender.sendMessage("You did it right!");
    }
}
 
