package com.example.advancedpokedex.data;

/*Custom Exception with or without exception*/
public class AuthServiceDatabaseException extends Exception
{
    private String message;

    /**
     * Constructor without Arguments
     */
    public AuthServiceDatabaseException()
    {
        super();
    }

    /**
     * Constructor with Arguments
     * @param msg message
     */
    public AuthServiceDatabaseException(String msg)
    {
        super();
        message=msg;
    }

    @Override
    public String getMessage()
    {
        return this.message;
    }
}
