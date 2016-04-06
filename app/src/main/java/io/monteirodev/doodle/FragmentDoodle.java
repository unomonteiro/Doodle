package io.monteirodev.doodle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.util.UUID;

public class FragmentDoodle extends Fragment implements View.OnClickListener {

    private DrawView drawView;
    private ImageButton newBtn, saveBtn,saveWallBtn, currPaint, brushBtn;
    private float smallBrush, mediumBrush, largeBrush;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_doodle, null);
        getAllWidgets(rootView);
        return rootView;
    }
    private void getAllWidgets(View view) {
        drawView = (DrawView) view.findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout) view.findViewById(R.id.paint_colors);

        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.paint_pressed));

        newBtn = (ImageButton)view.findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton)view.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        saveWallBtn = (ImageButton)view.findViewById(R.id.saveWallpaper_btn);
        saveWallBtn.setOnClickListener(this);

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        brushBtn = (ImageButton) view.findViewById(R.id.brush_btn);
        brushBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        //click click who's there ?
        if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(MainActivity.getInstance());
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new doodle with current color?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();
        } else if (view.getId()==R.id.brush_btn) {
            final Dialog brushDialog = new Dialog(MainActivity.getInstance());
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();

        } else if (view.getId()==R.id.save_btn) {
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(MainActivity.getInstance());
            saveDialog.setTitle("Save doodle");
            saveDialog.setMessage("Save doodle to Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);

                    //attempt to save
                    String url = MediaStore.Images.Media.insertImage(
                            MainActivity.getInstance().getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "doodle");

                    //feedback
                    if (url != null) {
                        Toast savedToast = Toast.makeText(MainActivity.getInstance(),
                                "Doodle saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else {
                        Toast unsavedToast = Toast.makeText(MainActivity.getInstance(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                    drawView.setDrawingCacheEnabled(true);
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
        } else if (view.getId()==R.id.saveWallpaper_btn){
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.getInstance());
            drawView.setDrawingCacheEnabled(true);
            try {
                //Set the clicked bitmap
                Bitmap bitmap = drawView.getDrawingCache();
                wallpaperManager.setBitmap(bitmap);
                Toast.makeText(MainActivity.getInstance(), "Wallpaper was set :)", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(MainActivity.getInstance(), "Error setting wallpaper", Toast.LENGTH_SHORT).show();
            }
            drawView.destroyDrawingCache();
            drawView.setDrawingCacheEnabled(true);
        }
    }

    public void paintClicked(View view){
        //use chosen color
        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();

            drawView.setColor(color);

            imgView.setImageDrawable(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.paint_pressed));
            currPaint.setImageDrawable(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }
}
