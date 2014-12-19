package com.lqb.multiselection;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestClickSelection {
	private List<Integer> listInteger;
	private ClickSelection<Integer> clickSelection;
	
	@Before
	public void setupTest() {
		listInteger=new ArrayList<Integer>();
		clickSelection=new ClickSelection<Integer>(listInteger);
	}
	
	@Test
	public void testNormalClick() {
		listInteger.add(1);
		assertEquals(0, clickSelection.size());
		clickSelection.normalClick(1);
		assertEquals(1, (int) clickSelection.iterator().next());
	}
		
	@Test
	public void testNormalClickTwice() {
		listInteger.add(1);
		clickSelection.normalClick(1);
		assertEquals(1, (int) clickSelection.iterator().next());
		clickSelection.normalClick(1);
		assertEquals(0, clickSelection.size());
	}
	
	@Test
	public void testDifferentNormalClick() {
		listInteger.add(1);
		listInteger.add(2);
		clickSelection.normalClick(1);
		assertEquals(1, (int) clickSelection.iterator().next());
		clickSelection.normalClick(2);
		assertEquals(1, clickSelection.size());
		assertEquals(2, (int) clickSelection.iterator().next());
	}
	
	@Test
	public void testControlClick() {
		listInteger.add(1);
		listInteger.add(2);
		clickSelection.ctrlClick(1);
		assertEquals(1, (int) clickSelection.iterator().next());
		assertEquals(1, clickSelection.size());
	}
		
	@Test
	public void testClearSelection() {
		listInteger.add(1);
		listInteger.add(2);
		clickSelection.ctrlClick(1);
		clickSelection.ctrlClick(2);
		assertEquals(2, clickSelection.size());
		clickSelection.clearSelection();
		assertEquals(0, clickSelection.size());
	}
	
	@Test
	public void testSeveralDifferentControlClick() {
		listInteger.add(1);
		listInteger.add(2);
		clickSelection.ctrlClick(1);
		assertEquals(1, clickSelection.size());
		assertEquals(1, (int) clickSelection.iterator().next());
		clickSelection.ctrlClick(2);
		assertEquals(2, clickSelection.size());
		Iterator<Integer> ite=clickSelection.iterator();
		assertEquals(1, (int) ite.next());
		assertEquals(2, (int) ite.next());
	}
	
	@Test
	public void testSeveralSameControlClick() {
		listInteger.add(1);
		clickSelection.ctrlClick(1);
		assertEquals(1, clickSelection.size());
		assertEquals(1, (int) clickSelection.iterator().next());
		clickSelection.ctrlClick(1);
		assertEquals(0, clickSelection.size());
		clickSelection.ctrlClick(1);
		assertEquals(1, clickSelection.size());
		assertEquals(1, (int) clickSelection.iterator().next());
	}
	
	@Test
	public void testSeveralDifferentControlClickAndNormalClickOne() {
		listInteger.add(1);
		listInteger.add(2);
		clickSelection.ctrlClick(1);
		clickSelection.ctrlClick(2);
		assertEquals(2, clickSelection.size());
		clickSelection.normalClick(1);
		assertEquals(1, clickSelection.size());
		assertEquals(1, (int) clickSelection.iterator().next());
	}
	
	@Test
	public void testSeveralDifferentControlClickAndNormalClickAnother() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		clickSelection.ctrlClick(1);
		clickSelection.ctrlClick(2);
		assertEquals(2, clickSelection.size());
		clickSelection.normalClick(3);
		assertEquals(1, clickSelection.size());
		assertEquals(3, (int) clickSelection.iterator().next());
	}
	
	@Test
	public void testShiftOnEmpty() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		clickSelection.shiftClick(3);
		assertEquals(3, clickSelection.size());
		Iterator<Integer> ite=clickSelection.iterator();
		assertEquals(1, (int) ite.next());
		assertEquals(2, (int) ite.next());
		assertEquals(3, (int) ite.next());
	}
	@Test
	public void testShiftFromBeginningToEnd() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		listInteger.add(4);
		listInteger.add(5);
		clickSelection.normalClick(1);
		clickSelection.shiftClick(5);
		assertEquals(5, clickSelection.size());
		Iterator<Integer> ite=clickSelection.iterator();
		assertEquals(1, (int) ite.next());
		assertEquals(2, (int) ite.next());
		assertEquals(3, (int) ite.next());
		assertEquals(4, (int) ite.next());
		assertEquals(5, (int) ite.next());
	}
	
	@Test
	public void testShiftFromEndToBeginning() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		listInteger.add(4);
		listInteger.add(5);
		clickSelection.normalClick(5);
		clickSelection.shiftClick(1);
		assertEquals(5, clickSelection.size());
		Iterator<Integer> ite=clickSelection.iterator();
		assertEquals(1, (int) ite.next());
		assertEquals(2, (int) ite.next());
		assertEquals(3, (int) ite.next());
		assertEquals(4, (int) ite.next());
		assertEquals(5, (int) ite.next());
		
	}
	
	@Test
	public void testShiftFromMiddleToBeginning() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		listInteger.add(4);
		listInteger.add(5);
		clickSelection.normalClick(3);
		clickSelection.shiftClick(1);
		assertEquals(3, clickSelection.size());
		Iterator<Integer> ite=clickSelection.iterator();
		assertEquals(1, (int) ite.next());
		assertEquals(2, (int) ite.next());
		assertEquals(3, (int) ite.next());
	}
	
	@Test
	public void testShiftFromMiddleToEnd() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		listInteger.add(4);
		listInteger.add(5);
		clickSelection.normalClick(3);
		clickSelection.shiftClick(5);
		assertEquals(3, clickSelection.size());
		Iterator<Integer> ite=clickSelection.iterator();
		assertEquals(3, (int) ite.next());
		assertEquals(4, (int) ite.next());
		assertEquals(5, (int) ite.next());
	}
	
	@Test
	public void testShiftAllAndControlOne() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		clickSelection.shiftClick(3);
		clickSelection.ctrlClick(2);
		assertEquals(2, clickSelection.size());
		Iterator<Integer> ite=clickSelection.iterator();
		assertEquals(1, (int) ite.next());
		assertEquals(3, (int) ite.next());
	}
	
	@Test
	public void testShiftAllAndNormalClickOne() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		clickSelection.shiftClick(3);
		clickSelection.normalClick(2);
		assertEquals(1, clickSelection.size());
		assertEquals(2, (int) clickSelection.iterator().next());
	}
	
	@Test
	public void testControlClickDisperateAndShiftEnd() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		listInteger.add(4);
		listInteger.add(5);
		clickSelection.normalClick(1);
		clickSelection.ctrlClick(3);
		clickSelection.ctrlClick(2);
		assertEquals(3, clickSelection.size());
		clickSelection.shiftClick(5);
		assertEquals(4, clickSelection.size());
		Iterator<Integer> ite=clickSelection.iterator();
		assertEquals(2, (int) ite.next());
		assertEquals(3, (int) ite.next());
		assertEquals(4, (int) ite.next());
		assertEquals(5, (int) ite.next());
	}
	
	@Test
	public void testControlClickDisperateAndShiftStart() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		listInteger.add(4);
		listInteger.add(5);
		clickSelection.normalClick(1);
		clickSelection.ctrlClick(4);
		clickSelection.ctrlClick(3);
		assertEquals(3, clickSelection.size());
		clickSelection.shiftClick(1);
		assertEquals(3, clickSelection.size());
		Iterator<Integer> ite=clickSelection.iterator();
		assertEquals(1, (int) ite.next());
		assertEquals(2, (int) ite.next());
		assertEquals(3, (int) ite.next());
	}
	
	@Test
	public void testShiftClickDoesNotSaveLastClicked() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		listInteger.add(4);
		listInteger.add(5);
		clickSelection.normalClick(2);
		clickSelection.shiftClick(4);
		clickSelection.clearSelection();
		assertEquals(0, clickSelection.size());
		clickSelection.shiftClick(1);
		assertEquals(2, clickSelection.size());
		Iterator<Integer> ite=clickSelection.iterator();
		assertEquals(1, (int) ite.next());
		assertEquals(2, (int) ite.next());
	}
	
	@Test
	public void testShiftMiddleStartShiftEnd() {
		listInteger.add(1);
		listInteger.add(2);
		listInteger.add(3);
		listInteger.add(4);
		listInteger.add(5);
		clickSelection.normalClick(3);
		clickSelection.shiftClick(1);
		clickSelection.shiftClick(5);
		assertEquals(3, clickSelection.size());
		Iterator<Integer> ite=clickSelection.iterator();
		assertEquals(3, (int) ite.next());
		assertEquals(4, (int) ite.next());
		assertEquals(5, (int) ite.next());
	}
}
