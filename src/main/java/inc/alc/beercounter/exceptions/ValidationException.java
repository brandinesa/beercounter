package inc.alc.beercounter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="F.U.")
public class ValidationException extends Exception {
    public ValidationException(String msg) { super(msg); }
}
