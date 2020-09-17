package com.example.chatfriends.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.chatfriends.R;
import com.example.chatfriends.controll.UsuariosAdapter;
import com.example.chatfriends.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProcurarActivity extends AppCompatActivity {
    RecyclerView rvCttProcurarCtt;
    EditText etProcurarPessoa;
    UsuariosAdapter usuariosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurar);

        rvCttProcurarCtt = findViewById(R.id.rvCttProcurarCtt);
        etProcurarPessoa = findViewById(R.id.etProcurarPessoa);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);
        usuariosAdapter = new UsuariosAdapter(getApplicationContext());

        rvCttProcurarCtt.setLayoutManager(layoutManager);
        rvCttProcurarCtt.setAdapter(usuariosAdapter);

        buscarUsuarios();

        etProcurarPessoa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
                usuariosAdapter.getListUsuarios().clear();
            }
        });

    }

    private void filter(final String text) {
        final ArrayList<Usuario> filteredList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Usuario usu = doc.toObject(Usuario.class);
                            usuariosAdapter.add(usu);
                        }
                        for (Usuario c : usuariosAdapter.getListUsuarios()) {
                            if (c.getNome().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add(c);
                            }
                        }
                        usuariosAdapter.filterList(filteredList);
                        usuariosAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void buscarUsuarios() {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Usuario usu = doc.toObject(Usuario.class);
                            //Se o id do user for diferente do id do atual user.
                            if(!usu.getIdUser().equals(FirebaseAuth.getInstance().getUid())) {
                                usuariosAdapter.add(usu);
                                usuariosAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

    }
}