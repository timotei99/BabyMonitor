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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.timotei.babymonitor.GraphsActivity;
import com.timotei.babymonitor.PairingActivity;
import com.timotei.babymonitor.R;
import com.timotei.babymonitor.RegisterActivity;
import com.timotei.babymonitor.RoomConditionActivity;
import com.timotei.babymonitor.StreamActivity;
import com.timotei.babymonitor.databinding.FragmentHomeBinding;
import com.timotei.babymonitor.ui.notifications.NotificationRepository;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private NotificationRepository notifications;
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
        db = FirebaseDatabase.getInstance("https://babymonitor-e580c-default-rtdb.europe-west1.firebasedatabase.app");

        notifications=new NotificationRepository();
        final TextView time = binding.time;
        final TextView name = binding.name;
        final Button watchBtn = binding.btnStream;
        final Button roomConditionBtn = binding.btnRoomCondition;
        final Button dataBtn = binding.btnData;
        final ImageView imgView = binding.imageView;
        loadingProgressBar = binding.loading;

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
        dataBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), GraphsActivity.class));
        });

        Button btnBlt=binding.btnBluetooth;
        btnBlt.setOnClickListener(v -> startActivity(new Intent(requireContext(), PairingActivity.class)));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setImage(ImageView image) {

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference imgRef=storage.getReference().child("baby_sleeping.jpg");


        imgRef.getDownloadUrl()
                .addOnSuccessListener(uri -> Picasso.get().load(uri).fit().into(image))
                .addOnFailureListener(e -> Log.e("FIREBASE_STORAGE",e.getMessage()));

    }

}