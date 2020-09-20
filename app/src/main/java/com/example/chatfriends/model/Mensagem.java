package com.example.chatfriends.model;

import android.os.Parcel;
import android.os.Parcelable;



public class Mensagem implements Parcelable {
    private String idMensagem;
    private String idUserRemetente;
    private String idUserDestinatario;
    private String data_hora;
    private String conteudo;
    private boolean ifLeft;
    private String urlFotoDono;

    //cada mensagem vai ter uma foto e dizer onde é sua posicao
    public Mensagem() {
    }

    protected Mensagem(Parcel in) {
        idMensagem = in.readString();
        idUserRemetente = in.readString();
        idUserDestinatario = in.readString();
        data_hora = in.readString();
        conteudo = in.readString();
        ifLeft = in.readByte() != 0;
        urlFotoDono = in.readString();
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

    public String getData_hora() {
        return data_hora;
    }

    public void setData_hora(String data_hora) {
        this.data_hora = data_hora;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isIfLeft() {
        return ifLeft;
    }

    public void setIfLeft(boolean ifLeft) {
        this.ifLeft = ifLeft;
    }

    public String getUrlFotoDono() {
        return urlFotoDono;
    }

    public void setUrlFotoDono(String urlFotoDono) {
        this.urlFotoDono = urlFotoDono;
    }

    public Mensagem(String idMensagem, String idUserRemetente, String idUserDestinatario, String data_hora, String conteudo, boolean ifLeft, String urlFotoDono) {
        this.idMensagem = idMensagem;
        this.idUserRemetente = idUserRemetente;
        this.idUserDestinatario = idUserDestinatario;
        this.data_hora = data_hora;
        this.conteudo = conteudo;
        this.ifLeft = ifLeft;
        this.urlFotoDono = urlFotoDono;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idMensagem);
        dest.writeString(idUserRemetente);
        dest.writeString(idUserDestinatario);
        dest.writeString(data_hora);
        dest.writeString(conteudo);
        dest.writeByte((byte) (ifLeft ? 1 : 0));
        dest.writeString(urlFotoDono);
    }
}
