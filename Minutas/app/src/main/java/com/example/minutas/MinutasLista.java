package com.example.minutas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MinutasLista extends AppCompatActivity implements MinutaAdapter.EditarClickListener {

    private Spinner spinnerAsunto;
    private RecyclerView recyclerViewMinutas;
    private MinutaAdapter minutaAdapter;

    private DatabaseReference minutasRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantallalistaminutas);

        spinnerAsunto = findViewById(R.id.spinnerAsunto);
        recyclerViewMinutas = findViewById(R.id.recyclerViewMinutas);
        recyclerViewMinutas.setLayoutManager(new LinearLayoutManager(this));

        minutaAdapter = new MinutaAdapter(new ArrayList<>());
        minutaAdapter.setEditarClickListener(this);

        recyclerViewMinutas.setAdapter(minutaAdapter);

        minutasRef = FirebaseDatabase.getInstance().getReference("minutas");

        cargarAsuntosMinutas();

        spinnerAsunto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedAsunto = (String) parentView.getItemAtPosition(position);
                cargarMinutasPorAsunto(selectedAsunto);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void cargarAsuntosMinutas() {
        minutasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> asuntos = new ArrayList<>();

                for (DataSnapshot minutaSnapshot : dataSnapshot.getChildren()) {
                    Minuta minuta = minutaSnapshot.getValue(Minuta.class);
                    if (minuta != null && minuta.getAsunto() != null) {
                        asuntos.add(minuta.getAsunto());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        MinutasLista.this,
                        android.R.layout.simple_spinner_item,
                        asuntos
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAsunto.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MinutasLista", "Error al cargar asuntos: " + databaseError.getMessage());
            }
        });
    }

    private void cargarMinutasPorAsunto(String asunto) {
        Query query = minutasRef.orderByChild("asunto").equalTo(asunto);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Minuta> minutas = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Minuta minuta = snapshot.getValue(Minuta.class);
                    if (minuta != null) {
                        minutas.add(minuta);
                    }
                }

                minutaAdapter.actualizarMinutas(minutas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void crearminuta(Minuta minuta) {
        Intent intent = new Intent(this, RegistroMinuta.class);

        intent.putExtra("id", minuta.getId());
        intent.putExtra("asunto", minuta.getAsunto());

        startActivity(intent);
    }

    private void borrarminuta(Minuta minuta) {

        Intent intent = new Intent(this, RegistroMinuta.class);

        intent.putExtra("id", minuta.getId());
        intent.putExtra("asunto", minuta.getAsunto());

        intent.putExtra("funcion","borrar");
        startActivity(intent);
    }

    private void editarminuta(Minuta minuta) {
        Intent intent = new Intent(this, RegistroMinuta.class);

        intent.putExtra("id", minuta.getId());
        intent.putExtra("asunto", minuta.getAsunto());

        intent.putExtra("funcion","editar");
        startActivity(intent);
    }

    @Override
    public void onEditarClick(Minuta minuta, String funcion) {
        if ("editar".equals(funcion)) {
            editarminuta(minuta);
        } else if ("borrar".equals(funcion)) {
            borrarminuta(minuta);
        } else {
            crearminuta(minuta);
        }
    }
}
