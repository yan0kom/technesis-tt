package technesis.client.backend;

public class ApiCallException extends RuntimeException {
    private final int httpStatus;
    private final int errorCode;

    public ApiCallException(int httpStatus, int errorCode) {
        super(errorCode == 0 ?
                String.format("API call returned HTTP status %d", httpStatus) :
                String.format("API call returned error code %d", errorCode));
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
