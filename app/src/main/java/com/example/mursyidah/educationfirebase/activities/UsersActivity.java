package com.example.mursyidah.educationfirebase.activities;

/**
 * Created by mursyidah on 15/11/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.mursyidah.educationfirebase.R;


/**
 * Created by mursyidah on 11/10/2017.
 */

public class UsersActivity extends AppCompatActivity {



    ImageButton  button2, button4;
    public void init() {

        button2 = (ImageButton) findViewById(R.id.imageButton2);
        button4 = (ImageButton) findViewById(R.id.imageButton3);

        button2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View view)
            {
                Intent i = new Intent(getApplicationContext(), ChoosePicture.class);
                startActivity(i);
            }
        });
        button4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v)
            {
                Intent b = new Intent(UsersActivity.this, ChooseVideo.class);
                startActivity(b);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


        init();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()== R.id.logOut){
            finish();
            startActivity(new Intent(UsersActivity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}