package com.multigenesis.ecomm_assesment.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.multigenesis.ecomm_assesment.payload.APIResponse;

@RestControllerAdvice
public class MyGlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
		Map<String,String> response=new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(err -> {
			String fieldName=((FieldError) err).getField();
			String message=err.getDefaultMessage();
			response.put(fieldName,message);
		});
		return new ResponseEntity<Map<String,String>>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
		String message=e.getMessage();
		APIResponse apiResponse=new APIResponse(message,false);
		return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);	
	}
	
	@ExceptionHandler(APIException.class)
	public ResponseEntity<APIResponse> myAPIException(APIException e){
		String message=e.getMessage();
		APIResponse apiResponse=new APIResponse(message,false);
		return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("File size exceeds limit!");
    }
	
	 @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
	    public ResponseEntity<Map<String, String>> handleConstraintViolation(
	            jakarta.validation.ConstraintViolationException ex) {

	        Map<String, String> errors = new HashMap<>();

	        ex.getConstraintViolations().forEach(error -> {
	            String field = error.getPropertyPath().toString();
	            String message = error.getMessage();
	            errors.put(field, message);
	        });

	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }


}
