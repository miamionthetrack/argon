package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.Event;
import dev.lvstrng.argon.event.Listener;
import net.minecraft.client.util.Window;

import java.util.ArrayList;

public interface ResolutionListener extends Listener {
	void onResolution(ResolutionEvent event);

	class ResolutionEvent extends Event<ResolutionListener> {
		public Window window;

		public ResolutionEvent(Window window) {
			this.window = window;
		}

		@Override
		public void fire(ArrayList<ResolutionListener> listeners) {
			listeners.forEach(l -> l.onResolution(this));
		}

		@Override
		public Class<ResolutionListener> getListenerType() {
			return ResolutionListener.class;
		}
	}
}
