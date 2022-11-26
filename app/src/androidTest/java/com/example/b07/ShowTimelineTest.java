package com.example.b07;

import android.app.Activity;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShowTimelineTest {
    @Rule public ActivityScenarioRule<ShowTimeline> activityScenarioRule
            = new ActivityScenarioRule<ShowTimeline>(ShowTimeline.class);

    @Test
    public void callShow(){
    }
}
