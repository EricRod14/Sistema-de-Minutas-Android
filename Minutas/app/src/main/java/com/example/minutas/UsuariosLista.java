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

public class UsuariosLista extends AppCompatActivity implements UsuarioAdapter.EditarClickListener {

    // Este codigo se encarga de obtener y visualizar las carreras en el spinner, dado que los
    // botones de Editar y Borrar se encuentran en el RecycleView,
    // tambien estan las funciones que los hacen funcionan, enviando a RegistrarUsuario.java un
    // "editar" o "borrar" para que interactue bien con el formulario de crear usuarios, si no se envia nada
    // significa que se va a crear un usuario.

    private Spinner spinnerCarrera;
    private RecyclerView recyclerViewUsuarios;
    private UsuarioAdapter usuarioAdapter;

    private List<String> carreras = new ArrayList<>();

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantallalistausuarios);

        spinnerCarrera = findViewById(R.id.spinnerCarrera);

        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));
        usuarioAdapter = new UsuarioAdapter(new ArrayList<>());
        usuarioAdapter.setEditarClickListener(this);
        recyclerViewUsuarios.setAdapter(usuarioAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        cargarCarreras();

        spinnerCarrera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCarrera = (String) parentView.getItemAtPosition(position);
                cargarUsuariosPorCarrera(selectedCarrera);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    @Override
    public void onEditarClick(Usuario usuario, String funcion) {
        if ("editar".equals(funcion)) {
            editarusuario(usuario);
        } else if ("borrar".equals(funcion)) {
            borrarUsuario(usuario);
        } else {
            crearusuario(usuario);
        }
    }
    private void borrarUsuario(Usuario usuario) {

        Intent intent = new Intent(this, RegistroUsuario.class);

        intent.putExtra("id", usuario.getId());
        intent.putExtra("nombre", usuario.getNombre());
        intent.putExtra("apellidoP", usuario.getApellidoP());
        intent.putExtra("apellidoM", usuario.getApellidoM());
        intent.putExtra("correo", usuario.getCorreo());
        intent.putExtra("clave", usuario.getClave());

        if (usuario.getCarrera() != null) {
            intent.putExtra("carrera", usuario.getCarrera().getCarrera());
        }

        if (usuario.getRol() != null) {
            intent.putExtra("rol", usuario.getRol().getRol());
        }
        intent.putExtra("funcion","borrar");
        startActivity(intent);
    }

    private void editarusuario(Usuario usuario) {
        Intent intent = new Intent(this, RegistroUsuario.class);

        intent.putExtra("id", usuario.getId());
        intent.putExtra("nombre", usuario.getNombre());
        intent.putExtra("apellidoP", usuario.getApellidoP());
        intent.putExtra("apellidoM", usuario.getApellidoM());
        intent.putExtra("correo", usuario.getCorreo());
        intent.putExtra("clave", usuario.getClave());

        if (usuario.getCarrera() != null) {
            intent.putExtra("carrera", usuario.getCarrera().getCarrera());
        }

        if (usuario.getRol() != null) {
            intent.putExtra("rol", usuario.getRol().getRol());
        }
        intent.putExtra("funcion","editar");
        startActivity(intent);
    }

    private void cargarCarreras() {
        DatabaseReference carrerasRef = databaseReference.child("carreras");

        carrerasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carreras.clear();

                for (DataSnapshot carreraSnapshot : dataSnapshot.getChildren()) {
                    Carrera carrera = carreraSnapshot.getValue(Carrera.class);
                    if (carrera != null) {
                        carreras.add(carrera.getCarrera().toString());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        UsuariosLista.this,
                        android.R.layout.simple_spinner_item,
                        carreras
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCarrera.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UsuariosLista", "Error al cargar carreras: " + databaseError.getMessage());
            }
        });
    }

    private void cargarUsuariosPorCarrera(String carrera) {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
        Query query = usuariosRef.orderByChild("carrera/carrera").equalTo(carrera);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Usuario> usuarios = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    if (usuario != null) {
                        usuarios.add(usuario);
                    }
                }

                usuarioAdapter.actualizarUsuarios(usuarios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void crearusuario(Usuario usuario) {
        Intent intent = new Intent(this, RegistroUsuario.class);

        startActivity(intent);
    }




}