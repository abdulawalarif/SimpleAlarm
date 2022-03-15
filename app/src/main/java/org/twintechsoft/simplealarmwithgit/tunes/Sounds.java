package org.twintechsoft.simplealarmwithgit.tunes;


import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.twintechsoft.simplealarmwithgit.R;

public class Sounds extends AppCompatActivity {
    private ListView listView;
    private TrackModel[] trackModels;
    private TrackAdapter adapter;
    private MediaPlayer mediaPlayer;
    Toolbar toolbarTop;
    ImageView backArrow;
    private long ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sounds);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        backArrow = (ImageView) findViewById(R.id.imageOnToolbar);
       //setSupportActionBar(toolbarTop);
       //getSupportActionBar().setDisplayShowTitleEnabled(false);
        backArrow.setOnClickListener(v->{

//            Intent intent = new Intent(Sounds.this, MainActivity.class);
//            intent.putExtra("ids",(long)ids );
//            startActivity(intent);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString("key", String.valueOf(ids)).apply();

            finish();
        });

        listView = findViewById(R.id.list_item);
        loadTracks();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadTracks();
                ids = id;
                TrackModel track = trackModels[position];

                if(mediaPlayer!=null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        track.setPlaying(false);
                    }
                }
                try{
                    mediaPlayer = MediaPlayer.create(Sounds.this, track.getId());
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        track.setPlaying(false);
                    }
                    else{
                        mediaPlayer.start();
                        track.setPlaying(true);
                    }
                }
                catch (Exception e){
                    Log.e("Exception ", e.getMessage());
                }
            }
        });

    }
    public void loadTracks(){
        trackModels = new TrackModel[]{
                new TrackModel(R.raw.sound_one, "First One", false),
                new TrackModel(R.raw.sound_two, "Second One", false),
                new TrackModel(R.raw.sound_three, "Third One", false),
                new TrackModel(R.raw.sound_four, "Fourth One", false),
                new TrackModel(R.raw.sound_five, "Fifth One", false),
                new TrackModel(R.raw.sound_six, "Sixth One", false)
        };
        adapter = new TrackAdapter(Sounds.this, trackModels);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        }
    }
}





