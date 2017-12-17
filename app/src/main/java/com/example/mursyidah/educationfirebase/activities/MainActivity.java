package com.example.mursyidah.educationfirebase.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mursyidah.educationfirebase.Model.PictureModel;
import com.example.mursyidah.educationfirebase.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView view;



    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Education");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_senarai);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView)findViewById(R.id.myList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter <PictureModel, PictureHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<PictureModel, PictureHolder> (
                        PictureModel.class, R.layout.viewholder_picture, PictureHolder.class, databaseReference) {


                    @Override
                    protected void populateViewHolder(PictureHolder viewHolder, PictureModel model, final int position) {

                        final String post_key = getRef(position).getKey();

                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setImage(getApplicationContext(), model.getImage());



                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MainActivity.this, DisplayData.class);
                                intent.putExtra("id", post_key);
                                startActivity(intent);
                            }
                        });
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PictureHolder extends RecyclerView.ViewHolder{

        View view;

        public PictureHolder(View itemView){
            super(itemView);

           view = itemView;
        }
        public void setTitle (String title){
            TextView title_view = (TextView) view.findViewById(R.id.title_view);
            title_view.setText(title);
        }

        public void setDesc (String description){
            TextView desc_view = (TextView) view.findViewById(R.id.desc_view);
            desc_view.setText(description);
        }

        public void setImage (Context ctx, String image){
            ImageView imageView = (ImageView) view.findViewById(R.id.picture_view);
            Picasso.with(ctx).load(image).into(imageView);

        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()== R.id.addButton){
            startActivity(new Intent(MainActivity.this, InsertActivity.class));
        }
        else if (item.getItemId()== R.id.logOut){
          Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }


}
