package org.twintechsoft.simplealarmwithgit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.twintechsoft.simplealarmwithgit.R;
import org.twintechsoft.simplealarmwithgit.createalarm.CreateAlarmViewModel;
import org.twintechsoft.simplealarmwithgit.data.Alarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class UpdateAlarm extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    //Spinner item
    String categoryText;//spinner items selection fetching
    String[] category = { "Sleeping Time", "Workout Time",
            "Wake Up time", "Study Time",
            "Android Time", "Food Time" };

    Spinner spinner;
    EditText title;
    TextView timeText,dateT;
    int hourUpdate,minuteUpdate;
    int updateID;
    Button updateAlarm;
    //CheckedTextView sa,su,mo,tu,we,th,fr;
    CheckedTextView mon;
    CheckedTextView tue;
    CheckedTextView wed;
    CheckedTextView thu;
    CheckedTextView fri;
    CheckedTextView sat;
    CheckedTextView sun;
    Toolbar toolbarTop;
    ImageView backArrow;

    private CreateAlarmViewModel createAlarmViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_alarm);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbarTop = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_update);
        backArrow = (ImageView) findViewById(R.id.backToolbar);
        backArrow.setOnClickListener(v->{
            finish();
        });

        createAlarmViewModel= new ViewModelProvider(this).get(CreateAlarmViewModel.class);
        timeText = findViewById(R.id.updatetimetext);
        dateT = findViewById(R.id.updatedatetext);
        title = findViewById(R.id.updatetitletext);
        updateAlarm = findViewById(R.id.updateButton);
        spinner = findViewById(R.id.updatecategoryspinner);
        mon =(CheckedTextView) findViewById(R.id.updatemonday);
        tue =(CheckedTextView) findViewById(R.id.updatetuesday);
        wed =(CheckedTextView) findViewById(R.id.updatewedday);
        thu =(CheckedTextView) findViewById(R.id.updatethuday);
        fri =(CheckedTextView) findViewById(R.id.updatefriday);
        sat = (CheckedTextView) findViewById(R.id.updatesaturday);
        sun =(CheckedTextView) findViewById(R.id.updatesunday);
        checkboxTextFunctionality();
        Intent intent = getIntent();
        updateID = intent.getIntExtra("IdOfAlar",0);
        hourUpdate = intent.getIntExtra("hour",0);
        minuteUpdate = intent.getIntExtra("minute",0);
        String titleUpdate = intent.getStringExtra("Title");
        String cateUpdate = intent.getStringExtra("Category");

        String dateUpdate = intent.getStringExtra("Date");
        Boolean s = intent.getBooleanExtra("s",false);
        Boolean su = intent.getBooleanExtra("su",false);
        Boolean m = intent.getBooleanExtra("m",false);
        Boolean t = intent.getBooleanExtra("t",false);
        Boolean w = intent.getBooleanExtra("w",false);
        Boolean th = intent.getBooleanExtra("th",false);
        Boolean f = intent.getBooleanExtra("f",false);
        Boolean rec = intent.getBooleanExtra("rec",false);
        if(rec){
            if(s){
                sat.setChecked(true);
            }
            if(su){
                sun.setChecked(true);
            }if(m){
                mon.setChecked(true);
            }if(t){
                tue.setChecked(true);
            }if(w){
                wed.setChecked(true);
            }if(th){
                thu.setChecked(true);
            }if(f){
                fri.setChecked(true);
            }
        }

        title.setText(titleUpdate);
        //TODO spinner.set
        String alarmText = String.format("%02d:%02d", hourUpdate,minuteUpdate);

        //starting
        Date datet = new Date();
        String smartDateTimet = alarmText;
        SimpleDateFormat inputFormatt = new SimpleDateFormat("HH:mm");
        SimpleDateFormat outt = new SimpleDateFormat("h:mm a");
        outt.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));
        try {
            datet = inputFormatt.parse(smartDateTimet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputt = outt.format(datet);
        timeText.setText(outputt);
        //ending



        dateT.setText(dateUpdate);

        //

        dateT.setOnClickListener(v->{
            dateSelection();
        });

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                category);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
        //this is for selecting spinner items based on users input
        if(cateUpdate.equals("Sleeping Time")){
            spinner.setSelection(0);
        } else if(cateUpdate.equals("Workout Time")){
            spinner.setSelection(1);
        } else if(cateUpdate.equals("Wake Up time")){
            spinner.setSelection(2);
        }else if(cateUpdate.equals("Study Time")){
            spinner.setSelection(3);
        }else if(cateUpdate.equals("Android Time")){
            spinner.setSelection(4);
        }else if(cateUpdate.equals("Food Time")){
            spinner.setSelection(5);
        }
        //TODO get spinner string

        timeText.setOnClickListener(v->{
            timeSetUp();
        });



        updateAlarm.setOnClickListener(v->{
            //will have to cancel the pending intent of this particular alarm
            //category text
            boolean recurringOption=false;
            if(!mon.isChecked() && !tue.isChecked() && !wed.isChecked() && !thu.isChecked() && !fri.isChecked() && !sat.isChecked() && !sun.isChecked()){
                recurringOption =false;
            }
            else{
                recurringOption = true;
            }

           Alarm alarm = new Alarm(
                   updateID,
                   hourUpdate,
                   minuteUpdate,
                   title.getText().toString(),
                   System.currentTimeMillis(),
                   true,
                   recurringOption,
                   mon.isChecked(),
                   tue.isChecked(),
                   wed.isChecked(),
                   thu.isChecked(),
                   fri.isChecked(),
                   sat.isChecked(),
                   sun.isChecked(),
                   dateT.getText().toString().trim(),
                   categoryText
            );
             new Alarm(this);
            createAlarmViewModel.update(alarm);
            alarm.schedule(this);
            finish();
            Toast.makeText(this, "Alarm Updated", Toast.LENGTH_SHORT).show();
        });
    }

    //Time setup
    private void timeSetUp(){



        // Launch Time Picker Dialog
        int pickerHour = hourUpdate;
        int pickerMinute = minuteUpdate;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        //hourUpdate,minuteUpdate
                        hourUpdate = hourOfDay;
                        minuteUpdate = minute;
                        // timeText.setText(" "+hourOfDay + ":" + minute);//May 22, 2012 8:52 PM

                        String alarmText = String.format("%02d:%02d", hourUpdate, minuteUpdate);

                        //starting
                        Date datet = new Date();
                        String smartDateTimet = alarmText;
                        SimpleDateFormat inputFormatt = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat outt = new SimpleDateFormat("h:mm a");
                        outt.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));
                        try {
                            datet = inputFormatt.parse(smartDateTimet);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String outputt = outt.format(datet);
                        //ending

                        timeText.setText(outputt);



                    }
                }, pickerHour,pickerMinute, false);
        timePickerDialog.show();
    }




    private void dateSelection() {

        //fetching data on date text string to int block start here
        String pattern = "MMM, dd, yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateT.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        int yearFromUser = now.get(Calendar.YEAR);
        int monthFromUser = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int dayFromUser = now.get(Calendar.DAY_OF_MONTH);

        //fetching data on date text string to int block end here



        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    String dayName;

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //getting the name fo week from the date picker
                        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date myDate = inFormat.parse(dayOfMonth+"-"+monthOfYear+"-"+year);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE");
                            dayName=simpleDateFormat.format(myDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //month start form zero so ill have to add one with each monthOdYear
                        dateT.setText(""+getMonthFormat(monthOfYear+1)+", "+dayOfMonth+", "+year);
                        //May 22, 2012 8:52 PM
                    }

                }, yearFromUser, monthFromUser, dayFromUser);
        datePickerDialog.show();
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "Jan";
        if(month == 2)
            return "Feb";
        if(month == 3)
            return "Mar";
        if(month == 4)
            return "Apr";
        if(month == 5)
            return "May";
        if(month == 6)
            return "Jun";
        if(month == 7)
            return "Jul";
        if(month == 8)
            return "Aug";
        if(month == 9)
            return "Sep";
        if(month == 10)
            return "Oct";
        if(month == 11)
            return "Nov";
        if(month == 12)
            return "Dec";

        //default should never happen
        return "Jan";
    }

    private void checkboxTextFunctionality() {

        //saturday
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean isChecked = sat.isChecked();
                if(isChecked)
                    sat.setChecked(false);
                else
                    sat.setChecked(true);
            }
        });
        //Sunday
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean isChecked = sun.isChecked();
                if(isChecked)
                    sun.setChecked(false);
                else
                    sun.setChecked(true);
            }
        });
        //monday
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean isChecked = mon.isChecked();
                if(isChecked)
                    mon.setChecked(false);
                else
                    mon.setChecked(true);
            }
        });
        //TuesDay
        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean isChecked = tue.isChecked();
                if(isChecked)
                    tue.setChecked(false);
                else
                    tue.setChecked(true);
            }
        });
        //Wednesday
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean isChecked = wed.isChecked();
                if(isChecked)
                    wed.setChecked(false);
                else
                    wed.setChecked(true);
            }
        });
        //Thursday
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean isChecked = thu.isChecked();
                if(isChecked)
                    thu.setChecked(false);
                else
                    thu.setChecked(true);
            }
        });
        //Friday
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean isChecked = fri.isChecked();
                if(isChecked)
                    fri.setChecked(false);
                else
                    fri.setChecked(true);
            }
        });
    }
    //spinner items implementation
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        categoryText= category[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}