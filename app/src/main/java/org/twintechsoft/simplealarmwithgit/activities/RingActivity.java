package org.twintechsoft.simplealarmwithgit.activities;

import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.CATEGORY;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.DATE;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.TITLE;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.twintechsoft.simplealarmwithgit.R;
import org.twintechsoft.simplealarmwithgit.data.Alarm;
import org.twintechsoft.simplealarmwithgit.service.AlarmService;

import java.util.Calendar;
import java.util.Random;


public class RingActivity extends AppCompatActivity {
    private ImageButton dismiss;
    private Button snooze;
    //private ImageView clock;
    private TextView textView1, textView2;
//intent.getStringExtra(CATEGORY)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ring);
        dismiss = findViewById(R.id.activity_ring_dismiss);
        snooze = findViewById(R.id.activity_ring_snooze);
        //clock = findViewById(R.id.activity_ring_clock);
        textView1 = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        Calendar tem = Calendar.getInstance();
        int currentYear = tem.get(Calendar.YEAR);
        int currentMonth = tem.get(Calendar.MONTH)+1;
        int currentDay = tem.get(Calendar.DAY_OF_MONTH);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String titleText = sharedPreferences.getString(TITLE,"");
        String categoryText = sharedPreferences.getString(CATEGORY,"");
        String dateText = sharedPreferences.getString(DATE,""+currentMonth+", "+currentDay+", "+currentYear);

        //ButterKnife.bind(this);

        textView1.setText(categoryText);
        if(titleText.isEmpty()){
            textView2.setText(dateText);
        }
        else{
            textView2.setText(titleText);
        }


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);
                finish();
            }
        });

        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MINUTE, 10);

                Alarm alarm = new Alarm(
                        new Random().nextInt(Integer.MAX_VALUE),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        "Snooze",
                        System.currentTimeMillis(),
                        true,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        dateText,
                        categoryText

                        //TODO need to be exact on snooze selection
                );

                alarm.schedule(getApplicationContext());

                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);
                finish();
            }
        });

//        animateClock();
   }

//    private void animateClock() {
//        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(clock, "rotation", 0f, 20f, 0f, -20f, 0f);
//        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
//        rotateAnimation.setDuration(800);
//        rotateAnimation.start();
//    }
}
