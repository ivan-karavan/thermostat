package ru.hse.paulgroup2.thermostat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Thermostat extends Activity {

    ImageView currentModeView;

    TextView currentTempView, userTempView, dayTempView, nightTempView, currentTimeView;

    Date currentTime = new Date(Calendar.getInstance());

    NewThermostatSchedule schedule = new NewThermostatSchedule();

    Temperature dayTemperature = new Temperature(10, 0);
    Temperature nightTemperature = new Temperature(15, 0);
    Temperature userTemperature = new Temperature(4, 2);
    Temperature currentTemperature = new Temperature(9, 9);

    static final int NIGHT = 0, DAY = 1;

    int currentMode = NIGHT;

    boolean vacationMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);

        currentModeView = (ImageView) findViewById(R.id.currentmode);
        currentTempView = (TextView) findViewById(R.id.currenttemp);
        userTempView    = (TextView) findViewById(R.id.usertemp);
        dayTempView     = (TextView) findViewById(R.id.daytemp);
        nightTempView   = (TextView) findViewById(R.id.nighttemp);
        currentTimeView = (TextView) findViewById(R.id.currenttime);

        initFieldsAndViews();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                        runOnUiThread(tickEvent);
                    }
                    catch (InterruptedException ie) {
                        System.out.println("Updater is dead");
                    }
                }
            }
        }).start();
    }

    private void setCurrentModeImage() {
        if (currentMode == NIGHT) {
            currentModeView.setImageResource(R.drawable.bigmoonpic);
        } else {
            currentModeView.setImageResource(R.drawable.bigsunpic);
        }
    }

    private Runnable tickEvent = new Runnable() {
        @Override
        public void run() {
            currentTime.addMinute();
            if (!vacationMode) {
                if (schedule.needTempUpdate(currentTime.day, new Time(currentTime.hour, currentTime.minute), currentMode)) {
                    currentMode = 1 - currentMode; // day->night, night->day
                    setCurrentModeImage();
                }
            }
            currentTimeView.setText(currentTime.toString());
        }
    };

    public void setNewUserTemp(View view) {
        //Todo: add choose temp
        Toast.makeText(this, "UNFORTUNATELY NOT WORKING\n WE BROKEN IT ._.\nTHIS BUTTON SET RANDOM TEMPERATURE", Toast.LENGTH_LONG).show();
        userTemperature = new Temperature(13, 3);
        userTempView.setText(userTemperature.toString());
    }

    public void changeVacation(View view) {
        if (vacationMode) {
            Toast.makeText(this, "Vacation mode is disabled", Toast.LENGTH_SHORT).show();
        } else {
            currentModeView.setImageResource(R.drawable.biglockpic);
            Toast.makeText(this, "Vacation mode is enabled", Toast.LENGTH_SHORT).show();
        }
        vacationMode = !vacationMode;
    }

    public void initFieldsAndViews() {
        vacationMode = false;
        if (currentMode == DAY) {
            currentModeView.setImageResource(R.drawable.bigsunpic);
        } else {
            currentModeView.setImageResource(R.drawable.bigmoonpic);
        }
        setViewTemp(nightTempView, nightTemperature);
        setViewTemp(dayTempView, dayTemperature);
        setViewTemp(currentTempView, currentTemperature);
        setViewTemp(userTempView, userTemperature);
    }

    private void setViewTemp(TextView view, Temperature temp) {
        view.setText(temp.toString());
    }

    public void onUserTemp(View view) {
        currentModeView.setImageResource(R.drawable.biguserpic);
        Toast.makeText(this, "User mode enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_thermostat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    public void setSchedule(View view) {
        Toast.makeText(this, "UNFORTUNATELY NOT WORKING\n WE BROKEN IT ._.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Thermostat.this, Schedule.class);
        intent.putExtra("SCHEDULE", schedule);
        startActivity(intent);
    }
}
