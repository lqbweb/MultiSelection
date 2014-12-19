package com.lqb.multiselection;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * It works pretty much like {@link ClickSelection} but adds a mechanism for notifying on DoubleClick on an image.
 * 
 * This differs a bit from how Windows work in the sense that Window would never open more than one item on double click,
 * but we do support this, by delaying a bit a normal click operation over an element that is selected
 * 
 * @author Rubén Pérez
 * @since 1.25
 * @param <T>
 */
public class DoubleClickSelection<T> extends ClickSelection<T>{
	protected static final int TIME_INTERVAL=350;
	private ScheduledExecutorService executor;
	private final ReentrantLock mainLock=new ReentrantLock();
	private RequestNormalClick lastRequest;
	private T lastNormalClick;
	private long lastTimeNormalClick;
	
	public DoubleClickSelection(List<T> collection) {
		this(collection, Executors.newScheduledThreadPool(1));
	}
	
	public DoubleClickSelection(List<T> collection, ScheduledExecutorService executor) {
		super(collection);
		this.executor=executor;
	}
	
	private boolean makesDoubleClick(final T element, long currentTime) {
		return (element.equals(lastNormalClick) && (currentTime - lastTimeNormalClick) < TIME_INTERVAL);
	}
	
	@Override
	public void normalClick(final T element) {
		mainLock.lock();
		try {
			long currentTime=System.currentTimeMillis();
			if(makesDoubleClick(element, currentTime)) {
				//double click
				cancelRequestWaiting(false);
				fireDoubleClick();
				return;
			} else {
				cancelRequestWaiting(true);
				if(selection.isSelected(element)) {
					//we delay it! maybe a second click comes
					Runnable task=new Runnable() {
						@Override
						public void run() {
							mainLock.lock();
							try {
								if(Thread.interrupted()) {
									return;
								} else {
									DoubleClickSelection.super.normalClick(element);
								}
							} finally {
								lastRequest=null;
								mainLock.unlock();
							}
						}
					};
					ScheduledFuture<?> fut=executor.schedule(task, TIME_INTERVAL, TimeUnit.MILLISECONDS);
					lastRequest=new RequestNormalClick(fut, task);
				} else {
					super.normalClick(element);
				}
				lastNormalClick=element;
				lastTimeNormalClick=currentTime;
			}
		} finally {
			mainLock.unlock();
		}
	}
	
	@Override
	public void shiftClick(T element) {
		mainLock.lock();
		try {
			cancelRequestWaiting(true);
			super.shiftClick(element);
		} finally {
			mainLock.unlock();
		}
	}
	
	@Override
	public void ctrlClick(T element) {
		mainLock.lock();
		try {
			cancelRequestWaiting(true);
			super.ctrlClick(element);
		} finally {
			mainLock.unlock();
		}
	}
	
	public void cleanup() {
		mainLock.lock();
		try {
			executor.shutdownNow();
		} finally {
			mainLock.unlock();
		}
	}
	
	private void cancelRequestWaiting(boolean runIfWaiting) {
		assert(mainLock.isHeldByCurrentThread());
		
		if(lastRequest!=null) {
			ScheduledFuture<?> fut=lastRequest.fut;
			if(runIfWaiting) {
				lastRequest.task.run();
			}
			fut.cancel(true);
			lastRequest=null;
		}
		lastNormalClick=null;
	}
	
	protected void fireDoubleClick() {
		
	}
	
	private class RequestNormalClick {
		private final ScheduledFuture<?> fut;
		private final Runnable task;
		public RequestNormalClick(ScheduledFuture<?> fut, Runnable task) {
			this.fut=fut;
			this.task=task;
		}
	}
}
