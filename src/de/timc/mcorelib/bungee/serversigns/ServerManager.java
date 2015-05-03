package de.timc.mcorelib.bungee.serversigns;

import java.util.ArrayList;

import org.bukkit.Location;

public class ServerManager {
	private ArrayList<ServerPortal> portals;
	
	
	public ArrayList<ServerPortal> getPortals() {
		return portals;
	}

	public ArrayList<Server> getServers() {
		ArrayList<Server> servers = new ArrayList<Server>();
		for (ServerPortal p : portals) {
			if (servers.contains(p.getServer())) {
				servers.add(p.getServer());
			}
		}
		return servers;
	}

	public ServerPortal createServerSign(String serverName, String serverShortName, int maxCountPlayers, String ip, int port, Location signLocation, Location glassBlockLocation, boolean initLocked){
		Server s = new Server(serverName, serverShortName, maxCountPlayers, ip, port, ServerState.FREE_WITHOUT_PLAYERS);
		ServerPortal p = new ServerPortal(signLocation, glassBlockLocation, s, initLocked);
		portals.add(p);
		return p;
	}
}
