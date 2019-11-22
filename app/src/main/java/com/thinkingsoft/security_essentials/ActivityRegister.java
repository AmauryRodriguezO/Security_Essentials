package com.thinkingsoft.security_essentials;

import android.annotation.SuppressLint;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ActivityRegister extends AppCompatActivity
{
    Button obtnregister;
    private EditText ocoreleusu;
    private EditText oclaaccusu1;
    private EditText oclaaccusu2;
    private TextView oenlforlog;

    private FirebaseAuth omAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        omAuth = FirebaseAuth.getInstance();

        ocoreleusu = findViewById(R.id.coreleusu);
        oclaaccusu1 = findViewById(R.id.claaccusu1);
        oclaaccusu2 = findViewById(R.id.claaccusu2);
        obtnregister = findViewById(R.id.btnregister);
        oenlforlog = findViewById(R.id.enlforlog);

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
                        //SecretKeySpec palabraclave = generar_clave();
                        try {
                            //byte[] passEncrypted;
                            //passEncrypted = cifrar_mensaje(claaccusudig, palabraclave);
                            //String passDecrypted = descifrar_mensaje(passEncrypted, palabraclave);
                            registrar_cuenta(coreleusudig , claaccusudig);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage() + "  error", Toast.LENGTH_LONG).show();
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


    public SecretKeySpec generar_clave()
    {
        String clave = "9876543210123456";
        return new SecretKeySpec(clave.getBytes(), "AES");
    }

    public byte[] cifrar_mensaje(String message, SecretKey key) throws Exception
    {
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
    }

    @SuppressLint("GetInstance")
    public static String descifrar_mensaje(byte[] cipherText, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
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


    private boolean verificar_claves(){
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

    private void registrar_cuenta(String emailCreate, String passwordCreate)
    {
      omAuth.createUserWithEmailAndPassword(emailCreate, passwordCreate)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(ActivityRegister.this, "OnComplete", Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(ActivityRegister.this, "Authentication exist.", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ActivityRegister.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
