package com.example.myday;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class HappyEventDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private HashSet<CalendarDay> dates;

    public HappyEventDecorator(Collection<CalendarDay> dates, Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.bb);
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.addSpan(new ForegroundColorSpan(0));
        view.setSelectionDrawable(drawable);
    }
}


