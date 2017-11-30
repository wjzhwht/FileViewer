package com.xero.dropdownmenu;

import android.content.Context;
import android.util.TypedValue;

public class Util {

    public static int dp(Context context, int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics()) + 0.5F);
    }
}
