package com.lqb.multiselection.events;

import java.util.Collection;

/**
 * Happens when the user does double click over a selection
 * 
 * @param <T>
 */
public class DoubleClickEvent<T> extends ClickEvent<T> {
	public final Collection<T> selected;
	public DoubleClickEvent(Object source, Collection<T> selected) {
		super(source);
		this.selected=selected;
	}

}
