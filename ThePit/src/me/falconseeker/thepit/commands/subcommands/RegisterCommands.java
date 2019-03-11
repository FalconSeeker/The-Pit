package me.falconseeker.thepit.commands.subcommands;

import org.bukkit.Bukkit;

import me.falconseeker.thepit.ThePit;

public class RegisterCommands {

	public RegisterCommands(ThePit main) {
		new Gold(main);
		new GUIS();
	}
}
