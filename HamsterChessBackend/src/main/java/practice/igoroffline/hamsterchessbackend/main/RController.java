package practice.igoroffline.hamsterchessbackend.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    GameMaster move(@RequestBody Move move) {
        gameMaster.setFromSquare(gameMaster.getBoard().getBoard().stream().filter(
                sq -> sq.getIndex() == move.from()).findFirst());
        gameMaster.setToSquare(gameMaster.getBoard().getBoard().stream().filter(
                sq -> sq.getIndex() == move.to()).findFirst());
        gameMaster.moveAndCalculate();
        return gameMaster;
    }
}
