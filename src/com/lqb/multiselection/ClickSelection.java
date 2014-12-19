package com.lqb.multiselection;

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
 *		ctrlClick: 		A new item will be added to the selection, or if it is already selected, it will be deselected
 *
 * In general it works pretty much like Windows would do
 * 
 * This API is assuming that when an item is removed from the collection, you deselect it from here.
 *
 * @author Rubén Pérez
 * @since 1.25
 * @param <T>
 */
public class ClickSelection<T> implements Iterable<T> {

	protected SetSelection<T> selection=new SetSelection<T>();
	private final List<T> collection;
	
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
	public void deselectItem(T element) {
		selection.deselect(element);
	}

	/**
	 * Requirements:
	 * 		- If there is only this item already selected, the item will be deselected
	 * 		- If there is nothing selected or another item selected, the new item will be selected
	 * 		- If there are multiple items selected, and element is not selected, "element" will be the only one selected
	 * 		- If there are multiple items selected, and element is in between them, element will be the only one selected
	 */
	public void normalClick(T element) {
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
	}
	
	/**
	 * Returns the index of the last element in "collection", that is contained in "selection"
	 * @return -1 if nothing is selected, and the index of the item in "collection" if at least one is selected
	 */
	private int getIndexLatestSelected() {
		if(selection.size()==0) {
			return -1;	//nothing selected
		} else if(selection.size()==1) {
			return collection.indexOf(selection.iterator().next());
		} else {
			ListIterator<T> li = collection.listIterator(collection.size());
			int nCounter=collection.size()-1;
			while(li.hasPrevious()) {
				T elem=li.previous();
				if(selection.isSelected(elem)) {
					return nCounter;
				}
				nCounter--;
			}
			return -1;	//nothing selected
		}
	}
	
	
	public void shiftClick(T element) {
		if(collection.size()==0) {
			return;
		} else {
			int index=collection.indexOf(element);
			if(index<0)
				return;
			
			int latestSelected=getIndexLatestSelected();
			if(latestSelected<0) {
				//if nothing is selected, we will select starting from the first one
				latestSelected=0;
			}
			
			selection.clearSelection();
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
		}
	}

	@Override
	public Iterator<T> iterator() {
		return selection.iterator();
	}
}
