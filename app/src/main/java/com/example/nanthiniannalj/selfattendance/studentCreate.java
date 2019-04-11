package com.example.nanthiniannalj.selfattendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class studentCreate extends AppCompatActivity {
    EditText e1,e2;
    Button b;
    TextView tv;
    String url= "https://annalprojects.000webhostapp.com/studentdetailspush.php";
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_create);
        e1=(EditText)findViewById(R.id.e1);
        e2=(EditText)findViewById(R.id.e2);
        b=(Button)findViewById(R.id.b);
        tv=(TextView)findViewById(R.id.tv);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),studentLogin.class);
                startActivity(i);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
            String email = e1.getText().toString();
            String password = e2.getText().toString();
            if(TextUtils.isEmpty(email)){
                Toast.makeText(getApplicationContext(),"Enter a email",Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(getApplicationContext(),"Enter a password",Toast.LENGTH_LONG).show();
                return;
            }
            progressDialog.setMessage("Creating account..");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //2nd verification
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        final String email=user.getEmail();
                        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Toast.makeText(getApplicationContext(), "You are not a valid user!!", Toast.LENGTH_LONG).show()
                                    finish();
                                    Intent i=new Intent(getApplicationContext(),studentLogin.class);
                                    startActivity(i);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //
                                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                            }
                        })
                        {
                            @Override
                            protected Map<String,String > getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("email", email);
                                return params;

                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);
                        progressDialog.dismiss();


                        Toast.makeText(getApplicationContext(),"account created...",Toast.LENGTH_LONG).show();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Cannot create account!!",Toast.LENGTH_LONG).show();
                    }
                }
            });



    }

}
