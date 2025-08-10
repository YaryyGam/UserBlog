package com.yaryy.user_service.ExceptionHandler.CustomeExceptions;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(String message){super(message);}
}
