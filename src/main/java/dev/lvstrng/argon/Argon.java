package dev.lvstrng.argon;

import dev.lvstrng.argon.event.EventManager;
import dev.lvstrng.argon.gui.ClickGui;
import dev.lvstrng.argon.managers.FriendManager;
import dev.lvstrng.argon.module.ModuleManager;
import dev.lvstrng.argon.managers.ProfileManager;
import dev.lvstrng.argon.utils.rotation.RotatorManager;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.io.File;
import java.io.IOException;
import java.net.*;

public final class Argon {
	public RotatorManager rotatorManager;
	@Getter
    public ProfileManager profileManager;
	@Getter
    public ModuleManager moduleManager;
	@Getter
    public EventManager eventManager;
	@Getter
    public FriendManager friendManager;
	public static MinecraftClient mc;
	public String version = " b1.3";
	public static boolean BETA;
	public static Argon INSTANCE;
	public boolean guiInitialized;
	@Getter
    public ClickGui clickGui;
	public Screen previousScreen = null;
	public long lastModified;
	public File argonJar;

	public Argon() throws InterruptedException, IOException {
		INSTANCE = this;
		this.eventManager = new EventManager();
		this.moduleManager = new ModuleManager();
		this.clickGui = new ClickGui();
		this.rotatorManager = new RotatorManager();
		this.profileManager = new ProfileManager();
		this.friendManager = new FriendManager();

		this.getProfileManager().loadProfile();
		this.setLastModified();

		this.guiInitialized = false;
		mc = MinecraftClient.getInstance();
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
    public void resetModifiedDate() {
		this.argonJar.setLastModified(lastModified);
	}

	public void setLastModified() {
		try {
			this.argonJar = new File(Argon.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			this.lastModified = argonJar.lastModified();
		} catch (URISyntaxException ignored) {}
	}
}