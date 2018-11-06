package com.example.pavan.mobiofire.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.pavan.mobiofire.Fragments.IndividThumbFragment;
import com.example.pavan.mobiofire.R;


public class Individual_thumb_Activity extends AppCompatActivity implements View.OnClickListener{
    String url=" ";
    Integer id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_thumb_);

        url= getIntent().getStringExtra("url");
        id= Integer.valueOf(getIntent().getIntExtra("id",0));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ind_thumb_frame,new
                IndividThumbFragment(Individual_thumb_Activity.this,url,id));
        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View v) {

    }
}
