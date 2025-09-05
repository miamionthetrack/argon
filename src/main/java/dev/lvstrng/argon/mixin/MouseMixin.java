package dev.lvstrng.argon.mixin;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.event.EventManager;
import dev.lvstrng.argon.event.events.ButtonListener;
import dev.lvstrng.argon.event.events.MouseMoveListener;
import dev.lvstrng.argon.event.events.MouseUpdateListener;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.client.SelfDestruct;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
	@Shadow @Final private MinecraftClient client;

	@Inject(method = "updateMouse", at = @At("RETURN"))
	private void onMouseUpdate(CallbackInfo ci) {
		EventManager.fire(new MouseUpdateListener.MouseUpdateEvent());
	}

	@Inject(method = "onCursorPos", at = @At("HEAD"), cancellable = true)
	private void onMouseMove(long window, double x, double y, CallbackInfo ci) {
		MouseMoveListener.MouseMoveEvent event = new MouseMoveListener.MouseMoveEvent(window, x, y);

		EventManager.fire(event);
		if (event.isCancelled())
			ci.cancel();
	}

	@Inject(method = "onMouseButton", at = @At("HEAD"))
	private void onMousePress(long window, int button, int action, int mods, CallbackInfo ci) {
		EventManager.fire(new ButtonListener.ButtonEvent(button, window, action));
	}
}