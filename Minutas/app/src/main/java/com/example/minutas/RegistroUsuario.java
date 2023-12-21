package com.example.minutas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.List;

public class RegistroUsuario extends AppCompatActivity {
    // El formulario para registro de usuario, es usado para crear, editar y borrar usuarios, en
    // los dos ultimos casos, los datos son traidos de la BD hacia el formulario.
    private EditText editTextNombre;
    private EditText editTextApellidoP;
    private EditText editTextApellidoM;
    private EditText editTextCorreo;
    private EditText editTextClave;
    private Spinner spinnerCarreras;
    private Spinner spinnerRoles;
    private DatabaseReference usuariosRef;
    private DatabaseReference carrerasRef;
    private DatabaseReference rolesRef;

    private List<Carrera> listaCarreras;
    private List<Rol> listaRoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearusuario);

        usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
        carrerasRef = FirebaseDatabase.getInstance().getReference("carreras");
        rolesRef = FirebaseDatabase.getInstance().getReference("roles");

        editTextNombre = findViewById(R.id.ETLugar);
        editTextApellidoP = findViewById(R.id.ETFecha);
        editTextApellidoM = findViewById(R.id.ETObjetivo);
        editTextCorreo = findViewById(R.id.ETAsunto);
        editTextClave = findViewById(R.id.ETParticipante);
        spinnerCarreras = findViewById(R.id.SPcarrera);
        spinnerRoles = findViewById(R.id.SProl);

        listaCarreras = new ArrayList<>();
        listaRoles = new ArrayList<>();

        obtenerCarreras();
        obtenerRoles();

        Button btnSubir = findViewById(R.id.BtnSubir);
        Button btnEditar = findViewById(R.id.BtnEditar);
        Button btnBorrar = findViewById(R.id.BtnBorrar);
        Intent intent = getIntent();
        String funcion = intent.getStringExtra("funcion");
        Log.d("RegistroUsuario", "estado:" + funcion);
        if(funcion == null){ // Esto significa que se va a crear un usuario.
            btnEditar.setVisibility(View.GONE);
            btnBorrar.setVisibility(View.GONE);
        }
        else if(funcion != null && funcion.equals("editar")){
            btnSubir.setVisibility(View.GONE);
            btnBorrar.setVisibility(View.GONE);
        }else if(funcion != null && funcion.equals("borrar")){
            btnEditar.setVisibility(View.GONE);
            btnSubir.setVisibility(View.GONE);
        }
        int userId = obtenerUserId();
        if (userId != 0) {
            cargarDatosUsuario(userId);
        }

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = obtenerUserId();
                if (userId != 0) {
                    editarUsuario(userId);
                }
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = obtenerUserId();
                if (userId != 0) {
                    borrarUsuario(userId);
                }
            }
        });
    }

    private void obtenerCarreras() {
        carrerasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCarreras.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Carrera carrera = snapshot.getValue(Carrera.class);
                    if (carrera != null) {
                        listaCarreras.add(carrera);
                    }
                }
                cargarCarrerasEnSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegistroUsuario.this, "Error al obtener carreras", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void cargarCarrerasEnSpinner() {
        ArrayAdapter<Carrera> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaCarreras);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarreras.setAdapter(adapter);
    }

    private void obtenerRoles() {
        rolesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaRoles.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rol rol = snapshot.getValue(Rol.class);
                    if (rol != null) {
                        listaRoles.add(rol);
                    }
                }
                cargarRolesEnSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegistroUsuario.this, "Error al obtener roles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarRolesEnSpinner() {
        ArrayAdapter<Rol> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaRoles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoles.setAdapter(adapter);
    }

    private void registrarUsuario() {
        String correo = editTextCorreo.getText().toString().trim();

        usuariosRef.orderByChild("correo").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(RegistroUsuario.this, "Ya existe un usuario con este correo", Toast.LENGTH_SHORT).show();
                } else {
                    usuariosRef.orderByChild("id").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int nuevoIdUsuario = generarSiguienteId(dataSnapshot);

                            String nombre = editTextNombre.getText().toString();
                            String apellidoP = editTextApellidoP.getText().toString();
                            String apellidoM = editTextApellidoM.getText().toString();
                            String clave = editTextClave.getText().toString();
                            String correo = editTextCorreo.getText().toString().trim();

                            Carrera carreraSeleccionada = (Carrera) spinnerCarreras.getSelectedItem();
                            Rol rolSeleccionado = (Rol) spinnerRoles.getSelectedItem();

                            Usuario nuevoUsuario = new Usuario(nuevoIdUsuario, nombre, apellidoP, apellidoM, correo, clave, carreraSeleccionada, rolSeleccionado);

                            usuariosRef.child(String.valueOf(nuevoIdUsuario)).setValue(nuevoUsuario)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegistroUsuario.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegistroUsuario.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(RegistroUsuario.this, "Error al obtener el último ID de usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegistroUsuario.this, "Error al verificar el correo", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private int generarSiguienteId(DataSnapshot dataSnapshot) {
        int maxId = 0;

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Usuario usuario = snapshot.getValue(Usuario.class);
            if (usuario != null && usuario.getId() > maxId) {
                maxId = usuario.getId();
            }
        }

        return maxId + 1;
    }



    private void cargarDatosUsuario(int id) {
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios").child(String.valueOf(id));
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                if (usuario != null) {
                    Log.d("RegistroUsuario", "ID del usuario: " + id);

                    editTextNombre.setText(usuario.getNombre());
                    editTextApellidoP.setText(usuario.getApellidoP());
                    editTextApellidoM.setText(usuario.getApellidoM());
                    editTextCorreo.setText(usuario.getCorreo());
                    editTextClave.setText(usuario.getClave());

                    seleccionarCarreraEnSpinner(usuario.getCarrera().getCarrera());
                    seleccionarRolEnSpinner(usuario.getRol().getRol());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void seleccionarCarreraEnSpinner(String carrera) {
        int index = -1;
        for (int i = 0; i < listaCarreras.size(); i++) {
            if (listaCarreras.get(i).getCarrera().equals(carrera)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            spinnerCarreras.setSelection(index);
        }
    }

    private void seleccionarRolEnSpinner(String rol) {
        int index = -1;
        for (int i = 0; i < listaRoles.size(); i++) {
            if (listaRoles.get(i).getRol().equals(rol)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            spinnerRoles.setSelection(index);
        }
    }

    private void editarUsuario(int userId) {

        String nombre = editTextNombre.getText().toString();
        String apellidoP = editTextApellidoP.getText().toString();
        String apellidoM = editTextApellidoM.getText().toString();
        String correo = editTextCorreo.getText().toString();
        String clave = editTextClave.getText().toString();
        Carrera carreraSeleccionada = (Carrera) spinnerCarreras.getSelectedItem();
        Rol rolSeleccionado = (Rol) spinnerRoles.getSelectedItem();
        Usuario usuarioEditado = new Usuario(userId, nombre, apellidoP, apellidoM, correo, clave, carreraSeleccionada, rolSeleccionado);
        usuariosRef.child(String.valueOf(userId)).setValue(usuarioEditado)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistroUsuario.this, "Usuario editado exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroUsuario.this, "Error al editar usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void borrarUsuario(int userId) {
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios").child(String.valueOf(userId));

        usuarioRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistroUsuario.this, "Usuario borrado exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroUsuario.this, "Error al borrar usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int obtenerUserId() {
        Intent intent = getIntent();
        return intent.getIntExtra("id", 0);
    }

}