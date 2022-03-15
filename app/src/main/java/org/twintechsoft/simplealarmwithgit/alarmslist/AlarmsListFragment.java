package org.twintechsoft.simplealarmwithgit.alarmslist;

import static org.twintechsoft.simplealarmwithgit.broadcastreceiver.AlarmBroadcastReceiver.TITLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.twintechsoft.simplealarmwithgit.R;
import org.twintechsoft.simplealarmwithgit.activities.MainActivity;
import org.twintechsoft.simplealarmwithgit.activities.RingActivity;
import org.twintechsoft.simplealarmwithgit.data.Alarm;
import org.twintechsoft.simplealarmwithgit.service.AlarmService;
import org.twintechsoft.simplealarmwithgit.tunes.Sounds;

import java.util.List;

import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

public class AlarmsListFragment extends Fragment implements OnToggleAlarmListener {
    private AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;
    private AlarmsListViewModel alarmsListViewModel;
    private RecyclerView alarmsRecyclerView;
    private TextView dummy1, dummy2;
    private Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getting individual item's id
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int idOfIndividulItems = sharedPreferences.getInt("DELETEWITHID",0);

        setupData();

    }

    //Starting block of adding data on recycler
    public void setupData(){
        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this);
        alarmsListViewModel= new ViewModelProvider(requireActivity(),getDefaultViewModelProviderFactory()).get(AlarmsListViewModel.class);
        alarmsListViewModel.getAlarmsLiveData().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                //need to remember i changed it a bit so, furthre error can cause from here
                if (!alarms.isEmpty()) {

                    alarmsRecyclerView.setVisibility(View.VISIBLE);
                    dummy1.setVisibility(View.GONE);
                    dummy2.setVisibility(View.GONE);
                    alarmRecyclerViewAdapter.setAlarms(alarms);
                }
                else if(alarms.isEmpty()){
                    alarmsRecyclerView.setVisibility(View.GONE);
                    dummy1.setVisibility(View.VISIBLE);
                    dummy2.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    //Ending block of adding data on recycler


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listalarms, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar_top);
        //((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

//        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("CHILD");


        //TODO is a working code of shared preference
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        sharedPreferences.edit().putString("key","value").apply();
       //writing it again so that data live become perfect and do not show the boxes that should show when there is no data
        alarmsListViewModel.getAlarmsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                //need to remember i changed it a bit so, furthre error can cause from here
                if (!alarms.isEmpty()) {

                    alarmsRecyclerView.setVisibility(View.VISIBLE);
                    dummy1.setVisibility(View.GONE);
                    dummy2.setVisibility(View.GONE);
                    alarmRecyclerViewAdapter.setAlarms(alarms);
                }
                else if(alarms.isEmpty()){
                    alarmsRecyclerView.setVisibility(View.GONE);
                    dummy1.setVisibility(View.VISIBLE);
                    dummy2.setVisibility(View.VISIBLE);
                }
            }
        });

        alarmsRecyclerView = view.findViewById(R.id.fragment_listalarms_recylerView);
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsRecyclerView.setAdapter(alarmRecyclerViewAdapter);
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        dummy1 = view.findViewById(R.id.empty2);
        dummy2 = view.findViewById(R.id.empty1);
        ImageView setIcon = mToolbar.findViewById(R.id.imageOnToolbar);
        setIcon.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), Sounds.class);
            startActivity(intent);
        });
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment);
            }
        });

        return view;
    }

    @Override
    public void onToggle(Alarm alarm) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(getContext());
            alarmsListViewModel.update(alarm);
        } else {
            alarm.schedule(getContext());
            alarmsListViewModel.update(alarm);
        }
    }
}