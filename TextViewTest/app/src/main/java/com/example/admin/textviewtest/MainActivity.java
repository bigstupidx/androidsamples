package com.example.admin.textviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // auto scrolling, if width is not enough
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setMarqueeRepeatLimit(-1);
        tv.setHorizontallyScrolling(true);
        tv.setSelected(true);
    }
}
