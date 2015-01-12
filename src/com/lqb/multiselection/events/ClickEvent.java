package com.lqb.multiselection.events;

/**
 * Represents a type of event related with the selection
 * 
 * @param <T>
 */
public abstract class ClickEvent<T> extends java.util.EventObject {
	public ClickEvent(Object source) {
		super(source);
	}
}