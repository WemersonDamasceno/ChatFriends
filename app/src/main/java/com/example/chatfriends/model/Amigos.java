package com.example.chatfriends.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Amigos implements Parcelable {
    String user1Id;
    String user2Id;
    String idAmigos;

    public Amigos() {
    }

    protected Amigos(Parcel in) {
        user1Id = in.readString();
        user2Id = in.readString();
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

    public String getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(String user1Id) {
        this.user1Id = user1Id;
    }

    public String getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(String user2Id) {
        this.user2Id = user2Id;
    }

    public String getIdAmigos() {
        return idAmigos;
    }

    public void setIdAmigos(String idAmigos) {
        this.idAmigos = idAmigos;
    }

    public Amigos(String user1Id, String user2Id, String idAmigos) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.idAmigos = idAmigos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user1Id);
        dest.writeString(user2Id);
        dest.writeString(idAmigos);
    }
}
