package com.example.chatfriends.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {
    private String nome;
    private String email;
    private String senha;
    private String idUser;
    private String urlFoto;
    private int qtdSeguidores;
    private int qtdSeguindo;
    private int qtdPublicacoes;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha,
                   String idUser, String urlFoto, int qtdSeguidores, int qtdSeguindo, int qtdPublicacoes) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.idUser = idUser;
        this.urlFoto = urlFoto;
        this.qtdSeguidores = qtdSeguidores;
        this.qtdSeguindo = qtdSeguindo;
        this.qtdPublicacoes = qtdPublicacoes;
    }

    protected Usuario(Parcel in) {
        nome = in.readString();
        email = in.readString();
        senha = in.readString();
        idUser = in.readString();
        urlFoto = in.readString();
        qtdSeguidores = in.readInt();
        qtdSeguindo = in.readInt();
        qtdPublicacoes = in.readInt();
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    public int getQtdSeguidores() {
        return qtdSeguidores;
    }

    public void setQtdSeguidores(int qtdSeguidores) {
        this.qtdSeguidores = qtdSeguidores;
    }

    public int getQtdSeguindo() {
        return qtdSeguindo;
    }

    public void setQtdSeguindo(int qtdSeguindo) {
        this.qtdSeguindo = qtdSeguindo;
    }

    public int getQtdPublicacoes() {
        return qtdPublicacoes;
    }

    public void setQtdPublicacoes(int qtdPublicacoes) {
        this.qtdPublicacoes = qtdPublicacoes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeString(senha);
        dest.writeString(idUser);
        dest.writeString(urlFoto);
        dest.writeInt(qtdSeguidores);
        dest.writeInt(qtdSeguindo);
        dest.writeInt(qtdPublicacoes);
    }
}
