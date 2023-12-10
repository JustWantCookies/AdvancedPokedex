package com.example.advancedpokedex.services;

import com.example.advancedpokedex.data.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

//ToDo: Write JavaDoc

public class AuthService
{
    HashSet<User> users = new HashSet<>();
    // SecureRandom random =new SecureRandom();

    public AuthService()
    {
        //Load List from File

    }

    @Override
    protected void finalize() throws Throwable
    {
        //Save list to file

        super.finalize();
    }

    //region Public-Methods
    public boolean addUser(String username, String passwd)
    {
        //only proceed if username, password is valid
        if (validateUsername(username) || validatePassword(passwd))
            return false;

        throw new java.util.EmptyStackException(); //for now just throw any exception

        /*try/catch*/

    }

    public boolean delUser(int uid)
    {
        //remove user wih provided uid from list, returns true if successful else false
        return users.removeIf(u -> u.getUid() == uid);
    }

    public boolean updUser(int uid, String username, String passwd)
    {
        //check for invalid input, when detected cancel operation
        if (validateUsername(username) || validatePassword(passwd))
            return false;


        User user = null;
        String oldUN = null, oldPW = null;
        try
        {
            //store old values
            user = getUserByUid(uid);
            oldUN = user.getUname();
            oldPW = user.getPasswd();

            //update values
            user.setUname(username);
            user.setPasswd(hashPasswd(passwd));

            return true; //update successful
        }
        catch (Exception e)
        {
            //revert to old data if possible
            if (user != null && StringIsNullOrEmpty(oldUN, oldPW))
            {
                updUsername(user, oldUN);
                updPassword(user, oldPW);
            }
            return false; //update failed
        }
    }

    public boolean updUsername(int uid, String username)
    {
        //check for invalid input, when detected cancel operation
        if (validateUsername(username))
            return false;

        try
        {
            updUsername(getUserByUid(uid), username);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean updPassword(int uid, String password)
    {
        //check for invalid input, when detected cancel operation
        if (validatePassword(password))
            return false;

        try
        {
            String pwHash = hashPasswd(password);
            updPassword(getUserByUid(uid), pwHash);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    //endregion Public-Methods

    //region Private-Methods-1
    private void updUsername(User user, String username)
    {
        user.setUname(username);
    }

    private void updPassword(User user, String hashedpw)
    {
        user.setPasswd(hashedpw);
    }
    //endregion Private-Methods-1


    //region Private-Methods-2 (Hilfsmethoden)
    private boolean StringIsNullOrEmpty(String... strings)
    {
        //ToDo: Implement validation of string
        return false;
    }

    private User getUserByUid(int uid) throws NoSuchElementException
    {
        /*
        get userobject and return it
         or throw a NoSuchElementException
        */
        return users.parallelStream()
                .filter(u -> u.getUid() == uid)
                .findAny()
                .orElseThrow();
    }

    private int generateUid()
    {
        //ToDo: Generate/Assign UID
        return 0;
        /**
         try
         {
         users.parallelStream().map(u -> u.getUid());
         //ArrayList lusers= users.parallelStream().map(u -> u.getUid()).toList();
         //int nextUid= Collections.max(lusers);

         }
         catch (NullPointerException e)
         {
         //
         }
         return 0;
         */
    }

    private boolean validateUsername(String uname)
    {
        //eventually implement username criteria

        //check if username is blank
        if (StringIsNullOrEmpty(uname))
            return false; //username blank

        //check if username already used
        if (users.parallelStream().filter(u -> u.getUname().equals(uname)).count() > 0)
            return false; //username used
        return true; //username free
    }

    private boolean validatePassword(String passwd)
    {
        //eventually implement password criteria

        //don't allow blank passwords
        if (passwd.isBlank())
            return false;

        return true;
    }

    private String hashPasswd(String plainpw) throws NoSuchAlgorithmException
    {
        //hash plain password
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(plainpw.getBytes(StandardCharsets.UTF_8));

        //return hash as string
        return Base64.getEncoder().encodeToString(hash);
    }
    //endregion Private-Methods-2 (Hilfsmethoden)
}
