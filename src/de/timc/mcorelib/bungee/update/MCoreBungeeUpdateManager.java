package de.timc.mcorelib.bungee.update;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import de.timc.mcorelib.core.MCore;

public class MCoreBungeeUpdateManager {
	private static boolean already_updated = false;

	public static MCoreBungeeUpdate checkUpdate() {
		if (!already_updated)
			try {
				String ver = MCore.version;
				URL updater = new URL("http://code.tisan.de/update/mcorelibbungee/last_version.ver");
				BufferedReader in = new BufferedReader(new InputStreamReader(updater.openStream()));
				String line = null;
				String version = null;
				String version_state = null;
				String path = null;
				ArrayList<String> changelog = new ArrayList<String>();
				String date = null;
				while ((line = in.readLine()) != null) {
					if (line.startsWith("VER:")) {
						version = line.replace("VER:", "");
					} else if (line.startsWith("VERSTATE:")) {
						version_state = line.replace("VERSTATE:", "");
					} else if (line.startsWith("PATH:")) {
						path = line.replace("PATH:", "");
					} else if (line.startsWith("CHANGELOG:")) {
						changelog.add(line.replace("CHANGELOG:", ""));
					} else if (line.startsWith("DATE:")) {
						date = line.replace("DATE:", "");
					}
				}
				String[] tmpRunning = ver.split("\\.");
				String[] tmpNew = version.split("\\.");
				boolean isNewer = false;
				if (tmpRunning.length > tmpNew.length) {
					for (int i = 0; i < tmpNew.length; i++) {
						if (tmpRunning.length > i) {
							if (Integer.valueOf(tmpNew[i]) > Integer.valueOf(tmpRunning[i])) {
								isNewer = true;
								break;
							} else {
								isNewer = false;
							}
						} else {

							isNewer = true;
							break;
						}
					}
				} else {
					for (int i = 0; i < tmpRunning.length; i++) {
						if (Integer.valueOf(tmpNew[i]) > Integer.valueOf(tmpRunning[i])) {
							isNewer = true;
							break;
						}
						isNewer = false;
					}
				}
				already_updated = true;
				if (isNewer) {
					return new MCoreBungeeUpdate(version, version_state, path, changelog, date);
				} else {
					return null;
				}
			} catch (FileNotFoundException e) {
				System.err.println("[TIMC Updater] An error occured while checking for updates. Do you have a connection to the internet?");
			} catch (IOException e) {
				System.err.println("[TIMC Updater] An error occured while checking for updates. Do you have a connection to the internet?");
			}
		already_updated = true;
		return null;

	}
}
