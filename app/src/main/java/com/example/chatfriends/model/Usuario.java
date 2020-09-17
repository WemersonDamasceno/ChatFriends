package com.example.chatfriends.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Usuario implements Parcelable {
    private String nome;
    private String email;
    private String status;
    private String idUser;
    private String urlFoto;
    private int qtdAmigos;
    private Mensagem lastMensagem;


    public Usuario(){}

    protected Usuario(Parcel in) {
        nome = in.readString();
        email = in.readString();
        status = in.readString();
        idUser = in.readString();
        urlFoto = in.readString();
        qtdAmigos = in.readInt();
        lastMensagem = in.readParcelable(Mensagem.class.getClassLoader());
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public int getQtdAmigos() {
        return qtdAmigos;
    }

    public void setQtdAmigos(int qtdAmigos) {
        this.qtdAmigos = qtdAmigos;
    }

    public Mensagem getLastMensagem() {
        return lastMensagem;
    }

    public void setLastMensagem(Mensagem lastMensagem) {
        this.lastMensagem = lastMensagem;
    }

    public Usuario(String nome, String email, String status, String idUser, String urlFoto, int qtdAmigos, Mensagem lastMensagem) {
        this.nome = nome;
        this.email = email;
        this.status = status;
        this.idUser = idUser;
        this.urlFoto = urlFoto;
        this.qtdAmigos = qtdAmigos;
        this.lastMensagem = lastMensagem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeString(status);
        dest.writeString(idUser);
        dest.writeString(urlFoto);
        dest.writeInt(qtdAmigos);
        dest.writeParcelable(lastMensagem, flags);
    }
}
