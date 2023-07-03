package practice.igoroffline.hamsterchessbackend.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.igoroffline.hamsterchessbackend.board.EnrichedBoard;

@RestController
public class RController {

    @GetMapping
    String home() {
        return "Hello, World!";
    }

    @GetMapping("/plusMinus")
    EnrichedBoard plusMinus() {
        return new GameMaster().getEnrichedBoard();
    }
}
