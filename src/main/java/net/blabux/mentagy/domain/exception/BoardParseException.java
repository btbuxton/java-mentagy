package net.blabux.mentagy.domain.exception;

public class BoardParseException extends RuntimeException {
    private static final long serialVersionUID = 8589966733606187218L;

    public BoardParseException(Exception ex) {
        super(ex);
    }


}
