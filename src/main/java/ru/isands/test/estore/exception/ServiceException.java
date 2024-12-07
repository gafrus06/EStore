package ru.isands.test.estore.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException{
    public ServiceException(String text){
        super(text);
    }
}
