package org.twintechsoft.simplealarmwithgit.createalarm;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.twintechsoft.simplealarmwithgit.data.Alarm;
import org.twintechsoft.simplealarmwithgit.data.AlarmRepository;


public class CreateAlarmViewModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;

    public CreateAlarmViewModel(@NonNull Application application) {
        super(application);

        alarmRepository = new AlarmRepository(application);
    }

    public void insert(Alarm alarm) {
        alarmRepository.insert(alarm);
    }

    public void delete(int id){alarmRepository.delete(id);}
    public  void update(Alarm alarm){alarmRepository.update(alarm);}
}
