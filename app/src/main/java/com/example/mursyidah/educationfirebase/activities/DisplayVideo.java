package com.example.mursyidah.educationfirebase.activities;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mursyidah.educationfirebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by mursyidah on 14/11/2017.
 */

public class DisplayVideo extends AppCompatActivity {

    private DatabaseReference db;
    private String post_keys = null;
    private VideoView v_view;
    private TextView tVideo;
    private TextView descVideo;
    private Button play, stop;
    private static MediaPlayer mediaPlayer;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_video);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_video);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        post_keys = getIntent().getExtras().getString("id");

        db = FirebaseDatabase.getInstance().getReference().child("Video");


        v_view = (VideoView) findViewById(R.id.v_view);
        tVideo = (TextView) findViewById(R.id.tv_video);
        descVideo = (TextView)findViewById(R.id.ds_video);
        play = (Button) findViewById(R.id.play);

     //   stop =(Button)findViewById(R.id.button3);

        db.child(post_keys).addValueEventListener(new ValueEventListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String title = (String) dataSnapshot.child("title").getValue();
                String description = (String) dataSnapshot.child("desc").getValue();
                String audio_B = (String) dataSnapshot.child("video").getValue();

                tVideo.setText(title);
                descVideo.setText(description);


          //    Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"video");

                Uri uri= Uri.parse(audio_B);
                MediaController mc = new MediaController(DisplayVideo.this);
                v_view.setVideoURI(uri);
                v_view.seekTo(100);
                v_view.setMediaController(mc);
                v_view.requestFocus();

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(v_view.isPlaying()){
                            v_view.pause();
                            play.setText("Play");

                        } else {
                            v_view.start();
                            play.setText("Pause");
                        }
                    }
                });


           //   play = (Button) findViewById(R.id.play);



//                play.setOnClickListener(new View.OnClickListener() {
//
//                    public void onClick(View v) {
//                        if(v_view.isPlaying()){
//                            v_view.pause();
//                        //    play.setBackgroundResource(R.drawable.play_button);
//
//                        } else {
//                           v_view.start();
//                        //    play.setBackgroundResource(R.drawable.stop_button);
//                        }
//                    }
//                });




//                MediaPlayer mediaPlayer = new MediaPlayer();
//
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//                try {
//                    mediaPlayer.setDataSource(audio_B);
//                    mediaPlayer.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                mediaPlayer.start();


            }





            @Override
            public void onCancelled(DatabaseError databaseError) {
                //  db.addValueEventListener(null);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.delete_btn);

        if(post_keys == null) {
            item.setEnabled(false);
            item.setVisible(false);
        } else {
            item.setEnabled(true);
            item.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()== R.id.delete_btn){
            if(!post_keys.isEmpty()) {
                db.child(post_keys).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        actionNotification(databaseError, R.string.done_deleted);
                    }
                });
            }

        }
//        else if(item.getItemId()==R.id.edit){
//
//        }
        return super.onOptionsItemSelected(item);
    }

    private void actionNotification(DatabaseError error, int successResourceId) {
        // close activity
        if(error == null) {
            Toast.makeText(DisplayVideo.this, successResourceId, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(DisplayVideo.this, error.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

}
