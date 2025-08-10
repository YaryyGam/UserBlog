package com.yaryy.comment_service.ExceptionHandler.CustomeExceptions;

public class NoCommentsException extends RuntimeException{
    public NoCommentsException(String message){super(message);}
}
