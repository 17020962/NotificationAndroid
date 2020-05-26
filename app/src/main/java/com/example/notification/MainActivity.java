package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    EditText editDate, editTime;
    private String repeat = "no";

    public String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction("NOTIFICATION");
        intent.putExtra("KEY_FOO_STRING", "Medium AlarmManager Demo");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 2, intent, 0);

        int ALARM_DELAY_IN_SECOND = 10;
        long alarmTimeAtUTC = System.currentTimeMillis() + ALARM_DELAY_IN_SECOND * 1000;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(alarmManager.RTC_WAKEUP,alarmTimeAtUTC,pendingIntent);
            alarmManager2.setExactAndAllowWhileIdle(alarmManager.RTC_WAKEUP, alarmTimeAtUTC + 10*1000, pendingIntent2);
        }
        */

        String[] listRepeat = {"Một lần", "Hàng tuần"};
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listRepeat);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        String item = spinner.getSelectedItem().toString();

        if (item.equals("Một lần")) {
            repeat = "no";
        } else {
            repeat = "yes";
        }

        editDate = (EditText) findViewById(R.id.input_date);
        editDate.setOnClickListener(this);
        editTime = (EditText) findViewById(R.id.input_time);
        editTime.setOnClickListener(this);

        Button btnAlarmDone = (Button) findViewById(R.id.btn_done_alarm);
        btnAlarmDone.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.input_date) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String day = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
                            String month = monthOfYear + 1 < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "";
                            editDate.setText(day + "-" + month + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v.getId() == R.id.input_time) {
            final Calendar mcurrentTime = Calendar.getInstance();
            mHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            mMinute = mcurrentTime.get(Calendar.MINUTE);
            mSecond = mcurrentTime.get(Calendar.SECOND);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hour = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                            String minuteString = minute < 10 ? "0" + minute : minute + "";
                            editTime.setText(hour + ":" + minuteString);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }

        if (v.getId() == R.id.btn_done_alarm) {
            Editable date = editDate.getText();
            Editable time = editTime.getText();

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.setAction("NOTIFICATION");
            intent.putExtra("KEY_FOO_STRING", "Medium AlarmManager Demo");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            try {
                long alarmTimeAtUTC = convertTime(date.toString(), time.toString());
                Log.d("current: ", System.currentTimeMillis() + "");
                Log.d("timeURC: ", alarmTimeAtUTC + "");
                long intervalTime = 10 * 1000;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                    if (repeat.equals("yes")) {
//                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                                alarmTimeAtUTC,
//                                intervalTime, pendingIntent);
//                    } else {
//                        alarmManager.setExactAndAllowWhileIdle(alarmManager.RTC_WAKEUP, alarmTimeAtUTC, pendingIntent);
//                    }
//                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarmTimeAtUTC, pendingIntent), pendingIntent);
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    alarmManager.setExact(AlarmManager.RTC, alarmTimeAtUTC, pendingIntent);
                else
                    alarmManager.set(AlarmManager.RTC, alarmTimeAtUTC, pendingIntent);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }

    private long convertTime(String date, String time) throws ParseException {
        String dateTime = date + " " + time + ":" + "00";
        Log.d("dateTime ", dateTime);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d = formatter.parse(dateTime);
        return d.getTime();
    }

}
