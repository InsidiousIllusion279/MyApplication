package com.example.eco;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.eco.databinding.ActivityMainBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    List<ImageInfo.Hit> hitList = new ArrayList<>();
    private boolean isCentered = true;
    private BottomSheetBehavior behavior;
    private boolean isVisible = false;
    private String key = "cute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        behavior = BottomSheetBehavior.from(binding.frameID);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        setSupportActionBar(binding.bottomBarID);
        binding.recyclerID.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerID.setHasFixedSize(true);


        key = getIntent().getStringExtra("my_key");
        getImage(key);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isVisible) {
                    binding.cardSearch.setVisibility(View.VISIBLE);
                    isVisible = true;
                } else {
                    binding.cardSearch.setVisibility(View.GONE);

                    isVisible = false;
                }

            }
        });


        binding.searchImageID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("key", binding.searchEditTextID.getText().toString());
                startActivity(intent);

            }
        });

        binding.bottomBarID.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(behavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
                {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else{
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });

        binding.navViewID.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.weatherID)
                {
                    Intent intent = new Intent(getApplicationContext(),SetFragMentActivity.class);
                    intent.putExtra("key","weather");
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),SetFragMentActivity.class);
                    intent.putExtra("key","wallp");
                    startActivity(intent);
                }

                return true;
            }
        });

    }

    private void getImage(String poet) {

        ApiCall apiCall = ClientApiInstance.getInstance().create(ApiCall.class);
        Call<ImageInfo> call = apiCall.getImageData("13269434-7c292aebf90e99a9e1a573256", poet, "photo", true);
        call.enqueue(new Callback<ImageInfo>() {
            @Override
            public void onResponse(Call<ImageInfo> call, Response<ImageInfo> response) {
                if (response.isSuccessful()) {
                    hitList.addAll(response.body().getHits());
                    ImageAdapter adapter = new ImageAdapter(hitList);
                    binding.recyclerID.setAdapter(adapter);
                    if (hitList.size() < 1) {
                        binding.noResult.setVisibility(View.VISIBLE);

                    } else {
                        binding.noResult.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onFailure(Call<ImageInfo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemID) {

            if (isCentered) {
                isCentered = false;
                binding.bottomBarID.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);

            } else {
                isCentered = true;
                binding.bottomBarID.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);

            }
        } else if (item.getItemId() == R.id.settingsID) {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), "Comming Soon...", Snackbar.LENGTH_SHORT)
                    .setTextColor(getResources().getColor(android.R.color.white));
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), "Comming Soon...", Snackbar.LENGTH_SHORT)
                    .setTextColor(getResources().getColor(android.R.color.white));
            snackbar.show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();

        builder.setTitle("Would you like to close the application...")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        moveTaskToBack(true);

                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });
        builder.show();




    }
}
