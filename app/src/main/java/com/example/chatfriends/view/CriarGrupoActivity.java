package com.example.chatfriends.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatfriends.R;
import com.example.chatfriends.model.Grupo;
import com.example.chatfriends.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CriarGrupoActivity extends AppCompatActivity {
    Usuario usuarioEu;
    ImageView imgTrocarFotoPerfil, imgPerfilPerfil;
    EditText etNomeGrupoAdd,descricaoGrupoAdd;
    CheckBox cbPrivacidadeGrupoAdd;
    Button btnCriarGrupo;
    Grupo grupo;

    ProgressDialog progressDialogFoto;
    ProgressDialog progressDialogAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_grupo);

        imgTrocarFotoPerfil = findViewById(R.id.ivAddFotoGrupo);
        imgPerfilPerfil = findViewById(R.id.fotoGrupo);
        etNomeGrupoAdd = findViewById(R.id.etNomeGrupoAdd);
        descricaoGrupoAdd = findViewById(R.id.descricaoGrupoAdd);
        cbPrivacidadeGrupoAdd = findViewById(R.id.cbPrivacidadeGrupoAdd);
        btnCriarGrupo = findViewById(R.id.btnCriarGrupo);

        grupo = new Grupo();
        progressDialogAdd = new ProgressDialog(this);
        progressDialogFoto = new ProgressDialog(this);


        imgTrocarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFoto();
            }
        });

        btnCriarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarGrupo();
            }
        });



        Bundle bundle;
        Intent intent = getIntent();
        try{
            bundle = intent.getBundleExtra("bundleGrupo");
            usuarioEu = bundle.getParcelable("userGrupo");
            Toast.makeText(this, "Ola "+usuarioEu.getNome(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.i("teste", "Error: "+e.getMessage());
        }




    }
    private void selecionarFoto() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"SelecionarFoto"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                                grupo.setUrlFotoGrupo(uri1.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("teste", "Falha ao fazer upload da foto: " + e.getMessage());
                        if (e.getMessage().equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")) {
                            Toast.makeText(CriarGrupoActivity.this, "Falha ao conectar-se com a internet", Toast.LENGTH_LONG).show();
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

    private void criarGrupo() {
        progressDialogAdd.setTitle("Criando grupo...");
        progressDialogAdd.show();

        String idGrupo = UUID.randomUUID().toString();
        String nomeGrupo = etNomeGrupoAdd.getText().toString();
        String idUserAdmin = usuarioEu.getIdUser();
        List<String> idUsersGrupo = new ArrayList<>();
        idUsersGrupo.add(FirebaseAuth.getInstance().getUid());
        String descricaoGrupo = descricaoGrupoAdd.getText().toString();
        boolean privacidadeGrupo = cbPrivacidadeGrupoAdd.isChecked();
        if(grupo.getUrlFotoGrupo() == null || grupo.getUrlFotoGrupo().equals("")){
            grupo.setUrlFotoGrupo("https://firebasestorage.googleapis.com/v0/b/chatfriends-625c9.appspot.com/o/" +
                    "images%2Favatarteste.png?alt=media&token=8c413d51-2a5c-4170-99e5-a5d587bb0d91");
        }

        grupo.setNomeGrupo(nomeGrupo);
        grupo.setIdGrupo(idGrupo);
        grupo.setIdUserAdminGrupo(idUserAdmin);
        grupo.setIdUsersGrupo(idUsersGrupo);
        grupo.setDescricaoGrupo(descricaoGrupo);
        grupo.setPrivacidadeGrupo(privacidadeGrupo);

        //criar o obj grupo
        salvarGrupoBanco(grupo);
    }

    private void salvarGrupoBanco(Grupo grupo) {
        FirebaseFirestore.getInstance().collection("/grupos")
                .add(grupo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("teste","Sucess ao criar o grupo");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("teste","Falha ao criar o grupo: "+e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                progressDialogAdd.dismiss();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }

}