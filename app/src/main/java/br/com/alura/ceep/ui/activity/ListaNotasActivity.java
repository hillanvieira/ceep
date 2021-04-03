package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import br.com.alura.ceep.R;
import br.com.alura.ceep.database.CeepDatabase;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.alura.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {
    private Menu optionsMenu;
    private ListaNotasAdapter adapter;
    private CeepDatabase db;
    private int listViewOption;
    private SharedPreferences sharedPref;

    public static final String TITULO_APPBAR = "Notas";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        optionsMenu = menu;
        setListOrGrid();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        listViewOption = sharedPref.getInt("ListViewOption", 0);
        setListOrGrid();
        return super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case R.id.list_ic:
//              break;
//            case R.id.grid_ic:
//              break;
//        }
    }

    public void setListOrGrid() {

        SharedPreferences.Editor editor = sharedPref.edit();

        if (listViewOption == 0) {
            optionsMenu.findItem(R.id.list_ic).setVisible(false);
            optionsMenu.findItem(R.id.grid_ic).setVisible(true);
            editor.putInt("ListViewOption", 1);
            editor.commit();
            configuraRecyclerView(listViewOption);
        } else {
            optionsMenu.findItem(R.id.grid_ic).setVisible(false);
            optionsMenu.findItem(R.id.list_ic).setVisible(true);
            editor.putInt("ListViewOption", 0);
            editor.commit();
            configuraRecyclerView(listViewOption);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR);

        sharedPref = ListaNotasActivity.this.getPreferences(Context.MODE_PRIVATE);
        listViewOption = sharedPref.getInt("ListViewOption", 0);

        db = Room.databaseBuilder(getApplicationContext(),
                CeepDatabase.class, "database-ceep").allowMainThreadQueries().build();

        configuraRecyclerView(listViewOption);
        configuraBotaoInsereNota();

    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota =
                new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota,
                CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ehResultadoInsereNota(requestCode, data)) {

            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            }

        }

        if (ehResultadoAlteraNota(requestCode, data)) {
            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if (ehPosicaoValida(posicaoRecebida)) {
                    altera(notaRecebida, posicaoRecebida);
                }
            }
        }
    }

    private void altera(Nota nota, int posicao) {
        //new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoInsereNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) &&
                temNota(data);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }


    private boolean ehResultadoAlteraNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                temNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private void adiciona(Nota nota) {
        adapter.adiciona(nota);
    }

    private boolean temNota(Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(int listViewOption) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        if (listViewOption == 0) {
            listaNotas.setLayoutManager(linearLayoutManager);
        } else {
            listaNotas.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView
        }

        configuraAdapter(listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, db);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int posicao) {
                Nota nota = db.notaDao().getAll().get(posicao);
                vaiParaFormularioNotaActivityAltera(nota, posicao);
            }
        });
    }

}
