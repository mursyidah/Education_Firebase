package com.example.mursyidah.educationfirebase;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by mursyidah on 12/11/2017.
 */

public class InsertActivity extends AppCompatActivity{

    private ImageButton imageButton;
    private EditText titleText;
    private EditText dsText;
    private Button submit;
    private Uri uri;
    private Uri imageUri;
    private StorageReference ref;
    private ProgressDialog mProgress;
    DatabaseReference mDatabase;


    private static final int GALLERY_REQUEST = 2;
    private static final int CAMERA_REQUEST_CODE = 1;

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

    }

    private void startPosting(){

        mProgress.setTitle("Simpan Cerita...");
        mProgress.show();

        final String title_v = titleText.getText().toString().trim();
        final String desc_v = dsText.getText().toString().trim();

        if(TextUtils.isEmpty(title_v)&& !TextUtils.isEmpty(desc_v)&& uri!=null){
            final StorageReference filePath = ref.child("Education").child(uri.getLastPathSegment());


            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    mProgress.dismiss();

                   @SuppressWarnings("VisibleForTests") Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();

                    newPost.child("tittle").setValue(title_v);
                    newPost.child("desc").setValue(desc_v);
                    newPost.child("image").setValue(downloadUrl.toString());

                }
            });

        }
    }

    private void SelectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InsertActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                   startActivityForResult(intent,CAMERA_REQUEST_CODE);


                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_REQUEST);


                } else if (options[item].equals("Cancel")) {
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
                uri = data.getData();
                imageButton.setImageURI(uri);


            } else if (requestCode == 2) {
                uri = data.getData();
                imageButton.setImageURI(uri);


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
