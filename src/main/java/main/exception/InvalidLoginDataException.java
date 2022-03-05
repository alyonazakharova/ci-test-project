package main.exception;

public class InvalidLoginDataException extends RuntimeException {
    public InvalidLoginDataException() {
        super("Wrong login or password");
    }

    public InvalidLoginDataException(String string) {
        super(string);
    }
}
