package technesis.server.api.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetListOutDto extends ApiResponseDto {
    private final List<Float> values;

    public GetListOutDto(int errorCode, List<Float> values) {
        super(errorCode);
        this.values = new ArrayList<>(values);
    }

    public List<Float> getValues() {
        return Collections.unmodifiableList(values);
    }
}
