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
import android.widget.ImageButton;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment {

    private HomeRepository repo;
    private FragmentHomeBinding binding;
    private TextView time;
    private TextView name;
    private Button watchBtn;
    private Button roomConditionBtn;
    private Button dataBtn;
    private ImageView imgView;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        repo=HomeRepository.getInstance();

        time = binding.time;
        name = binding.name;
        watchBtn = binding.btnStream;
        roomConditionBtn = binding.btnRoomCondition;
        dataBtn = binding.btnData;
        imgView = binding.imageView;

        repo.setName(name);
        time.setText(dateTimeFormatter.format(LocalDateTime.now()));
        setImage(imgView);

        View root = binding.getRoot();
        setListeners();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setListeners(){
        watchBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), StreamActivity.class));
        });
        roomConditionBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), RoomConditionActivity.class));
        });
        dataBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), GraphsActivity.class));
        });
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