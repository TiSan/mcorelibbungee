package de.timc.mcorelib.bungee.core;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.timc.mcorelib.bungee.plugin.MCoreBungeePlugin;
import de.timc.mcorelib.bungee.serversigns.ServerManager;
import de.timc.mcorelib.bungee.serversigns.ServerPortalHandler;
import de.timc.mcorelib.bungee.serversigns.ServerState;
import de.timc.mcorelib.bungee.update.MCoreBungeeUpdate;
import de.timc.mcorelib.bungee.update.MCoreBungeeUpdateManager;

public class MCoreBungee {
	private static MCoreBungee instance;
	private MCoreBungeePlugin plugin;
	public static String version = "0.1";
	public static String header = "[MCore Bungee-Pack] ";
	private ServerManager serverManager;
	private boolean lobby;
	private ServerPortalHandler portalHandler;
	public static MCoreBungee get() {
		return (instance == null ? (instance = new MCoreBungee()) : instance);
	}

	private MCoreBungee() {
		this.lobby = false;
		new Thread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				MCoreBungeeUpdate upd = MCoreBungeeUpdateManager.checkUpdate();
				if (upd != null) {
					System.out.println("[MCoreLibBungee] A new version of MCoreLib BungeePack is now available! Check it out soon!");
					System.out.println("[MCoreLibBungee] Your version: '" + version + "', new version: '" + upd.getVersion() + " >" + upd.getVersionState() + "<'!");
					System.out.println("[MCoreLibBungee] Download-Link: " + upd.getPath());
					System.out.println("[MCoreLibBungee] Things changed in new version (Changelog):");
					for (String tmp : upd.getChangelog()) {
						System.out.println("[MCoreLibBungee] [Changelog] " + tmp);
					}
					System.out.println("[MCoreLibBungee] You don't need to update, but it is highly recommended.");
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (p.isOp()) {
							p.sendMessage(header + "§eFor the MCore library (PVP Pack Extention) is a new version available. More information in your console.");
						}
					}
				} else {
					System.out.println("[MCoreLibBungee] MCoreLib BungeePack version " + version + " activated and running!");
				}
			}

		}).start();
	}

	public void setPluginInstance(MCoreBungeePlugin plugin) {
		this.plugin = plugin;
	}

	public MCoreBungeePlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(MCoreBungeePlugin plugin) {
		this.plugin = plugin;
	}
	
	public void sendServerStatusToLobby(ServerState state){
		try {
			Socket sock = new Socket("localhost", 26010);
			BufferedOutputStream s = new BufferedOutputStream(sock.getOutputStream());
			String text = "serverstatus:" + Bukkit.getServer().getMotd() + ":" + state + "\n";
			for (char c : text.toCharArray()) {
				s.write(c);
			}
			s.flush();
			s.close();
			sock.close();

		} catch (IOException e) {
			
		}
	}
	public void startServerLobbyFeatures(){
		this.portalHandler = new ServerPortalHandler();
		this.lobby = true;
	}

	public boolean isLobby() {
		return lobby;
	}

	public ServerManager getServerManager() {
		return serverManager;
	}

	public ServerPortalHandler getPortalHandler() {
		return portalHandler;
	}

}
