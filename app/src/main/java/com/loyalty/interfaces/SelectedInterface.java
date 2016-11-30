package com.loyalty.interfaces;

import android.widget.CheckBox;
import android.widget.TextView;

public interface SelectedInterface {

    void onItemSelected(String string, int position);
    void onItemUnSelected(String string, int position);

}