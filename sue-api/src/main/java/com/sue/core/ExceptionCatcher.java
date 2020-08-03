package com.sue.core;

import com.sue.core.configuration.ExceptionCodeConfiguration;
import com.sue.exception.passportexception.PassportException;
import com.sue.utils.IMOOCJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sue
 * @date 2020/8/2 18:46
 */

@RestControllerAdvice
public class ExceptionCatcher {


    @Autowired
    private ExceptionCodeConfiguration exceptionCodeConfiguration;

    @ExceptionHandler(value = PassportException.class)
    public ResponseEntity<IMOOCJSONResult> handlePassportException(HttpServletRequest request, HttpServletResponse response, PassportException e){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.resolve(HttpStatus.UNAUTHORIZED.value());
        IMOOCJSONResult imoocjsonResult = IMOOCJSONResult.errorMsg(exceptionCodeConfiguration.getMessage(e.getCode()));
        ResponseEntity<IMOOCJSONResult> responseEntity = new ResponseEntity<IMOOCJSONResult>(
                imoocjsonResult,
                httpHeaders,
                httpStatus
        );
        return responseEntity;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public IMOOCJSONResult handleBeanValidation(
            HttpServletRequest request,
            MethodArgumentNotValidException e
    ){
        System.out.println("捕获到了2");
        String requestUrl = request.getRequestURL().toString();
        String method = request.getMethod();

        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        String errors = formatAllErrorMessage(allErrors);

        return IMOOCJSONResult.errorMsg(errors);
    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public IMOOCJSONResult handleConstraintException(HttpServletRequest request, ConstraintViolationException e){
//        String requestUrl = request.getRequestURL().toString();
//        String method = request.getMethod();
//        String message = e.getMessage();
//        return new IMOOCJSONResult(message);
//    }

    private String formatAllErrorMessage(List<ObjectError> errors){
        StringBuffer stringBuffer = new StringBuffer();
        errors.forEach(i->{
            stringBuffer.append(i.getDefaultMessage()).append(';');
        });
        return stringBuffer.toString();
    }


}
