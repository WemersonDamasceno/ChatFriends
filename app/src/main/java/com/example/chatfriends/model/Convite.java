package com.example.chatfriends.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Convite implements Parcelable {
    private String idConvite;
    private Usuario userQuemEnviou;
    private Usuario userQuemRecebeu;
    private boolean foiAceito;

    public Convite() {
    }

    public String getIdConvite() {
        return idConvite;
    }

    public void setIdConvite(String idConvite) {
        this.idConvite = idConvite;
    }

    public Usuario getUserQuemEnviou() {
        return userQuemEnviou;
    }

    public void setUserQuemEnviou(Usuario userQuemEnviou) {
        this.userQuemEnviou = userQuemEnviou;
    }

    public Usuario getUserQuemRecebeu() {
        return userQuemRecebeu;
    }

    public void setUserQuemRecebeu(Usuario userQuemRecebeu) {
        this.userQuemRecebeu = userQuemRecebeu;
    }

    public boolean isFoiAceito() {
        return foiAceito;
    }

    public void setFoiAceito(boolean foiAceito) {
        this.foiAceito = foiAceito;
    }

    public Convite(String idConvite, Usuario userQuemEnviou, Usuario userQuemRecebeu, boolean foiAceito) {
        this.idConvite = idConvite;
        this.userQuemEnviou = userQuemEnviou;
        this.userQuemRecebeu = userQuemRecebeu;
        this.foiAceito = foiAceito;
    }

    protected Convite(Parcel in) {
        idConvite = in.readString();
        userQuemEnviou = in.readParcelable(Usuario.class.getClassLoader());
        userQuemRecebeu = in.readParcelable(Usuario.class.getClassLoader());
        foiAceito = in.readByte() != 0;
    }

    public static final Creator<Convite> CREATOR = new Creator<Convite>() {
        @Override
        public Convite createFromParcel(Parcel in) {
            return new Convite(in);
        }

        @Override
        public Convite[] newArray(int size) {
            return new Convite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idConvite);
        dest.writeParcelable(userQuemEnviou, flags);
        dest.writeParcelable(userQuemRecebeu, flags);
        dest.writeByte((byte) (foiAceito ? 1 : 0));
    }
}
