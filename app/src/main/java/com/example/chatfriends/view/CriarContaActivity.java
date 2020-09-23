package com.example.chatfriends.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatfriends.R;
import com.example.chatfriends.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CriarContaActivity extends AppCompatActivity {
    EditText nomeUserCriarConta;
    EditText emailUserCriarConta;
    EditText senhaUserCriarConta;
    Button btnCCCriarConta;
    Usuario usuarioNovo;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);
        //getSupportActionBar().hide();

        nomeUserCriarConta = findViewById(R.id.nomeUserCriarConta);
        emailUserCriarConta = findViewById(R.id.emailUserCriarConta);
        senhaUserCriarConta = findViewById(R.id.senhaUserCriarConta);
        btnCCCriarConta = findViewById(R.id.btnCCCriarConta);
        usuarioNovo = new Usuario();
        progressDialog = new ProgressDialog(this);

        btnCCCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Criando sua conta.");
                progressDialog.show();
                String email = emailUserCriarConta.getText().toString();
                String senha = senhaUserCriarConta.getText().toString();
                String nome = nomeUserCriarConta.getText().toString();

                if (email.equals("") || senha.equals("") || nome.equals("")) {
                    if (email.equals(""))
                        emailUserCriarConta.setError("O campo email é obrigatório!");
                    if (senha.equals(""))
                        senhaUserCriarConta.setError("O campo email é obrigatório!");
                    if (nome.equals(""))
                        nomeUserCriarConta.setError("O campo nome é obrigatório!");
                } else {
                    creatNewUser(email, senha);
                }
            }
        });




    }

    private void creatNewUser(String email, String senha) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i("teste","Sucesso ao autenticar.");
                        criarUsuario();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("teste", "erro: "+e.getMessage());
                if (e.getMessage().equals("The email address is already in use by another account.")) {
                    emailUserCriarConta.setError("O email ja está sendo utilizado!");
                }
                if (e.getMessage().equals("The given password is invalid. [ Password should be at least 6 characters ]")) {
                    senhaUserCriarConta.setError("A senha deve no minímo 6 caracteres");
                }
                if (e.getMessage().equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")) {
                    Toast.makeText(CriarContaActivity.this, "Falha ao conectar-se com a internet", Toast.LENGTH_LONG).show();
                }
                if (e.getMessage().equals("The email address is badly formatted.")) {
                    Toast.makeText(CriarContaActivity.this, "O email esta em um formato válido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void criarUsuario() {
        //povoar o usuario
        usuarioNovo.setNome(nomeUserCriarConta.getText().toString());
        usuarioNovo.setEmail(emailUserCriarConta.getText().toString());
        usuarioNovo.setIdUser(FirebaseAuth.getInstance().getUid());
        usuarioNovo.setQtdAmigos(0);
        usuarioNovo.setUrlFoto("https://firebasestorage.googleapis.com/v0/b/chatfriends-625c9.appspot.com/o/" +
                "images%2Favatarteste.png?alt=media&token=8c413d51-2a5c-4170-99e5-a5d587bb0d91");
        salvarUsuario(usuarioNovo);

    }

    public void salvarUsuario(Usuario user) {
        FirebaseFirestore.getInstance().collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("teste", "Usuario salvo no banco.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("teste", "erro ao salvar o usuario: " + e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                progressDialog.dismiss();
                startActivity(new Intent(getBaseContext(),HomeActivity.class));
            }
        });
    }
}
