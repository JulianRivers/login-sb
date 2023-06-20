package me.julianrivers.login.exceptions;

import me.julianrivers.login.model.dto.ErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Si la excepción es de tipo AppException
     * @param exception la excepción ocurrida
     * @param request
     * @return Excepción con su debida http status
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDTO> handleAppException(AppException exception, WebRequest request) {
        ErrorDTO error = new ErrorDTO(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Si la excepción es de que no encuentra algún recurso
     * @param exception la excepción ocurrida
     * @param request la actual solicitud
     * @return Excepción con su debida http status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        ErrorDTO error = new ErrorDTO(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Si ocurre un error inesperado
     * @param exception la excepción ocurrida
     * @param request la actual solicitud
     * @return Excepción con su debida http status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGlobalException(Exception exception, WebRequest request) {
        ErrorDTO error = new ErrorDTO(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo =((FieldError)error).getField();
            String mensaje = error.getDefaultMessage();

            errores.put(nombreCampo,mensaje);
        });
        return new ResponseEntity<>(errores,HttpStatus.BAD_REQUEST);
    }

}
