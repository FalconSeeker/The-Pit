package me.falconseeker.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import me.falconseeker.thepit.enchants.enchants.PantEnchantTypes;

public class TabComplete implements TabCompleter {

  private static final String[] COMMANDS = { "spawnitems", "giveitem", "setevent", "setgold", "reload", "kills", "levelup", "help", "stats", "goldstart", "respawn", "enchant", "createholo" };
    //create a static array of values you want to return

  	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<String>(Arrays.asList(COMMANDS));
        //copy matches of first argument from list (if first arg is 'm' will return just 'minecraft')
      //you want to sort no?
        if (args.length > 0 && args[0].equalsIgnoreCase("enchant")) {
        	List<String> enchants = Stream.of(PantEnchantTypes.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());  
        	return enchants;
        }
        final List<String> arg = Arrays.asList(args);
        StringUtil.copyPartialMatches(args[0], arg, completions);
        Collections.sort(completions);
        return completions;
    }
}
