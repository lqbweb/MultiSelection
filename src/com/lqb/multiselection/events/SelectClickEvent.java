package com.lqb.multiselection.events;


public class SelectClickEvent<T> extends ClickEvent<T> {
	public final T item;
	public SelectClickEvent(Object source, T item) {
		super(source);
		this.item=item;
	}
}