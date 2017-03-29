package com.rent_it_app.rent_it;

import android.content.Context;
import android.util.AttributeSet;

import com.borax12.materialdaterangepicker.date.DatePickerController;
import com.borax12.materialdaterangepicker.date.MonthAdapter;
import com.borax12.materialdaterangepicker.date.SimpleDayPickerView;
import com.borax12.materialdaterangepicker.date.SimpleMonthAdapter;

/**
 * Created by malhan on 3/20/17.
 */

public class MySimpleDayPickerView extends SimpleDayPickerView{

    public MySimpleDayPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySimpleDayPickerView(Context context, DatePickerController controller) {
        super(context, controller);
    }

    @Override
    public MonthAdapter createMonthAdapter(Context context, DatePickerController controller) {
        return new SimpleMonthAdapter(context, controller);
    }
}
