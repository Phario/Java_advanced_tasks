package pl.pwr.ite.dynak.lab06.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.ite.dynak.lab06.core.services.CurrentDateService;

@RestController
@AllArgsConstructor
@RequestMapping("/time")
public class CurrentDayController {
    private final CurrentDateService currentDateService;

    @GetMapping("/current-day")
    public ResponseEntity<Integer> getCurrentDay() {
        return ResponseEntity.ok(currentDateService.getCurrentDate());
    }

    @PostMapping("/skip")
    public ResponseEntity<Void> skipDays(@RequestParam int amount) {
        currentDateService.skipDays(amount);
        return ResponseEntity.ok().build();
    }
}
