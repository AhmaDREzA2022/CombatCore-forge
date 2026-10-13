package com.combatcore.module.setting;

public abstract class Setting<T> {

	private final String name;
	private T value;

	protected Setting(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(T value) {
		this.value = sanitize(value);
	}

	protected T sanitize(T value) {
		return value;
	}
}
