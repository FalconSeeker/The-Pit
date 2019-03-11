package me.falconseeker.thepit.gui.mysticwell;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.XMaterial;

public class AnimationStill extends BukkitRunnable{
	
    private Inventory inv;
    ThePit main = ThePit.getPlugin(ThePit.class);
    private XMaterial mat;
    
    public AnimationStill(Inventory inv, XMaterial mat)
    {
      this.inv = inv;
      this.mat = mat;
    }
     
    	int tick = 0;
    	
    	@Override
    	public void run() {
    		
    		switch(tick) {
    		case 0:
    			animateInv(11, 12);
    		    break;
    		case 1:
    			animateInv(12, 21);
    		    break;
    		case 2:
    			animateInv(21, 30);
    		    break;
    		case 3:
    			animateInv(30, 29);
    		    break;
    	    case 4:
    			animateInv(29, 28);
    	    break;
    		case 5:
    			animateInv(28, 19);
    		    break;
    		case 6:
    			animateInv(19, 10);
    		    break;
    	    case 7:
    			animateInv(10, 11);
    	    break;
    		}
    		if (tick == 8) {
    			tick = -1;
    		}
    		tick++;
    	}
  

private void animateInv(int prevSlot, int nextSlot) {
    
    inv.setItem(nextSlot, main.getMethods().createGuiItem(
    		ChatColor.YELLOW + "Rolling...",
    		null, 
    		mat.parseItem()));
    inv.setItem(prevSlot, main.getMethods().createGuiItem(
    		" ",
    		null, 
    		XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()));

	}
}	
