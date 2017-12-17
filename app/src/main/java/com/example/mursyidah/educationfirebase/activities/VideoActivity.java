package com.example.mursyidah.educationfirebase.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mursyidah.educationfirebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by mursyidah on 23/11/2017.
 */

public class VideoActivity extends AppCompatActivity {

    private VideoView videoButton;
    private TextView titleVideo, descVideo;
    boolean paused = true;

    private Uri videoUri;
    private Button recordButton, save ;
    private ImageButton playVideo;
    private ProgressDialog mProgress;
    private static final int VIDEO_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private StorageReference storageReference;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_v);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference().child("Video");

        videoButton = (VideoView) findViewById(R.id.video_button);
        titleVideo = (TextView) findViewById(R.id.title_video);
        descVideo = (TextView)findViewById(R.id.desc_video);
        recordButton = (Button) findViewById(R.id.record_button);
        save = (Button) findViewById(R.id.save_video);
        mProgress = new ProgressDialog(this);


        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectVideo();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });

    }

    private void startPosting(){
        mProgress.setTitle("Simpan Cerita...");
        mProgress.show();

        final String title_v = titleVideo.getText().toString().trim();
        final String desc_v = descVideo.getText().toString().trim();

        // if(!TextUtils.isEmpty(title_v) && !TextUtils.isEmpty(desc_v)&& uri!=null){



        final StorageReference video = storageReference.child("Video/video").child(videoUri.getLastPathSegment());

       video.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot videoSnapshot) {



                        mProgress.dismiss();

                        @SuppressWarnings("VisibleForTests") Uri videoUrl= videoSnapshot.getDownloadUrl();

                        DatabaseReference newPost = db.push();

                        newPost.child("title").setValue(title_v);
                        newPost.child("desc").setValue(desc_v);

                        if(videoUrl != null) {
                            newPost.child("video").setValue(videoUrl.toString());
                        }

                       ToastMessage("data inserted");
                    }
        });



        }
        private void ToastMessage(String s)
        {
        Toast.makeText(this, s ,Toast.LENGTH_SHORT).show();
        }




private void SelectVideo() {

        final CharSequence[] options = {"Ambil Video", "Pilih dari Galeri", "Batal"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VideoActivity.this);
        builder.setTitle("Tambah Video!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Ambil Video")) {

                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, VIDEO_REQUEST);
                    }

                } else if (options[item].equals("Pilih dari Galeri")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("video/*");
                    startActivityForResult(intent, GALLERY_REQUEST);

                } else if (options[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                videoUri = data.getData();
                videoButton.setVideoURI(videoUri);
                videoButton.seekTo(100);
                playVideo = (ImageButton) findViewById(R.id.play_video);
                playVideo.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        if(videoButton.isPlaying()){
                            videoButton.pause();
                            playVideo.setBackgroundResource(R.drawable.play_button);
                        } else {
                            videoButton.start();
                           playVideo.setImageResource(R.drawable.stop_button);
                        }
                    }
                });

            } else if (requestCode == 2) {
                videoUri = data.getData();
                videoButton.setVideoURI(videoUri);
                videoButton.seekTo(100);
                playVideo = (ImageButton) findViewById(R.id.play_video);



                playVideo.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        if(videoButton.isPlaying()){
                            videoButton.pause();
                            playVideo.setBackgroundResource(R.drawable.play_button);

                        } else {
                            videoButton.start();
                            playVideo.setBackgroundResource(R.drawable.stop_button);
                        }
                    }
                });

                }

                super.onActivityResult(requestCode,resultCode,data);
        }

    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()== R.id.senarai){
            startActivity(new Intent(VideoActivity.this, videoRecycler.class));
        }
        return super.onOptionsItemSelected(item);
    }



        }
