package com.velvetcase.app.material;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.wizrocket.android.sdk.ActivityLifecycleCallback;

/**
 * Created by Prashant Patil on 22-07-2015.
 */
public class MainApplicationActivity extends Application {

    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    @Override
    public void onCreate() {
        ActivityLifecycleCallback.register(this);  /// for WebRocket configuration.
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-37575096-2"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
        super.onCreate();
    }
}
