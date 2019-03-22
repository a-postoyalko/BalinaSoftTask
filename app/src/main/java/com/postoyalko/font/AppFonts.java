package com.postoyalko.font;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by aleksei on 21.03.2019.
 */

public class AppFonts {
    private AppFonts() {}

    private static Typeface typeface;
    private static final String APP_FONT = "fonts/HelveticaNeueCyr-Roman.ttf";

    public static Typeface getInstance(Context context){
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), APP_FONT);
        }
        return typeface;
    }
}
