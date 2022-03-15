package org.twintechsoft.simplealarmwithgit.createalarm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import org.twintechsoft.simplealarmwithgit.R;
import org.twintechsoft.simplealarmwithgit.data.Alarm;
import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class CreateAlarmFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    //Spinner item
    String categoryText;//spinner items selection fetching
    String[] category = { "Sleeping Time", "Workout Time",
            "Wake Up time", "Study Time",
            "Android Time", "Food Time" };

    Spinner spinner;
    EditText title;
    TextView timeText,dateT;
    int hour, minutes;
    Button scheduleAlarm;
    //CheckedTextView sa,su,mo,tu,we,th,fr;
    CheckedTextView mon;
    CheckedTextView tue;
    CheckedTextView wed;
    CheckedTextView thu;
    CheckedTextView fri;
    CheckedTextView sat;
    CheckedTextView sun;

    private CreateAlarmViewModel createAlarmViewModel;
    Toolbar toolbar;
    ImageView backArrow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createAlarmViewModel= new ViewModelProvider(requireActivity(),getDefaultViewModelProviderFactory()).get(CreateAlarmViewModel.class);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createalarm, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.toolbar);
        backArrow = (ImageView) view.findViewById(R.id.image_backArrow);
        backArrow.setOnClickListener(v->{
            getActivity().onBackPressed();
        });

        timeText = view.findViewById(R.id.timetext);
        dateT = view.findViewById(R.id.datetext);
        title = view.findViewById(R.id.titletext);
        scheduleAlarm = view.findViewById(R.id.createButton);
        spinner = view.findViewById(R.id.categoryspinner);
        mon =(CheckedTextView) view.findViewById(R.id.monday);
        tue =(CheckedTextView) view.findViewById(R.id.tuesday);
        wed =(CheckedTextView) view.findViewById(R.id.wedday);
        thu =(CheckedTextView) view.findViewById(R.id.thuday);
        fri =(CheckedTextView) view.findViewById(R.id.friday);
        sat = (CheckedTextView)view.findViewById(R.id.saturday);
        sun =(CheckedTextView) view.findViewById(R.id.sunday);
        checkboxTextFunctionality();
        //setup time and date for alarm on on creation of fragment
        long datee = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM, dd, yyyy");
        String dateString = sdf.format(datee);
        dateT.setText(dateString);
        SimpleDateFormat timeOnCreate = new SimpleDateFormat("h:mm a");
        String timeOnCreat = timeOnCreate.format(datee);
        timeText.setText(timeOnCreat);

        dateT.setOnClickListener(v->{
            dateSelection();
        });

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter ad
                = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,
                category);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner.setAdapter(ad);


        timeText.setOnClickListener(v->{
            timeSetUp();
        });

        scheduleAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleAlarm();
                Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });

        return view;
    }



    private void dateSelection() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
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

                }, mYear, mMonth, mDay);
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


    private void scheduleAlarm() {
        boolean recurringOption=false;
        if(!mon.isChecked() && !tue.isChecked() && !wed.isChecked() && !thu.isChecked() && !fri.isChecked() && !sat.isChecked() && !sun.isChecked()){
            recurringOption =false;
        }
        else{
            recurringOption = true;
        }
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                hour,
                minutes,
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

        createAlarmViewModel.insert(alarm);

        alarm.schedule(getContext());
    }

    //Time setup
    private void timeSetUp(){

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        hour = mHour;
        minutes = mMinute;

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        hour = hourOfDay;
                        minutes = minute;
                       // timeText.setText(" "+hourOfDay + ":" + minute);//May 22, 2012 8:52 PM





                        String alarmText = String.format("%02d:%02d", hourOfDay, minute);

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
                }, mHour, mMinute, false);
        timePickerDialog.show();
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
