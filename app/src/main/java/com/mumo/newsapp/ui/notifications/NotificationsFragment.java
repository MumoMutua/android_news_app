package com.mumo.newsapp.ui.notifications;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.mumo.newsapp.Networking.ChatServiceGenerator;
import com.mumo.newsapp.Networking.pojos.RegisterRequest;
import com.mumo.newsapp.Networking.pojos.UserResponse;
import com.mumo.newsapp.R;
import com.mumo.newsapp.databinding.FragmentNotificationsBinding;
import com.mumo.newsapp.utils.PreferenceStorage;

import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Context context;
    private SweetAlertDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

                    SweetAlertDialog successDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                    successDialog.setTitle("Welcome " + response.body().getUsername());
                    successDialog.setContentText("Registration Successful");
                    successDialog.show();
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
}