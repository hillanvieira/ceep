package br.com.alura.ceep.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.CoresEnum;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.PicColorAdapter;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class FormularioNotaActivity extends AppCompatActivity implements PicColorAdapter.OnItemClicked {

    private PicColorAdapter adapter;
    private ConstraintLayout constraintLayout;
    private int backGroundColor;
    static final String STATE_BACKGROUND = "Background color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
        constraintLayout = findViewById(R.id.activity_formulario_nota_id);
        RecyclerView listPicColor = findViewById(R.id.color_select_recyclerview);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            //Log.i("State", ""+savedInstanceState.getInt(STATE_BACKGROUND));
            backGroundColor = savedInstanceState.getInt(STATE_BACKGROUND);
        } else {
            constraintLayout.setBackgroundColor(0xFFFFFFFF);
            // Probably initialize members with default values for a new instance
        }

        constraintLayout.setBackgroundColor(backGroundColor);
        adapter = new PicColorAdapter(this);
        listPicColor.setAdapter(adapter);
        adapter.setOnClick(this);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i("BGCOLOR", ""+backGroundColor);
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
        setResult(CODIGO_RESULTADO_NOTA_CRIADA, resultadoInsercao);
    }

    private Nota criaNota() {
        EditText titulo = findViewById(R.id.formulario_nota_titulo);
        EditText descricao = findViewById(R.id.formulario_nota_descricao);
        return new Nota(titulo.getText().toString(),
                descricao.getText().toString(), backGroundColor);
    }

    private boolean ehMenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }

    @Override
    public void onItemClick(int position) {

        backGroundColor = adapter.cor(CoresEnum.values()[position]);
        constraintLayout.setBackgroundColor(backGroundColor);

//        Toast toast = Toast.makeText(this, CoresEnum.values()[position].name(), Toast.LENGTH_SHORT);
//        toast.show();

    }
}
