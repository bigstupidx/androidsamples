package com.example.admin.timepickerdialogtest;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * refer to
 * https://developer.android.com/reference/android/app/TimePickerDialog.html
 * http://stackoverflow.com/questions/30964343/how-to-change-the-default-color-scheme-timepicker-dialog-fragment
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            final TimePickerFragment frag = new TimePickerFragment();
            frag.show(getFragmentManager(), "");
        } else if (v.getId() == R.id.btn2) {
            final TimePickerFragment2 frag = new TimePickerFragment2();
            frag.show(getFragmentManager(), "");
        }
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            final int hour = c.get(Calendar.HOUR_OF_DAY);
            final int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String str = hourOfDay + " " + minute;
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        }
    }

    public static class TimePickerFragment2 extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            final int hour = c.get(Calendar.HOUR_OF_DAY);
            final int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), R.style.TimePicker, this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String str = hourOfDay + " " + minute;
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        }
    }
}
