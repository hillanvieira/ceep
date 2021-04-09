package br.com.alura.ceep.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.CoresEnum;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.PicColorAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;


public class FormularioNotaActivity extends AppCompatActivity implements OnItemClickListener {

    static final String STATE_BACKGROUND = "Background color";
    public static final String TITULO_APPBAR_INSERE = "Insere nota";
    public static final String TITULO_APPBAR_ALTERA = "Altera nota";
    private RecyclerView listPicColor;
    private EditText titulo;
    private EditText descricao;
    private PicColorAdapter adapter;
    private ConstraintLayout constraintLayout;
    private int backGroundColor;
    private String uid;
    private int position;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(TITULO_APPBAR_INSERE);
        inicializaCampos();

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            backGroundColor = savedInstanceState.getInt(STATE_BACKGROUND);
            constraintLayout.setBackgroundColor(backGroundColor);
        } else {
            constraintLayout.setBackgroundColor(0xFFFFFFFF);
            // Probably initialize members with default values for a new instance
        }

        Intent dadosRecebidos = getIntent();
        if (dadosRecebidos.hasExtra(CHAVE_NOTA)) {
            setTitle(TITULO_APPBAR_ALTERA);
            Nota notaRecebida = (Nota) dadosRecebidos
                    .getSerializableExtra(CHAVE_NOTA);
            preencheCampos(notaRecebida);
        }

        adapter = new PicColorAdapter(this);
        listPicColor.setAdapter(adapter);
        adapter.setOnClick(this);

    }

    private void inicializaCampos() {
        constraintLayout = findViewById(R.id.activity_formulario_nota_id);
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
        listPicColor = findViewById(R.id.color_select_recyclerview);

    }

    private void preencheCampos(Nota notaRecebida) {
        titulo.setText(notaRecebida.titulo);
        descricao.setText(notaRecebida.descricao);
        constraintLayout.setBackgroundColor(notaRecebida.color);
        backGroundColor = notaRecebida.color;
        uid = notaRecebida.uid;
        position = notaRecebida.position;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i("BGCOLOR", "" + backGroundColor);
        outState.putInt(STATE_BACKGROUND, backGroundColor);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ehMenuSalvaNota(item)) {
            Nota notaCriada = criaNota();
            retornaNota(notaCriada);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    private Nota criaNota() {

        if (this.getTitle().equals(TITULO_APPBAR_INSERE)) {
            return new Nota(titulo.getText().toString(),
                    descricao.getText().toString(), backGroundColor, 0);
        } else {
            return new Nota(uid,titulo.getText().toString(),
                    descricao.getText().toString(), backGroundColor, position);
        }

    }

    private boolean ehMenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }

    @Override
    public void onItemClick(int position) {
        backGroundColor = adapter.cor(CoresEnum.values()[position]);
        constraintLayout.setBackgroundColor(backGroundColor);
    }

}
