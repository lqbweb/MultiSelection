package com.lqb.multiselection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A very basic selection class, that providers select, unselect and toggle functions with events.
 * 
 * @since 1.25
 * @param <T>
 */
class SetSelection<T> implements Iterable<T> {
	private Set<T> elements=new HashSet<T>();
	protected ReentrantLock lock=new ReentrantLock();

	/**
	 * adds an item to the selection.
	 * 
	 * @param element
	 * @return true if it was not selected before
	 */
	public boolean select(T element) {
		lock.lock();
		try {
			return elements.add(element);
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Unselects the item if it is selected. If not, nothing happens
	 * 
	 * @param element
	 * @return true if it existed and was unselected
	 */
	public boolean unselect(T element) {
		lock.lock();
		try {
			return elements.remove(element);
		} finally {
			lock.unlock();
		}
	}
	
	public void toggle(T element) {
		if(elements.contains(element)) {
			unselect(element);
		} else {
			select(element);
		}
	}
	
	public boolean isSelected(T element) {
		lock.lock();
		try {
			return elements.contains(element);
		} finally {
			lock.unlock();
		}
	}
	
	public void clearSelection() {
		lock.lock();
		try {
			elements.clear();
		} finally {
			lock.unlock();
		}
	}
	
	public int size() {
		lock.lock();
		try {
			return elements.size();
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public Iterator<T> iterator() {
		return elements.iterator();
	}
	
	public Set<T> elements() {
		return new HashSet<T>(elements);
	}
}
