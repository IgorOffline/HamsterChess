package practice.igoroffline.hamsterchessbackend.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import practice.igoroffline.hamsterchessbackend.board.Piece;

@Slf4j
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
        log.info("move= {}", move);
        // && sq.getPiece() != Piece.NONE -> We don't want to do similar checks here, this is just to
        // avoid awkward frontend bugs
        gameMaster.setFromSquare(gameMaster.getBoard().getBoard().stream().filter(
                sq -> sq.getIndex() == move.from() && sq.getPiece() != Piece.NONE).findFirst());
        gameMaster.setToSquare(gameMaster.getBoard().getBoard().stream().filter(
                sq -> sq.getIndex() == move.to()).findFirst());
        gameMaster.moveAndCalculate();
        return gameMaster;
    }
}
