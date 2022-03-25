package com.mumo.newsapp;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.mumo.newsapp.databinding.FragmentVideoFormBinding;
import com.mumo.newsapp.models.Discover;

import java.text.SimpleDateFormat;
import java.util.Date;
import io.objectbox.Box;


public class VideoFormFragment extends Fragment {

    /**
     * Binding is an easier way of accessing XML Components in Java
     */

    FragmentVideoFormBinding binding;
    ActivityResultLauncher<Intent> launchFilePicker;
    String image_url = "Empty";
    Box<Discover> discoverBox = ObjectBox.get().boxFor(Discover.class);
    Discover discover;

    public VideoFormFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifyPermissions();
        launchFilePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();

                            fillImageData(data);

                        } else {
                            try {
                                Toast.makeText(getActivity(), "Image Picking Cancelled", Toast.LENGTH_SHORT).show();
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    @Override
    public void onResume() {
        super.onResume();

        if(getArguments().containsKey("UPDATE") && getArguments().getBoolean("UPDATE")){

            discover = discoverBox.get(getArguments().getLong("DISCOVER"));
            binding.inputVideoUrl.setText(discover.getVideo_url());
            //binding.imgAddDiscover.setImageURI(Uri.parse(discover.getImage()));
            Glide.with(requireActivity()).load(Uri.parse(discover.getImage())).into(binding.imgAddDiscover);
            image_url = discover.getImage();

            Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        else {
            discover = new Discover();
            SimpleDateFormat formatter = new SimpleDateFormat("dd / MM /yyyy");
            Date date = new Date();
            discover.setCreated_at(formatter.format(date));

        }

    }

    private void fillImageData(Intent data) {

        image_url = data.getData().toString();
        binding.imgAddDiscover.setImageURI(data.getData());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentVideoFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnAddDiscover.setOnClickListener(v ->{

            validateInputs();

        });
        binding.imgAddDiscover.setOnClickListener(v->{

            // Go to file explorer and select image
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            String [] mimeTypes = {"image/jpeg", "",""};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

            launchFilePicker.launch(intent);

        });

        return root;
    }

    private void validateInputs() {

        String video_url = binding.inputVideoUrl.getText().toString().trim();

        if(image_url.contentEquals("Empty")){
            Toast.makeText(getActivity(),"Please Select an Image", Toast.LENGTH_SHORT).show();
        }
        else if (video_url.isEmpty()){
            binding.inputVideoUrl.setError("Please enter a valid video url");
        }
        else {
            //Save data in the box

            // 1. arrange data in the object box

            discover.setImage(image_url);
            discover.setVideo_url(video_url);
            discover.setIs_external_image(false);

            // 2. save data
            discoverBox.put(discover);

            Toast.makeText(getActivity(),"Discover saved Successfully", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_home);
        }

    }


    public void verifyPermissions(){
        String [] permissions={
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),permissions[0]) == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getActivity(),"Permission Granted", Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), permissions, 134);
        }
    }

}