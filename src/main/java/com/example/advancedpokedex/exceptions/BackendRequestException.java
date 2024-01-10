package com.example.advancedpokedex.exceptions;

public class BackendRequestException extends RuntimeException{
        public BackendRequestException(String message) {
            super(message);
        }
}
