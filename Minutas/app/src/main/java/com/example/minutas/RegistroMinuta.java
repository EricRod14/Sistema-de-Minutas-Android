package com.example.minutas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistroMinuta extends AppCompatActivity {
    // El formulario para registro de minuta, es usado para crear, editar y borrar minutas, en
    // los dos ultimos casos, los datos son traidos de la BD hacia el formulario.
    private EditText editTextLugar;
    private EditText editTextAsunto;
    private EditText editTextObjetivo;
    private EditText editTextFecha;
    private EditText editTextAcuerdo;
    private EditText editTextParticipante;

    private DatabaseReference minutasRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearminuta);
        editTextAcuerdo = findViewById(R.id.ETAcuerdo);
        editTextParticipante = findViewById(R.id.ETParticipante);
        editTextLugar = findViewById(R.id.ETLugar);
        editTextAsunto = findViewById(R.id.ETAsunto);
        editTextObjetivo = findViewById(R.id.ETObjetivo);
        editTextFecha = findViewById(R.id.ETFecha);

        Button btnSubirMinuta = findViewById(R.id.BtnSubir);
        Button btnEditarMinuta = findViewById(R.id.BtnEditar);
        Button btnBorrarMinuta = findViewById(R.id.BtnBorrar);
        Intent intent = getIntent();
        String funcion = intent.getStringExtra("funcion");
        Log.d("RegistroMinuta", "estado:" + funcion);
        minutasRef = FirebaseDatabase.getInstance().getReference("minutas");

        if(funcion == null){ // Esto significa que se va a crear una minuta.
            btnEditarMinuta.setVisibility(View.GONE);
            btnBorrarMinuta.setVisibility(View.GONE);
        }
        else if(funcion != null && funcion.equals("editar")){
            btnSubirMinuta.setVisibility(View.GONE);
            btnBorrarMinuta.setVisibility(View.GONE);
        }else if(funcion != null && funcion.equals("borrar")){
            btnEditarMinuta.setVisibility(View.GONE);
            btnSubirMinuta.setVisibility(View.GONE);
        }        int minutaId = obtenerMinutaId();
        if (minutaId != 0) {
            cargarDatosMinuta(minutaId);
        }

        btnSubirMinuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarMinuta();
            }
        });

        btnEditarMinuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int minutaId = obtenerMinutaId();
                if (minutaId != 0) {
                    editarMinuta(minutaId);
                }
            }
        });

        btnBorrarMinuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int minutaId = obtenerMinutaId();
                if (minutaId != 0) {
                    borrarMinuta(minutaId);
                }

            }
        });
    }


    private void registrarMinuta() {
        minutasRef.orderByChild("id").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int nuevoIdMinuta = generarSiguienteId(dataSnapshot);

                String lugar = editTextLugar.getText().toString();
                String asunto = editTextAsunto.getText().toString();
                String objetivo = editTextObjetivo.getText().toString();
                String fecha = editTextFecha.getText().toString();
                String acuerdo = editTextAcuerdo.getText().toString();
                String participante = editTextParticipante.getText().toString();

                Minuta nuevaMinuta = new Minuta(nuevoIdMinuta, lugar, asunto, objetivo, fecha, acuerdo, participante);

                minutasRef.child(String.valueOf(nuevoIdMinuta)).setValue(nuevaMinuta)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegistroMinuta.this, "Minuta registrada exitosamente", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistroMinuta.this, "Error al registrar minuta", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private int generarSiguienteId(DataSnapshot dataSnapshot) {
        int maxId = 0;

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Minuta minuta = snapshot.getValue(Minuta.class);
            if (minuta != null && minuta.getId() > maxId) {
                maxId = minuta.getId();
            }
        }

        return maxId + 1;
    }

    private void cargarDatosMinuta(int id) {
        DatabaseReference minutaRef = FirebaseDatabase.getInstance().getReference("minutas").child(String.valueOf(id));
        minutaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Minuta minuta = dataSnapshot.getValue(Minuta.class);
                if (minuta != null) {
                    Log.d("RegistroMinuta", "ID de la minuta: " + id);

                    editTextLugar.setText(minuta.getLugar());
                    editTextAsunto.setText(minuta.getAsunto());
                    editTextObjetivo.setText(minuta.getObjetivo());
                    editTextFecha.setText(minuta.getFecha());
                    editTextAcuerdo.setText(minuta.getAcuerdo());
                    editTextParticipante.setText(minuta.getParticipante());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void editarMinuta(int minutaId) {
        String lugar = editTextLugar.getText().toString();
        String asunto = editTextAsunto.getText().toString();
        String objetivo = editTextObjetivo.getText().toString();
        String fecha = editTextFecha.getText().toString();
        String acuerdo = editTextAcuerdo.getText().toString();
        String participante = editTextParticipante.getText().toString();

        Minuta minutaEditada = new Minuta(minutaId, lugar, asunto, objetivo, fecha, acuerdo, participante);
        minutasRef.child(String.valueOf(minutaId)).setValue(minutaEditada)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistroMinuta.this, "Minuta editada exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroMinuta.this, "Error al editar minuta", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void borrarMinuta(int minutaId) {
        DatabaseReference minutaRef = FirebaseDatabase.getInstance().getReference("minutas").child(String.valueOf(minutaId));

        minutaRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistroMinuta.this, "Minuta borrada exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroMinuta.this, "Error al borrar minuta", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int obtenerMinutaId() {
        Intent intent = getIntent();
        return intent.getIntExtra("id", 0);
    }
}
