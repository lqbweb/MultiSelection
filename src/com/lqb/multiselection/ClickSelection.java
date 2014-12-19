package com.lqb.multiselection;

import java.awt.event.InputEvent;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
 * @author Rubén Pérez
 * @since 1.25
 * @param <T>
 */
public class ClickSelection<T> implements Iterable<T> {

	protected SetSelection<T> selection=new SetSelection<T>();
	private final List<T> collection;
	private T lastModified=null;
	
	public ClickSelection(List<T> collection) {
		this.collection=collection;
	}
	
	public boolean isSelected(T element) {
		return selection.isSelected(element);
	}
	
	public int size() {
		return selection.size();
	}
		
	
	/**
	 * Call it when you have removed an element from "collection"
	 * @param element
	 */
	public void unselectItem(T element) {
		selection.unselect(element);
	}

	/**
	 * Requirements:
	 * 		- If there is only this item already selected, the item will be deselected
	 * 		- If there is nothing selected or another item selected, the new item will be selected
	 * 		- If there are multiple items selected, and element is not selected, "element" will be the only one selected
	 * 		- If there are multiple items selected, and element is in between them, element will be the only one selected
	 */
	public void normalClick(T element) {
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
				}
			}
			lastModified=element;
		}
	}
	
	public void clearSelection() {
		selection.clearSelection();
	}
	
	public void shiftCtrlClick(T element) {
		shiftClick(element, false);
	}
	
	public void shiftClick(T element) {
		shiftClick(element, true);
	}
	
	private void shiftClick(T element, boolean clearSelection) {
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
	}
	
	public void ctrlClick(T element) {
		if(collection.size()==0) {
			return;
		} else {
			selection.toggle(element);
			lastModified=element;
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
		return selection.iterator();
	}
}
