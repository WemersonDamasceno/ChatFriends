package com.example.chatfriends.controll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfriends.R;
import com.example.chatfriends.model.Usuario;
import com.example.chatfriends.view.TrocaMensagensActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ConversaAdapter extends  RecyclerView.Adapter<ConversaAdapter.ViewHolderConversas> {
    private Context getContext;
    private List<String> amigosListIdsConversas;

    public ConversaAdapter(Context getContext) {
        this.getContext = getContext;
        this.amigosListIdsConversas = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolderConversas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conversas_item_list, null, false);
        return new ViewHolderConversas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderConversas holder, int position) {
        holder.setDados(amigosListIdsConversas.get(position));
    }


    @Override
    public int getItemCount() {
        return amigosListIdsConversas.size();
    }

    public void add(String user) {
        amigosListIdsConversas.add(user);
    }

    public List<String> getAmigosListIdsConversas(){
        return amigosListIdsConversas;
    }

    class ViewHolderConversas extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nomeAmigoConversas;
        ImageView fotoAmigoConversas;
        TextView lastMsgConversas;


        ViewHolderConversas(@NonNull View itemView) {
            super(itemView);
            nomeAmigoConversas = itemView.findViewById(R.id.nomeUserConversaLista);
            fotoAmigoConversas = itemView.findViewById(R.id.fotoUserConversaLista);
            lastMsgConversas = itemView.findViewById(R.id.lastMsgConversaLista);

            //Quando alguem clicar no icone é pra adicionar ela.
            nomeAmigoConversas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore.getInstance().collection("/users")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                                    final Intent intent = new Intent(getContext, TrocaMensagensActivity.class);
                                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                        final Usuario user = doc.toObject(Usuario.class);
                                        if (user.getIdUser().equals(FirebaseAuth.getInstance().getUid())) {
                                            intent.putExtra("userEuMensagem", user);
                                            Log.i("teste","Enviou eu");
                                        }else
                                        if(user.getIdUser().equals(amigosListIdsConversas.get(getAdapterPosition()))){
                                            intent.putExtra("userMensagem",user);
                                        }
                                    }
                                    getContext.startActivity(intent);
                                }
                            });
                }
            });


        }


        @Override
        public void onClick(View v) {
            Log.i("teste", "Clicou no item " + getAdapterPosition());
        }

        private void setDados(final String userId) {
            FirebaseFirestore.getInstance().collection("/users")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : docs) {
                                Usuario userBanco = doc.toObject(Usuario.class);
                                if (userBanco.getIdUser().equals(userId)) {
                                    nomeAmigoConversas.setText(userBanco.getNome());
                                    lastMsgConversas.setText(userBanco.getLastMensagem().getConteudo());
                                    Picasso.get().load(userBanco.getUrlFoto()).into(fotoAmigoConversas);
                                }
                            }
                        }
                    });
        }
    }
}