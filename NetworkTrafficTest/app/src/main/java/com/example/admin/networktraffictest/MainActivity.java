package com.example.admin.networktraffictest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private TextView tv, tv2;
    private H mHandler = new H();
    private Handler mNetHandler = new Handler();
    private NetworkTrafficRunnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);

        r = new NetworkTrafficRunnable(mNetHandler);
    }

    private void handleRefreshData(String rate) {
        tv.setText(rate);
    }

    private void handleNoData() {
        //tv.setVisibility(View.INVISIBLE);
    }

    private void handleInvalidData() {
        tv.setText("-B/s");
    }

    @Override
    public void onPause() {
        super.onPause();
        mNetHandler.removeCallbacks(r);
    }

    @Override
    public void onResume() {
        super.onResume();
        mNetHandler.post(r);
    }

    protected class H extends Handler {
        protected static final int REFRESH_DATA = 1;
        protected static final int NO_NETWORK = 2;
        protected static final int INVALID = 3;

        @Override
        public void handleMessage(Message msg) {
            String name = null;
            try {
                switch(msg.what) {
                case REFRESH_DATA:
                    name = "handleRefreshData";
                    handleRefreshData((String)msg.obj);
                    break;
                case NO_NETWORK:
                    name = "handleNoData";
                    handleNoData();
                    break;
                case INVALID:
                    name = "handleInvalidData";
                    handleInvalidData();
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

    private class NetworkTrafficRunnable implements Runnable {
        private Handler handler;

        private final static long KB_IN_BYTES = 1024;
        private final static long MB_IN_BYTES = 1048576;
        private final static long GB_IN_BYTES = 1073741824;

        private int sampleInterval = 1000;
        private int timeShift = 400;
        private Rate lastRate, curRate;

        private class Rate {
            private long tx;
            private long rx;
            private long ts;
            private long curRate;

            public void reset() {
                rx = 0;
                tx = 0;
                ts = 0;
                curRate = 0;
            }

            public void setRx(long rx) { this.rx = rx; }
            public void setTx(long tx) { this.tx = tx; }
            public void setTimestamp(long ts) { this.ts = ts; }
            public void setCurRate(long curRate) { this.curRate = curRate; }
            public long getRx() { return rx; }
            public long getTx() { return tx; }
            public long getTimestamp() {return ts; }
            public long getCurRate() { return curRate; }

            public void copyTo(Rate other) {
                other.setTx(tx);
                other.setRx(rx);
                other.setTimestamp(ts);
                other.setCurRate(curRate);
            }
        }

        public NetworkTrafficRunnable(Handler handler) {
            this.handler = handler;

            lastRate = new Rate();
            curRate = new Rate();
            curRate.setRx(TrafficStats.getTotalRxBytes());
            curRate.setTx(TrafficStats.getTotalTxBytes());
            curRate.setTimestamp(System.nanoTime());
            curRate.copyTo(lastRate);
        }

        /**
         * set network traffic fetching interval in milliseconds
         */
        public void setSampleInterval(int sampleInterval) {
            this.sampleInterval = sampleInterval;
        }

        public void setTimeShift(int timeShift) {
            this.timeShift = timeShift;
        }

        /**
         * check if network is connected(not Internet)
         * @return
         */
        private boolean isNetworkEnabled() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return (cm.getActiveNetworkInfo() != null);
        }

        private String byteToString(long bytes) {
            String rate;
            if (bytes / KB_IN_BYTES < 1) {
                rate = String.valueOf(bytes) + "B/s";
            } else if (bytes / MB_IN_BYTES < 1) {
                rate = String.valueOf(bytes / KB_IN_BYTES) + "KB/s";
            } else if (bytes / GB_IN_BYTES < 1) {
                float val = (float) bytes / MB_IN_BYTES;
                rate = String.format(Locale.US, "%.1f", val) + "MB/s";
            } else {
                float val = (float) bytes / GB_IN_BYTES;
                rate = String.format(Locale.US, "%.1f", val) + "GB/s";
            }

            return (rate);
        }

        @Override
        public void run() {
            if (isNetworkEnabled()) {
                curRate.setRx(TrafficStats.getTotalRxBytes());
                curRate.setTx(TrafficStats.getTotalTxBytes());
                curRate.setTimestamp(System.nanoTime());
                Log.d(TAG, "curRx: " + curRate.getRx() +
                            ", curTx: " + curRate.getTx() +
                            ", curTimestamp: " + curRate.getTimestamp() +
                            ", lastRx: " + lastRate.getRx() +
                            ", lastTx: " + lastRate.getTx() +
                            ", lastTimestamp: " + lastRate.getTimestamp() +
                            ", ts delta: " + (curRate.getTimestamp()-lastRate.getTimestamp()));

                if (curRate.getTimestamp()-lastRate.getTimestamp() < (1000000)*(sampleInterval+timeShift)) {
                    long bytes = (curRate.getRx()+curRate.getTx()) - (lastRate.getRx()+lastRate.getTx());
                    curRate.setCurRate(bytes);
                    mHandler.obtainMessage(H.REFRESH_DATA, byteToString(bytes)).sendToTarget();
                } else {
                    mHandler.sendEmptyMessage(H.INVALID);
                }
                curRate.copyTo(lastRate);
            } else {
                Log.d(TAG, "no network.");
                mHandler.sendEmptyMessage(H.NO_NETWORK);
            }

            handler.postDelayed(this, sampleInterval);
        }
    }

}
