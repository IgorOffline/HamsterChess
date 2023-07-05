package practice.igoroffline.hamsterchessbackend.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.igoroffline.hamsterchessbackend.board.EnrichedBoard;

@RestController
public class RController {

    private GameMaster gameMaster = new GameMaster();

    @GetMapping
    String home() {
        return "Hello, World!";
    }

    @GetMapping("/reset")
    EnrichedBoard reset() {
        gameMaster = new GameMaster();
        return gameMaster.getEnrichedBoard();
    }

    @PostMapping("/move")
    EnrichedBoard move() {
        return gameMaster.getEnrichedBoard();
    }
}
