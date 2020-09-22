package com.example.chatfriends.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatfriends.R;
import com.example.chatfriends.controll.ConviteAdapter;
import com.example.chatfriends.model.Convite;
import com.example.chatfriends.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import javax.annotation.Nullable;

public class MyPerfilActivity extends AppCompatActivity {
    RecyclerView rvConvites;
    ConviteAdapter conviteAdapter;
    ImageView imgPerfilPerfil, imgTrocarFotoPerfil,ic_sair;
    TextView userMyPerfil, emailMyPerfil, statusMyPerfil;
    Bundle bundle;
    Usuario usuarioRecebido;
    LinearLayout llLost;
    ProgressDialog progressDialogFoto;

    @SuppressLint("SetTextI18n")
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
        ic_sair = findViewById(R.id.ic_sair);
        llLost = findViewById(R.id.layoutLostConvites);

        progressDialogFoto = new ProgressDialog(this);

        conviteAdapter = new ConviteAdapter(getBaseContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);

        rvConvites.setAdapter(conviteAdapter);
        rvConvites.setLayoutManager(layoutManager);

        //Recebeu o usuario atual
        if (getIntent() == null) {
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

            imgTrocarFotoPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selecionarFoto();
                }
            });


            if (usuarioRecebido.getStatus() == null) {
                statusMyPerfil.setText("Sem Status...");
            } else {
                statusMyPerfil.setText(usuarioRecebido.getStatus());
            }
        } catch (RuntimeException e) {
            Log.i("teste", "Error: " + e.getMessage());
            startActivity(new Intent(this,HomeActivity.class));
        }

        ic_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        });

    }
    private void selecionarFoto() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"SelecionarFoto"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1){
            Uri uri = data.getData();
            imgPerfilPerfil.setImageURI(uri);
            imgTrocarFotoPerfil.setVisibility(View.INVISIBLE);
            enviarFoto(uri);
        }
    }
    private void enviarFoto(Uri selectedImage) {
        progressDialogFoto.setTitle("Enviando sua foto...");
        progressDialogFoto.show();

        //Arrumar esse upload da foto
        String fileName = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + fileName);
        ref.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri1) {
                                progressDialogFoto.dismiss();
                                updateUser(usuarioRecebido,uri1);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("teste", "Falha ao fazer upload da foto: " + e.getMessage());
                        if (e.getMessage().equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")) {
                            Toast.makeText(MyPerfilActivity.this, "Falha ao conectar-se com a internet", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialogFoto.setMessage("Enviado: "+ (int) progress + "% completo");
            }
        });

        //

    }

    private void updateUser(final Usuario usuarioRecebido, final Uri uri) {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            Usuario user = doc.toObject(Usuario.class);
                            if(usuarioRecebido.getIdUser().equals(user.getIdUser())){
                                FirebaseFirestore.getInstance().collection("/users")
                                        .document(doc.getId())
                                        .update("urlFoto", uri.toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i("teste","Update sucess");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("teste", "erro: "+e.getMessage());
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void buscarConvites() {
        FirebaseFirestore.getInstance().collection("/convites")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            final Convite convite = doc.toObject(Convite.class);
                            if (convite.getUserQuemRecebeu().getIdUser().equals(usuarioRecebido.getIdUser())) {
                                //adicionar na lista
                                if (!convite.isFoiAceito()) {
                                    conviteAdapter.add(convite.getUserQuemEnviou().getIdUser());
                                    conviteAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        if (conviteAdapter.getSize() == 0) {
                            llLost.setVisibility(View.VISIBLE);
                        }
                        Log.i("teste", "tam: " + conviteAdapter.getSize());
                    }
                });

    }
}