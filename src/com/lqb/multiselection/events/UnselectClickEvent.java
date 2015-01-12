package com.lqb.multiselection.events;


public class UnselectClickEvent<T> extends ClickEvent<T> {
	public final T item;
	public UnselectClickEvent(Object source, T item) {
		super(source);
		this.item=item;
	}
}