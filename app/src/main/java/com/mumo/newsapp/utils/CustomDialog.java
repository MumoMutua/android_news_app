package com.mumo.newsapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.mumo.newsapp.ObjectBox;
import com.mumo.newsapp.R;
import com.mumo.newsapp.models.Discover;

import io.objectbox.Box;

public class CustomDialog {

    BottomSheetDialog discoverSheet;
    Context context;
    View view;
    Box<Discover> discoverBox = ObjectBox.get().boxFor(Discover.class);

    public CustomDialog(Context context, View view){

        this.context = context;
        this.view = view;

    }

    public void showDiscoverDialog(Discover discover) {

        discoverSheet = new BottomSheetDialog(context);
        discoverSheet.setContentView(R.layout.discover_options);

        Button btnUpdate, btnDelete, btnPlay;
        btnPlay = discoverSheet.findViewById(R.id.btn_dialog_play);
        btnUpdate = discoverSheet.findViewById(R.id.btn_dialog_update);
        btnDelete = discoverSheet.findViewById(R.id.btn_dialog_delete);
        TextView txtTitle = discoverSheet.findViewById(R.id.txt_video_title);

        txtTitle.setText(discover.getVideo_url());

        btnPlay.setOnClickListener( v ->{
            // Play the Video
            String url = discover.getVideo_url();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        });
        btnUpdate.setOnClickListener(v -> {
            // Go to Video Fragment and update data

            Bundle bundle = new Bundle();
            bundle.putBoolean("UPDATE",true);
            bundle.putLong("DISCOVER", discover.getId());

            Navigation.findNavController(view).navigate(R.id.videoFormFragment, bundle);

            discoverSheet.dismiss();

        });
        btnDelete.setOnClickListener(v -> {

            discoverSheet.dismiss(); // used to programmatically remove the dialog from view

            Snackbar.make(view, "Are you sure you want to delete this discover?", Snackbar.LENGTH_SHORT)
                    .setAction("Confirm", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            discoverBox.remove(discover.getId());
                            Navigation.findNavController(view).navigate(R.id.navigation_home);

                        }
                    }).show();


        });

        discoverSheet.show();
    }
}
