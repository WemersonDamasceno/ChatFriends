package com.example.chatfriends.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Grupo implements Parcelable {
    private String idGrupo;
    private String nomeGrupo;
    private String idUserAdminGrupo;
    private List<String> idUsersGrupo;
    private String descricaoGrupo;
    private boolean privacidadeGrupo;
    private String urlFotoGrupo;

    public Grupo() {
    }

    protected Grupo(Parcel in) {
        idGrupo = in.readString();
        nomeGrupo = in.readString();
        idUserAdminGrupo = in.readString();
        idUsersGrupo = in.createStringArrayList();
        descricaoGrupo = in.readString();
        privacidadeGrupo = in.readByte() != 0;
        urlFotoGrupo = in.readString();
    }

    public static final Creator<Grupo> CREATOR = new Creator<Grupo>() {
        @Override
        public Grupo createFromParcel(Parcel in) {
            return new Grupo(in);
        }

        @Override
        public Grupo[] newArray(int size) {
            return new Grupo[size];
        }
    };

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    public String getIdUserAdminGrupo() {
        return idUserAdminGrupo;
    }

    public void setIdUserAdminGrupo(String idUserAdminGrupo) {
        this.idUserAdminGrupo = idUserAdminGrupo;
    }

    public List<String> getIdUsersGrupo() {
        return idUsersGrupo;
    }

    public void setIdUsersGrupo(List<String> idUsersGrupo) {
        this.idUsersGrupo = idUsersGrupo;
    }

    public String getDescricaoGrupo() {
        return descricaoGrupo;
    }

    public void setDescricaoGrupo(String descricaoGrupo) {
        this.descricaoGrupo = descricaoGrupo;
    }

    public boolean isPrivacidadeGrupo() {
        return privacidadeGrupo;
    }

    public void setPrivacidadeGrupo(boolean privacidadeGrupo) {
        this.privacidadeGrupo = privacidadeGrupo;
    }

    public String getUrlFotoGrupo() {
        return urlFotoGrupo;
    }

    public void setUrlFotoGrupo(String urlFotoGrupo) {
        this.urlFotoGrupo = urlFotoGrupo;
    }

    public Grupo(String idGrupo, String nomeGrupo, String idUserAdminGrupo, List<String> idUsersGrupo, String descricaoGrupo, boolean privacidadeGrupo, String urlFotoGrupo) {
        this.idGrupo = idGrupo;
        this.nomeGrupo = nomeGrupo;
        this.idUserAdminGrupo = idUserAdminGrupo;
        this.idUsersGrupo = idUsersGrupo;
        this.descricaoGrupo = descricaoGrupo;
        this.privacidadeGrupo = privacidadeGrupo;
        this.urlFotoGrupo = urlFotoGrupo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idGrupo);
        dest.writeString(nomeGrupo);
        dest.writeString(idUserAdminGrupo);
        dest.writeStringList(idUsersGrupo);
        dest.writeString(descricaoGrupo);
        dest.writeByte((byte) (privacidadeGrupo ? 1 : 0));
        dest.writeString(urlFotoGrupo);
    }
}
