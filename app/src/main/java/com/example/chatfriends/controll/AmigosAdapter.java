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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.ViewHolderAmigos> {
    private Context getContext;
    private List<Usuario> amigosList;

    public AmigosAdapter(Context getContext) {
        this.getContext = getContext;
        this.amigosList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolderAmigos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amigos_item_list, null, false);
        return new ViewHolderAmigos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAmigos holder, int position) {
        holder.setDados(amigosList.get(position));
    }


    @Override
    public int getItemCount() {
        return amigosList.size();
    }

    public void add(Usuario user) {
        amigosList.add(user);
    }


    class ViewHolderAmigos extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nomeAmigo;
        ImageView fotoAmigo;
        TextView statusAmigo;


        ViewHolderAmigos(@NonNull View itemView) {
            super(itemView);
            nomeAmigo = itemView.findViewById(R.id.nomeAmigo);
            fotoAmigo = itemView.findViewById(R.id.fotoAmigo);
            statusAmigo = itemView.findViewById(R.id.statusAmigo);

            //Quando alguem clicar no icone é pra adicionar ela.
            nomeAmigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext, TrocaMensagensActivity.class);
                    intent.putExtra("userMensagem", amigosList.get(getAdapterPosition()));
                    getContext.startActivity(intent);
                }
            });


        }


        @Override
        public void onClick(View v) {
            Log.i("teste", "Clicou no item " + getAdapterPosition());
        }

        private void setDados(final Usuario usuario) {
            FirebaseFirestore.getInstance().collection("/users")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : docs) {
                                Usuario user = doc.toObject(Usuario.class);
                                if (user.getIdUser().equals(usuario.getIdUser())) {
                                    nomeAmigo.setText(user.getNome());
                                    if (user.getStatus() == null) {
                                        statusAmigo.setText("Sem status também é um status...");
                                    } else {
                                        statusAmigo.setText(user.getStatus());
                                    }
                                    Picasso.get().load(usuario.getUrlFoto()).into(fotoAmigo);
                                }
                            }
                        }
                    });
        }
    }
}

