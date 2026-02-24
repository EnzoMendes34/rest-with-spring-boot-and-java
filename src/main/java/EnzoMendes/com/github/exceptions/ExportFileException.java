package EnzoMendes.com.github.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExportFileException extends RuntimeException {
    public ExportFileException(String message) {
        super(message);
    }

    public ExportFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
