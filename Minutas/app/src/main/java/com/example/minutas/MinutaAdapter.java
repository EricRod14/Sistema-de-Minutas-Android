package com.example.minutas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MinutaAdapter extends RecyclerView.Adapter<MinutaAdapter.MinutaViewHolder> {

    private List<Minuta> minutas;

    public MinutaAdapter(List<Minuta> minutas) {
        this.minutas = minutas;
    }

    public interface EditarClickListener {
        void onEditarClick(Minuta minuta, String funcion);
        // funcion es usado para indicar si se va a editar o borrar, si es null significa que se va a crear una minuta.
    }

    private MinutaAdapter.EditarClickListener editarClickListener;

    public void setEditarClickListener(MinutaAdapter.EditarClickListener listener) {
        this.editarClickListener = listener;
    }

    @NonNull
    @Override
    public MinutaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_minuta, parent, false);
        return new MinutaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MinutaViewHolder holder, int position) {
        Minuta minuta = minutas.get(position);

        holder.textViewAsunto.setText(minuta.getAsunto());

        holder.btnEditar.setOnClickListener(v -> {
            if (editarClickListener != null) {
                editarClickListener.onEditarClick(minuta, "editar");
            }
        });
        holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editarClickListener != null) {
                    editarClickListener.onEditarClick(minuta, "borrar");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return minutas.size();
    }

    public void actualizarMinutas(List<Minuta> nuevasMinutas) {
        minutas.clear();
        minutas.addAll(nuevasMinutas);
        notifyDataSetChanged();
    }


    static class MinutaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAsunto;
        Button btnEditar;
        Button btnBorrar;

        MinutaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAsunto = itemView.findViewById(R.id.textViewAsunto);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
        }
    }
}
