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
import com.example.chatfriends.model.Amigos;
import com.example.chatfriends.model.Convite;
import com.example.chatfriends.model.Usuario;
import com.example.chatfriends.view.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConviteAdapter extends RecyclerView.Adapter<ConviteAdapter.ViewHolderConvites> {
    private Context getContext;
    private List<Usuario> conviteList;

    public ConviteAdapter(Context getContext) {
        this.getContext = getContext;
        this.conviteList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolderConvites onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.convites_item_list, null, false);
        return new ViewHolderConvites(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderConvites holder, int position) {
        holder.setDados(conviteList.get(position));
    }


    @Override
    public int getItemCount() {
        return conviteList.size();
    }

    public void add(Usuario user) {
        conviteList.add(user);
    }

    public int getSize(){
        return conviteList.size();
    }


    class ViewHolderConvites extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nomeCttLista;
        ImageView imgCttLista;
        TextView statusCttLista;
        ImageView imgAceitarConvite,ivConviteAceito;



        ViewHolderConvites(@NonNull View itemView) {
            super(itemView);
            nomeCttLista = itemView.findViewById(R.id.nomeConvite);
            imgCttLista = itemView.findViewById(R.id.fotoConvite);
            statusCttLista = itemView.findViewById(R.id.statusConvite);
            imgAceitarConvite = itemView.findViewById(R.id.ivAceitarConvite);
            ivConviteAceito = itemView.findViewById(R.id.ivConviteAceito);

            //Quando alguem clicar no icone é pra adicionar ela.
            imgAceitarConvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext, "Você aceitou o convite"+ conviteList.get(getAdapterPosition()),
                            Toast.LENGTH_SHORT).show();
                    imgAceitarConvite.setVisibility(View.GONE);
                    ivConviteAceito.setVisibility(View.VISIBLE);
                    //Adicionar o user que enviou convite a lista de amigos de ambos.
                    pegarUser();

                }
            });


        }

        private void pegarUser() {
            FirebaseFirestore.getInstance().collection("/users")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                Usuario user = doc.toObject(Usuario.class);
                                if(user.getIdUser().equals(FirebaseAuth.getInstance().getUid())){
                                    //fazer amizade
                                    fazerAmizade(user);
                                }
                            }
                        }
                    });
        }

        private void fazerAmizade(final Usuario userEu) {
            FirebaseFirestore.getInstance().collection("/convites")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                Convite convite = doc.toObject(Convite.class);
                                //Se convite for recebido por mim
                                if(convite.getUserQuemRecebeu().getIdUser().equals(userEu.getIdUser())){
                                    if(convite.getUserQuemEnviou().getIdUser().
                                            equals(conviteList.get(getAdapterPosition()).getIdUser())){
                                        upConvite(doc);
                                        //salvar em amigos
                                        salvarAmigos(userEu, conviteList.get(getAdapterPosition()));
                                    }
                                }
                            }
                            if(conviteList.size() == 0){

                            }
                        }
                    });
        }

        private void upConvite(DocumentSnapshot doc) {
            FirebaseFirestore.getInstance().collection("/convites")
                    .document(doc.getId())
                    .update("foiAceito",true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("teste","Sucesso ao update na convite");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("teste","Erro ao update convite: "+e.getMessage());
                }
            });
        }

        private void salvarAmigos(final Usuario userEu, final Usuario usuarioAmigo) {
            incrementarQtdAmigos(userEu,usuarioAmigo);
            Amigos amigos = new Amigos(userEu,usuarioAmigo, UUID.randomUUID().toString());

            //Enviar essa parte pro completed do update
            FirebaseFirestore.getInstance().collection("/amigos")
                    .add(amigos)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.i("teste","Sucesso ao adicionar amizade");
                        }
                    }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    getContext.startActivity(new Intent(getContext,HomeActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext, "Erro..."+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            FirebaseFirestore.getInstance().terminate().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });


        }

        private void incrementarQtdAmigos(final Usuario userEu, final Usuario usuarioAmigo) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("/users")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                Usuario user = doc.toObject(Usuario.class);
                                if(user.getIdUser().equals(userEu.getIdUser())){
                                    int qtd = user.getQtdAmigos()+1;
                                    updateQtdAmigos(doc, qtd);
                                }
                                if(user.getIdUser().equals(usuarioAmigo.getIdUser())){
                                    int qtd = user.getQtdAmigos()+1;
                                    updateQtdAmigos(doc, qtd);
                                }
                            }
                        }
                    });
        }

        private void updateQtdAmigos(DocumentSnapshot doc, int qtd){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("/users")
                    .document(doc.getId())
                    .update("qtdAmigos",qtd)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("teste", "Upload sucess");
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
