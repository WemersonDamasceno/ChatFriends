package com.example.chatfriends.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfriends.R;
import com.example.chatfriends.controll.AmigosAdapter;
import com.example.chatfriends.model.Amigos;
import com.example.chatfriends.model.Usuario;
import com.example.chatfriends.view.ProcurarActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class AmigosFragment extends Fragment {
    RecyclerView rvListContatos;
    AmigosAdapter amigosAdapter;
    LinearLayout layoutLost;
    Button btnProcurarContatos;

    public AmigosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);

        layoutLost = view.findViewById(R.id.layoutLost);
        btnProcurarContatos = view.findViewById(R.id.btnProcurarContatos);
        rvListContatos = view.findViewById(R.id.rvlistAmigos);

        amigosAdapter = new AmigosAdapter(getActivity().getBaseContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);

        rvListContatos.setAdapter(amigosAdapter);
        rvListContatos.setLayoutManager(layoutManager);

        encontrarUser();

        btnProcurarContatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ProcurarActivity.class));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void encontrarUser() {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable final QuerySnapshot queryDocumentSnapshots, @Nullable final FirebaseFirestoreException e) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            Usuario user = doc.toObject(Usuario.class);
                            if(user.getIdUser().equals(FirebaseAuth.getInstance().getUid())){
                                if(user.getQtdAmigos() == 0){
                                    layoutLost.setVisibility(View.VISIBLE);
                                }else {
                                    buscarContatos(user);
                                }
                            }
                        }
                    }
                });
    }

    private void buscarContatos(final Usuario usuarioEu) {
        FirebaseFirestore.getInstance().collection("/amigos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            Amigos amigos = doc.toObject(Amigos.class);
                            final String idAmigo1 = amigos.getUser1Id();
                            final String idAmigo2 = amigos.getUser2Id();

                            FirebaseFirestore.getInstance().collection("/users")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                                            for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                                Usuario user = doc.toObject(Usuario.class);
                                                if(idAmigo1.equals(usuarioEu.getIdUser())){
                                                    if(!amigosAdapter.getAmigosListIds().contains(idAmigo2)) {
                                                        amigosAdapter.add(idAmigo2);
                                                        amigosAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        }
                                    });

                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("/amigos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            final Amigos amigos = doc.toObject(Amigos.class);
                            final String amigo1 = amigos.getUser1Id();
                            final String amigo2 = amigos.getUser2Id();
                            FirebaseFirestore.getInstance().collection("/users")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                                            for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                                Usuario user = doc.toObject(Usuario.class);
                                                if(amigo2.equals(usuarioEu.getIdUser())){
                                                    if(!amigosAdapter.getAmigosListIds().contains(amigo1)) {
                                                        amigosAdapter.add(amigo1);
                                                        amigosAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }



}