package org.twintechsoft.simplealarmwithgit.data;


import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.DATE;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.FRIDAY;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.MONDAY;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.RECURRING;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.SATURDAY;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.SUNDAY;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.THURSDAY;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.TITLE;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.CATEGORY;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.TUESDAY;
import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.WEDNESDAY;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver;
import org.twintechsoft.simplealarmwithgit.createalarm.DayUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Entity(tableName = "alarm_table")
public class Alarm {
    @PrimaryKey
    @NonNull
    private int alarmId;

    private int hour, minute;
    private boolean started, recurring;
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private String title;
    private String dateText;
    private String categoryText;

    private long created;

    public Alarm(int alarmId, int hour, int minute, String title, long created,
                 boolean started, boolean recurring, boolean monday, boolean tuesday,
                 boolean wednesday, boolean thursday, boolean friday, boolean saturday,
                 boolean sunday,String dateText, String categoryText) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.started = started;

        this.recurring = recurring;

        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;

        this.title = title;

        this.created = created;
        this.dateText = dateText;
        this.categoryText = categoryText;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isStarted() {
        return started;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void schedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(RECURRING, recurring);
        intent.putExtra(MONDAY, monday);
        intent.putExtra(TUESDAY, tuesday);
        intent.putExtra(WEDNESDAY, wednesday);
        intent.putExtra(THURSDAY, thursday);
        intent.putExtra(FRIDAY, friday);
        intent.putExtra(SATURDAY, saturday);
        intent.putExtra(SUNDAY, sunday);

        intent.putExtra(TITLE, title);
        intent.putExtra(CATEGORY, categoryText);
        intent.putExtra(DATE, dateText);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (!recurring) {
            String toastText = null;
            try {
                toastText = String.format("One Time Alarm %s scheduled for %s at %02d:%02d", title, DayUtil.toDay(calendar.get(Calendar.DAY_OF_WEEK)), hour, minute, alarmId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
            //if()TODO this the block for setup time and date wise alarm begaining

            //fetching data on date text string to int block start here
            String pattern = "MMM, dd, yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = null;
            try {
                date = simpleDateFormat.parse(dateText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            int yearFromUser = now.get(Calendar.YEAR);
            int monthFromUser = now.get(Calendar.MONTH) + 1; // Note: zero based!
            int dayFromUser = now.get(Calendar.DAY_OF_MONTH);

            //fetching data on date text string to int block end here


            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTimeInMillis(System.currentTimeMillis());
            calendarDate.set(Calendar.YEAR, yearFromUser);
            calendarDate.set(Calendar.MONTH, monthFromUser);
            calendarDate.set(Calendar.DAY_OF_MONTH, dayFromUser);
            calendarDate.set(Calendar.HOUR_OF_DAY, hour);
            calendarDate.set(Calendar.MINUTE, minute);
            calendarDate.set(Calendar.SECOND, 0);
            calendarDate.set(Calendar.MILLISECOND, 0);

            final Calendar newDate = Calendar.getInstance();
            newDate.set(yearFromUser,monthFromUser,dayFromUser);
            Calendar tem = Calendar.getInstance();
            int currentYear = tem.get(Calendar.YEAR);
            int currentMonth = tem.get(Calendar.MONTH)+1;
            int currentDay = tem.get(Calendar.DAY_OF_MONTH);
            tem.set(currentYear,currentMonth ,currentDay);

    //This block is for calculation only the date based on user input and if they choose a date then it will ring on that day only
            if(newDate.getTimeInMillis()-tem.getTimeInMillis()>0){
                Toast.makeText(context,"Your Alarm will ring once"+"\n"+dateText,Toast.LENGTH_LONG).show();
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendarDate.getTimeInMillis(),
                        alarmPendingIntent
                );
            }
            else{
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        alarmPendingIntent
                );
                Toast.makeText(context,"Your Alarm will ring once",Toast.LENGTH_LONG).show();
            }

            //TODO this the block for setup time and date wise alarm ending



        } else {
            String toastText = String.format("Recurring Alarm %s scheduled for %s at %02d:%02d", title, getRecurringDaysText(), hour, minute, alarmId);
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            final long RUN_DAILY = 24 * 60 * 60 * 1000;
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    RUN_DAILY,
                    alarmPendingIntent
            );
        }

        this.started = true;
    }
    //this constructor is for canceling alarm fo single items from the ViewHolder class
    public Alarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        this.started = false;

    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        this.started = false;

        String toastText = String.format("Alarm cancelled for %02d:%02d", hour, minute);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        Log.i("cancel", toastText);
    }

    public String getRecurringDaysText() {
        if (!recurring) {
            return null;
        }

        String days = "";
        if (monday) {
            days += "Mo ";
        }
        if (tuesday) {
            days += "Tu ";
        }
        if (wednesday) {
            days += "We ";
        }
        if (thursday) {
            days += "Th ";
        }
        if (friday) {
            days += "Fr ";
        }
        if (saturday) {
            days += "Sa ";
        }
        if (sunday) {
            days += "Su ";
        }

        return days;
    }

    public String getTitle() {
        return title;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public String getDateText() {
        return dateText;
    }


    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
