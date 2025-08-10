package com.yaryy.user_service.ExceptionHandler.CustomeExceptions;

public class CommentOwnershipException extends RuntimeException{
    public CommentOwnershipException(String message){super(message);}
}
