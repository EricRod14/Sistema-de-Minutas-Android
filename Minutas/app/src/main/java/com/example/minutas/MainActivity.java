package com.example.minutas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    // Esto es el login de la app

    private EditText editTextCorreo;
    private EditText editTextClave;
    private DatabaseReference usuariosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");

        editTextCorreo = findViewById(R.id.ETcorreo);
        editTextClave = findViewById(R.id.ETclave);

        Button btnIniciarSesion = findViewById(R.id.btnLogin);
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }

        });
        FloatingActionButton fabMensaje = findViewById(R.id.FABMensaje);
        fabMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, datoseric.class);
                startActivity(intent);
            }
        });

    }

    private void iniciarSesion() {
        final String correo = editTextCorreo.getText().toString().trim();
        final String clave = editTextClave.getText().toString().trim();

        if (correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Ingresa correo y clave", Toast.LENGTH_SHORT).show();
            return;
        }

        usuariosRef.orderByChild("correo").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String claveFromDB = snapshot.child("clave").getValue(String.class);
                        if (clave.equals(claveFromDB)) {
                            Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                            String nombreUsuario = snapshot.child("nombre").getValue(String.class);

                            Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                            intent.putExtra("NOMBRE_USUARIO", nombreUsuario);
                            startActivity(intent);
                            finish(); // Cierra la actividad actual
                        } else {
                            // Clave incorrecta
                            Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error de base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
