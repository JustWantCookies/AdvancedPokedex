package com.example.advancedpokedex.data;

import java.io.Serializable;

public class User implements Serializable
{
    private final int uid; //muss auto-generated sein, denn darf nur einmal vorkommen
    private String uname; //Username plain-text

    private String passwd; //Passwort hashed

    public User(int uid,String uname, String passwd)
    {
        this.uid=uid;
        this.uname = uname;
        this.passwd = passwd;
    }

    public synchronized int getUid()
    {
        return uid;
    }

    public synchronized String getUname()
    {
        return uname;
    }

    public synchronized void setUname(String uname)
    {
        this.uname = uname;
    }

    public synchronized String getPasswd()
    {
        return passwd;
    }

    public synchronized void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }
}
