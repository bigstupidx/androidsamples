package com.example.admin.switchtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * https://developer.android.com/reference/android/widget/Switch.html
 * http://stackoverflow.com/questions/10173590/how-to-change-the-size-of-a-switch-widget
 * http://blog.csdn.net/u012585142/article/details/50756872
  */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
 
    public String getMacAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = manager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            Log.e(TAG, "device without mac address");
        } else {
            if (macAddress.equals("00:00:00:00:00:00"))
                Toast.makeText(context, "invalid mac address detected", Toast.LENGTH_LONG).show();
        }
        return macAddress;
    }
}
