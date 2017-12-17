package com.example.mursyidah.educationfirebase.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.mursyidah.educationfirebase.Model.PictureModel;
import com.example.mursyidah.educationfirebase.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class videoRecycler extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView view;
    Uri uri;

    private ArrayList<String> mKeys;

    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_recycler);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Video");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list_video);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mKeys = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.videoList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter <PictureModel, VideoHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<PictureModel, VideoHolder> (
                        PictureModel.class, R.layout.viewholder_video, VideoHolder.class, databaseReference) {


                    @Override
                    protected void populateViewHolder(VideoHolder viewHolder, PictureModel model, final int position) {

                        final String post_keys = getRef(position).getKey();

                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setImage(getApplicationContext(), model.getImage());



                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(videoRecycler.this, DisplayVideo.class);
                                intent.putExtra("id", post_keys);
                                startActivity(intent);
                            }
                        });
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class VideoHolder extends RecyclerView.ViewHolder{

        View view;

        public VideoHolder(View itemView){
            super(itemView);

            view = itemView;
        }
        public void setTitle (String title){
            TextView title_view = (TextView) view.findViewById(R.id.V_title_view);
            title_view.setText(title);
        }

        public void setDesc (String description){
            TextView desc_view = (TextView) view.findViewById(R.id.V_desc_view);
            desc_view.setText(description);
        }

        public void setImage (Context ctx, String Image) {


            VideoView videoView = (VideoView) view.findViewById(R.id.video_view);
          //  Uri uri = Uri.parse("audio");
            MediaController mc = new MediaController(view.getContext());
            //  videoView.setVideoURI(uri);
         //   videoView.setVideoURI(uri);
            videoView.seekTo(100);
          //  mc.setAnchorView(videoView);
          //  videoView.setMediaController(mc);
            videoView.requestFocus();
         //   videoView.start();



        }}


    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()== R.id.addButton){
            startActivity(new Intent(videoRecycler.this, VideoActivity.class));
        }
        else if (item.getItemId()== R.id.logOut){
            Intent intent = new Intent(videoRecycler.this, LoginActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

}
