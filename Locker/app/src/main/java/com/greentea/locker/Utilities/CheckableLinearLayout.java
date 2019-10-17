package com.greentea.locker.Utilities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.greentea.locker.R;

public class CheckableLinearLayout  extends LinearLayout implements Checkable {

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1);
        return cb.isChecked();
    }

    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1);
        setChecked(cb.isChecked() ? false : true);
    }

    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1);
        if(cb.isChecked() != checked) {
            cb.setChecked(checked);
        }
    }
}
