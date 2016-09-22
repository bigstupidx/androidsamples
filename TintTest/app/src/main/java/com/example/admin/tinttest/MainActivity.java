package com.example.admin.tinttest;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * ref:
 * https://developer.android.com/reference/android/graphics/PorterDuff.Mode.html
 * http://blog.csdn.net/aigestudio/article/details/41316141
 *
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int dst_color = 0xFFFF0000;

        // [Cs+Cd] -> 0xFFFF00FF, 0xFFFFFFFF
        ImageView iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_add.setColorFilter(dst_color, PorterDuff.Mode.ADD); // must with alpha = 0xff

        // [Cs*Cd] -> 0xFF000000, 0xFFFF0000
        ImageView iv_mul = (ImageView) findViewById(R.id.iv_mul);
        iv_mul.setColorFilter(dst_color, PorterDuff.Mode.MULTIPLY);

        ImageView iv_atop = (ImageView) findViewById(R.id.iv_atop);
        iv_atop.setColorFilter(dst_color, PorterDuff.Mode.SRC_ATOP);
        // same as iv_atop.setColorFilter(dst_color);
    }
}
