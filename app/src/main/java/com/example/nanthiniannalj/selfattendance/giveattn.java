package com.example.nanthiniannalj.selfattendance;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationManager;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class giveattn extends AppCompatActivity implements LocationListener {
    Button b, b2;
    TextView tv, users,dates,errorview,rangeview;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,user1;
    String url= "https://annalprojects.000webhostapp.com/push.php";
    LocationManager lm;
    //String latitude;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giveattn);
        b = (Button) findViewById(R.id.b);
        dates=(TextView)findViewById(R.id.dates);
        b2 = (Button) findViewById(R.id.b2);
        errorview=(TextView)findViewById(R.id.textView);
        rangeview=(TextView)findViewById(R.id.clg);
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Attend");
        users = (TextView) findViewById(R.id.user);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        tv = (TextView) findViewById(R.id.tv);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        users.setText(user.getEmail());
        Calendar cal = Calendar. getInstance();
        Date date=cal. getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate=dateFormat. format(date);
        DateFormat dateFormattime = new SimpleDateFormat("dd-MM-yyyy");
        String formattedTime =dateFormattime.format(date);


        dates.setText(formattedDate+" "+formattedTime);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                Toast.makeText(getApplicationContext(), "Logged out...", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), studentLogin.class);
                startActivity(i);

            }
        });
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);

        }
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Checking Location...");
                progressDialog.show();
                getLocation();


            }
        });

    }


    public void getLocation() {
        try {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 5, this);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onLocationChanged(Location loc) {
        final String latitude=String.valueOf(loc.getLatitude());
        final String longitude = String.valueOf(loc.getLongitude());
        tv.setText("Latitude" + loc.getLatitude()
                + "\nLongitude" + loc.getLongitude());
        try {
            Geocoder gc = new Geocoder(this, Locale.getDefault());
            List<Address> addr = gc.getFromLocation(loc.getLatitude(), loc.getLongitude(), 101);
            tv.setText(tv.getText() + "\n" + addr.get(0).getAddressLine(0) + "\n" + addr.get(1).getAddressLine(1));
            progressDialog.dismiss();
            //Datapush starts
            double la = loc.getLatitude();
            double lo = loc.getLongitude();
            if ((la < 13.009700 && la > 13.007000)&&(lo < 80.005300 && lo > 80.002600)) {
                rangeview.setText("You are in REC");
                //String latitude=String.valueOf( loc.getLatitude());
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String uid = user.getUid();
                final String useremail = user.getEmail();

                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
                final String formatterTime = dateFormat1.format(date);
                final String formattedDate = dateFormat.format(date);
                final String statuses = "1";

                AddDb addDb = new AddDb(useremail, formattedDate, statuses, latitude, longitude, formatterTime);
                databaseReference.child(formattedDate).child(formatterTime).setValue(addDb);
                // Toast.makeText(getApplicationContext(),"Attendance completed",Toast.LENGTH_LONG).show();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        errorview.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                        errorview.setText(error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", useremail);
                        params.put("dates", formattedDate);
                        params.put("statuses", statuses);
                        params.put("latitude", latitude);
                        params.put("longitude", longitude);
                        params.put("times", formatterTime);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
                stringRequest.setShouldCache(false);

                //datapush ends

                lm.removeUpdates(this);
            }
            else{
                rangeview.setText("You are not in REC");
                Toast.makeText(getApplicationContext(),"You are out of Range!",Toast.LENGTH_LONG).show();
                lm.removeUpdates(this);
            }
        }catch(Exception e){
                e.printStackTrace();

        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle b) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Confirmation:").setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                }).create().show();
    }
}
