package models;

import android.graphics.Bitmap;

/**
 * Created by marcelo.cunha on 22/01/2018.
 */

public class Contato {
    private String name;
    private String phoneNumber;
    private long blockCalls;
    private Bitmap photo;


    public Contato(String name, String phoneNumber, long blockCalls) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.blockCalls = blockCalls;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getBlockCalls() {
        return blockCalls;
    }

    public void setBlockCalls(long blockCalls) {
        this.blockCalls = blockCalls;
    }


}
