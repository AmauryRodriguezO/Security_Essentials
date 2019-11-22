package com.thinkingsoft.security_essentials;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{
    EditText ocoreleusu, oclaaccusu;
    TextView oenlforreg;
    Button obtnlogin;
    private FirebaseAuth ofirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ocoreleusu = findViewById(R.id.coreleusu);
        oclaaccusu = findViewById(R.id.claaccusu);
        oenlforreg = findViewById(R.id.enlforreg);
        obtnlogin = findViewById(R.id.btnlogin);

        // Initialize Firebase Auth
        ofirebaseAuth = FirebaseAuth.getInstance();

        obtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this,HomeActivity.class));
                Autenticar_Usuario();
            }
        });
        oenlforreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ActivityRegister.class));
            }
        });

        //signOut();
        informationUser();
    }

    private void signIn(String emailSignIn, String passwordSignIn)
    {
        ofirebaseAuth.signInWithEmailAndPassword(emailSignIn, passwordSignIn)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signIn", "signInWithEmail:success");
                            //FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this, ActivityUser.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void Autenticar_Usuario(){
        if (validar_datos_entrada()) {
            signIn(ocoreleusu.getText().toString().trim(),oclaaccusu.getText().toString().trim());

        }
        else
        {
            Toast.makeText(MainActivity.this, "Complete la información en todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    private void informationUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(MainActivity.this, ActivityUser.class));
        }
    }
    private boolean validar_datos_entrada() {
        boolean estado = true;

        String coreleusudig = ocoreleusu.getText().toString();
        if (TextUtils.isEmpty(coreleusudig)) {
            ocoreleusu.setError("La clave de acceso es requerida.");
            estado = false;
        } else {
            ocoreleusu.setError(null);
        }

        String claaccusudig = oclaaccusu.getText().toString();
        if (TextUtils.isEmpty(claaccusudig)) {
            oclaaccusu.setError("La contraseña es requerida.");
            estado = false;
        }
        else
        {
            oclaaccusu.setError(null);
        }
        return estado;
    }


}
