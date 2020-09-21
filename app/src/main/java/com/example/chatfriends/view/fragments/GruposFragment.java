package com.example.chatfriends.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfriends.R;
import com.example.chatfriends.controll.GruposAdapter;
import com.example.chatfriends.model.Grupo;
import com.example.chatfriends.model.Usuario;
import com.example.chatfriends.view.CriarGrupoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class GruposFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    Bundle bundle;
    RecyclerView rvGrupos;
    GruposAdapter gruposAdapter;


    public GruposFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grupos, container, false);

        bundle = new Bundle();
        floatingActionButton = view.findViewById(R.id.fab);


        rvGrupos = view.findViewById(R.id.rvGrupos);
        gruposAdapter = new GruposAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(false);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        rvGrupos.setLayoutManager(layoutManager);
        rvGrupos.setAdapter(gruposAdapter);


        buscarGrupos();

        //Algo ta bugnado quando clico pra abrir uma conversa em grupo

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("/users")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                    Usuario user = doc.toObject(Usuario.class);
                                    if(user.getIdUser().equals(FirebaseAuth.getInstance().getUid())){
                                        bundle.putParcelable("userGrupo", user);
                                        Intent intent = new Intent(getContext(), CriarGrupoActivity.class);
                                        intent.putExtra("bundleGrupo", bundle);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
            }
        });



        return view;
    }

    private void buscarGrupos() {
        FirebaseFirestore.getInstance().collection("/grupos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            Grupo grupo = doc.toObject(Grupo.class);
                            gruposAdapter.add(grupo);
                            gruposAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}