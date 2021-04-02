package br.com.alura.ceep.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class ListaNotasActivity extends AppCompatActivity {
    Menu optionsMenu;
    private ListaNotasAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        optionsMenu = menu;

        SharedPreferences sharedPref = ListaNotasActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int ListViewOption = sharedPref.getInt("ListViewOption", 0);
        setListOrGrid(editor, ListViewOption);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        SharedPreferences sharedPref = ListaNotasActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int ListViewOption = sharedPref.getInt("ListViewOption", 0);
        setListOrGrid(editor, ListViewOption);

        return super.onOptionsItemSelected(item);

//        switch (item.getItemId()) {
//            case R.id.list_ic:
//
//                item.setVisible(false);
//                optionsMenu.findItem(R.id.grid_ic).setVisible(true);
//                editor.putInt("ListViewOption", 1);
//                editor.commit();
//                configuraRecyclerView(ListViewOption);
//                return true;
//
//            case R.id.grid_ic:
//
//                item.setVisible(false);
//                optionsMenu.findItem(R.id.list_ic).setVisible(true);
//                editor.putInt("ListViewOption", 0);
//                editor.commit();
//                configuraRecyclerView(ListViewOption);
//
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }

    }

    public void setListOrGrid(SharedPreferences.Editor editor, int listViewOption) {
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

        SharedPreferences sharedPref = ListaNotasActivity.this.getPreferences(Context.MODE_PRIVATE);
        int ListViewOption = sharedPref.getInt("ListViewOption", 0);

        configuraRecyclerView(ListViewOption);
        configuraBotaoInsereNota();

    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivity();
            }
        });
    }

    private void vaiParaFormularioNotaActivity() {
        Intent iniciaFormularioNota =
                new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota,
                CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ehResultadoComNota(requestCode, resultCode, data)) {
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            adiciona(notaRecebida);
        }
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean ehResultadoComNota(int requestCode, int resultCode, Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) &&
                ehCodigoResultadoNotaCriada(resultCode) &&
                temNota(data);
    }

    private boolean temNota(Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean ehCodigoResultadoNotaCriada(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(int listViewOption) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        List<Nota> todasNotas = pegaTodasNotas();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        if (listViewOption == 0) {
            listaNotas.setLayoutManager(linearLayoutManager);
        } else {
            listaNotas.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView
        }

        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
    }

}
