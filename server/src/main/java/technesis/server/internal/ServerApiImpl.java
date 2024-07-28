package technesis.server.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import technesis.regulator.Regulator;
import technesis.server.api.ServerApi;
import technesis.server.api.dto.ApiResponseDto;
import technesis.server.api.dto.GetCurrentOutDto;
import technesis.server.api.dto.GetListOutDto;

import java.util.ArrayList;
import java.util.Collections;

@RestController
public class ServerApiImpl implements ServerApi {
    private final Regulator regulator;

    public ServerApiImpl(Regulator regulator) {
        this.regulator = regulator;
    }

    @Override
    public ResponseEntity<ApiResponseDto> setTemperature(float value, int test) {
        int err = regulator.adjustTemp(
                (byte) (Regulator.setCmd | Regulator.reservedBit),
                value, null, 0);
        return ResponseEntity.ok(new ApiResponseDto(err));
    }

    @Override
    public ResponseEntity<ApiResponseDto> clear(int test) {
        int err = regulator.adjustTemp(
                (byte) (Regulator.clearCmd | Regulator.reservedBit),
                0, null, 0);
        return ResponseEntity.ok(new ApiResponseDto(err));
    }

    @Override
    public ResponseEntity<GetCurrentOutDto> getCurrent(int test) {
        var list = new ArrayList<Float>(1);
        int err = regulator.adjustTemp(
                (byte) (Regulator.getCmd | (1 << 1) | Regulator.reservedBit),
                0, list, 0);
        if (err == 0) {
            return ResponseEntity.ok(new GetCurrentOutDto(0, list.get(0)));
        } else {
            return ResponseEntity.ok(new GetCurrentOutDto(err, 0));
        }
    }

    @Override
    public ResponseEntity<GetListOutDto> getList(int count, int offset, int test) {
        var list = new ArrayList<Float>(count);
        int err = regulator.adjustTemp(
                (byte) (Regulator.getCmd | (count << 1) | Regulator.reservedBit),
                0, list, offset);
        if (err == 0) {
            return ResponseEntity.ok(new GetListOutDto(0, list));
        } else {
            return ResponseEntity.ok(new GetListOutDto(err, Collections.emptyList()));
        }
    }
}
