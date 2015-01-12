package com.lqb.multiselection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.eventbus.EventBus;
import com.lqb.multiselection.events.ClearSelectionEvent;
import com.lqb.multiselection.events.SelectClickEvent;
import com.lqb.multiselection.events.UnselectClickEvent;

/**
 * A very basic selection class, that providers select, unselect and toggle functions with events.
 * 
 * @param <T>
 */
class SetSelection<T> implements Iterable<T> {
	private Set<T> elements=new HashSet<T>();

	private EventBus eventBus=new EventBus();
	
	/**
	 * adds an item to the selection.
	 * 
	 * @param element
	 * @return the current instance so you can join operations
	 */
	public SetSelection<T> select(T element) {
		if(elements.add(element)) {
			fireSelectItem(element);
		}
		return this;
	}
	
	/**
	 * Unselects the item if it is selected. If not, nothing happens
	 * 
	 * @param element
	 * @return a reference to this class
	 */
	public SetSelection<T> unselect(T element) {
		if(elements.remove(element)) {
			fireUnselectItem(element);
		}
		return this;
	}
	
	public SetSelection<T> toggle(T element) {
		if(elements.contains(element)) {
			unselect(element);
		} else {
			select(element);
		}
		return this;
	}
	
	public boolean isSelected(T element) {
		return elements.contains(element);
	}
	
	public void clearSelection() {
		internalClearSelection();
		fireClearSelection();
	}

	void internalClearSelection() {
		elements.clear();
	}
	public int size() {
		return elements.size();
	}
	
	@Override
	public Iterator<T> iterator() {
		return elements.iterator();
	}
	
	public Set<T> elements() {
		return new HashSet<T>(elements);
	}
	
	public void addListener(Object l) {
		eventBus.register(l);
	}

	public void removeListener(Object l) {
		eventBus.unregister(l);
	}
		
	private void fireSelectItem(T item) {
		eventBus.post(new SelectClickEvent<T>(this, item));
	}
	
	private void fireUnselectItem(T item) {
		eventBus.post(new UnselectClickEvent<T>(this, item));
	}
	
	private void fireClearSelection() {
		eventBus.post(new ClearSelectionEvent<T>(this));
	}
}
