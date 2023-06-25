package practice.igoroffline.hamsterChessBackend.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RController {

    @GetMapping
    String home() {
        return "Hello, World!";
    }

    @GetMapping("/plusMinus")
    GameMaster plusMinus() {
        return new GameMaster();
    }
}
