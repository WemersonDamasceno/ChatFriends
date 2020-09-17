package com.example.chatfriends.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mensagem implements Parcelable {
    private String idMensagem;
    private String idUserRemetente;
    private String idUserDestinatario;
    private String hora;

    public Mensagem(String idMensagem, String idUserRemetente, String idUserDestinatario, String hora) {
        this.idMensagem = idMensagem;
        this.idUserRemetente = idUserRemetente;
        this.idUserDestinatario = idUserDestinatario;
        this.hora = hora;
    }

    public Mensagem(){}

    public String getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(String idMensagem) {
        this.idMensagem = idMensagem;
    }

    public String getIdUserRemetente() {
        return idUserRemetente;
    }

    public void setIdUserRemetente(String idUserRemetente) {
        this.idUserRemetente = idUserRemetente;
    }

    public String getIdUserDestinatario() {
        return idUserDestinatario;
    }

    public void setIdUserDestinatario(String idUserDestinatario) {
        this.idUserDestinatario = idUserDestinatario;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    protected Mensagem(Parcel in) {
        idMensagem = in.readString();
        idUserRemetente = in.readString();
        idUserDestinatario = in.readString();
        hora = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idMensagem);
        dest.writeString(idUserRemetente);
        dest.writeString(idUserDestinatario);
        dest.writeString(hora);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Mensagem> CREATOR = new Creator<Mensagem>() {
        @Override
        public Mensagem createFromParcel(Parcel in) {
            return new Mensagem(in);
        }

        @Override
        public Mensagem[] newArray(int size) {
            return new Mensagem[size];
        }
    };
}
