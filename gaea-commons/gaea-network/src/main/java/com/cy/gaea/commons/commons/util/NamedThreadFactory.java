package com.cy.gaea.commons.commons.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cy on 2016/8/15.
 */
public class NamedThreadFactory implements ThreadFactory {
	private AtomicInteger counter = new AtomicInteger(0);

	private String name;
	private boolean daemon;

	public NamedThreadFactory(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("name is empty");
		}
		this.name = name;
	}

	public NamedThreadFactory(String name, boolean daemon) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("name is empty");
		}
		this.name = name;
		this.daemon = daemon;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName(name + " - " + counter.incrementAndGet());
		if (daemon) {
			thread.setDaemon(daemon);
		}
		return thread;
	}
}
