package com.example.eco;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    List<ImageInfo.Hit> hitList = new ArrayList<>();

    public ImageAdapter(List<ImageInfo.Hit> hitList) {
        this.hitList = hitList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.wallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new asyncWallpaper().execute(holder);
                Snackbar snackbar = Snackbar.make(holder.itemView,"Set as wallpaper...",Snackbar.LENGTH_SHORT)
                        .setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
                snackbar.show();

            }
        });
        holder.tag.setText(hitList.get(position).getTags());
        Picasso.with(holder.itemView.getContext()).load(hitList.get(position).getLargeImageURL()).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                AlertDialog dialog = builder.create();
                builder.setMessage("Would you like to download this image...")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(hitList.get(position).getWebformatURL()));
                            holder.itemView.getContext().startActivity(intent);
                            }
                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return hitList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        TextView tag;
        ImageView wallButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tagID);
            image = itemView.findViewById(R.id.imageID);
            wallButton=itemView.findViewById(R.id.setWllID);



        }
    }

    private class asyncWallpaper extends AsyncTask<ViewHolder,Void,Void> {


        @Override
        protected Void doInBackground(ViewHolder... viewHolders) {
            try {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(viewHolders[0].itemView.getContext());

                Bitmap myImage = Picasso.with(viewHolders[0].itemView.getContext()).load(hitList.get(viewHolders[0].getAdapterPosition()).getWebformatURL()).get();
           wallpaperManager.setBitmap(myImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
