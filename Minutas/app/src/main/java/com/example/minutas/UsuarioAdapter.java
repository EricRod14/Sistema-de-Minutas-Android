package com.example.minutas;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {

    private List<Usuario> listaUsuarios;

    public UsuarioAdapter(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public interface EditarClickListener {
        void onEditarClick(Usuario usuario, String funcion);
        // funcion es usado para indicar si se va a editar o borrar, si es null significa que se va a crear un usuario.
    }

    private EditarClickListener editarClickListener;

    public void setEditarClickListener(EditarClickListener listener) {
        this.editarClickListener = listener;
    }

    public void actualizarUsuarios(List<Usuario> nuevosUsuarios) {
        this.listaUsuarios = nuevosUsuarios;
        notifyDataSetChanged();
        Log.d("UsuarioAdapter", "Usuarios actualizados. Total: " + listaUsuarios.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_usuario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);
        holder.textViewNombre.setText(usuario.getNombre());

        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editarClickListener != null) {
                    editarClickListener.onEditarClick(usuario, "editar");
                }
            }
        });

        holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editarClickListener != null) {
                    editarClickListener.onEditarClick(usuario, "borrar");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewNombre;
        public Button btnEditar;
        public Button btnBorrar;

        public ViewHolder(View view) {
            super(view);
            textViewNombre = view.findViewById(R.id.textViewNombre);
            btnEditar = view.findViewById(R.id.btnEditar);
            btnBorrar = view.findViewById(R.id.btnBorrar);
        }
    }
}
