package com.travelblog.http;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Author implements Parcelable {
    private String name;
    private String avatar;

    public Author(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    protected Author(Parcel in) {
        name = in.readString();
        avatar = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(avatar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };


    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAvatarURL() {
        return BlogHttpClient.BASE_URL + BlogHttpClient.PATH + getAvatar();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(name, author.name) &&
                Objects.equals(avatar, author.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, avatar);
    }
}
