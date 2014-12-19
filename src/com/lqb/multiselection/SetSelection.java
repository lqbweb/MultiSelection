package com.lqb.multiselection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class SetSelection<T> implements Iterable<T> {
	private Set<T> elements=new HashSet<T>();
	
	/**
	 * adds an item to the selection.
	 * 
	 * @param element
	 * @param toggle on true, and the element is already selected, it will be deselected
	 * @return
	 */
	public SetSelection<T> select(T element) {
		elements.add(element);
		return this;
	}
	
	public SetSelection<T> toggle(T element) {
		if(elements.contains(element)) {
			deselect(element);
		} else {
			elements.add(element);
		}
		return this;
	}
	
	/**
	 * Deselects the item if it is selected. If not, nothing happens
	 * 
	 * @param element
	 * @return a reference to this class
	 */
	public SetSelection<T> deselect(T element) {
		elements.remove(element);
		return this;
	}
	
	public boolean isSelected(T element) {
		return elements.contains(element);
	}
	
	public void clearSelection() {
		internalClearSelection();
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
}
