package com.lqb.multiselection;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.lqb.multiselection.SetSelection;


public class TestSetSelection {

	private SetSelection<Integer> newSelection;
	
	@Before
	public void setupTest() {
		newSelection=new SetSelection<Integer>();
	}
	
	
	@Test
	public void testSelectWithoutToggle() {
		newSelection.select(2);
		assertTrue(newSelection.isSelected(2));
	}

	
	@Test
	public void testMultipleSelectWithoutToggle() {
		newSelection.select(2);
		assertTrue(newSelection.isSelected(2));
		newSelection.select(2);
		assertTrue(newSelection.isSelected(2));
	}
	
	@Test
	public void testMultipleSelectWitToggle() {
		newSelection.toggle(2);
		assertTrue(newSelection.isSelected(2));
		newSelection.toggle(2);
		assertFalse(newSelection.isSelected(2));
	}
	
	@Test
	public void testDeselect() {
		newSelection.toggle(2);
		assertTrue(newSelection.isSelected(2));
		newSelection.deselect(2);
		assertFalse(newSelection.isSelected(2));
	}
	
	@Test
	public void testIsSelected() {
		assertFalse(newSelection.isSelected(1));
		newSelection.select(2);
		assertFalse(newSelection.isSelected(1));
		assertTrue(newSelection.isSelected(2));
	}
	
	@Test
	public void testClearSelection() {
		newSelection.select(1);
		assertEquals(1, newSelection.size());
		newSelection.clearSelection();
		assertFalse(newSelection.isSelected(1));
		assertEquals(0, newSelection.size());
	}
	
}
