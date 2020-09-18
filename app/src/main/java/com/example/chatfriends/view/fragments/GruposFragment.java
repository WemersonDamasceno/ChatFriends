package com.example.chatfriends.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatfriends.R;
import com.example.chatfriends.model.Usuario;
import com.example.chatfriends.view.CriarContaActivity;
import com.example.chatfriends.view.CriarGrupoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class GruposFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    Bundle bundle;
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
}