package org.twintechsoft.simplealarmwithgit.alarmslist;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import org.twintechsoft.simplealarmwithgit.R;
import org.twintechsoft.simplealarmwithgit.activities.MainActivity;
import org.twintechsoft.simplealarmwithgit.activities.UpdateAlarm;
import org.twintechsoft.simplealarmwithgit.createalarm.CreateAlarmViewModel;
import org.twintechsoft.simplealarmwithgit.data.Alarm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private TextView alarmTime,alarmDate;
    private ImageView alarmRecurring;
    private TextView alarmRecurringDays;
    private TextView alarmTitle, alarmCategory;
    int alarmID,hour,minute;
    String title, category, date;
    Boolean s,su,m,t,w,th,f,rec;
    Switch alarmStarted;
    private OnToggleAlarmListener listener;

    //Context context;
    Context context;
    boolean cancellationIsCalled = false;


    public AlarmViewHolder(@NonNull View itemView, OnToggleAlarmListener listener) {

        super(itemView);
        alarmDate = itemView.findViewById(R.id.item_alarm_date);
        alarmTime = itemView.findViewById(R.id.item_alarm_time);
        alarmStarted = itemView.findViewById(R.id.item_alarm_started);
        alarmRecurring = itemView.findViewById(R.id.item_alarm_recurring);
        alarmRecurringDays = itemView.findViewById(R.id.item_alarm_recurringDays);
        alarmTitle = itemView.findViewById(R.id.item_alarm_title);
        alarmCategory = itemView.findViewById(R.id.item_alarm_category);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context= itemView.getContext();

                Intent intent = new Intent(context, UpdateAlarm.class);
                intent.putExtra("IdOfAlar",alarmID);
                intent.putExtra("hour",hour);
                intent.putExtra("minute",minute);
                intent.putExtra("Title",title);
                intent.putExtra("Category",category);
                intent.putExtra("Date",date);
                intent.putExtra("s",s);
                intent.putExtra("su",su);
                intent.putExtra("m",m);
                intent.putExtra("t",t);
                intent.putExtra("w",w);
                intent.putExtra("th",th);
                intent.putExtra("f",f);
                intent.putExtra("rec",rec);
                context.startActivity(intent);

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context= itemView.getContext();

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.delete_items);
                Button btPositive = dialog.findViewById(R.id.btn_yes);
                Button btNegative = dialog.findViewById(R.id.btn_no);


                btPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlarmsListViewModel alarmsListViewModel  = new ViewModelProvider((ViewModelStoreOwner) context).get(AlarmsListViewModel.class);
                        alarmsListViewModel.delete(alarmID);
                        Alarm c = new Alarm(context);
                        dialog.dismiss();

                        Toast.makeText(context, "Alarm Deleted Successfully", Toast.LENGTH_SHORT).show();

                    }
                });

                btNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                return false;
            }
        });

        this.listener = listener;
    }


    public void bind(Alarm alarm) {
        //greedy approach for sending alarm data on the alarmUpdate activity
        alarmID = alarm.getAlarmId();
        hour = alarm.getHour();
        minute = alarm.getMinute();
        title = alarm.getTitle();
        category = alarm.getCategoryText();
        date = alarm.getDateText();
        s = alarm.isSaturday();
        su = alarm.isSunday();
        m = alarm.isMonday();
        t = alarm.isTuesday();
        w = alarm.isWednesday();
        th = alarm.isThursday();
        f = alarm.isFriday();
        rec = alarm.isRecurring();


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
        sharedPref.edit().putInt("DELETEWITHID", alarmID).apply();

        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

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
        alarmDate.setText(alarm.getDateText().toString());
        alarmCategory.setText(alarm.getCategoryText());
        alarmTime.setText(outputt);
        alarmStarted.setChecked(alarm.isStarted());

        if (alarm.isRecurring()) {
            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);
            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
            alarmRecurringDays.setText("Once");
        }

        if (alarm.getTitle().length() != 0) {
            alarmTitle.setText(alarm.getTitle());
        } else {
            alarmTitle.setText("Alarm");
        }


        alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onToggle(alarm);
            }
        });

    }
}
