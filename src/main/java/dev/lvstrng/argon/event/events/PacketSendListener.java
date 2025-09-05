package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.CancellableEvent;
import dev.lvstrng.argon.event.Listener;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;


public interface PacketSendListener extends Listener {
	void onPacketSend(PacketSendEvent event);

	class PacketSendEvent extends CancellableEvent<PacketSendListener> {
		public Packet packet;

		public PacketSendEvent(Packet packet) {
			this.packet = packet;
		}

		@Override
		public void fire(ArrayList<PacketSendListener> listeners) {
			listeners.forEach(e -> e.onPacketSend(this));
		}

		@Override
		public Class<PacketSendListener> getListenerType() {
			return PacketSendListener.class;
		}
	}
}
