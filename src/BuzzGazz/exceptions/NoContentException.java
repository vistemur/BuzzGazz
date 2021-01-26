package BuzzGazz.exceptions;

public class NoContentException extends Throwable {

    String message;

    public NoContentException() {
        message = "no content";
    }

    public NoContentException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
