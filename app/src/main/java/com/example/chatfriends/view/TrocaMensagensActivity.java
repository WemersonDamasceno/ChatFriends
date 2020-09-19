package com.example.chatfriends.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatfriends.R;
import com.example.chatfriends.model.Grupo;
import com.example.chatfriends.model.Usuario;
import com.squareup.picasso.Picasso;

public class TrocaMensagensActivity extends AppCompatActivity {
    ImageView ivSendMensagem;
    EditText etMensagem;
    RecyclerView rvMensagens;
    ImageView ic_foto_perfil_tela_mensg;
    TextView txtNomeMensagem;
    Usuario usuarioAmigo;
    Grupo grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_mensagens);
        getSupportActionBar().hide();

        ivSendMensagem = findViewById(R.id.ivSendMensagem);
        etMensagem = findViewById(R.id.etMensagem);
        rvMensagens = findViewById(R.id.rvMensagens);
        ic_foto_perfil_tela_mensg = findViewById(R.id.ic_foto_perfil_tela_mensg);
        txtNomeMensagem = findViewById(R.id.txtNomeMensagem);

        Intent intent = getIntent();
        try{
            usuarioAmigo = intent.getParcelableExtra("userMensagem");
            Picasso.get().load(usuarioAmigo.getUrlFoto()).into(ic_foto_perfil_tela_mensg);
            txtNomeMensagem.setText(usuarioAmigo.getNome());
        }catch (Exception e){
            Log.i("teste","Erro ao receber user: "+e.getMessage());
        }
        try{
            grupo = intent.getParcelableExtra("userMensagem");
            Picasso.get().load(grupo.getUrlFotoGrupo()).into(ic_foto_perfil_tela_mensg);
            txtNomeMensagem.setText(grupo.getNomeGrupo());
        }catch (Exception e){
            Log.i("teste","Erro ao receber user: "+e.getMessage());
        }



        ivSendMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMensagem.getText().toString().equals("")){
                    etMensagem.setError("Digite uma mensagem....");
                }
            }
        });


    }
}