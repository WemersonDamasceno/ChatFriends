package com.example.chatfriends.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfriends.R;
import com.example.chatfriends.controll.MensagemAdapter;
import com.example.chatfriends.model.Grupo;
import com.example.chatfriends.model.Mensagem;
import com.example.chatfriends.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.UUID;

public class TrocaMensagensActivity extends AppCompatActivity {
    ImageView ivSendMensagem;
    EditText etMensagem;
    RecyclerView rvMensagens;
    MensagemAdapter mensagemAdapter;
    ImageView ic_foto_perfil_tela_mensg,ivBack;
    TextView txtNomeMensagem;
    Usuario usuarioAmigo;
    Usuario usuarioEu;
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
        ivBack = findViewById(R.id.ivBack);

        mensagemAdapter = new MensagemAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);

        rvMensagens.setLayoutManager(layoutManager);
        rvMensagens.setAdapter(mensagemAdapter);

       buscarMensagens();

        //Receber pessoa ou grupo
        Intent intent = getIntent();
        try{
            if(intent.getParcelableExtra("grupoMensagem") != null){
                usuarioEu = intent.getParcelableExtra("userEuMensagem");
                grupo = intent.getParcelableExtra("grupoMensagem");
                Picasso.get().load(grupo.getUrlFotoGrupo()).into(ic_foto_perfil_tela_mensg);
                txtNomeMensagem.setText(grupo.getNomeGrupo());
            }
            if(intent.getParcelableExtra("userMensagem") != null){
                usuarioEu = intent.getParcelableExtra("userEuMensagem");
                usuarioAmigo = intent.getParcelableExtra("userMensagem");
                Picasso.get().load(usuarioAmigo.getUrlFoto()).into(ic_foto_perfil_tela_mensg);
                txtNomeMensagem.setText(usuarioAmigo.getNome());
            }
        }catch (Exception e){
            Log.i("teste","Erro ao receber user: "+e.getMessage());
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), HomeActivity.class));
            }
        });

        ivSendMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMensagem.getText().toString().equals("")){
                    etMensagem.setError("Digite uma mensagem....");

                }else{
                    Mensagem mensagem = new Mensagem();
                    mensagem.setConteudo(etMensagem.getText().toString());
                    mensagem.setIdMensagem(UUID.randomUUID().toString());
                    mensagem.setIdUserRemetente(usuarioEu.getIdUser());
                    mensagem.setIdUserDestinatario(usuarioAmigo.getIdUser());
                    mensagem.setUrlFotoDono(usuarioEu.getUrlFoto());
                    mensagem.setIfLeft(false); //se a msg for minha fica na direita

                    long milles = System.currentTimeMillis();
                    Timestamp timestamp = new Timestamp(milles);
                    mensagem.setData_hora(timestamp.toString());

                    salvarMensagem(mensagem);
                    etMensagem.setText("");
                    //atualizar a ultima mensagem dos dois usuarios

                    recreate();

                }
            }
        });


    }

    private void salvarMensagem(Mensagem mensagem) {
        FirebaseFirestore.getInstance().collection("/mensagens")
                .add(mensagem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("teste","enviado a mensagem");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("teste","falha ao enviar mensagem: "+e.getMessage());
            }
        });
    }

    private void buscarMensagens() {
        FirebaseFirestore.getInstance().collection("/mensagens")
                .orderBy("data_hora")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            Log.i("teste",e.getMessage());
                            return;
                        }
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            Mensagem mensg = doc.toObject(Mensagem.class);
                            if(mensg.getIdUserDestinatario().equals(usuarioEu.getIdUser())
                            || mensg.getIdUserRemetente().equals(usuarioEu.getIdUser())){
                                if(mensg.getIdUserDestinatario().equals(usuarioAmigo.getIdUser())
                                        || mensg.getIdUserRemetente().equals(usuarioAmigo.getIdUser())){
                                    mensagemAdapter.add(mensg);
                                    mensagemAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }
}