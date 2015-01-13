package com.lqb.multiselection;

import java.awt.event.InputEvent;
import java.util.List;
import java.util.ListIterator;

import com.google.common.eventbus.EventBus;
import com.lqb.multiselection.events.ClearSelectionEvent;
import com.lqb.multiselection.events.SelectClickEvent;
import com.lqb.multiselection.events.UnselectClickEvent;

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
public class ClickSelection<T> extends SetSelection<T> {
	private final List<T> collection;		//this contains the collection in which elements in "selection" will be selected.
	private T lastModified=null;
	
	protected EventBus eventBus=new EventBus();
	
	public ClickSelection(List<T> collection) {
		this.collection=collection;
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
				boolean isElementSelected=isSelected(element);
				int selectionSize=size();
				clearSelection();
				if(!isElementSelected) {
					select(element);
				} else {
					if(selectionSize>1) {
						select(element);
					} else {
					}
				}
				lastModified=element;
			}
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void clearSelection() {
		super.clearSelection();
		fireClearSelection();
	}
	
	@Override
	public boolean select(T element) {
		if(super.select(element)) {
			fireSelectItem(element);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean unselect(T element) {
		if(super.unselect(element)) {
			fireUnselectItem(element);
			return true;
		}
		return false;
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
					clearSelection();
				}
				
				if(index < latestSelected) {
					ListIterator<T> li = collection.listIterator(latestSelected + 1);
					while(li.hasPrevious()) {
						T currentElement=li.previous();
						select(currentElement);
						if(currentElement.equals(element)) {
							break;
						}
					}
				} else {
					ListIterator<T> li = collection.listIterator(latestSelected);
					while(li.hasNext()) {
						T currentElement=li.next();
						select(currentElement);
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
				toggle(element);
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
