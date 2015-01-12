package com.lqb.multiselection;

import java.awt.event.InputEvent;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A Selection mechanism that selects some items out of a List and support three types of operations:
 * 
 *		normalClick: 	All the selection will be cleaned and the new element marked as selected.
 *						If the element is already selected, it will be deselected
 *
 *		shiftClick: 	A range of items will be selected. If there are more than one item already selected before this event,
 *						the starting point will be the latest selected
 *
 *		ctrlClick: 		A new item will be added to the selection, or if it is already selected, it will be unselected
 *
 * In general it works pretty much like Windows would do
 * 
 * This API is assuming that when an item is removed from the collection, you unselect it from here.
 *
 * @param <T>
 */
public class ClickSelection<T> implements Iterable<T> {

	protected SetSelection<T> selection=new SetSelection<T>();
	private final List<T> collection;		//this contains the collection in which elements in "selection" will be selected.
	private T lastModified=null;
	protected ReentrantLock lock=new ReentrantLock();
		
	public ClickSelection(List<T> collection) {
		this.collection=collection;
	}
	
	public boolean isSelected(T element) {
		lock.lock();
		try {
			return selection.isSelected(element);
		} finally {
			lock.unlock();
		}
	}
	
	public int size() {
		lock.lock();
		try {
			return selection.size();
		} finally {
			lock.unlock();
		}
	}
		
	
	/**
	 * Call it when you have removed an element from "collection"
	 * @param element
	 */
	public void unselectItem(T element) {
		lock.lock();
		try {
			selection.unselect(element);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Requirements:
	 * 		- If there is only this item already selected, the item will be deselected
	 * 		- If there is nothing selected or another item selected, the new item will be selected
	 * 		- If there are multiple items selected, and element is not selected, "element" will be the only one selected
	 * 		- If there are multiple items selected, and element is in between them, element will be the only one selected
	 * @param element 
	 */
	public void normalClick(T element) {
		lock.lock();
		try {
			if(collection.size()==0) {
				return;
			} else {
				boolean isElementSelected=selection.isSelected(element);
				int selectionSize=selection.size();
				selection.clearSelection();
				if(!isElementSelected) {
					selection.select(element);
				} else {
					if(selectionSize>1) {
						selection.select(element);
					} else {
					}
				}
				lastModified=element;
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void clearSelection() {
		lock.lock();
		try {
			selection.clearSelection();
		} finally {
			lock.unlock();
		}
	}
	
	public void shiftCtrlClick(T element) {
		shiftClick(element, false);
	}
	
	public void shiftClick(T element) {
		shiftClick(element, true);
	}
	
	private void shiftClick(T element, boolean clearSelection) {
		lock.lock();
		try {
			if(collection.size()==0) {
				return;
			} else {
				int index=collection.indexOf(element);
				if(index<0)
					return;
							
				int latestSelected=0;
				if(lastModified!=null) {
					int res=collection.indexOf(lastModified);
					if(res>=0) {
						latestSelected=res;
					}
				}
				
				if(clearSelection) {
					selection.clearSelection();
				}
				
				if(index < latestSelected) {
					ListIterator<T> li = collection.listIterator(latestSelected + 1);
					while(li.hasPrevious()) {
						T currentElement=li.previous();
						selection.select(currentElement);
						if(currentElement.equals(element)) {
							break;
						}
					}
				} else {
					ListIterator<T> li = collection.listIterator(latestSelected);
					while(li.hasNext()) {
						T currentElement=li.next();
						selection.select(currentElement);
						if(currentElement.equals(element)) {
							break;
						}
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void ctrlClick(T element) {
		lock.lock();
		try {
			if(collection.size()==0) {
				return;
			} else {
				selection.toggle(element);
				lastModified=element;
			}
		} finally {
			lock.unlock();
		}
	}

	public void click(T element, int modifiers) {
		boolean ctrlPressed=((modifiers & InputEvent.CTRL_MASK) > 0);
		boolean shiftPressed=((modifiers & InputEvent.SHIFT_MASK) > 0);
		if(shiftPressed) {
			shiftClick(element, !ctrlPressed);
		} else if(ctrlPressed) {
			ctrlClick(element);
		} else {
			normalClick(element);
		}
	}
	
	@Override
	public Iterator<T> iterator() {
		lock.lock();
		try {
			return selection.iterator();
		} finally {
			lock.unlock();
		}
	}
	
	public void addListener(Object l) {
		selection.addListener(l);
	}

	public void removeListener(Object l) {
		selection.removeListener(l);
	}
}
