package com.example.qrscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReferenceDate,databaseReferenceVisitor,databaseReferenceDB;
    MyAdapter myAdapter,mysecondAdapter;
    ArrayList<Visitors> list;
    ArrayList<DB> listDB;
    FloatingActionButton faBtn;
    String time, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.historyList);
        faBtn = (FloatingActionButton) findViewById(R.id.fab);
        databaseReferenceDate = FirebaseDatabase.getInstance().getReference("Date");
        databaseReferenceVisitor = FirebaseDatabase.getInstance().getReference("Visitors");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        faBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();

                Date today = Calendar.getInstance().getTime();
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat tf = new SimpleDateFormat("hh:mm a");
                 date = df.format(today);
                 time = tf.format(today);
            }
        });

        list = new ArrayList<>();
        listDB = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);


//        databaseReferenceDate.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    Visitors visitors = dataSnapshot.getValue(Visitors.class);
//                    list.add(visitors);
//
//                }
//                myAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        databaseReferenceVisitor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Visitors visitors = dataSnapshot.getValue(Visitors.class);
                    list.add(visitors);

                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                String userId = intentResult.getContents();

                //Here we can Search for the User Details from userId and pass it in

                for (int i = 0; i < list.size(); i++){
//                    if (list.get(i).getId() == null)
//                        continue;
                    if (list.get(i).getId().compareTo(userId) == 0){
                        Visitors v = list.get(i);
                        addVisitor(v.getName(),userId,v.getSem(),v.getBranch(),date,time);
                    }

                }

     //        addVisitor("Doremon",userId,"7","CSE",date,time);
//                addDB("New User",userId,"7","CSE");

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addVisitor(String Name, String userId, String sem, String branch,String date,String time) {

         Visitors visitor = new Visitors(Name, userId, sem, branch,time);
        //    Visitors v = new Visitors("Suniyo","1111","7","ECE",time);
        databaseReferenceDate.child(date).child(userId).setValue(visitor);
 //       databaseReferenceDate.child(date).child("1111").setValue(v);
//    databaseReferenceVisitor.child(date).child(userId).setValue(visitor);
    }

//    private void addDB(String Name,String userID,String sem, String branch){
//
//
//    }

//    private void addVisitor(Visitors visitor, String date, String time){
//
//        if (visitor == null){
//
//            //TODO: Implement later
//
//            System.exit(1);
//        }
//        Visitors visitor = new Visitors(Name, userId, sem, branch,time);
//        databaseReferenceDate.child(date).child(visitor.getId()).setValue(visitor);
//    }

}