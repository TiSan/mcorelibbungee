package de.timc.mcorelib.bungee.serversigns;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import de.timc.mcorelib.bungee.core.MCoreBungee;


public class ServerPortalHandler implements Runnable {
	private ServerSocket socket;
	private Thread t;

	public ServerPortalHandler() {
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		try {
			this.socket = new ServerSocket(26010);
			while (MCoreBungee.get().getPlugin().isEnabled()) {
				final Socket s = socket.accept();
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							BufferedInputStream in = new BufferedInputStream(s.getInputStream());
							String line = "";
							while (s.isConnected()) {
								int nr = in.read();
								line += (char) nr;
								if (nr == 10) {
									process(line);
									line = "";
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}).start();
			}
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void process(String line) {
		String[] args = line.split(":");
		for (Server s : MCoreBungee.get().getServerManager().getServers()) {
			if (s.getServerShortName().equalsIgnoreCase(args[1])) {
				if (args[2].equalsIgnoreCase("§2§lReady")) {
					s.setServerState(ServerState.FREE_WITH_PLAYERS);
				} else if (args[2].equalsIgnoreCase("§1§lIngame")) {
					s.setServerState(ServerState.FULL);
				} else {

				}
			}
		}
	}

	public void stop() {
		try {

			socket.close();
			t.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
