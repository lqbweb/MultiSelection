package com.lqb.multiselection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDoubleClickSelection {

	private List<Integer> listInteger;
	private DoubleClickSelection<Integer> clickSelection;
	private final AtomicInteger countDoubleClick=new AtomicInteger(); 
	
	@Before
	public void setupTest() {
		listInteger=new ArrayList<Integer>();
		clickSelection=new DoubleClickSelection<Integer>(listInteger) {
			@Override
			protected void fireDoubleClick(Collection<Integer> selection) {
				countDoubleClick.incrementAndGet();
			}
		};
	}
	
	@After
	public void afterTest() {
		countDoubleClick.set(0);
		clickSelection.cleanup();
	}

	
	
	@Test
	public void testOneNormalClick() {
		listInteger.add(1);
		assertFalse(clickSelection.isSelected(1));
		clickSelection.normalClick(1);
		assertTrue(clickSelection.isSelected(1));
	}

	@Test
	public void testOneNormalClick_Wait_OneNormalClick_Wait() throws InterruptedException {
		listInteger.add(1);
		clickSelection.normalClick(1);
		assertTrue(clickSelection.isSelected(1));
		Thread.sleep(DoubleClickSelection.TIME_INTERVAL + 10);
		clickSelection.normalClick(1);
		assertTrue(clickSelection.isSelected(1));
		Thread.sleep(DoubleClickSelection.TIME_INTERVAL + 10);
		assertEquals(0, clickSelection.size());
		assertFalse(clickSelection.isSelected(1));
		assertEquals(0, countDoubleClick.get());
	}
	
	@Test
	public void testOneNormalClickImage1_OneNormalClickImage2() throws InterruptedException {
		listInteger.add(1);
		listInteger.add(2);
		clickSelection.normalClick(1);
		assertTrue(clickSelection.isSelected(1));
		clickSelection.normalClick(2);
		assertEquals(1, clickSelection.size());
		assertTrue(clickSelection.isSelected(2));
		assertEquals(0, countDoubleClick.get());
	}
	
	@Test
	public void testOneNormalClickImage1_OneNormalClickImage1_CtrlImage2() throws InterruptedException {
		listInteger.add(1);
		listInteger.add(2);
		clickSelection.normalClick(1);
		assertTrue(clickSelection.isSelected(1));
		Thread.sleep(DoubleClickSelection.TIME_INTERVAL + 10);
		clickSelection.normalClick(1);
		clickSelection.ctrlClick(2);
		assertFalse(clickSelection.isSelected(1));
		assertTrue(clickSelection.isSelected(2));
		assertEquals(0, countDoubleClick.get());
	}
	
	@Test
	public void testDoubleClickImage1() throws InterruptedException {
		listInteger.add(1);
		clickSelection.normalClick(1);
		Thread.sleep(DoubleClickSelection.TIME_INTERVAL/2);
		clickSelection.normalClick(1);
		assertTrue(clickSelection.isSelected(1));
		assertEquals(1, countDoubleClick.get());
	}
	
	@Test
	public void testClickImage1_ClickImage2_ClickImage1() throws InterruptedException {
		listInteger.add(1);
		listInteger.add(2);
		clickSelection.shiftClick(2);
		assertTrue(clickSelection.isSelected(1));
		assertTrue(clickSelection.isSelected(2));
		clickSelection.normalClick(1);
		assertTrue(clickSelection.isSelected(2));
		clickSelection.normalClick(2);
		assertFalse(clickSelection.isSelected(1));	//click on 2, has forced to run the task waiting "1", and therefore selected. But the task for "2" has not run yet (still a chance of double click)
		assertTrue(clickSelection.isSelected(2));
	}
}
