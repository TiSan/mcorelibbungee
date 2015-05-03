package de.timc.mcorelib.bungee.serversigns;

public class Server {
	private String serverName;
	private String serverShortName;
	private int maxCountPlayers;
	private ServerState serverState;
	private int port;
	private String ip;
	private boolean outside;
	public Server(String serverName, String serverShortName, int maxCountPlayers, String ip, int port, ServerState serverState) {
		this.serverName = serverName;
		this.serverShortName = serverShortName;
		this.maxCountPlayers = maxCountPlayers;
		this.serverState = serverState;
		this.port = port;
		this.ip = ip;
		if(!ip.equals("localhost") && port != 25565){
			outside = true;
		} else {
			outside = false;
		}
	}

	public ServerState getServerState() {
		return serverState;
	}

	public void setServerState(ServerState serverState) {
		this.serverState = serverState;
	}

	public String getServerName() {
		return serverName;
	}

	public String getServerShortName() {
		return serverShortName;
	}

	public int getMaxCountPlayers() {
		return maxCountPlayers;
	}

	public int getPort() {
		return port;
	}

	public String getIp() {
		return ip;
	}

	public boolean isOutside() {
		return outside;
	}

	
}
