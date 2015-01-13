package com.lqb.multiselection;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.lqb.multiselection.events.DoubleClickEvent;

/**
 * It works pretty much like {@link ClickSelection} but adds a mechanism for notifying on DoubleClick on an image.
 * 
 * This differs a bit from how Windows works in the sense that Windows would never open more than one item on double click,
 * but we do support this, by delaying a bit a normal click operation over an element that is already selected
 * 
 * @author Rubén Pérez
 * @since 1.25
 * @param <T>
 */
public class DoubleClickSelection<T> extends ClickSelection<T>{
	protected static final int TIME_INTERVAL=350;
	private ScheduledExecutorService executor;
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
		lock.lock();
		try {
			long currentTime=System.currentTimeMillis();
			if(makesDoubleClick(element, currentTime)) {
				//double click
				cancelRequestWaiting(false);
				fireDoubleClick(elements());
				return;
			} else {
				cancelRequestWaiting(true);
				if(isSelected(element)) {
					//we delay it! maybe a second click comes
					Runnable task=new Runnable() {
						@Override
						public void run() {
							try {
								if(Thread.interrupted()) {
									return;
								} else {
									DoubleClickSelection.super.normalClick(element);
								}
							} finally {
								lastRequest=null;
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
			lock.unlock();
		}
	}
	
	@Override
	public void shiftClick(T element) {
		lock.lock();
		try {
			cancelRequestWaiting(true);
			super.shiftClick(element);
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void ctrlClick(T element) {
		lock.lock();
		try {
			cancelRequestWaiting(true);
			super.ctrlClick(element);
		} finally {
			lock.unlock();
		}
	}
	
	public void cleanup() {
		lock.lock();
		try {
			executor.shutdownNow();
		} finally {
			lock.unlock();
		}
	}
	
	private void cancelRequestWaiting(boolean runIfWaiting) {
		assert(lock.isHeldByCurrentThread());
		
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
	
	@Override
	public void addListener(Object listener) {
		super.addListener(listener);
	}
	
	@Override
	public void removeListener(Object listener) {
		super.removeListener(listener);
	}
	
	protected void fireDoubleClick(Collection<T> selection) {
		eventBus.post(new DoubleClickEvent<T>(this, selection));
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
