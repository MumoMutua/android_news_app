package com.mumo.newsapp.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.mumo.newsapp.R;
import com.mumo.newsapp.databinding.FragmentNotificationsBinding;
import com.mumo.newsapp.utils.PreferenceStorage;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Context context;

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
        bottomSheetDialog.show();

    }
}