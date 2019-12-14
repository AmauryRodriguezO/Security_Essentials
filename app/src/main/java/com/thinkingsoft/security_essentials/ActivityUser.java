package com.thinkingsoft.security_essentials;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityUser extends AppCompatActivity {

    private TextView onomcomusu;
    private TextView ocoreleusu;
    private TextView opunlatusu;
    private TextView opunlogusu;
    private TextView onomcomusu2;
    private TextView ocoreleusu2;

    private FirebaseAuth omAuth;
    private DatabaseReference odatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        omAuth = FirebaseAuth.getInstance();
        odatabase = FirebaseDatabase.getInstance().getReference();

        onomcomusu = findViewById(R.id.nomcomusu);
        ocoreleusu = findViewById(R.id.coreleusu);
        opunlatusu = findViewById(R.id.punlatusu);
        opunlogusu = findViewById(R.id.punlogusu);
        onomcomusu2 = findViewById(R.id.nomcomusu2);
        ocoreleusu2 = findViewById(R.id.coreleusu2);
        Button obtncerrarsesion = findViewById(R.id.btncerrarsesion);

        obtncerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                omAuth.signOut();
                startActivity(new Intent(ActivityUser.this, MainActivity.class));
            }
        });

        datosUsuario();
        // permisos de localizacion
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                obtenerLocalizacion();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        } else {
            obtenerLocalizacion();
        }
    }

    @SuppressLint("SetTextI18n")
    private void obtenerLocalizacion() {
        LocationManager locationManager = (LocationManager) ActivityUser.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            public void onLocationChanged(Location location) {
                opunlatusu.setText("Latitud: " + location.getLatitude());
                opunlogusu.setText("Longitud: " + location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            @SuppressLint("SetTextI18n")
            public void onProviderDisabled(String provider) {
            }
        };
        if (ContextCompat.checkSelfPermission(ActivityUser.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            opunlatusu.setText("Latitud no obtenida");
            opunlogusu.setText("Longitud no obtenida");
            Toast.makeText(ActivityUser.this, "Por favor, permita el acceso a la localizacion",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void datosUsuario() {
        FirebaseUser usuarioLogeado = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioLogeado != null) {
            ocoreleusu.setText(usuarioLogeado.getEmail());
            odatabase.child("USER").child(usuarioLogeado.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    assert usuario != null;
                    onomcomusu.setText(usuario.getName());
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    onomcomusu.setText("nombres no registrados");
                }
            });
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            String coreleusu2 = "no resgistrado en el telefono";
            String nomcomusu2="no resgistrado en el telefono";
            try
            {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor =  db.rawQuery("SELECT nombre, correo FROM usuarios where uid = '"+usuarioLogeado.getUid()+"'",null);
                cursor.moveToFirst();
                if (cursor.getString(cursor.getColumnIndex ("correo")) != null)
                {
                    coreleusu2=cursor.getString(cursor.getColumnIndex("correo"));
                }
                if (cursor.getString(cursor.getColumnIndex("nombre")) != null)
                {
                    nomcomusu2=cursor.getString(cursor.getColumnIndex("nombre"));
                }
                ocoreleusu2.setText(coreleusu2);
                onomcomusu2.setText(nomcomusu2);
                cursor.close();
            }
            catch (Exception e)
            {
                ocoreleusu2.setText(coreleusu2);
                onomcomusu2.setText(nomcomusu2);
            }

        } else {
            startActivity(new Intent(ActivityUser.this, MainActivity.class));
        }
    }
}