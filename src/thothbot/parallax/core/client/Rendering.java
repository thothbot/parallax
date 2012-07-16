/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.client;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.animation.client.AnimationScheduler.AnimationHandle;
import com.google.gwt.core.client.Duration;

/**
 * An {@link Rendering} is a continuous event that updates progressively over
 * time at a non-fixed frame rate.
 * 
 * @author thothbot
 *
 */
public abstract class Rendering
{
	private final AnimationCallback callback = new AnimationCallback() 
	{
		@Override
		public void execute(double timestamp)
		{
			// Schedule the next animation frame.
			if (update(timestamp))
				requestHandle = scheduler.requestAnimationFrame(callback);
			else
				requestHandle = null;
		}
	};

	/**
	 * Is the animation running, even if it hasn't started yet.
	 */
	private boolean isRunning = false;

	/**
	 * Has the {@link Rendering} actually started.
	 */
	private boolean isStarted = false;
	
	/**
	 * Did the animation start before {@link #stop()} was called.
	 */
	private boolean isStoped = false;

	/**
	 * The ID of the pending animation request.
	 */
	private AnimationHandle requestHandle;

	/**
	 * The unique ID of the current run. Used to handle cases where an animation
	 * is restarted within an execution block.
	 */
	private int runId = -1;

	private final AnimationScheduler scheduler;

	/**
	 * The start time of the {@link Rendering}.
	 */
	private double startTime = -1;
	
	/**
	 * The stop time of the {@link Rendering}.
	 */
	private double stopTime = 0;
	
	/**
	 * (stopTime - CurrentTime) if ifStopped = true
	 */
	private double stoppingDelta = 0;
	
	/**
	 * Construct a new {@link Rendering}.
	 */
	public Rendering() 
	{
		this(AnimationScheduler.get());
	}

	/**
	 * Construct a new {@link AnimationScheduler} using the specified scheduler
	 * to sheduler request frames.
	 * 
	 * @param scheduler
	 *            an {@link AnimationScheduler} instance
	 */
	protected Rendering(AnimationScheduler scheduler) 
	{
		this.scheduler = scheduler;
	}
	
	/**
	 * Stops this rendering. If the rendering is running or is
	 * scheduled to run, {@link #onStop()} will be called.
	 */
	public void stop()
	{
		// Ignore if the animation is not currently running.
		if (!isRunning) 
			return;

		// Reset the state.
		this.isStoped = this.isStarted; // Used by onCancel.
		this.isRunning = false;
		this.stopTime = Duration.currentTimeMillis();

		// Cancel the animation request.
		if (this.requestHandle != null) 
		{
			this.requestHandle.cancel();
			this.requestHandle = null;
		}

		onStop();
	}

	/**
	 * Run this rendering. If the rendering is already running, it will be
	 * canceled first.
	 * <p>
	 * If the element is not <code>null</code>, the {@link #onUpdate(double)}
	 * method might be called only if the element may be visible (generally left
	 * at the appreciation of the browser). Otherwise, it will be called
	 * unconditionally.
	 */
	public void run()
	{
		// Cancel the animation if it is running
		stop();
		this.isRunning = true;
		++this.runId;

		// Save the duration and startTime
		if(!this.isStarted)
			this.startTime = Duration.currentTimeMillis();

		// Execute the first callback.
		this.callback.execute(Duration.currentTimeMillis());
	}

	/**
	 * Called immediately after the rendering is stopped. The default
	 * implementation of this method calls {@link #onStop()} only if the
	 * animation has actually started running.
	 */
	protected abstract void onStop();

	/**
	 * Called immediately before the rendering starts.
	 */
	protected abstract void onStart();

	/**
	 * Called when the rendering should be updated.
	 *  
	 * @param duration
	 *            The duration of the {@link Rendering} in milliseconds.
	 */
	protected abstract void onUpdate(double duration);

	/**
	 * Check if the specified run ID is still being run.
	 * 
	 * @param curRunId
	 *            the current run ID to check
	 * @return true if running, false if canceled or restarted
	 */
	private boolean isRunning(int curRunId)
	{
		return isRunning && (runId == curRunId);
	}

	/**
	 * Update the {@link Rendering}.
	 * 
	 * @param curTime
	 *            the current time
	 * @return true if the animation should run again, false if it is complete
	 */
	private boolean update(double curTime)
	{
		/*
		 * Save the run id. If the runId is incremented during this execution
		 * block, we know that this run has been canceled.
		 */
		final int curRunId = runId;
	
		if (isStoped) 
		{
			stoppingDelta += curTime -  this.stopTime;
			this.isStoped = false;
		}

		if (!isStarted) 
		{
			/*
			 * Start the animation. We do not call onUpdate() because onStart()
			 * calls onUpdate() by default.
			 */
			isStarted = true;
			onStart();

			// This run was canceled.
			return isRunning(curRunId);
		}
		else if (isStarted) 
		{
			// Rendering is in progress.
			onUpdate(curTime - this.startTime - stoppingDelta);

			// Check if this run was canceled.
			return isRunning(curRunId); 
		}

		return true;
	}
}
