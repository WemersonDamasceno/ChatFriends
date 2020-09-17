package com.example.chatfriends.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Amigos implements Parcelable {
    Usuario user1;
    Usuario user2;
    String idAmigos;

    public Amigos(Usuario user1, Usuario user2, String idAmigos) {
        this.user1 = user1;
        this.user2 = user2;
        this.idAmigos = idAmigos;
    }

    protected Amigos(Parcel in) {
        user1 = in.readParcelable(Usuario.class.getClassLoader());
        user2 = in.readParcelable(Usuario.class.getClassLoader());
        idAmigos = in.readString();
    }

    public static final Creator<Amigos> CREATOR = new Creator<Amigos>() {
        @Override
        public Amigos createFromParcel(Parcel in) {
            return new Amigos(in);
        }

        @Override
        public Amigos[] newArray(int size) {
            return new Amigos[size];
        }
    };

    public Usuario getUser1() {
        return user1;
    }

    public void setUser1(Usuario user1) {
        this.user1 = user1;
    }

    public Usuario getUser2() {
        return user2;
    }

    public void setUser2(Usuario user2) {
        this.user2 = user2;
    }

    public String getIdAmigos() {
        return idAmigos;
    }

    public void setIdAmigos(String idAmigos) {
        this.idAmigos = idAmigos;
    }

    public Amigos() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user1, flags);
        dest.writeParcelable(user2, flags);
        dest.writeString(idAmigos);
    }
}
