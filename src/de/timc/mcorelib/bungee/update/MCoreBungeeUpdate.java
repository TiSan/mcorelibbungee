package de.timc.mcorelib.bungee.update;

import java.util.ArrayList;

public class MCoreBungeeUpdate {
	private String version;
	private String path;
	private ArrayList<String> changelog;
	private String date;
	private String version_state;

	protected MCoreBungeeUpdate(String version, String version_state, String path, ArrayList<String> changelog, String date) {
		this.version = version;
		this.version_state = version_state;
		this.path = path;
		this.changelog = changelog;
		this.date = date;
	}

	public String getVersionState() {
		return version_state;
	}

	public String getReleaseDate() {
		return date;
	}

	public String getVersion() {
		return version;
	}

	public String getPath() {
		return path;
	}

	public ArrayList<String> getChangelog() {
		return changelog;
	}

}
