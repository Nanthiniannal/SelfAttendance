package com.example.nanthiniannalj.selfattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class StuAttendDetails extends AppCompatActivity {
    Button b1,b2,b3;
    FirebaseAuth firebaseAuth;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_attend_details);
        b1=(Button)findViewById(R.id.getdetails);
        b2=(Button)findViewById(R.id.logout);
        b3=(Button)findViewById(R.id.show);
        editText=(EditText)findViewById(R.id.e1);
        firebaseAuth = FirebaseAuth.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em=editText.getText().toString();
                Intent i=new Intent(getApplicationContext(),AttendanceList.class);
                i.putExtra("email",em);
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                Toast.makeText(getApplicationContext(), "Logged out...", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), staffLogin.class);
                startActivity(i);
            }
        });
    }
}
