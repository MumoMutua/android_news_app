package com.mumo.newsapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.mumo.newsapp.databinding.ActivityHomeBinding;
import com.mumo.newsapp.utils.NetworkReceiver;
import com.mumo.newsapp.utils.Notifications;
import com.mumo.newsapp.utils.PreferenceStorage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity {

    private  NavController navController;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Display today's date on the text View

        TextView dateText = findViewById(R.id.textDate);

        Date today = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        String date = formatter.format(today);

        dateText.setText(date);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        new Notifications(getApplicationContext()).createSyncNotificationChannel("Sync Chats", "Syncing Chats",
                Notifications.CHAT_SYNC_NOTIFICATION_ID);


        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(new NetworkReceiver(), filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.item_add){

            navController.navigate(R.id.videoFormFragment);
        }
        else if (id == R.id.item_logout){

            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitle("Warning");
            sweetAlertDialog.setContentText("Are you sure you want to Log out?");
            sweetAlertDialog.setCancelText("No").setCancelClickListener(Dialog::onBackPressed);

            sweetAlertDialog.setConfirmText("Yes").setConfirmClickListener(sweetAlertDialog1 -> {
                sweetAlertDialog1.dismiss();
                new PreferenceStorage(HomeActivity.this).logout();
                navController.popBackStack(R.id.navigation_home, true);
                navController.navigate(R.id.navigation_home);

            });
            sweetAlertDialog.show();

        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onSupportNavigateUp(){
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}