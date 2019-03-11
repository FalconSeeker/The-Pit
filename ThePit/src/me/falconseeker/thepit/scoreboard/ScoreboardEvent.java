package me.falconseeker.thepit.scoreboard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ScoreboardEvent
  extends PlayerEvent
{
  public static final HandlerList handlers = new HandlerList();
  private List<ScoreboardLine> lines;
  private String scoreboardName;
  private String header;
  private String footer;
  
  public ScoreboardEvent(Player player, String scoreboardName)
  {
    super(player);
    this.scoreboardName = scoreboardName;
    this.lines = new ArrayList();
    this.header = "";
    this.footer = "";
  }
  
  public String toString()
  {
    return this.lines.toString();
  }
  
  public void setHeader(String header)
  {
    this.header = header;
  }
  
  public void setFooter(String footer)
  {
    this.footer = footer;
  }
  
  public String getHeader()
  {
    return this.header;
  }
  
  public String getFooter()
  {
    return this.footer;
  }
  
  public void setScoreboardName(String scoreboardName)
  {
    this.scoreboardName = scoreboardName;
  }
  
  public String getScoreboardName()
  {
    return this.scoreboardName;
  }
  
  public void writeLine(String text)
  {
    if (this.lines.size() >= ChatColor.values().length) {
      throw new IllegalStateException("Cannot write anymore lines.");
    }
    this.lines.add(new ScoreboardLine(text));
  }
  
  public void insertLine(int position, String text)
  {
    if (this.lines.size() >= ChatColor.values().length) {
      throw new IllegalStateException("Cannot write anymore lines.");
    }
    this.lines.add(position, new ScoreboardLine(text));
  }
  
  public ScoreboardLine getLine(int index)
  {
    return (ScoreboardLine)this.lines.get(index);
  }
  
  public List<ScoreboardLine> getLines()
  {
    return Collections.unmodifiableList(this.lines);
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
}


