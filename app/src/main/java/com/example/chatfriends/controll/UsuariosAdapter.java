package com.example.chatfriends.controll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.chatfriends.view.PerfilActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolderUsuarios> {
    private ArrayList<Usuario> listUsuarios;
    private Context getContext;

    public UsuariosAdapter(Context getContext) {
        listUsuarios = new ArrayList<>();
        this.getContext = getContext;
    }

    public ArrayList<Usuario> getListUsuarios() {
        return listUsuarios;
    }


    @NonNull
    @Override
    public ViewHolderUsuarios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contatos_item_list, null, false);
        return new ViewHolderUsuarios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUsuarios holder, int position) {
        holder.setDados(listUsuarios.get(position));
    }


    @Override
    public int getItemCount() {
        return listUsuarios.size();
    }

    public void add(Usuario user) {
        listUsuarios.add(user);
    }

    public void filterList(ArrayList<Usuario> filteredList) {
        listUsuarios = filteredList;
        notifyDataSetChanged();
    }

    class ViewHolderUsuarios extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nomeCttLista;
        ImageView imgCttLista;
        TextView statusCttLista;
        Bundle bundle = new Bundle();
        Intent intent;



        ViewHolderUsuarios(@NonNull View itemView) {
            super(itemView);
            nomeCttLista = itemView.findViewById(R.id.nomeUserContatosLista);
            imgCttLista = itemView.findViewById(R.id.fotoUserContatosLista);
            statusCttLista = itemView.findViewById(R.id.statusUserContatosLista);

            //Quando alguem clicar no nome da pessoa é pra abrir o perfil dela pra adicionar ela.
            nomeCttLista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verificarUser();
                }
            });

        }

        private void verificarUser() {
            final Usuario userClicado = getListUsuarios().get(getAdapterPosition());
            FirebaseFirestore.getInstance().collection("/users")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                Usuario user = doc.toObject(Usuario.class);
                                if(user.getIdUser().equals(FirebaseAuth.getInstance().getUid())){
                                    bundle.putParcelable("user", userClicado);
                                    intent = new Intent(getContext, PerfilActivity.class);
                                    intent.putExtra("bundle", bundle);
                                    getContext.startActivity(intent);
                                }
                            }
                        }
                    });
        }

        @Override
        public void onClick(View v) {
            Log.i("teste","Clicou no item "+getAdapterPosition());
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
                                if(user.getIdUser().equals(usuario.getIdUser())){
                                    nomeCttLista.setText(user.getNome());
                                    if(user.getStatus() == null || user.getStatus().equals("")){
                                        statusCttLista.setText("Sem status também é um status...");
                                    }else {
                                        statusCttLista.setText(user.getStatus());
                                    }
                                    Picasso.get().load(usuario.getUrlFoto()).into(imgCttLista);
                                }
                             }


                        }
                    });

        }


    }


}