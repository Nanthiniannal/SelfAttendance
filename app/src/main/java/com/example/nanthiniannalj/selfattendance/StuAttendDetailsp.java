package com.example.nanthiniannalj.selfattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class StuAttendDetailsp extends AppCompatActivity {
    Button b1,b2;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_attend_detailsp);
        b1=(Button)findViewById(R.id.getdetails);
        b2=(Button)findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                Toast.makeText(getApplicationContext(), "Logged out...", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), parentLogin.class);
                startActivity(i);
            }
        });
    }
}


