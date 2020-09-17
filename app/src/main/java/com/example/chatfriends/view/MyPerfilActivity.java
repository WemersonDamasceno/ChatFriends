package com.example.chatfriends.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatfriends.R;
import com.example.chatfriends.controll.ConviteAdapter;
import com.example.chatfriends.model.Convite;
import com.example.chatfriends.model.Usuario;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class MyPerfilActivity extends AppCompatActivity {
    RecyclerView rvConvites;
    ConviteAdapter conviteAdapter;
    ImageView imgPerfilPerfil,imgTrocarFotoPerfil;
    TextView userMyPerfil,emailMyPerfil,statusMyPerfil;
    Bundle bundle;
    Usuario usuarioRecebido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_perfil);

        imgPerfilPerfil = findViewById(R.id.imgPerfilPerfil);
        imgTrocarFotoPerfil = findViewById(R.id.imgTrocarFotoPerfil);
        userMyPerfil = findViewById(R.id.userMyPerfil);
        emailMyPerfil = findViewById(R.id.emailMyPerfil);
        statusMyPerfil = findViewById(R.id.statusMyPerfil);
        rvConvites = findViewById(R.id.rvConvites);


        conviteAdapter = new ConviteAdapter(getBaseContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);

        rvConvites.setAdapter(conviteAdapter);
        rvConvites.setLayoutManager(layoutManager);

        //Recebeu o usuario atual
        if(getIntent() == null){
            recreate();
        }
        Intent intent = getIntent();
        try {
            bundle = intent.getBundleExtra("bundleperfil");
            usuarioRecebido = bundle.getParcelable("useratual");

            Picasso.get().load(usuarioRecebido.getUrlFoto())
                    .into(imgPerfilPerfil);
            userMyPerfil.setText(usuarioRecebido.getNome());
            emailMyPerfil.setText(usuarioRecebido.getEmail());



            buscarConvites();

            if(usuarioRecebido.getStatus() == null){
                statusMyPerfil.setText("Sem Status...");
            }else {
                statusMyPerfil.setText(usuarioRecebido.getStatus());
            }
        } catch (RuntimeException e) {
            Log.i("teste", "Error: " + e.getMessage());
        }





    }

    private void buscarConvites() {
        FirebaseFirestore.getInstance().collection("/convites")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            Convite convite = doc.toObject(Convite.class);
                            if(convite.getUserQuemRecebeu().getIdUser().equals(usuarioRecebido.getIdUser())){
                                //adicionar na lista
                                conviteAdapter.add(convite.getUserQuemEnviou());
                                conviteAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

    }
}