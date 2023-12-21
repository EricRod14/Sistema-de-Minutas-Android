package com.example.minutas;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EnviarCorreo extends AppCompatActivity {

    // Este codigo se encarga de mandar los datos que el usuario llene en el formulario a ModuloEmail.java.
    private Spinner spinnerCarreras;
    private EditText etAsunto;
    private EditText etCuerpo;
    private EditText etLugar;
    private EditText etFecha;
    private EditText etHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enviarcorreo);

        spinnerCarreras = findViewById(R.id.spinnerCarreras);
        cargarCarrerasEnSpinner();

        Button btnEnviarCorreo = findViewById(R.id.btnEnviarCorreo);
        btnEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarCorreo();
            }
        });
    }

    private void cargarCarrerasEnSpinner() {
        DatabaseReference carrerasRef = FirebaseDatabase.getInstance().getReference("carreras");

        carrerasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> carreras = new ArrayList<>();

                for (DataSnapshot carreraSnapshot : dataSnapshot.getChildren()) {
                    Carrera carrera = carreraSnapshot.getValue(Carrera.class);
                    if (carrera != null) {
                        carreras.add(carrera.getCarrera());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        EnviarCorreo.this,
                        android.R.layout.simple_spinner_item,
                        carreras
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCarreras.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void enviarCorreo() {
        String carreraSeleccionada = spinnerCarreras.getSelectedItem().toString();

        etLugar = findViewById(R.id.etLugar);
        etFecha = findViewById(R.id.etFecha);
        etAsunto = findViewById(R.id.etAsunto);
        etCuerpo = findViewById(R.id.etCuerpo);
        etHora = findViewById(R.id.etHora);

        final String lugar = etLugar.getText().toString().trim();
        final String fecha = etFecha.getText().toString().trim();
        final String hora = etHora.getText().toString().trim();
        final String asunto = etAsunto.getText().toString().trim();
        final String cuerpo = etCuerpo.getText().toString().trim();

        if (TextUtils.isEmpty(lugar) || TextUtils.isEmpty(fecha) ||  TextUtils.isEmpty(hora) |TextUtils.isEmpty(asunto) || TextUtils.isEmpty(cuerpo)) {
            Toast.makeText(getApplicationContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
        Query query = usuariosRef.orderByChild("carrera/carrera").equalTo(carreraSeleccionada);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    int correosEnviados = 0;

                    for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                        Usuario usuario = usuarioSnapshot.getValue(Usuario.class);
                        Log.d("EnviarCorreo", "ID del usuario: " + usuarioSnapshot.getKey());
                        if (usuario != null) {
                            String destinatario = usuario.getCorreo();

                            Log.d("EnviarCorreo", "Correo del usuario: " + destinatario);

                            ModuloEmail.sendEmail(destinatario, asunto, cuerpo, fecha, lugar, hora);
                            correosEnviados++;
                        }
                    }
                    if (correosEnviados > 0) {
                        Toast.makeText(getApplicationContext(), "Correos enviados exitosamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error en la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
