package technesis.server.api.dto;

public class ApiResponseDto {
    private final int errorCode;

    public ApiResponseDto(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
