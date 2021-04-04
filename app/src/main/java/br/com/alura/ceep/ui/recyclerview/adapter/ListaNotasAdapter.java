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

        for (Nota nota : notas) {
            if(nota.position == position){
                holder.vincula(nota);
                break;
            }
        }

//        Nota nota = notas.get(position);
//        holder.vincula(nota);

    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public void altera(int posicao, Nota nota) {
        notas.set(nota.position, nota);
        db.notaDao().update(nota);
        Log.i("UPDATE NOTA", nota.toString());
        notifyDataSetChanged();
    }

    public void remove(int posicao) {

        db.notaDao().delete(notas.get(posicao));
        notas.remove(posicao);

        for(int i = 0; i < notas.size(); i++){
           notas.get(i).position = i;
            db.notaDao().update(notas.get(i));
        }

        notifyItemRemoved(posicao);
    }

    public void troca(int posicaoInicial, int posicaoFinal) {

        notas.get(posicaoInicial).position = posicaoFinal;
        notas.get(posicaoFinal).position = posicaoInicial;
        db.notaDao().update(notas.get(posicaoInicial),notas.get(posicaoFinal));

        Collections.swap(notas, posicaoInicial, posicaoFinal);
        notifyItemMoved(posicaoInicial, posicaoFinal);

        Log.i("TROCA NOTA", notas.get(posicaoInicial).toString());
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

        nota.position = notas.size();
        try {
            db.notaDao().insertAll(nota);
            notas = db.notaDao().getAll();
            Log.i("SALVA NOTA", nota.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }

}
