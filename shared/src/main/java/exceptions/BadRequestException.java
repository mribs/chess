package exceptions;

public class BadRequestException extends Exception{
  public BadRequestException() {
    super("bad request");
  }

}
