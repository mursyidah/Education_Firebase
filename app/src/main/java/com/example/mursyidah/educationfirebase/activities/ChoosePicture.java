package com.example.mursyidah.educationfirebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mursyidah.educationfirebase.R;

/**
 * Created by mursyidah on 03/12/2017.
 */

public class ChoosePicture extends AppCompatActivity {
    Button uploadImage, ImageList;
    public void init() {

        uploadImage = (Button) findViewById(R.id.upload_image);
        ImageList = (Button) findViewById(R.id.image_list);

       uploadImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View view)
            {
                Intent i = new Intent(getApplicationContext(), InsertActivity.class);
                startActivity(i);
            }
        });
        ImageList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v)
            {
                Intent b = new Intent(ChoosePicture.this, MainActivity.class);
                startActivity(b);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_picture);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_picture);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()== R.id.home){
            startActivity(new Intent(ChoosePicture.this,UsersActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}