package me.falconseeker.thepit.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {
	
	private static final String OBJECTIVE_ID = "objective";
	
	static HashMap<String, Scoreboard> boards = new HashMap();
	static void applyScoreboard(Player player){
		if(!(boards.containsKey(player.getName()))){
			Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
			boards.put(player.getName(), board);
		}
		Scoreboard board = boards.get(player.getName());
		Objective obj = board.getObjective(OBJECTIVE_ID);
		
		if(obj == null){
			obj = board.registerNewObjective(OBJECTIVE_ID, "dummy");
			obj.setDisplayName(" ");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
		
		ScoreboardEvent event = new ScoreboardEvent(player, obj.getDisplayName());
		
		Bukkit.getPluginManager().callEvent(event);
		
		if(!obj.getDisplayName().equals(event.getScoreboardName())){
			obj.setDisplayName(event.getScoreboardName());
		}
		
		if(event.getLines().size() > 0){
			if(!event.getHeader().isEmpty())
				event.insertLine(0, event.getHeader());
			if(!event.getFooter().isEmpty())
				event.writeLine(event.getFooter());
		}
		
		List<Team> teams = new ArrayList<>();
		
		for(int i = 0; i < ChatColor.values().length;i++){
			
			if(board.getTeam("#line-"+i) == null){
				board.registerNewTeam("#line-"+i);
			}
			
			teams.add(board.getTeam("#line-"+i));
		}
		
		for(int i = 0; i < event.getLines().size();i++){
			
			Team team = teams.get(i);
			
			ScoreboardLine line = event.getLine(i);
			
			String prefix = line.getPrefix(); //TODO
			String suffix = line.getSuffix();
			
			if(!team.getPrefix().equals(prefix)){
				team.setPrefix(prefix);
			}
			
			if(!team.getSuffix().equals(suffix)){
				team.setSuffix(line.getSuffix());
			}
			
			String entry = ChatColor.values()[i]+line.getPrefixFinalColor();
			
			if(team.getEntries().size() == 0){
				team.addEntry(entry);
				obj.getScore(entry).setScore(event.getLines().size() - i);
			}else if(team.getEntries().size() == 1){
				String already = team.getEntries().iterator().next();
				
				if(!entry.equals(already)){
					board.resetScores(already);
					team.removeEntry(already);
					team.addEntry(entry);
					obj.getScore(entry).setScore(event.getLines().size() - i);
				}else{
					obj.getScore(already).setScore(event.getLines().size() - i);
				}
			}
			
		}
		
		for(int i = event.getLines().size(); i < ChatColor.values().length;i++){
			Team team = teams.get(i);
			
			if(team.getEntries().size() > 0){
				team.getEntries().forEach(entry -> {
					board.resetScores(entry);
					team.removeEntry(entry);
				});
			}
		}
		boards.put(player.getName(), board);
		player.setScoreboard(board);
	}
}