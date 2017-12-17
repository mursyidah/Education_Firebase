package com.example.mursyidah.educationfirebase.activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mursyidah.educationfirebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by mursyidah on 14/11/2017.
 */

public class DisplayData extends AppCompatActivity {

    private DatabaseReference db;
    private String post_key = null;
    private ImageView i_view;
    private TextView t_view;
    private TextView desc_view;
    private Button audio, button3;
    private static MediaPlayer mediaPlayer;


    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_data);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        post_key = getIntent().getExtras().getString("id");

        db = FirebaseDatabase.getInstance().getReference().child("Education");


        i_view = (ImageView) findViewById(R.id.i_view);
        t_view = (TextView) findViewById(R.id.t_view);
        desc_view = (TextView)findViewById(R.id.ds_view);
        audio = (Button)findViewById(R.id.audio);
        button3 =(Button)findViewById(R.id.button3);

        db.child(post_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                String title = (String) dataSnapshot.child("title").getValue();
                String description = (String) dataSnapshot.child("desc").getValue();
                String image = (String) dataSnapshot.child("image").getValue();
                final String audio_B = (String) dataSnapshot.child("audio").getValue();

                t_view.setText(title);
                desc_view.setText(description);
                Picasso.with(DisplayData.this).load(image).into(i_view);

//              Uri.parse(audio_B);


                final MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {
                    mediaPlayer.setDataSource(audio_B);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                            audio.setText("Play");

                        } else {
                           mediaPlayer.start();
                            audio.setText("Pause");
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
//
        });
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        MenuItem item = menu.findItem(R.id.delete_btn);
//
//        if(post_key == null) {
//            item.setEnabled(false);
//            item.setVisible(false);
//        } else {
//            item.setEnabled(true);
//            item.setVisible(true);
//        }
//
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.delete_btn){
            if(!post_key.isEmpty()) {
                db.child(post_key).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        actionNotification(databaseError, R.string.done_deleted);
                    }
                });

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void actionNotification(DatabaseError error, int successResourceId) {
        // close activity
        if(error == null) {
            Toast.makeText(DisplayData.this, successResourceId, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(DisplayData.this, error.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

}
