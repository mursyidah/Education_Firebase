package com.example.mursyidah.educationfirebase.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mursyidah.educationfirebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * Created by mursyidah on 12/11/2017.
 */

public class InsertActivity extends AppCompatActivity{

    private ImageButton imageButton;
    private EditText titleText;
    private EditText dsText;
    private Button submit;
    private Uri uriImage;
    private Uri UriAudio;
    private StorageReference ref;
    private ProgressDialog mProgress;
    DatabaseReference mDatabase;
    private boolean isRecording = false;
    private static String audioFilePath = null;
    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;
    private ImageButton  playButton, recordButton, stopButton;



    private static final int GALLERY_REQUEST = 2;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int VIDEO_REQUEST = 3;
    private static final String LOG_TAG = "Record_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        ref = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Education");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        titleText = (EditText) findViewById(R.id.titleText);
        dsText = (EditText) findViewById(R.id.editText4);
        submit = (Button)findViewById(R.id.submit);
        playButton = (ImageButton) findViewById(R.id.play);
        recordButton = (ImageButton) findViewById(R.id.record);
        stopButton = (ImageButton) findViewById(R.id.stop);
        mProgress = new ProgressDialog(this);



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });

        if (!hasMicrophone()) {
            stopButton.setEnabled(false);
            playButton.setEnabled(false);
            recordButton.setEnabled(false);
        } else {
            playButton.setEnabled(false);
            stopButton.setEnabled(false);
        }

        audioFilePath =
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/myaudio.3gp";

    }

    ////audio function

    public void recordAudio (View view) throws IOException
    {
        isRecording = true;
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
        recordButton.setEnabled(false);

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    public void stopAudio (View view)
    {

        stopButton.setEnabled(false);
        playButton.setEnabled(true);

        if (isRecording)
        {
            recordButton.setEnabled(false);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        } else {
            mediaPlayer.release();
            mediaPlayer = null;
            recordButton.setEnabled(true);
        }
    }

    public void playAudio (View view) throws IOException
    {
        playButton.setEnabled(false);
        recordButton.setEnabled(false);
        stopButton.setEnabled(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(audioFilePath);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }



    ///save button function

    private void startPosting(){

        mProgress.setTitle("Simpan Cerita...");
        mProgress.show();

       final String title_v = titleText.getText().toString().trim();
       final String desc_v = dsText.getText().toString().trim();


       Uri uriAudio = Uri.fromFile(new File(audioFilePath).getAbsoluteFile());
            final StorageReference filePath = ref.child("Education/image").child(uriImage.getLastPathSegment());
            final StorageReference audioRef = ref.child("Education/audio").child(uriAudio.getLastPathSegment());


                    // on success upload audio
                    audioRef.putFile(uriAudio).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot audioSnapshot) {

                            //upload image
                            filePath.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(final UploadTask.TaskSnapshot imageSnapshot) {

                                    mProgress.dismiss();

                            @SuppressWarnings("VisibleForTests") Uri audioUrl= audioSnapshot.getDownloadUrl();
                            @SuppressWarnings("VisibleForTests") Uri imageUrl= imageSnapshot.getDownloadUrl();


                            DatabaseReference newPost = mDatabase.push();

                            newPost.child("title").setValue(title_v);
                            newPost.child("desc").setValue(desc_v);

                            if(imageUrl != null) {
                                newPost.child("image").setValue(imageUrl.toString());
                            }

                            if(audioUrl != null) {
                                newPost.child("audio").setValue(audioUrl.toString());
                            }

                            ToastMessage("data inserted");
                        }
                    });
                }
            });


            }

    private void ToastMessage(String s)
    {
        Toast.makeText(this, s ,Toast.LENGTH_SHORT).show();
    }
//}
    ////image function

    private void SelectImage() {
        final CharSequence[] options = {"Ambil gambar", "Pilih dari galeri", "Batal"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InsertActivity.this);
        builder.setTitle("Tambah Gambar!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Ambil gambar")) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                   startActivityForResult(intent,CAMERA_REQUEST_CODE);


                } else if (options[item].equals("Pilih dari galeri")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
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
                uriImage = data.getData();
                imageButton.setImageURI(uriImage);


            } else if (requestCode == 2) {
                uriImage = data.getData();
                imageButton.setImageURI(uriImage);

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
            startActivity(new Intent(InsertActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
