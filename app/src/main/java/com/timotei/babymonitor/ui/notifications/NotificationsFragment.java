package com.timotei.babymonitor.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.timotei.babymonitor.R;
import com.timotei.babymonitor.data.model.NotificationModel;
import com.timotei.babymonitor.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView recycler;
    private FirestoreRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        recycler= binding.recycler;

        String uid=mAuth.getCurrentUser().getUid();
        Query query=db.collection("notifications").orderBy("date").whereEqualTo("userId",uid);
        FirestoreRecyclerOptions<NotificationModel> options= new FirestoreRecyclerOptions.Builder<NotificationModel>()
                .setQuery(query,NotificationModel.class)
                .build();

        adapter= new FirestoreRecyclerAdapter<NotificationModel,NotificationViewHolder>(options){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull NotificationModel model) {
                holder.title.setText(model.getTitle());
                holder.description.setText(model.getDescription());
                holder.date.setText(model.getDate());

                if(model.getType().equals("alarm")){
                    holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crying_32));
                }
                else{
                    holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.notification_bell));
                }
                holder.itemView.setOnClickListener(v -> {
                    holder.itemView.setVisibility(View.GONE);
                    String docId=getSnapshots().getSnapshot(holder.getAdapterPosition()).getId();
                    Log.d("Firebase","Doc id is: "+docId);
                    NotificationRepository repo = new NotificationRepository();
                    repo.deleteNotification(docId,requireContext());
                });
            }

            @NonNull
            @Override
            public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_notification,parent,false);
                return new NotificationViewHolder(view);
            }
        };

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }



}