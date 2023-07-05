package practice.igoroffline.hamsterchessbackend.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RController {

    private GameMaster gameMaster = new GameMaster();

    @GetMapping
    String home() {
        return "Hello, World!";
    }

    @GetMapping("/reset")
    GameMaster reset() {
        gameMaster = new GameMaster();
        return gameMaster;
    }

    @PostMapping("/move")
    GameMaster move() {
        throw new UnsupportedOperationException();
    }
}
