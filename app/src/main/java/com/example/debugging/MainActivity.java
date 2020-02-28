package com.example.debugging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nameet;
    EditText qtyet;
    EditText priceet;
    Button post;
    Button retrieve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameet=findViewById(R.id.name);
        priceet=findViewById(R.id.price);
        qtyet=findViewById(R.id.qty);
        post=findViewById(R.id.add);
        retrieve=findViewById(R.id.retrieve);

        post.setOnClickListener(this);
        retrieve.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.retrieve:
                retrieveData();
                break;
            case R.id.add:
                addDataFirestore();
                break;
        }
    }

    public void retrieveData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                  for (QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                      System.out.println(documentSnapshot.getData().toString());
                  }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void addDataFirestore(){
        String name= nameet.getText().toString();
        String qty= qtyet.getText().toString();
        String price= priceet.getText().toString();

        HashMap<String,String> order= new HashMap<>();
        order.put("name",name);
        order.put("qty",qty);
        order.put("price", price);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Orders").add(order).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
              Toast.makeText(MainActivity.this,"Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
