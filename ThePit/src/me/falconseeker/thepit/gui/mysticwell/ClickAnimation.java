package me.falconseeker.thepit.gui.mysticwell;

import java.awt.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.enchants.enchants.PantEnchantTypes;
import me.falconseeker.thepit.utils.XMaterial;
import me.falconseeker.thepit.utils.XSounds;
import me.falconseeker.thepit.utils.XTags;
import net.md_5.bungee.api.ChatColor;

public class ClickAnimation extends BukkitRunnable {

	private ThePit main = ThePit.getPlugin(ThePit.class);
	private Inventory inv;
	private MysticWell mystic;
	private UUID uuid;
	private AnimationStill as;
	
    public ClickAnimation(Inventory inv, MysticWell mystic, UUID uuid) {
    	this.mystic = mystic;
    	this.inv = inv;
    	this.uuid = uuid;
	    as = new AnimationStill(inv, XMaterial.GREEN_STAINED_GLASS_PANE);

	}
    
    private int tick = 0;

	@Override
	public void run() {
		tick++;

		switch(tick) {
		case 1:
			mystic.defaultItems(XMaterial.GREEN_STAINED_GLASS_PANE);
		    inv.setItem(20, main.getMethods().createGuiItem(
		     		ChatColor.YELLOW + "Rolling...",
		     		null, 
		     		XMaterial.GREEN_STAINED_GLASS_PANE.parseMaterial()));
			break;
		case 2:
			mystic.defaultItems(XMaterial.BLACK_STAINED_GLASS_PANE);
		    inv.setItem(20, main.getMethods().createGuiItem(
		     		ChatColor.YELLOW + "Rolling...",
		     		null, 
		     		XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial()));
			break;
		case 3:
			mystic.defaultItems(XMaterial.GREEN_STAINED_GLASS_PANE);
		    inv.setItem(20, main.getMethods().createGuiItem(
		     		ChatColor.YELLOW + "Rolling...",
		     		null, 
		     		XMaterial.GREEN_STAINED_GLASS_PANE.parseMaterial()));
			break;
		case 4:
			mystic.defaultItems(XMaterial.BLACK_STAINED_GLASS_PANE);
		    inv.setItem(20, main.getMethods().createGuiItem(
		     		ChatColor.YELLOW + "Rolling...",
		     		null, 
		     		XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial()));
			break;
		case 5:
			
			as.runTaskTimer(main, 0L, 2L);
			inv.setItem(20, XMaterial.LIME_DYE.parseItem());
			break;
		case 6: case 7: case 8: case 0: case 10: case 11: case 12: case 13: case 14: case 15: case 16:
		case 17:case 18: case 19:
			ranItem();
			break;
		case 22:
			inv.setItem(20, mystic.enchanteditem.get(uuid));		    
		    ItemStack b = main.getMethods().setItemName(
		    		mystic.enchanteditem.get(uuid), 
		    		
		    		mystic.enchanteditem.get(uuid).getItemMeta().getLore(), 
		    		mystic.ranEnchant(),
		    		Bukkit.getPlayer(uuid)
		    		, false);
		    ItemMeta meta = b.getItemMeta();
		    meta.setDisplayName((String) XTags.getItemTag(b, "Tier") + meta.getDisplayName()); 
		    inv.setItem(20, b);
		    
		    mystic.enchanteditem.remove(uuid);
		    mystic.enchanteditem.put(uuid, b);
		    
		     ArrayList<String> lore = new ArrayList<String>();
		     
		     for (String s : main.guis.getStringList("GUI.MysticWell.Enchanter-Enabled.Lore")) {
		    	 String t = s.replace("%tier%", (String) XTags.getItemTag(mystic.enchanteditem.get(uuid), "Tier"));
		    	 String a = t.replace("%cost%", String.valueOf(XTags.getItemTag(mystic.enchanteditem.get(uuid), "Cost")));
		    	 lore.add(ChatColor.translateAlternateColorCodes('&', a));
		    	 }
		     
		     ItemStack i = main.getMethods().createGuiItem(
	 	    		ChatColor.translateAlternateColorCodes('&', main.guis.getString("GUI.MysticWell.Enchanter-Enabled.Title")), 
	 	    		lore, 
	 	    		Material.valueOf(main.guis.getString("GUI.MysticWell.Enchanter-Enabled.Material")));
		    
		    ItemStack myi = XTags.setItemTag(i, "true", "Enabled");

		    inv.setItem(24, myi);
		    mystic.defaultItems(XMaterial.GREEN_STAINED_GLASS_PANE);
		    Player p = Bukkit.getPlayer(uuid);
		    Location loc = p.getLocation();
		    
		    p.getWorld().playEffect(loc, Effect.EXPLOSION, 500);
		    p.getWorld().playSound(loc, XSounds.EXPLODE.parseSound(), 1, 1);
			as.cancel();
			as = null;
			cancel();
		     break;
		}
		
	}
	private void ranItem() {
		Random ran = new Random();
		int a = 1;
		int o = ran.nextInt(6);
		if (o == a) {
			Random rand = new Random();
			o = rand.nextInt(6);
		}
		if (o != a) {
			a = o;
		}
		
		ArrayList<ItemStack> i = new ArrayList<ItemStack>();
		
		i.add(XMaterial.MAGENTA_DYE.parseItem());
		i.add(XMaterial.CYAN_DYE.parseItem());
		i.add(XMaterial.LIGHT_BLUE_DYE.parseItem());
		i.add(XMaterial.PURPLE_DYE.parseItem());
		i.add(XMaterial.ROSE_RED.parseItem());
		i.add(XMaterial.LIME_DYE.parseItem());
		i.add(XMaterial.CYAN_DYE.parseItem());
		i.add(XMaterial.MELON_SLICE.parseItem());

		ItemStack result = i.get(o);
		
		inv.setItem(20, result);
	}
}
