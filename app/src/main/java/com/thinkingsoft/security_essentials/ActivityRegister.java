package com.thinkingsoft.security_essentials;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ActivityRegister extends AppCompatActivity
{
    private EditText ocoreleusu;
    private EditText oclaaccusu1;
    private EditText oclaaccusu2;

    private FirebaseAuth omAuth;
    private DatabaseReference odatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        omAuth = FirebaseAuth.getInstance();
        odatabase = FirebaseDatabase.getInstance().getReference();

        Button obtnregister = findViewById(R.id.btnregister);
        ocoreleusu = findViewById(R.id.coreleusu);
        oclaaccusu1 = findViewById(R.id.claaccusu1);
        oclaaccusu2 = findViewById(R.id.claaccusu2);
        TextView oenlforlog = findViewById(R.id.enlforlog);

        obtnregister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String coreleusudig = ocoreleusu.getText().toString().trim();
                String claaccusudig = oclaaccusu1.getText().toString().trim();

                if (validar_datos_registro())
                {
                    if (verificar_claves()){
                        try {
                            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                            SecretKeySpec clave = new SecretKeySpec("9876543210123456".getBytes(), "AES");
                            cipher.init(Cipher.ENCRYPT_MODE, clave);
                            String passEncriptada = android.util.Base64.encodeToString(cipher.doFinal(claaccusudig.getBytes(StandardCharsets.UTF_8)),32);
                            registrar_cuenta(coreleusudig , passEncriptada);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_LONG).show();
                        }
                    }
                    else Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_LONG).show();
                }
            }
        });

        oenlforlog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ActivityRegister.this,MainActivity.class));
            }
        });
    }


    private String obtenerNombreDeEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void registrar_Usuario(String uid, String email, String name) {
        Usuario usuario = new Usuario(uid, email, name);
        odatabase.child("USER").child(uid).setValue(usuario);
    }

    private void registrar_cuenta(final String correoRegistrar, String passwordRegistrar)
    {
        omAuth.createUserWithEmailAndPassword(correoRegistrar, passwordRegistrar)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = Objects.requireNonNull(task.getResult()).getUser().getUid();
                            String nombre = obtenerNombreDeEmail(correoRegistrar);
                            registrar_Usuario(uid, correoRegistrar,nombre);
                            registrar_Usuario_Sqlite(nombre, correoRegistrar, uid);
                            Toast.makeText(ActivityRegister.this, "Autenticacion exitosa", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ActivityRegister.this,ActivityUser.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ActivityRegister.this, "La autenticacion fallida", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void registrar_Usuario_Sqlite(String nombre, String correo, String uid)
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        try{
            dbHelper.insertRow(nombre, correo, uid);
        }
        catch (Exception e){
            Log.d("TAG", "registrar_Usuario_Sqlite: "+e.getMessage());
        }
    }

    private boolean validar_datos_registro()
    {
        boolean estado = true;

        String lcoreleusu = ocoreleusu.getText().toString();

        if (TextUtils.isEmpty(lcoreleusu))
        {
            ocoreleusu.setError("El correo electronico del usuario es requerido.");
            estado = false;
        }
        else
        {
            ocoreleusu.setError(null);
        }

        String lclaaccusu1 = oclaaccusu1.getText().toString();
        if (TextUtils.isEmpty(lclaaccusu1))
        {
            oclaaccusu1.setError("La Clave de usuario es requerida.");
            estado = false;
        }
        else
        {
            oclaaccusu1.setError(null);
        }

        String lclaaccusu2 = oclaaccusu2.getText().toString();
        if (TextUtils.isEmpty(lclaaccusu2))
        {
            oclaaccusu2.setError("La Clave de usuario es requerida.");
            estado = false;
        }
        else
        {
            oclaaccusu2.setError(null);
        }
        return estado;
    }

    private boolean verificar_claves()
    {
        boolean estado = true;
        String lclaaccusu1 = oclaaccusu1.getText().toString();
        String lclaaccusu2 = oclaaccusu2.getText().toString();
        if (!lclaaccusu1.equals(lclaaccusu2))
        {
            oclaaccusu1.setError("Las claves de acceso no coinciden.");
            oclaaccusu2.setError("Las claves de acceso no coinciden.");
            estado = false;
        }
        else
        {
            oclaaccusu1.setError(null);
            oclaaccusu2.setError(null);
        }
        return estado;
    }
}
