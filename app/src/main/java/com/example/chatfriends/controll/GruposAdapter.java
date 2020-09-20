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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatfriends.R;
import com.example.chatfriends.model.Grupo;
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

public class GruposAdapter extends RecyclerView.Adapter<GruposAdapter.ViewHolderGrupos> {
    private Context getContext;
    private List<Grupo> gruposList;

    public GruposAdapter(Context getContext) {
        this.getContext = getContext;
        this.gruposList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolderGrupos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grupos_item_list, null, false);
        return new ViewHolderGrupos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGrupos holder, int position) {
        holder.setDados(gruposList.get(position));
    }


    @Override
    public int getItemCount() {
        return gruposList.size();
    }

    public void add(Grupo grupo) {
        gruposList.add(grupo);
    }


    class ViewHolderGrupos extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nomeGrupo;
        ImageView fotoGrupo, ivEntrarGrupo;
        TextView descGrupo;


        ViewHolderGrupos(@NonNull View itemView) {
            super(itemView);
            nomeGrupo = itemView.findViewById(R.id.nomeGrupo);
            fotoGrupo = itemView.findViewById(R.id.fotoGrupo);
            descGrupo = itemView.findViewById(R.id.descGrupo);
            ivEntrarGrupo = itemView.findViewById(R.id.ivEntrarGrupo);

            //Quando alguem clicar no icone é pra adicionar ela.
            nomeGrupo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //me mandar tambem
                    FirebaseFirestore.getInstance().collection("/users")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                        Usuario user = doc.toObject(Usuario.class);
                                        if (user.getIdUser().equals(FirebaseAuth.getInstance().getUid())) {
                                            Intent intent = new Intent(getContext, TrocaMensagensActivity.class);
                                            intent.putExtra("grupoMensagem", gruposList.get(getAdapterPosition()));
                                            intent.putExtra("userEuMensagem", user);
                                            getContext.startActivity(intent);
                                        }
                                    }
                                }
                            });


                }
            });

            ivEntrarGrupo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext, "Você já esta no grupo", Toast.LENGTH_SHORT).show();
                }
            });


        }


        @Override
        public void onClick(View v) {
            Log.i("teste", "Clicou no item " + getAdapterPosition());
        }

        private void setDados(final Grupo grupo) {
            FirebaseFirestore.getInstance().collection("/grupos")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : docs) {
                                Grupo grupos = doc.toObject(Grupo.class);
                                if(grupos.getIdGrupo().equals(gruposList.get(getAdapterPosition()).getIdGrupo())) {
                                    nomeGrupo.setText(grupos.getNomeGrupo());
                                    String desc = grupo.getDescricaoGrupo();
                                    descGrupo.setText(desc.substring(0,30)+"...");
                                    Picasso.get().load(grupos.getUrlFotoGrupo()).into(fotoGrupo);
                                }

                            }
                        }
                    });
        }
    }
}

