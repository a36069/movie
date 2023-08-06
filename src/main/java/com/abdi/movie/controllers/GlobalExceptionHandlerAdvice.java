package com.abdi.movie.controllers;


import com.abdi.movie.dtos.Response;
import com.abdi.movie.exceptions.DataNotFoundException;
import com.abdi.movie.exceptions.GlobalException;
import com.abdi.movie.exceptions.NotAccessException;
import com.abdi.movie.models.dtos.FieldValidation;
import com.abdi.movie.services.MessageComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandlerAdvice {

    private final MessageComponent messageComponent;

    @ExceptionHandler({GlobalException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response<Object> internalErrException(GlobalException exception, HandlerMethod handlerMethod, ServletWebRequest request) {

        return Response.builder()
                .code(exception.getStatusCode())
                .message(exception.getStatusMessage())
                .path(getPath(request))
                .build();
    }



    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response<Object> methodArgumentNotValid(MethodArgumentNotValidException exception,HandlerMethod handlerMethod, ServletWebRequest request) {

        List<FieldValidation> notValidFields = exception.getBindingResult().getFieldErrors().stream()
                .map(err -> new FieldValidation(err.getField(), err.getDefaultMessage()))
                .distinct()
                .toList();

        return Response.builder()
                .code(messageComponent.getInputValidationCode())
                .message(messageComponent.getInputValidationMessage())
                .error(notValidFields)
                .path(getPath(request))
                .build();
    }

    @ExceptionHandler({NotAccessException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Response<Object> notAccessExceptionExceptionHandler(NotAccessException exception,HandlerMethod handlerMethod, ServletWebRequest request) {

        return Response.builder()
                .code(messageComponent.getForbiddenCode())
                .message(messageComponent.getForbiddenMessage())
                .path(getPath(request))
                .build();
    }

    @ExceptionHandler({DataNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Response<Object> resourceNotFoundExceptionHandler(DataNotFoundException exception, HandlerMethod handlerMethod, ServletWebRequest request) {

        return Response.builder()
                .code(messageComponent.getNotfoundCode())
                .message(messageComponent.getNotfoundMessage())
                .path(getPath(request))
                .build();
    }

    private String getPath(ServletWebRequest request) {
        return request.getRequest().getRequestURI();
    }
}