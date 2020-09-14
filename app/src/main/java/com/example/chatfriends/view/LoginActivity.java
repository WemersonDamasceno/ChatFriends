package com.example.chatfriends.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatfriends.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView btnCriarConta;
    Button btnLoginEmailSenha;
    EditText edEmail,edSenha;
    ProgressDialog progressDialogLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        btnCriarConta = findViewById(R.id.txtCriarConta);
        btnLoginEmailSenha = findViewById(R.id.btnButtonLogin);
        edEmail = findViewById(R.id.edEmailLogin);
        edSenha = findViewById(R.id.edSenhaLogin);


        
        progressDialogLogin = new ProgressDialog(this);

        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CriarContaActivity.class));
            }
        });

        btnLoginEmailSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialogLogin.setTitle("Aguarda um instante...");
                progressDialogLogin.setMessage("Verificando sua conta...️\uD83D\uDD75\uD83C\uDFFB\u200D♂️");
                progressDialogLogin.show();
                String email, senha;
                email = edEmail.getText().toString();
                senha = edSenha.getText().toString();

                if (email.equals("")) {
                    edEmail.setError("O campo de email é obrigatório!");
                }
                if (senha.equals("")) {
                    edSenha.setError("O campo de senha é obrigatório!");
                } else {
                    fazerLogin(email, senha);
                }
            }
        });



    }

    private void fazerLogin(String email, String senha) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialogLogin.dismiss();
                        startActivity(new Intent(getBaseContext(),SplashActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("teste", "erro ao fazer login: "+e.getMessage());
            }
        });
    }
}
