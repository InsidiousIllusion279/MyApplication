package com.example.eco;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationFragment extends Fragment {
    private static final int PERMISSION_ID = 1234;
    private String str = "Lol", status;
    private Retrofit retrofit;
    FusedLocationProviderClient mFusedLocationClient;
    com.example.eco.databinding.FragmentLocationBinding binding;


    public LocationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = com.example.eco.databinding.FragmentLocationBinding.inflate(inflater, container, false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        View view = binding.getRoot();
       /* setTime();*/
        getLastLocation();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private boolean checkPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Worked", Toast.LENGTH_SHORT).show();
            }
        }
    }


    ////retrieve data

    public void getAllWeaterData(float lat, float lon) {

        retrofit = WeatherClientApi.getInstance();

        Calendar calendar = Calendar.getInstance();
        String date = DateFormat.getDateInstance().format(calendar.getTime());

        WeatherApiCall apiCall = retrofit.create(WeatherApiCall.class);
        Call<WeatherInfo> call = apiCall.getWeatherData(lat, lon, "5b76939fef2379df448ba7473b209f9e");
        call.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                assert response.body() != null;
               /* str += response.body().getName() + "\n";
                str += response.body().getMain().getHumidity();
*/
                status = response.body().getWeather().get(0).getMain();

                /*SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM, YYYY");*/




                /*String day = dayFormat.format(new Date());"
                String date = dateFormat.format(new Date());*/
                binding.weatherDayTextID.setText(getCurrentDay());
                binding.weatherDateTextID.setText(date);
                binding.weatherStatusID.setText(response.body().getWeather().get(0).getMain());
                binding.weatherDescriptionID.setText(response.body().getWeather().get(0).getDescription());
                String imgUrl = "http://api.openweathermap.org/img/w/" + response.body().getWeather().get(0).getIcon() + ".png";
                Picasso.with(getActivity()).load(imgUrl).into(binding.weatherImageID);
                binding.degrreeTextID.setText(Math.round((Math.abs(response.body().getMain().getTemp() - 273))) + "");
                binding.weatherHumidityTextID.setText(response.body().getMain().getHumidity() + "%");
                binding.weatherPressureTextID.setText(response.body().getMain().getPressure() + " mm");


//                Log.e("TAG", "onResponse: " + str);


            }

            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {

            }


        });

    }

///find location

    public String getCurrentDay() {

        String daysArray[] = {"শনিবার", "রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার"};

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        return daysArray[day];

    }

    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {

                                    getAllWeaterData(((int) location.getLatitude()), ((int) location.getLongitude()));

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(getActivity(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );

    }

    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

        }
    };

  /*  public void setTime() {

        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                getLastLocation();

                ha.postDelayed(this, 3000);
            }
        }, 3000);
    }
*/
}
