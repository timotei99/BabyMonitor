package com.timotei.babymonitor.ui.home;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.timotei.babymonitor.R;
import com.timotei.babymonitor.RegisterActivity;
import com.timotei.babymonitor.RoomConditionActivity;
import com.timotei.babymonitor.StreamActivity;
import com.timotei.babymonitor.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private FirebaseDatabase db;
    private ProgressBar loadingProgressBar;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        db=FirebaseDatabase.getInstance("https://babymonitor-e580c-default-rtdb.europe-west1.firebasedatabase.app");


        final TextView time = binding.time;
        final TextView name = binding.name;
        final Button watchBtn = binding.btnStream;
        final Button roomConditionBtn = binding.btnRoomCondition;
        final ImageView imgView= binding.imageView;
        loadingProgressBar= binding.loading;

        setImage(imgView);


        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> time.setText(s));
        homeViewModel.getName().observe(getViewLifecycleOwner(), name::setText);


        View root = binding.getRoot();
        watchBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), StreamActivity.class));
        });
        roomConditionBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), RoomConditionActivity.class));
        });



        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setImage(ImageView image){

        DatabaseReference imgRef=db.getReference().child("server/baby_image");
        imgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link= snapshot.getValue(String.class);
                Picasso.get().load(link).fit().into(image);
                //loadingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Error Loading Image", Toast.LENGTH_SHORT).show();
            }
        });


    }

}