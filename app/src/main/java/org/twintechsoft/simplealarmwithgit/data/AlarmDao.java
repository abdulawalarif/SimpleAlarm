package org.twintechsoft.simplealarmwithgit.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert
    void insert(Alarm alarm);

    @Query("DELETE FROM alarm_table")
    void deleteAll();

    @Query("DELETE FROM alarm_table WHERE alarmId=:alarmId")
    void deleteAlarm(int alarmId);

    @Query("SELECT * FROM alarm_table ORDER BY created ASC")
    LiveData<List<Alarm>> getAlarms();


    @Update
    void update(Alarm alarm);

}
