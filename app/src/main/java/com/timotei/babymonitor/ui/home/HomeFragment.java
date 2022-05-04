package com.timotei.babymonitor.ui.home;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.timotei.babymonitor.R;
import com.timotei.babymonitor.RegisterActivity;
import com.timotei.babymonitor.StreamActivity;
import com.timotei.babymonitor.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        //Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView time = binding.time;
        final TextView name = binding.name;
        final Button watchBtn = binding.btnStream;


        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> time.setText(s));
        homeViewModel.getName().observe(getViewLifecycleOwner(), name::setText);



        watchBtn.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), StreamActivity.class));
        });



        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}