package me.falconseeker.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.falconseeker.thepit.ThePit;

public class CmdExecutor implements CommandExecutor
{
	private ThePit main = ThePit.getPlugin(ThePit.class);
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
    	if (!cmd.getLabel().equalsIgnoreCase("thepit")) return true;
        //Checks if the label is one of yours.
        if(main.commands.containsKey(label))
        {
            //If so, it will check if they have the permission.
            if(sender.hasPermission("pluginName." + label))
            {
                //Get the module so you can access its variables.
                CommandModule mod = main.commands.get(label);
               
                //Checks if the amount of arguments are within the minimum amount and maximum amount.
                if(args.length >= mod.minArgs && args.length <= mod.maxArgs)
                {
                    //If so, it will run the command.
                    mod.run(sender, args);
                }else
                {
                    //If not, it will send the player some sass.
                    sender.sendMessage("You should learn how to use this command...");
                }
            }else
            {
                //If not, it will send the player some more sass.
                sender.sendMessage("Looks like you are lacking some perms.");
            }
        }
       
        return false;
    }
}
