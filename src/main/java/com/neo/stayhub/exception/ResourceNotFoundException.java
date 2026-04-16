package com.neo.stayhub.exception;

import org.springframework.web.servlet.resource.NoResourceFoundException;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message)
    {
        super(message);
    }

}
