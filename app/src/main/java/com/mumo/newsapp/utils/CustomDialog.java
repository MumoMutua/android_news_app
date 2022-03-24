package com.mumo.newsapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mumo.newsapp.R;
import com.mumo.newsapp.models.Discover;

public class CustomDialog {

    BottomSheetDialog discoverSheet;
    Context context;

    public CustomDialog(Context context){
        this.context = context;
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

        });
        btnDelete.setOnClickListener(v -> {
            //
            discoverSheet.dismiss(); // used to programmatically remove the dialog from view

        });

        discoverSheet.show();
    }
}
