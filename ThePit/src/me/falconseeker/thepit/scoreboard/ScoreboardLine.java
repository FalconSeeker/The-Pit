package me.falconseeker.thepit.scoreboard;
import org.bukkit.ChatColor;

public class ScoreboardLine
{
  private String text;
  private int cutPosition;
  
  protected ScoreboardLine(String text)
  {
    this.cutPosition = 16;
    setText(text);
  }
  
  public boolean equals(Object obj)
  {
    return super.equals(obj);
  }
  
  public String toString()
  {
    return this.text;
  }
  
  private void setText(String text)
  {
    if (text.length() > 32) {
      throw new IllegalArgumentException("text must be less than 33 characters long.");
    }
    this.text = text;
    if (text.length() > 16)
    {
      String prefix = getPrefix();
      if (prefix.endsWith(String.valueOf('�')))
      {
        this.cutPosition = 15;
        if (getSuffix().length() > 16) {
          throw new IllegalArgumentException("text must be less than 32 characters long. This is because you have a color character in the middle.");
        }
      }
      else
      {
        this.cutPosition = 16;
      }
    }
  }
  
  public String getPrefix()
  {
    if (this.text.length() <= 16) {
      return this.text;
    }
    return this.text.substring(0, this.cutPosition);
  }
  
  public String getPrefixFinalColor()
  {
    ChatColor color = ChatColor.WHITE;
    boolean bold = false;
    boolean underlined = false;
    boolean italic = false;
    boolean strikethrough = false;
    String prefix = getPrefix();
    
    boolean next = false;
    char[] arrayOfChar;
    int j = (arrayOfChar = prefix.toCharArray()).length;
    for (int i = 0; i < j; i++)
    {
      char c = arrayOfChar[i];
      if (next)
      {
        next = false;
        ChatColor co = ChatColor.getByChar(c);
        if (co == ChatColor.BOLD)
        {
          bold = true;
        }
        else if (co == ChatColor.ITALIC)
        {
          italic = true;
        }
        else if (co == ChatColor.UNDERLINE)
        {
          underlined = true;
        }
        else if (co == ChatColor.RESET)
        {
          color = ChatColor.WHITE;
          bold = false;
          underlined = false;
          italic = false;
          strikethrough = false;
        }
        else if (co == ChatColor.STRIKETHROUGH)
        {
          strikethrough = true;
        }
        else
        {
          color = co;
          bold = false;
          underlined = false;
          italic = false;
          strikethrough = false;
        }
      }
      if (c == '�') {
        next = true;
      }
    }
    String co = color.toString();
    if (bold) {
      co = co + ChatColor.BOLD;
    }
    if (italic) {
      co = co + ChatColor.ITALIC;
    }
    if (underlined) {
      co = co + ChatColor.UNDERLINE;
    }
    if (strikethrough) {
      co = co + ChatColor.STRIKETHROUGH;
    }
    return co;
  }
  
  public String getSuffix()
  {
    if (this.text.length() <= 16) {
      return "";
    }
    return this.text.substring(this.cutPosition);
  }
  
  public String getText()
  {
    return this.text;
  }
}


