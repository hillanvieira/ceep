package br.com.alura.ceep.ui.recyclerview.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
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
    private ArrayList<Nota> notas = new ArrayList<>(0);

    public ListaNotasAdapter(Context context, @NonNull CeepDatabase db) {
        this.context = context;
        this.db = db;
        List<Nota> sourceNotasDB = db.notaDao().getAll();

        for (int i = 0; i < sourceNotasDB.size(); i++) {
            for (Nota nota : sourceNotasDB) {
                if (nota.position == i) {
                    this.notas.add(nota);
                }
            }
        }
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
        Nota nota = notas.get(position);
        holder.vincula(nota);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public void altera(Nota nota) {
        notas.set(nota.position, nota);
        db.notaDao().update(nota);
        Log.i("UPDATE_NOTA", nota.toString());
        notifyItemChanged(nota.position);
    }

    public void remove(int position) {
        notas.remove(position);
        db.notaDao().deleteByPosition(position);
        for (Nota nota : notas) {
            if (nota.position > position) {
                nota.position -= 1;
                db.notaDao().update(nota);
            }
        }
        notifyItemRemoved(position);
    }

    public void troca(int posicaoInicial, int posicaoFinal) {

        int notePosicaoInicial = notas.get(posicaoInicial).position;
        int notePosicaoFinal = notas.get(posicaoFinal).position;

        notas.get(posicaoInicial).position = notePosicaoFinal;
        notas.get(posicaoFinal).position = notePosicaoInicial;

        db.notaDao().update(notas.get(posicaoInicial));
        db.notaDao().update(notas.get(posicaoFinal));


        Collections.swap(notas, posicaoInicial, posicaoFinal);
        notifyItemMoved(posicaoInicial, posicaoFinal);
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
                    onItemClickListener.onItemClick(getAdapterPosition());
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
        notas.add(0,nota);
        db.notaDao().insertAll(nota);

        for(int i = 0; i < getItemCount(); i++){
            notas.get(i).position = i;
            db.notaDao().update(notas.get(i));
        }

        notifyItemInserted(0);
    }
}


