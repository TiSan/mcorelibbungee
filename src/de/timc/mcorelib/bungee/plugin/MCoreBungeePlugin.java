package de.timc.mcorelib.bungee.plugin;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import de.timc.mcorelib.bungee.core.MCoreBungee;
import de.timc.mcorelib.metrics.Metrics;

public class MCoreBungeePlugin extends JavaPlugin {
	
	@Override
	public void onEnable() {
		MCoreBungee.get().setPluginInstance(this);
		Metrics me;
		try {
			me = new Metrics(this);
			me.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onDisable() {
		if(MCoreBungee.get().isLobby()){
			MCoreBungee.get().getPortalHandler().stop();
		}
	}

}
