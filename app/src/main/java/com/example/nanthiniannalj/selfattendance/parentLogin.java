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

public class parentLogin extends AppCompatActivity {
    EditText e1,e2;
    Button b;
    TextView tv,tv2;
    String url= "https://annalprojects.000webhostapp.com/parentauth.php";
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener mAuth;
    // FirebaseAuth.
    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(mAuth);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        e1=(EditText)findViewById(R.id.e1);
        e2=(EditText)findViewById(R.id.e2);
        b=(Button)findViewById(R.id.b);
        tv=(TextView)findViewById(R.id.tv);
        tv2=(TextView)findViewById(R.id.tv2);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        mAuth=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    Toast.makeText(getApplicationContext(),"Trying to Redirect!",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    //2nd verification
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    final String email=user.getEmail();
                    final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Toast.makeText(getApplicationContext(), "You are not a valid user!!", Toast.LENGTH_LONG).show();
                            if(response.equals("1"))
                            {
                                finish();

                                Intent i=new Intent(getApplicationContext(),StuAttendDetailsp.class);
                                startActivity(i);
                            }
                            else
                                Toast.makeText(getApplicationContext(), "You are not a valid user!!", Toast.LENGTH_LONG).show();
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
                }

            }
        };
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),parentCreate.class);
                startActivity(i);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fp();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    private void fp() {
        String email=e1.getText().toString();
        studentauth sa=new studentauth(email);
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Enter the email id to get Link",Toast.LENGTH_LONG).show();
        }
        progressDialog.setMessage("Sending reset mail..");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Check ur email",Toast.LENGTH_LONG).show();

                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Cannot proceed",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    private void login() {
        final String email = e1.getText().toString();
        final String password = e2.getText().toString();
        // final String[] res = new String[1];
        final String res;
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter a email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter a password", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            //removeAuthStateListener(mAuth);
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Cannot signin!!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
