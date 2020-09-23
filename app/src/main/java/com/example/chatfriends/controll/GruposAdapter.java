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
import com.example.chatfriends.model.Grupo;
import com.example.chatfriends.model.Usuario;
import com.example.chatfriends.view.TrocaMensagensActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
                    FirebaseFirestore.getInstance().collection("/grupos")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                        Grupo grupo = doc.toObject(Grupo.class);
                                        if(gruposList.get(getAdapterPosition()) != null) {
                                            if (grupo.getIdGrupo().equals(gruposList.get(getAdapterPosition()).getIdGrupo())) {
                                                List<String> idUsers = grupo.getIdUsersGrupo();
                                                idUsers.add(FirebaseAuth.getInstance().getUid());
                                                updateGrupo(doc, idUsers);
                                            }
                                        }
                                    }
                                }
                            });
                }
            });


        }

        private void updateGrupo(DocumentSnapshot doc, List<String> idUsers) {
            FirebaseFirestore.getInstance().collection("/grupos")
                    .document(doc.getId())
                    .update("idUsersGrupo", idUsers)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("teste","Sucess ao add no grupo");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("teste","Erro ao add no grupo: "+e.getMessage());
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
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : docs) {
                                Grupo grupo1 = doc.toObject(Grupo.class);
                                if(grupo1.getIdGrupo().equals(grupo.getIdGrupo())){
                                    nomeGrupo.setText(grupo1.getNomeGrupo());
                                    Picasso.get().load(grupo.getUrlFotoGrupo()).into(fotoGrupo);
                                    descGrupo.setText(grupo.getDescricaoGrupo());
                                    if(grupo1.getIdUsersGrupo().contains(FirebaseAuth.getInstance().getUid())){
                                        ivEntrarGrupo.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    });
        }
    }
}

