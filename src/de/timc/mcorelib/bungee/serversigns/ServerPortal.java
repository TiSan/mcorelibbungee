package de.timc.mcorelib.bungee.serversigns;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import de.timc.mcorelib.bungee.core.MCoreBungee;
import de.timc.mcorelib.core.MCore;


public class ServerPortal {
	private Sign sign;
	private Block glassBlock;
	private Location signLocation;
	private Location glassBlockLocation;
	private Server server;
	private boolean initLocked;
	public ServerPortal(Location signLocation, Location glassBlockLocation, Server server, boolean initLocked) {
		this.signLocation = signLocation;
		this.glassBlockLocation = glassBlockLocation;
		this.server = server;
		this.initLocked = initLocked;
		startThread();
	}

	private void startThread() {
		sign = (Sign) signLocation.getBlock().getState();
		glassBlock = (Block) glassBlockLocation.getBlock();
		new Thread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				while (MCoreBungee.get().getPlugin().isEnabled()) {
					if (Bukkit.getOnlinePlayers().size() > 0) {
						MCore.get().getServerTool().requestPlayerCount(Bukkit.getOnlinePlayers().iterator().next(), server.getServerShortName());
						String infoLine = "";
						int playercount = MCore.get().getServerTool().getPlayerCount(server.getServerShortName());
						if (!(server.getServerState().equals(ServerState.LOCKED) || server.getServerState().equals(ServerState.OFFLINE) || server.getServerState().equals(ServerState.RESTART))) {
							if (playercount == 0) {
								server.setServerState(ServerState.FREE_WITHOUT_PLAYERS);
							} else if (playercount > 0 && playercount < server.getMaxCountPlayers()) {
								server.setServerState(ServerState.FREE_WITH_PLAYERS);
							} else if (playercount == server.getMaxCountPlayers()) {
								server.setServerState(ServerState.FULL);
							}
						}
						try {
							Socket sock = new Socket();
							sock.connect(new InetSocketAddress(server.getIp(), server.getPort()), 500);
							if (!sock.isConnected()) {
								server.setServerState(ServerState.RESTART);
								
							} else {
								if (server.getServerState().equals(ServerState.RESTART) || server.getServerState().equals(ServerState.OFFLINE)) {
									server.setServerState(ServerState.PREPAIR);
								}
								if (server.isOutside()) {
									server.setServerState(ServerState.FREE_WITH_PLAYERS);
								}
								if(initLocked){
									server.setServerState(ServerState.LOCKED);
								}
								sock.close();
							}
						} catch (Exception ex) {
							server.setServerState(ServerState.OFFLINE);
						}
						if(playercount >= server.getMaxCountPlayers()){
							server.setServerState(ServerState.FULL);
						}
						
						switch (server.getServerState()) {
						case FREE_WITHOUT_PLAYERS:
							glassBlock.setData((byte) 11);
							infoLine = "§1§lJOIN";
							break;
						case FREE_WITH_PLAYERS:
							glassBlock.setData((byte) 5);
							infoLine = "§1§lJOIN";
							break;
						case FULL:
							glassBlock.setData((byte) 14);
							infoLine = "§4§lFULL";
							break;
						case LOCKED:
							glassBlock.setData((byte) 1);
							infoLine = "§4§lLOCKED";
							break;
						case OFFLINE:
							glassBlock.setData((byte) 14);
							infoLine = "§4§lRESTART";
							break;
						case RESTART:
							glassBlock.setData((byte) 1);
							infoLine = "§4§lRESTART";
							break;
						case PREPAIR:
							glassBlock.setData((byte) 15);
							infoLine = "§4§lSTARTUP";
							break;
						default:
							break;

						}

						if (server.isOutside()) {
							sign.setLine(0, server.getServerName());
							sign.setLine(1, "");
							sign.setLine(2, ((server.getServerState().equals(ServerState.RESTART) || server.getServerState().equals(ServerState.OFFLINE))? "Â§4Â§lOFFLINE" : "Â§2Â§lONLINE"));
							
							if(server.getServerState().equals(ServerState.RESTART) || server.getServerState().equals(ServerState.OFFLINE)){

							} else {
								sign.setLine(3, "§8§oMC Servers");
							}
						} else {
							sign.setLine(0, server.getServerName());
							sign.setLine(1, infoLine);
							if (!server.getServerState().equals(ServerState.RESTART) && !server.getServerState().equals(ServerState.OFFLINE)) {
								sign.setLine(2, playercount + "/" + server.getMaxCountPlayers());
							} else {
								sign.setLine(2, "");
							}
							sign.setLine(3, "");
							if (server.getServerState().equals(ServerState.PREPAIR)) {
								sign.setLine(2, "Bitte warten!");
								sign.update();
								try {
									Thread.sleep(12000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								server.setServerState(ServerState.FREE_WITH_PLAYERS);
								if(initLocked){
									server.setServerState(ServerState.LOCKED);
								}
							}
						}

						sign.update();
					}

					try {
						if (server.isOutside()) {
							Thread.sleep(10000);
						} else {
							Thread.sleep(1000);
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
		new Thread(new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				int step = 0;
				boolean b = false;
				while(MCoreBungee.get().getPlugin().isEnabled()){
					if(Bukkit.getOnlinePlayers().size() > 0 && (server.getServerState().equals(ServerState.PREPAIR) || server.getServerState().equals(ServerState.RESTART) || server.getServerState().equals(ServerState.OFFLINE))){
						String tmp = "§f";
						for(int i = 0; i < step; i++){
							tmp += "░";
						}
						tmp += "███";
						for(int i = 0; i < (4-step); i++){
							tmp += "░";
						}
						
						if(step == 4){
							b = true;
						} else if(step == 0){
							b = false;
						}
						if(!b){
							step++;
						} else {
							step--;
						}
						sign.setLine(3, tmp);
						sign.update();
						try {
							Thread.sleep(150);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					} else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
					
					
				}
			}
		}).start();
	}

	public Sign getSign() {
		return sign;
	}

	public Block getGlassBlock() {
		return glassBlock;
	}

	public Location getSignLocation() {
		return signLocation;
	}

	public Location getGlassBlockLocation() {
		return glassBlockLocation;
	}

	public Server getServer() {
		return server;
	}

}
