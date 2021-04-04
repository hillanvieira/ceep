package br.com.alura.ceep.ui.recyclerview.adapter;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.database.CeepDatabase;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {

    private final Context context;
    private CeepDatabase db;
    private OnItemClickListener onItemClickListener;
    private List<Nota> notas;

    public ListaNotasAdapter(Context context, CeepDatabase db) {
        this.context = context;
        this.db = db;
        this.notas = db.notaDao().getAll();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ListaNotasAdapter.NotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaNotasAdapter.NotaViewHolder holder, int position) {

        int rPosition = getItemCount() - position - 1;
        //Loop que organiza as notas conforme as posisoes salvas nelas
        for (Nota nota : notas) {
            if (nota.position == rPosition) {
                holder.vincula(nota);
            }
        }

//        Nota nota = notas.get(position);
//        holder.vincula(nota);

    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public void altera(Nota nota) {

        notas.set(nota.position, nota);
        db.notaDao().update(nota);
        Log.i("UPDATE_NOTA", nota.toString());
        notifyDataSetChanged();

    }

    public void remove(int position) {

        int rPosition = getItemCount() - position - 1;
        int notePosition = notas.get(rPosition).position;

        Log.i("REMOVE_NOTA", "position Adapter: " + position + " title: " + notas.get(rPosition).titulo + " position on note: " + notas.get(rPosition).position);

        notas.remove(rPosition);
        notifyItemRemoved(position);

        db.notaDao().deleteByPosition(notePosition);

        for (Nota nota : notas) {
            if(nota.position > notePosition){
                nota.position -= 1;
                db.notaDao().update(nota);
            }
        }
        notas = db.notaDao().getAll();

      // notifyDataSetChanged();

    }

    public void troca(int posicaoInicial, int posicaoFinal) {
//
//        notas.get(posicaoInicial).position = posicaoFinal;
//        notas.get(posicaoFinal).position = posicaoInicial;
//        db.notaDao().update(notas.get(posicaoInicial),notas.get(posicaoFinal));
//
//        Collections.swap(notas, posicaoInicial, posicaoFinal);
//        notifyItemMoved(posicaoInicial, posicaoFinal);
//
//        Log.i("TROCA NOTA", notas.get(posicaoInicial).toString());
    }

    class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;
        private final ConstraintLayout constraintLayout;

        public NotaViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);
            constraintLayout = itemView.findViewById(R.id.item_nota_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int rAdpterPosition = getItemCount() - getAdapterPosition() - 1;
                    onItemClickListener.onItemClick(rAdpterPosition);
                }
            });
        }

        public void vincula(Nota nota) {
            preencheCampo(nota);
        }

        private void preencheCampo(Nota nota) {
            titulo.setText(nota.titulo);
            descricao.setText(nota.descricao);
            constraintLayout.setBackgroundColor(nota.color);
        }
    }

    public void adiciona(Nota nota) {

        notas = db.notaDao().getAll();
        nota.position = notas.size();

        if (db.notaDao().findByPosition(nota.position) == null) {
            db.notaDao().insertAll(nota);
            notas = db.notaDao().getAll();
            Log.i("SALVA NOTA", nota.toString());
        }

        notifyDataSetChanged();
    }
}
