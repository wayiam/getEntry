package com.example.qrscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReferenceDate,databaseReferenceVisitor, databaseReferenceChild;
    MyAdapter myAdapter;
    DateAdapter dateAdapter;
    ArrayList<Visitors> list;
    ArrayList<Dates> listDates;
    FloatingActionButton faBtn;
    String time, date;
    final Calendar myCalendar = Calendar.getInstance();
    EditText editText;
    String chosenDate;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.historyList);
        faBtn = (FloatingActionButton) findViewById(R.id.fab);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.Birthday);
        chosenDate = editText.getEditableText().toString();
        databaseReferenceDate = FirebaseDatabase.getInstance().getReference("Date");
        databaseReferenceVisitor = FirebaseDatabase.getInstance().getReference("Visitors");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Take the date of today

        Date tod = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat tf = new SimpleDateFormat("hh:mm a");
        chosenDate = df.format(tod);
        editText.setHint(chosenDate);



        DatePickerDialog.OnDateSetListener d =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this,d,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checking if Date is changed if changed the clearlistDates
                databaseReferenceDate.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Dates dates = dataSnapshot.getValue(Dates.class);
                            listDates.clear();

                        }
                        dateAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                databaseReferenceChild = FirebaseDatabase.getInstance().getReference("Date").child(chosenDate);
                //Getting the state of data and updating listDates according to chosendate.
                databaseReferenceChild.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Dates dates = dataSnapshot.getValue(Dates.class);
                            listDates.add(dates);
                        }
                        dateAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


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
        listDates = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        dateAdapter = new DateAdapter(this,listDates);
        recyclerView.setAdapter(dateAdapter);




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
                        addDate(v.getName(),userId,v.getSem(),v.getBranch(),date,time);
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addDate(String Name, String userId, String sem, String branch,String date,String time) {
        Visitors visitor = new Visitors(Name, userId, sem, branch,time);
        databaseReferenceDate.child(date).child(userId).setValue(visitor);
    }

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

    private void updateLabel(){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        editText.setText(df.format(myCalendar.getTime()));
        chosenDate = editText.getEditableText().toString();
    }

}