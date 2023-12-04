package stepanalyzer.exception;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	// handle specific exception
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFountException(EntityNotFoundException exception, WebRequest request) {
		LOG.error("ResponseProcessingException= ", exception);
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<Object>(errorDetails, HttpStatus.NOT_FOUND);
	}

	// handle specific exception
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleResourceValidationException(ValidationException exception, WebRequest request) {
		LOG.error("ResponseProcessingException= ", exception);
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<Object>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// handle global exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception exception, WebRequest request) {
		LOG.error("ResponseProcessingException= ", exception);
		ErrorDetails errorDetails = new ErrorDetails(new Date(), "Internal Server Error", request.getDescription(false));
		return new ResponseEntity<Object>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
