package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<SliderItems> sliderItems = new ArrayList<>();

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> newsLink = new ArrayList<>();
    ArrayList<String> heads = new ArrayList<>();

    DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VerticalViewPager verticalViewPager =  (VerticalViewPager) findViewById(R.id.vertialViewPager);



        mRef = FirebaseDatabase.getInstance().getReference("News");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    titles.add(ds.child("title").getValue(String.class));
                    desc.add(ds.child("desc").getValue(String.class));
                    images.add(ds.child("imagelink").getValue(String.class));
                    newsLink.add(ds.child("newsLink").getValue(String.class));
                    heads.add(ds.child("head").getValue(String.class));

                }

                for(int i=0;i<images.size();i++){
                    sliderItems.add(new SliderItems(images.get(i)));
                }
                verticalViewPager.setAdapter(new ViewPagerAdapter(MainActivity.this,sliderItems,titles,desc,images,newsLink,heads,verticalViewPager));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}