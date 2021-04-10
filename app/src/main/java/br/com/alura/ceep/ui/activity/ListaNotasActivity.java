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

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.alura.ceep.R;
import br.com.alura.ceep.database.CeepDatabase;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.alura.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;

public class ListaNotasActivity extends AppCompatActivity {
    private Menu optionsMenu;
    private ListaNotasAdapter adapter;
    private CeepDatabase db;

    public static final String TITULO_APPBAR = "Notas";

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        optionsMenu = menu;
        menuItemInit();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // toggleListOrGrid();

        switch (item.getItemId()) {
            case R.id.list_ic:
            case R.id.grid_ic:
                toggleListOrGrid();
                break;
            case R.id.feed_back_ic:

                Intent intent =
                        new Intent(this,
                                FormularioFeedBackActivity.class);
                startActivity(intent);

                Log.i("FEEDBACK", "Clicked");
                break;
        }

        return super.onOptionsItemSelected(item);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        setTitle(TITULO_APPBAR);

        db = Room.databaseBuilder(getApplicationContext(),
                CeepDatabase.class, "database-ceep").allowMainThreadQueries().build();

        configuraBotaoInsereNota();

    }

    private void toggleListOrGrid() {
        SharedPreferences sharedPref = this.getSharedPreferences("br.com.alura.ceep", Context.MODE_PRIVATE);
        int listViewOption;
        listViewOption = sharedPref.getInt("ListViewOption", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (listViewOption == 0) {
//            optionsMenu.findItem(R.id.list_ic).setVisible(true);
//            optionsMenu.findItem(R.id.grid_ic).setVisible(false);
            editor.putInt("ListViewOption", 1);
            editor.commit();

            Intent intent = getIntent();
            finish();
            startActivity(intent);

        } else if (listViewOption == 1) {
//            optionsMenu.findItem(R.id.grid_ic).setVisible(true);
//            optionsMenu.findItem(R.id.list_ic).setVisible(false);

            editor.putInt("ListViewOption", 0);
            editor.commit();

            Intent intent = getIntent();
            finish();
            startActivity(intent);

        }

//        editor.commit();
//        listViewOption = sharedPref.getInt("ListViewOption", 0);
//        configuraRecyclerView(listViewOption);
    }

    private void menuItemInit() {
        SharedPreferences sharedPref = this.getSharedPreferences("br.com.alura.ceep", Context.MODE_PRIVATE);
        int listViewOption = sharedPref.getInt("ListViewOption", 0);

        if (listViewOption == 0) {
            optionsMenu.findItem(R.id.list_ic).setVisible(false);
            optionsMenu.findItem(R.id.grid_ic).setVisible(true);
            configuraRecyclerView(0);
        } else if (listViewOption == 1) {
            optionsMenu.findItem(R.id.grid_ic).setVisible(false);
            optionsMenu.findItem(R.id.list_ic).setVisible(true);
            configuraRecyclerView(1);
        }
    }


    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(view -> vaiParaFormularioNotaActivityInsere());
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota =
                new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota,
                CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("REQUEST ACTIVITY RESULT", "" + requestCode + "  " + data + " " + requestCode);

        if (ehResultadoInsereNota(requestCode, data)) {

            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            }
        }

        if (ehResultadoAlteraNota(requestCode, data)) {
            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                altera(notaRecebida);
            }
        }
    }

    private void altera(Nota nota) {
        adapter.altera(nota);
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

        if (listViewOption == 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            listaNotas.setLayoutManager(linearLayoutManager);
        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
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

                Nota nota = db.notaDao().findByPosition(posicao);

                vaiParaFormularioNotaActivityAltera(nota);
            }
        });

    }

}
