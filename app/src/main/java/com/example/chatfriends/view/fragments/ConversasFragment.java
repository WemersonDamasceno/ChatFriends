package com.example.chatfriends.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfriends.R;
import com.example.chatfriends.controll.ConversaAdapter;
import com.example.chatfriends.model.Mensagem;
import com.example.chatfriends.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ConversasFragment extends Fragment {
    LinearLayout layoutLostConversas;
    RecyclerView rvConversas;
    ConversaAdapter adapter;

    public ConversasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        layoutLostConversas = view.findViewById(R.id.layoutLostConversas);
        rvConversas = view.findViewById(R.id.rvConversas);
        adapter = new ConversaAdapter(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(false);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        rvConversas.setLayoutManager(layoutManager);
        rvConversas.setAdapter(adapter);

        encontrarUser();




        return view;
    }
    private void encontrarUser() {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable final QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable final FirebaseFirestoreException e) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            Usuario user = doc.toObject(Usuario.class);
                            if(user.getIdUser().equals(FirebaseAuth.getInstance().getUid())){
                                if(user.getQtdAmigos() == 0){
                                    layoutLostConversas.setVisibility(View.VISIBLE);
                                }else {
                                    buscarConversas(user);
                                }
                            }
                        }
                    }
                });
    }

    private void buscarConversas(final Usuario user) {
        FirebaseFirestore.getInstance().collection("/mensagens")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            Mensagem msg = doc.toObject(Mensagem.class);
                            if(msg.getIdMensagem().equals(user.getLastMensagem().getIdMensagem())){
                                String destinatario = msg.getIdUserDestinatario();
                                String remetente = msg.getIdUserRemetente();
                                buscarUsersDaConversa(remetente, destinatario);
                            }
                        }
                    }
                });
    }

    private void buscarUsersDaConversa(final String remetente, final String destinatario) {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            Usuario user = doc.toObject(Usuario.class);
                            if(user.getIdUser().equals(remetente)){
                                if(!user.getIdUser().equals(FirebaseAuth.getInstance().getUid())){
                                    //add na lista
                                    adapter.add(user.getIdUser());
                                }
                            }else
                                if(user.getIdUser().equals(destinatario)){
                                    if(!user.getIdUser().equals(FirebaseAuth.getInstance().getUid())){
                                        //add na lista
                                        adapter.add(user.getIdUser());
                                    }
                                }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}