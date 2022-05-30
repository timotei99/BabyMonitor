package com.timotei.babymonitor.ui.notifications;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NotificationRepository {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public NotificationRepository(){
        mAuth=FirebaseAuth.getInstance();
        firestore =FirebaseFirestore.getInstance();
    }

    public void deleteNotification(String id, Context context){
        firestore.collection("notifications").document(id)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d("Firebase","Notification deleted successfully!");
                        Toast.makeText(context,"Deleted successfully!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.d("Firebase", "onComplete: Error Unable to delete : " + task.getException());
                        Toast.makeText(context,"Failed to delete!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean checkForNotifications(){
        final int[] count = {0};
        firestore.collection("notifications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                count[0]++;
                                Log.d("NOTIFICATIONS", "HERRE");
                            }
                        } else {
                            Log.d("NOTIFICATIONS", "Error getting documents: ", task.getException());
                        }
                    }
                });
        Log.d("NOTIFICATIONS", "Got "+count[0]+" notifications");
        if(count[0] >0){
            return true;
        }
        return false;
    }


}
