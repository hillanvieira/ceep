package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import br.com.alura.ceep.R;

public class FormularioFeedBackActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText messageEditText;
    TextView emailTextView;
    TextView messageTextView;
    TextView infoTextView;

    String info = "Envie duvidas os sugestões de sua experiência com o Ceep. Usaremos as informações fornecidas para melhorar a experiência da App.";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        finish();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_feedback);
        setTitle("FeedBack");

        configuraFormulario();


    }

    private void configuraFormulario() {
        emailEditText = findViewById(R.id.feedback_email_edittext);
        messageEditText = findViewById(R.id.feedback_message_edittext);
        emailTextView = findViewById(R.id.feedback_email_textview);
        messageTextView = findViewById(R.id.feedback_message_textview);
        infoTextView = findViewById(R.id.feedback_info);

        infoTextView.setText(info);

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    emailEditText.setHint("");
                    emailTextView.setVisibility(View.VISIBLE);
                }else{
                    emailEditText.setHint("E-mail");
                    emailTextView.setVisibility(View.INVISIBLE);
                }
            }
        });

        messageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    messageEditText.setHint("");
                    messageTextView.setVisibility(View.VISIBLE);
                }else{
                    messageEditText.setHint("Escreva seu feedback");
                    messageTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


}
