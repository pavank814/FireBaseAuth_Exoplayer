package com.example.pavan.mobiofire.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.pavan.mobiofire.Fragments.ThumbFragment;
import com.example.pavan.mobiofire.R;


public class Thumb_Activity extends AppCompatActivity {
ListView thumb_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumb_);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.thumb_frame,new
                ThumbFragment(Thumb_Activity.this));
        fragmentTransaction.commit();

    }


}
