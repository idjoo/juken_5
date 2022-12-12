package com.androidplot.util;

import android.util.Log;
import com.androidplot.Plot;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Redrawer implements Runnable {
    private static final int ONE_SECOND_MS = 1000;
    private static final String TAG = Redrawer.class.getName();
    private boolean keepAlive;
    private boolean keepRunning;
    private List<WeakReference<Plot>> plots;
    private long sleepTime;
    private Thread thread;

    public Redrawer(List<Plot> plots2, float maxRefreshRate, boolean startImmediately) {
        this.plots = new ArrayList(plots2.size());
        for (Plot plot : plots2) {
            this.plots.add(new WeakReference(plot));
        }
        setMaxRefreshRate(maxRefreshRate);
        this.thread = new Thread(this, "Androidplot Redrawer");
        this.thread.start();
        if (startImmediately) {
            start();
        }
    }

    public Redrawer(Plot plot, float maxRefreshRate, boolean startImmediately) {
        this((List<Plot>) Collections.singletonList(plot), maxRefreshRate, startImmediately);
    }

    public synchronized void pause() {
        this.keepRunning = false;
        notify();
        Log.d(TAG, "Redrawer paused.");
    }

    public synchronized void start() {
        this.keepRunning = true;
        notify();
        Log.d(TAG, "Redrawer started.");
    }

    public synchronized void finish() {
        this.keepRunning = false;
        this.keepAlive = false;
        notify();
    }

    public void run() {
        this.keepAlive = true;
        while (this.keepAlive) {
            try {
                if (this.keepRunning) {
                    for (WeakReference<Plot> plotRef : this.plots) {
                        ((Plot) plotRef.get()).redraw();
                    }
                    synchronized (this) {
                        wait(this.sleepTime);
                    }
                } else {
                    synchronized (this) {
                        wait();
                    }
                }
            } catch (InterruptedException e) {
                Log.d(TAG, "Redrawer thread exited.");
                return;
            } catch (Throwable th) {
                Log.d(TAG, "Redrawer thread exited.");
                throw th;
            }
        }
        Log.d(TAG, "Redrawer thread exited.");
    }

    public void setMaxRefreshRate(float refreshRate) {
        this.sleepTime = (long) (1000.0f / refreshRate);
        Log.d(TAG, "Set Redrawer refresh rate to " + refreshRate + "( " + this.sleepTime + " ms)");
    }
}
