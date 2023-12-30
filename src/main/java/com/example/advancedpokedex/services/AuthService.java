package com.example.advancedpokedex.services;

import com.example.advancedpokedex.data.AuthServiceDatabaseException;
import com.example.advancedpokedex.data.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import com.example.advancedpokedex.ui.LoginWindow;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthService
{
    HashSet<User> users = new HashSet<>();
    ObjectMapper objectMapper = new ObjectMapper();

    static Random random=new Random();

    final String FILENAME="users.json";

    public AuthService() throws AuthServiceDatabaseException
    {
        //Load Users from File, if file exits, and deserialize it
        try
        {
            Path filePath = Paths.get(FILENAME);
            if(Files.exists(filePath))
            {
                String json=new String(Files.readAllBytes(filePath));
                users=objectMapper.readValue(json, new TypeReference<>(){});
            }

        }
        catch (IOException e)
        {
            throw new AuthServiceDatabaseException(e.getMessage());
        }

    }

    @Override
    protected void finalize() throws Throwable
    {
        //Save list to file
        PrintWriter writer=null;
        try
        {
            writer= new PrintWriter(FILENAME);
            String json=objectMapper.writeValueAsString(users);
            writer.println(json);

        }
        catch (IOException e)
        {
            //ignoring message in finalize
           throw new AuthServiceDatabaseException();
        }
        finally
        {
            if (writer != null)
            {
                writer.flush();
                writer.close();
            }
        }
        super.finalize();
    }


    //region Public-Methods

    /**
     * Shows Login-Window and handle Login Procedure
     * @return userid
     */
    public int performLogon() //returns uid
    {
        try
        {
            LoginWindow window=new LoginWindow();
            User tmp=window.showWindow();

            if(tmp==null)
                return -1;

            String pwLgn=hashPasswd(tmp.getPasswd());
            User user=getUserByName(tmp.getUname());

            if(user==null)
                return -1;

            if(user.getPasswd().equals(pwLgn))
                return user.getUid(); //login successful -> return uid
            else
                return  -1;
        }
        catch (Exception e)
        {
            return  -1;
        }
    }

    /**
     * Add User with prompting for name and password
     * @return userid
     */
    public int addUserWithDlg()
    {
        try
        {
            LoginWindow window=new LoginWindow();
            User tmp=window.showWindow();

            if(tmp==null)
                return -1;

            boolean successful = addUser(tmp.getUname(),tmp.getPasswd());

            if(successful)
                return getUserByName(tmp.getUname()).getUid();
            else
                return -1;

        }
        catch (Exception e)
        {
            return  -1;
        }
    }

    /**
     * Tries to create and add a user
     * @param username Name of User
     * @param passwd Password of User
     * @return creation successful
     */
    public boolean addUser(String username, String passwd)
    {
        //only proceed if username, password is valid
        if (isInvalidUsername(username) || isInvalidPassword(passwd))
            return false;

        try
        {
            int uid=generateUid();
            String pw=hashPasswd(passwd);
            users.add(new User(uid,username,pw));

            return true;
        }
        catch (IndexOutOfBoundsException | NoSuchAlgorithmException except)
        {
            return false;
        }
    }

    /**
     * Tries to delete a user
     * @param uid UserId
     * @return deletion successful
     */
    public boolean delUser(int uid)
    {
        //remove user wih provided uid from list, returns true if successful else false
        return users.removeIf(u -> u.getUid() == uid);
    }

    /**
     * Update a users name and password
     * @param uid UserId
     * @param username New Username
     * @param passwd New Password
     * @return update successful
     */
    public boolean updUser(int uid, String username, String passwd)
    {
        //check for invalid input, when detected cancel operation
        if (isInvalidUsername(username) || isInvalidPassword(passwd))
            return false;


        User user = null;
        String oldUN = null, oldPW = null;
        try
        {
            //store old values
            user = getUserByUid(uid);

            if(user==null)
                return false;

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
            if (user != null && !StringIsNullOrEmpty(oldUN, oldPW))
            {
                updUsername(user, oldUN);
                updPassword(user, oldPW);
            }
            return false; //update failed
        }
    }

    /**
     * Update a users name
     * @param uid UserId
     * @param username New Username
     * @return update successful
     */
    public boolean updUsername(int uid, String username)
    {
        //check for invalid input, when detected cancel operation
        if (isInvalidUsername(username))
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

    /**
     * Update a users password
     * @param uid UserId
     * @param password New Password
     * @return update successful
     */
    public boolean updPassword(int uid, String password)
    {
        //check for invalid input, when detected cancel operation
        if (isInvalidPassword(password))
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

    /**
     * Get User by UserId for external use
     * @param uid UserId
     * @return User or Null
     */
    public User getUserByUid(int uid)
    {
        /*
        get user object and return it
         or returns null
        */
        return users.parallelStream()
                .filter(u -> u.getUid() == uid)
                .findAny()
                .orElse(null);
    }
    //endregion Public-Methods


    //region Private-Methods-1
    /** helper method: perform update on user object
     * @param user UserObject
     * @param username new username
     */
    private void updUsername(User user, String username)
    {
        user.setUname(username);
    }

    /** helper method: perform update on user object
     * @param user UserObject
     * @param hashedpw new password
     */
    private void updPassword(User user, String hashedpw)
    {
        user.setPasswd(hashedpw);
    }
    //endregion Private-Methods-1


    /**
     * helper method: Get User only providing username
     * @param name username
     * @return UserObject or Null
     */
    //region Private-Methods-2 (Hilfsmethoden)
    private User getUserByName(String name)
    {
        /*
        get user object and return it
         or returns null
        */
        return users.parallelStream()
                .filter(u -> u.getUname().equals(name))
                .findAny()
                .orElse(null);
    }

    /**
     * helper method: Generate a Random UserId for User-Creation
     * @return userid
     * @throws IndexOutOfBoundsException seed to big -> user limit reached
     */
    private int generateUid() throws IndexOutOfBoundsException
    {
        boolean uidused=true;
        int uid=-1;

        while (uidused)
        {
            //generate random uid

            if((users.size()>=Integer.MAX_VALUE-1000))
                throw new IndexOutOfBoundsException();

            int randuid=random.nextInt(users.size()+10000);

            //check if uid already used -> no->return false, true=return true
            uidused=users.parallelStream().noneMatch(u->u.getUid()==randuid);
        }

        return uid;
    }

    /**
     * helper method: Checks if any of the provided strings is null or empty
     * @param strings strings to check
     * @return string null or empty
     */
    private boolean StringIsNullOrEmpty(String... strings)
    {
        //If any of the provided strings is null or empty -> return true
        return Arrays.stream(strings).anyMatch(s->s==null||s.isEmpty());
    }

    /**
     * helper method: validate username
     * @param uname username
     * @return username is valid
     */
    private boolean isInvalidUsername(String uname)
    {
        //eventually implement username criteria

        //check if username is blank
        if (StringIsNullOrEmpty(uname))
            return false; //username blank

        //check if username already used (true=used,false=free)
        return users.parallelStream().anyMatch(u -> u.getUname().equals(uname));
    }

    /**
     * helper method: validate password
     * @param passwd plaintext password
     * @return password is valid
     */
    private boolean isInvalidPassword(String passwd)
    {
        //eventually implement password criteria

        //don't allow blank passwords (true=invalid,false=valid)
        return StringIsNullOrEmpty(passwd);
    }

    /**
     * helper method: Hashes a given password
     * @param plainpw password in plaintext
     * @return hashed password
     * @throws NoSuchAlgorithmException invalid algorithm
     */
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
