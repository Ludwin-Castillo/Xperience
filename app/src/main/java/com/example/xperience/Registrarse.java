package com.example.xperience;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registrarse extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registrarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmpassword);
        registrarButton = findViewById(R.id.buttonsesion);

        registrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });
    }

    private void validarCampos() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mostrarMensaje("Ingrese un correo electrónico válido");
            return;
        }

        if (password.isEmpty() || password.length() != 8 || !contieneNumerosYLetras(password)) {
            mostrarMensaje("La contraseña debe tener exactamente 8 caracteres y contener números y letras");
            return;
        }

        if (!confirmPassword.equals(password)) {
            mostrarMensaje("Las contraseñas no coinciden");
            return;
        }

        registrarUsuario(email, password);
    }

    private boolean contieneNumerosYLetras(String str) {
        return str.matches(".*\\d.*") && str.matches(".*[a-zA-Z].*");
    }

    private void registrarUsuario(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Registrarse.this, "Se ha enviado un correo de verificación", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Registrarse.this, Login.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(Registrarse.this, "Fallo en el envío del correo de verificación", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Registrarse.this, "Fallo en el registro: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void netx(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}