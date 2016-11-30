package com.loyalty.webserivcemodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jayendrapratapsingh on 19/9/16.
 */
public class FilterModel implements Serializable {
    public int filter;
    public String  businessTypeId;
    public String businessTypeName;
    public Boolean isSelected;


    public  boolean isChecked() {
        return isChecked;
    }

    public static void setChecked(boolean checked) {
        isChecked = checked;
    }

    private static boolean isChecked;
}
