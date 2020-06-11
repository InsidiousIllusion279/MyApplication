package com.example.eco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.eco.databinding.ActivitySetFragMentBinding;

public class SetFragMentActivity extends AppCompatActivity {

    ActivitySetFragMentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySetFragMentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String value = getIntent().getStringExtra("key");

        if(value.equals("weather"))
        {
            LocationFragment fragment = new LocationFragment();
            loadFragment(fragment);
        }
        else{
            LocationFragment1 fragment = new LocationFragment1();
            loadFragment(fragment);
        }

    }

    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.setFragmentID,fragment);
        transaction.commit();

    }


}