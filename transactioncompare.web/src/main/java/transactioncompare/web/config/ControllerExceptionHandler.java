package transactioncompare.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import transactioncompare.web.exception.FileCompareException;
import transactioncompare.web.exception.ReconciliationException;

@ControllerAdvice
public class ControllerExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);
	
    @ExceptionHandler(value = { FileCompareException.class })
    public ResponseEntity<Object> handleInvalidInputException(FileCompareException ex) {
    	LOGGER.error("Error occured while comparing the files ", ex);
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(value = { ReconciliationException.class })
    public ResponseEntity<Object> handleReconcilitationException(ReconciliationException ex) {
    	LOGGER.error("Error occured while generating suggestions ", ex);
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
