package com.example.admin.rippledrawabletest;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // dynamically
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setMarqueeRepeatLimit(-1);
        tv.setHorizontallyScrolling(true);
        tv.setSelected(true);
        RippleDrawable ripple = getPressedColorRippleDrawable(0x00ffffff, 0xffff0000, 0xff00ff00);
        tv.setBackground(ripple);

        // statically
        TextView tv2 = (TextView) findViewById(R.id.tv2);


    }

    public static RippleDrawable getPressedColorRippleDrawable(int normalColor, int pressedColor, int longColor) {
        return new RippleDrawable(getPressedColorSelector(normalColor, pressedColor, longColor), null, null);
    }

    public static ColorStateList getPressedColorSelector(int normalColor, int pressedColor, int longColor) {
        return new ColorStateList(
                new int[][]
                        {
                                new int[]{-android.R.attr.state_pressed},  // pressed
                                new int[]{}
                        },
                new int[]
                        {
                                Color.GREEN,
                                normalColor
                        }
        );
    }

    public static ColorDrawable getColorDrawableFromColor(int color) {
        return new ColorDrawable(color);
    }
}
