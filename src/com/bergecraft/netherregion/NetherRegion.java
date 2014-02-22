package com.bergecraft.netherregion;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NetherRegion  extends JavaPlugin implements Listener {

	private static final Logger LOG = Logger.getLogger("NetherRegion");
	
	private double mScaleFactor;
	
	private static final double DEFAULT_SCALE = 1.0;
	
	private static final String CONFIG_SECTION_NAME = "nether_region";
	
	private static final String CONFIG_SCALE_KEY = "nether_to_overworld_scale";

	@Override
	public void onEnable() {
		super.onEnable();
		
		if (!getConfig().isSet(CONFIG_SECTION_NAME)) {
			saveDefaultConfig();
			LOG.warning("Config not found or invalid, saving default.");
		}
		this.reloadConfig();
		
		ConfigurationSection config = getConfig().getConfigurationSection(CONFIG_SECTION_NAME);
		
		mScaleFactor = config.getDouble(CONFIG_SCALE_KEY, DEFAULT_SCALE);
		if(mScaleFactor <= 0.0) {
			mScaleFactor = DEFAULT_SCALE;
			LOG.warning("NetherRegion scale must be greater than 0! Defaulting to 1");
		}
		
		getServer().getPluginManager().registerEvents(this, this);
		LOG.info("[Nether Region] is now enabled at scale 1:" + mScaleFactor);
	}
	
	@Override
	public void onDisable() {
		LOG.info("[Nether Region] is now disabled");
		super.onDisable();
	}
	
	@Override
	public void saveDefaultConfig() {
		super.saveDefaultConfig();
		ConfigurationSection config = getConfig().createSection(CONFIG_SECTION_NAME);
		config.set(CONFIG_SCALE_KEY, DEFAULT_SCALE);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPortalEvent(PlayerPortalEvent e) {
		Location loc = e.getPlayer().getLocation().clone();
		
		World dest = e.getTo().getWorld();
		World.Environment to = dest.getEnvironment();
		World.Environment from = e.getFrom().getWorld().getEnvironment();
		if( from == World.Environment.NETHER && to == World.Environment.NORMAL) {
			loc.setX(loc.getX() * mScaleFactor);
			loc.setZ(loc.getZ() * mScaleFactor);
		} else if(from == World.Environment.NORMAL && to == World.Environment.NETHER) {
			loc.setX(loc.getX() / mScaleFactor);
			loc.setZ(loc.getZ() / mScaleFactor);
		} else {
			return;
		}
		
		loc.setWorld(dest);
		Location newLoc = e.getPortalTravelAgent().findOrCreate(loc);
		e.setTo(newLoc);
		LOG.fine(new StringBuilder("Player ").append(e.getPlayer().getName()).append(" portal event from ").append(e.getFrom()).append(" to ").append(newLoc).toString());
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPortalEvent(EntityPortalEvent e) {
		Location loc = e.getEntity().getLocation().clone();
		
		World dest = e.getTo().getWorld();
		World.Environment to = dest.getEnvironment();
		World.Environment from = e.getFrom().getWorld().getEnvironment();
		if( from == World.Environment.NETHER && to == World.Environment.NORMAL) {
			loc.setX(loc.getX() * mScaleFactor);
			loc.setZ(loc.getZ() * mScaleFactor);
		} else if(from == World.Environment.NORMAL && to == World.Environment.NETHER) {
			loc.setX(loc.getX() / mScaleFactor);
			loc.setZ(loc.getZ() / mScaleFactor);
		} else {
			return;
		}
		
		loc.setWorld(dest);
		Location newLoc = e.getPortalTravelAgent().findOrCreate(loc);
		e.setTo(newLoc);
		LOG.fine(new StringBuilder("Entity ").append(e.getEntity()).append(" portal event from ").append(e.getFrom()).append(" to ").append(newLoc).toString());
	}
	
}
