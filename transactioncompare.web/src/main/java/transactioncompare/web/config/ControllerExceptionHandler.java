package transactioncompare.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import transactioncompare.web.exception.FileCompareException;

@ControllerAdvice
public class ControllerExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);
	
    @ExceptionHandler(value = { FileCompareException.class })
    public ResponseEntity<Object> handleInvalidInputException(FileCompareException ex) {
    	LOGGER.error("Error occured while comparing the files " + ex.getMessage());
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
