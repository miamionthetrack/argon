package dev.lvstrng.argon.module.setting;

import lombok.Setter;

public final class BooleanSetting extends Setting<BooleanSetting> {
	@Setter
    private boolean value;
	private final boolean originalValue;

	public BooleanSetting(CharSequence name, boolean value) {
		super(name);
		this.value = value;
		this.originalValue = value;
	}

	public void toggle() {
		setValue(!value);
	}

    public boolean getOriginalValue() {
		return originalValue;
	}

	public boolean getValue() {
		return value;
	}
}
