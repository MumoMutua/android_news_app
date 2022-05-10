package com.mumo.newsapp.ui.notifications;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mumo.newsapp.Networking.ChatServiceGenerator;
import com.mumo.newsapp.Networking.PeopleResponse;
import com.mumo.newsapp.Networking.pojos.ChatResponse;
import com.mumo.newsapp.Networking.pojos.RegisterRequest;
import com.mumo.newsapp.Networking.pojos.UserResponse;
import com.mumo.newsapp.ObjectBox;
import com.mumo.newsapp.R;
import com.mumo.newsapp.adapters.ChatAdapter;
import com.mumo.newsapp.adapters.PeopleAdapter;
import com.mumo.newsapp.databinding.FragmentNotificationsBinding;
import com.mumo.newsapp.models.People;
import com.mumo.newsapp.utils.Notifications;
import com.mumo.newsapp.utils.PreferenceStorage;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.objectbox.Box;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Context context;
    private SweetAlertDialog pDialog;
    private ChatAdapter chatAdapter;
    private List<ChatResponse> chats = new ArrayList<>();
    private List<PeopleResponse> people = new ArrayList<>();
    private PeopleAdapter peopleAdapter;
    private Notifications notifications;
    private NotificationManagerCompat notificationManager;
    private Box<People> peopleBox = ObjectBox.get().boxFor(People.class);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notifications = new Notifications(context);
        notificationManager = NotificationManagerCompat.from(getActivity());

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        chatAdapter = new ChatAdapter(chats, context);
        peopleAdapter = new PeopleAdapter(people, context);
        binding.recyclerMessage.setAdapter(peopleAdapter);
        binding.recyclerMessage.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerMessage.setNestedScrollingEnabled(true);

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Loading...");
        showOrHideLayouts();

        binding.btnLogIn.setOnClickListener(v->{
            showLogInDialog();
        });
        binding.btnRegister.setOnClickListener(v->{
            showRegisterDialog();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showLogInDialog(){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_login_dialog);

        Button login = bottomSheetDialog.findViewById(R.id.btn_dialog_login);
        Button register = bottomSheetDialog.findViewById(R.id.btn_dialog_register);
        TextInputEditText inputUsername, inputPass;
        inputUsername = bottomSheetDialog.findViewById(R.id.input_username);
        inputPass = bottomSheetDialog.findViewById(R.id.input_password);

        login.setOnClickListener(v -> {
            login(
                    inputUsername.getText().toString().trim(),
                    inputPass.getText().toString().trim()
            );
            new PreferenceStorage(context).setAuthStatus(true);
            bottomSheetDialog.dismiss();
            showOrHideLayouts();
        });
        register.setOnClickListener(v ->{
            bottomSheetDialog.dismiss();
            showRegisterDialog();
        });
        bottomSheetDialog.show();
    }

    private void showOrHideLayouts() {
        Boolean isAuthenticated = new PreferenceStorage(context).isAuthenticated();
        if (isAuthenticated){
            binding.btnRegister.setVisibility(View.GONE);
            binding.btnLogIn.setVisibility(View.GONE);
            binding.imageView2.setVisibility(View.GONE);
            binding.textView7.setVisibility(View.GONE);
            binding.recyclerMessage.setVisibility(View.VISIBLE);

            if (peopleBox.isEmpty()){
                getPeople();
            }
           else {
               people.clear();
               List<People> boxPeople = peopleBox.getAll();
               for (int i=0; i< boxPeople.size(); i++){
                   people.add(
                           new PeopleResponse(
                                   boxPeople.get(i).getUser_id(),
                                   boxPeople.get(i).getUsername(),
                                   boxPeople.get(i).getEmail()
                           )
                   );
               }
            }
        }
        else {
            binding.btnRegister.setVisibility(View.VISIBLE);
            binding.btnLogIn.setVisibility(View.VISIBLE);
            binding.imageView2.setVisibility(View.VISIBLE);
            binding.textView7.setVisibility(View.VISIBLE);
            binding.recyclerMessage.setVisibility(View.GONE);
        }
    }

    private void showRegisterDialog(){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_register_dialog);

        Button btnRegister = bottomSheetDialog.findViewById(R.id.button_register);
        Button switchLogIn = bottomSheetDialog.findViewById(R.id.button_log_in);

        TextInputEditText inputRegisterUsername, inputEmail, inputPhoneNumber, inputPassword, inputConfirmPassword;
        inputRegisterUsername = bottomSheetDialog.findViewById(R.id.input_username);
        inputEmail = bottomSheetDialog.findViewById(R.id.input_email);
        inputPhoneNumber = bottomSheetDialog.findViewById(R.id.input_phone_number);
        inputPassword = bottomSheetDialog.findViewById(R.id.input_password);
        inputConfirmPassword = bottomSheetDialog.findViewById(R.id.input_confirm_password);

        switchLogIn.setOnClickListener(v-> {
            bottomSheetDialog.dismiss();
            showLogInDialog();
        });

        btnRegister.setOnClickListener(v->{
            RegisterRequest registerRequest = new RegisterRequest(
                    inputPhoneNumber.getText().toString().trim(),
                    inputRegisterUsername.getText().toString().trim(),
                    inputEmail.getText().toString().trim(),
                    inputPassword.getText().toString().trim()
            );
            registerUser(registerRequest);
        });
        bottomSheetDialog.show();

    }
    public void login(String username, String password) {
        pDialog.setContentText("Logging In");
        pDialog.show();
        Call<UserResponse> call = ChatServiceGenerator.getInstance()
                .getApiConnector().login(username, password);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                pDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    new PreferenceStorage(context).setAuthStatus(true);
                    new PreferenceStorage(context).setUserData(response.body());

                    showOrHideLayouts();

                    SweetAlertDialog successDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                    successDialog.setTitle("Welcome " + response.body().getUsername());
                    successDialog.setContentText("LogIn Successful");
                    successDialog.show();
                } else if (response.code() == 500) {
                    SweetAlertDialog errorDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                    errorDialog.setTitle("Oops ");
                    errorDialog.setContentText(response.message());
                    errorDialog.show();
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                Log.d("TEST::", "onResponse: " + response.message());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                Log.d("TEST::", "onFailure: " + t.getMessage());
            }
        });
    }
    public void registerUser(RegisterRequest registerRequest){
        pDialog.setContentText("Registering User");
        pDialog.show();
        Call<UserResponse> call = ChatServiceGenerator.getInstance()
                .getApiConnector().register(registerRequest);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                pDialog.dismiss();
                if (response.code() == 200 && response.body()!= null){
                    new PreferenceStorage(context).setAuthStatus(true);
                    new PreferenceStorage(context).setUserData(response.body());

                    showOrHideLayouts();

                    SweetAlertDialog successDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                    successDialog.setTitle("Welcome " + response.body().getUsername());
                    successDialog.setContentText("Registration Successful");
                    successDialog.show();

                    notificationManager.notify(254, notifications.registerNotification("Welcome", "Thank you for using NewsApp."+
                            "You can share it with your friends and stay up to date together").build());
                }
                else if (response.code() == 500){
                    SweetAlertDialog errorDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                    errorDialog.setTitle("Oops ");
                    errorDialog.setContentText(response.message());
                    errorDialog.show();
                }
                else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                Log.d("TEST::", "onResponse: " +response.message());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                Log.d("TEST::", "onFailure: " +t.getMessage());
            }
        });
    }
    public void getPeople(){
        pDialog.setContentText("Fetching People");
        pDialog.show();

        Call<List<PeopleResponse>> call = ChatServiceGenerator.getInstance()
                .getApiConnector().getPeople(
                        "Token " +new PreferenceStorage(context).getUserToken()
                );
        call.enqueue(new Callback<List<PeopleResponse>>() {
            @Override
            public void onResponse(Call<List<PeopleResponse>> call, Response<List<PeopleResponse>> response) {
                pDialog.dismiss();

                if (response.code() == 200 && response.body()!= null){
                    people.clear();
                    people.addAll(response.body());
                    peopleAdapter.notifyDataSetChanged();

                    for (int i=0; i<people.size(); i++){
                        peopleBox.put(new People(
                                people.get(i).getUser_id(),
                                people.get(i).getUsername(),
                                people.get(i).getEmail()
                        ));
                    }
                }
                else{
                    Snackbar.make(binding.getRoot(), "You have no chats", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<PeopleResponse>> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                Log.d("TEST::", "onFailure: "+t.getMessage());
            }
        });
    }
}