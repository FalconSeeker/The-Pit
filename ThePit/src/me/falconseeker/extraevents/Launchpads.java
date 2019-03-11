package me.falconseeker.extraevents;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.XMaterial;

public class Launchpads
  implements Listener
{
	
    public Launchpads(ThePit main) {
    	Bukkit.getPluginManager().registerEvents(this, main);	
    }
  
  @EventHandler
  public void onPlayerBlockit(PlayerMoveEvent e)
  {
    Player p = e.getPlayer();
    Location loc = p.getLocation();
    Block block = loc.getBlock().getRelative(BlockFace.DOWN);
    if (block.getType().equals(Material.SLIME_BLOCK))
    {
	  ArmorStand s = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
	  s.setVisible(false);
	  s.setPassenger(p);
	  loc.getWorld().createExplosion(loc, 0.0F);
	  s.setVelocity(p.getLocation().getDirection().multiply(4).add(new Vector(0.0D, 2D, 0.0D)));
    }
  }
  
  @EventHandler
  public void onDamage(EntityDamageEvent  e) {
	 if (e.getEntity().getType() == EntityType.ARMOR_STAND) {
		 if (e.getEntity().getPassenger() != null) {
			 e.getEntity().remove();
		 }
	 }
  }
  
  @EventHandler
  public void onj(EntityDismountEvent e) {
	  if (e.getDismounted().getType().equals(EntityType.ARMOR_STAND)) {
		  e.getDismounted().setPassenger(e.getEntity());
	  }
  }
}
