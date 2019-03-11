package me.falconseeker.commands;

import org.bukkit.command.CommandSender;

import me.falconseeker.thepit.ThePit;

public abstract class CommandModule
{
    public String arg1;
    public int minArgs;
    public int maxArgs;
    
    private ThePit main = ThePit.getPlugin(ThePit.class);
    /**
     * @param label - The second argument of the command.
     * @param minArgs - The minimum amount of arguments.
     * @param maxArgs - The maximum amount of arguments.
     */
    public CommandModule(String arg1, int minArgs, int maxArgs)
    {
        this.arg1 = arg1;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
       
        main.getCommand("thepit").setExecutor(new CmdExecutor());
        main.commands.put(arg1, this);
    }
   
    //This method will process the command.
    public abstract void run(CommandSender sender, String[] args);
}