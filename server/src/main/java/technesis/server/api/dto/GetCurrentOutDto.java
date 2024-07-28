package technesis.server.api.dto;

public class GetCurrentOutDto extends ApiResponseDto {
    private final float value;

    public GetCurrentOutDto(int errorCode, float value) {
        super(errorCode);
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
