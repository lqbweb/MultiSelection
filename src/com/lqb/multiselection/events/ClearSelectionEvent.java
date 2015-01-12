package com.lqb.multiselection.events;

/**
 * An event thrown when the selection has been cleared. This may happen every time you do normal click
 * 
 * @param <T>
 */
public class ClearSelectionEvent<T> extends ClickEvent<T> {
	public ClearSelectionEvent(Object source) {
		super(source);
	}
}