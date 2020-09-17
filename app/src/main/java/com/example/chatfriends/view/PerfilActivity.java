package com.example.chatfriends.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatfriends.R;
import com.example.chatfriends.model.Convite;
import com.example.chatfriends.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

public class PerfilActivity extends AppCompatActivity {
    ImageView ivAddContato;
    Usuario usuarioRecebido;
    Bundle bundle;
    ProgressDialog progressDialog;
    AlertDialog.Builder alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        progressDialog = new ProgressDialog(this);
        alert = new AlertDialog.Builder(this);
        ivAddContato = findViewById(R.id.ivAddContato);

        //receber usuario
        //pegando os dados da outra activity
        Intent intent = getIntent();
        try {
            bundle = intent.getBundleExtra("bundle");
            usuarioRecebido = bundle.getParcelable("user");
        } catch (RuntimeException e) {
            Log.i("teste", "Error: " + e.getMessage());
        }

        Log.i("teste", "Perfil do "+usuarioRecebido.getNome());

        ivAddContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Aguarde um momento...");
                progressDialog.setMessage("Enviando seu convite..");
                progressDialog.show();

                FirebaseFirestore.getInstance().collection("/users")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                    Usuario userI = doc.toObject(Usuario.class);
                                    if(userI.getIdUser().equals(FirebaseAuth.getInstance().getUid())){
                                        enviarConvite(userI);
                                    }
                                }
                            }
                        });
            }
        });
        
    }

    private void enviarConvite(Usuario userI) {
        //Enviar convite para um amigo e criar a relação entre os users.
        final Convite convite = new Convite(UUID.randomUUID().toString(), userI, usuarioRecebido, false);

        FirebaseFirestore.getInstance().collection("/convites")
                .add(convite)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        alert.setTitle("O convite foi enviado!");
                        alert.setMessage("Agora só esperar a resposta do amigo!");
                        alert.setPositiveButton("Ok", null);
                        progressDialog.dismiss();
                        alert.show();
                        ivAddContato.setVisibility(View.GONE);
                        Log.i("teste","Convite enviado para "+convite.getUserQuemRecebeu().getNome());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("teste","Erro: "+e.getMessage());
            }
        });
    }

}