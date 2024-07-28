package technesis.server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import technesis.server.api.dto.ApiResponseDto;
import technesis.server.api.dto.GetCurrentOutDto;
import technesis.server.api.dto.GetListOutDto;

public interface ServerApi {
    @PostMapping("set")
    ResponseEntity<ApiResponseDto> setTemperature(@RequestParam("value") float value, @RequestHeader("test") int test);

    @PostMapping("clear")
    ResponseEntity<ApiResponseDto> clear(@RequestHeader("test") int test);

    @GetMapping("current")
    ResponseEntity<GetCurrentOutDto> getCurrent(@RequestHeader("test") int test);

    @GetMapping("list")
    ResponseEntity<GetListOutDto> getList(@RequestParam("count") int count, @RequestParam("offset") int offset, @RequestHeader("test") int test);
}
