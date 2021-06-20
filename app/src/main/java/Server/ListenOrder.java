package Server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.androidfood.OrderStatus;
import com.example.androidfood.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Database.Database;
import Model.Commen;
import Model.Request;

public class ListenOrder extends Service implements ChildEventListener {
    FirebaseDatabase database;
    DatabaseReference requests;
    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requests.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Request request = snapshot.getValue(Request.class);
        showNotification(snapshot.getKey(),request);

    }

    private void showNotification(String key, Request request) {
        Intent intent = new Intent(getBaseContext(), OrderStatus.class);
        intent.putExtra("userPhone",request.getPhone());
        PendingIntent contextIntent = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Food App")
                .setContentInfo("Your order was update status to" + Commen.convertCodeToStatus(request.getStatus()))
                .setContentIntent(contextIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}