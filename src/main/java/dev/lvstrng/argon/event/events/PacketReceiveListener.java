package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.CancellableEvent;
import dev.lvstrng.argon.event.Listener;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;


public interface PacketReceiveListener extends Listener {
	void onPacketReceive(PacketReceiveEvent event);

	class PacketReceiveEvent extends CancellableEvent<PacketReceiveListener> {
		public Packet packet;

		public PacketReceiveEvent(Packet packet) {
			this.packet = packet;
		}

		@Override
		public void fire(ArrayList<PacketReceiveListener> listeners) {
			listeners.forEach(e -> e.onPacketReceive(this));
		}

		@Override
		public Class<PacketReceiveListener> getListenerType() {
			return PacketReceiveListener.class;
		}
	}
}
