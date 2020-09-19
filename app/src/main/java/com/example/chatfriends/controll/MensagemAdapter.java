package com.example.chatfriends.controll;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.chatfriends.model.Mensagem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MensagemAdapter extends RecyclerView.Adapter<MensagemAdapter.ViewHolderMensagem> {
    private Context getContext;
    private List<Mensagem> mensagemList;

    public MensagemAdapter(Context getContext) {
        this.getContext = getContext;
        this.mensagemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolderMensagem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amigos_item_list, null, false);
        return new ViewHolderMensagem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMensagem holder, int position) {
        holder.setDados(mensagemList.get(position));
    }


    @Override
    public int getItemCount() {
        return mensagemList.size();
    }

    public void add(Mensagem mensagem) {
        mensagemList.add(mensagem);
    }


    class ViewHolderMensagem extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgPerfilRemetente;
        TextView txtMensagemRemetente;

        ImageView imgPerfilDestinario;
        TextView txtMensagemDestinario;


        ViewHolderMensagem(@NonNull View itemView) {
            super(itemView);
            imgPerfilRemetente = itemView.findViewById(R.id.imgPerfilRemetente);
            txtMensagemRemetente = itemView.findViewById(R.id.txtMensagemRemetente);

        }


        @Override
        public void onClick(View v) {
            Log.i("teste", "Clicou no item " + getAdapterPosition());
        }

        private void setDados(final Mensagem mensagem) {
            FirebaseFirestore.getInstance().collection("/mensagens")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : docs) {
                                Mensagem mensagem1 = doc.toObject(Mensagem.class);
                                if(mensagem1.getIdMensagem().equals(mensagem.getIdMensagem())){

                                }
                            }
                        }
                    });
        }
    }
}

