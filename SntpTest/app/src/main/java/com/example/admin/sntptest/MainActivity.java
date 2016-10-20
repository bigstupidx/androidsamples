package com.example.admin.sntptest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * reference:
 * http://www.pool.ntp.org/zone/north-america
 * https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/net/SntpClient.java
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Runnable mRunnable;
    private H mHandler = new H();
    private String resultString = "";
    private TextView resultTv;

    private final String[] hosts = new String[]{
            "cn.pool.ntp.org",
            "asia.pool.ntp.org",
            "north-america.pool.ntp.org",
            "hk.pool.ntp.org",
            "0.north-america.pool.ntp.org",
            "1.north-america.pool.ntp.org",
            "2.north-america.pool.ntp.org",
            "3.north-america.pool.ntp.org",
            "0.europe.pool.ntp.org",
            "1.europe.pool.ntp.org",
            "2.europe.pool.ntp.org",
            "3.europe.pool.ntp.org",
            "0.asia.pool.ntp.org",
            "1.asia.pool.ntp.org",
            "2.asia.pool.ntp.org",
            "3.asia.pool.ntp.org",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        miscTest();

        resultTv = (TextView) findViewById(R.id.tv2);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        int len = hosts.length;
                        resultString = "";
                        for (int i = 0; i < len; i++) {
                            resultString += fetchNetworkTIme(hosts[i]) + "\n";
                        }
                        mHandler.sendEmptyMessage(H.RETURN_RESULT);
                    }
                };
                thread.start();
            }
        });
    }

    public String fetchNetworkTIme(String host) {
        String ret = "";
        try {
            SntpClient client = new SntpClient();
            if (client.requestTime(host, 3000)) {
                // as NtpTrustedTime.currentTimesMillis() definition
                long now = client.getNtpTime() + SystemClock.elapsedRealtime() - client.getNtpTimeReference();
                ret = "host: " + host + ", now: " + now + ", round trip: " + client.getRoundTripTime() + "ms";
            } else {
                ret = "host: " + host + " - connection timeout..";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    protected class H extends Handler {
        protected static final int RETURN_RESULT = 1;

        @Override
        public void handleMessage(Message msg) {
            String name = null;
            try {
                switch (msg.what) {
                    case RETURN_RESULT:
                        resultTv.setText(resultString);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown msg: " + msg.what);
                }
            } catch (Throwable t) {
                final String error = "Error in " + name;
                Log.w(TAG, error, t);
            }
        }
    }

    private void miscTest() {
        final String testStr = "123 , 456 , 789";
        String[] parts = testStr.split(",");
        for (int i = 0; i < parts.length; i++)
            Log.d(TAG, i + ": [" + parts[i].trim() + "]");

        final String testStr2 = "123456789";
        String[] parts2 = testStr2.split(",");
        for (int i = 0; i < parts2.length; i++)
            Log.d(TAG, i + ": [" + parts2[i].trim() + "]");
    }
}
