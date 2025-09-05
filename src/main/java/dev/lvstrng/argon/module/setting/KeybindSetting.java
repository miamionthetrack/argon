package dev.lvstrng.argon.module.setting;

import lombok.Getter;
import lombok.Setter;

public final class KeybindSetting extends Setting<KeybindSetting> {
	private int keyCode;
	@Setter
    @Getter
    private boolean listening;
	@Getter
    private final boolean moduleKey;
	@Getter
    private final int originalKey;

	public KeybindSetting(CharSequence name, int key, boolean moduleKey) {
		super(name);
		this.keyCode = key;
		this.originalKey = key;
		this.moduleKey = moduleKey;
	}

    public int getKey() {
		return keyCode;
	}

	public void setKey(int key) {
		this.keyCode = key;
	}

    public void toggleListening() {
		this.listening = !listening;
	}
}
