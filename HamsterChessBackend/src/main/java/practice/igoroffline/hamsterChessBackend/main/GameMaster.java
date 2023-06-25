package practice.igoroffline.hamsterChessBackend.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import practice.igoroffline.hamsterChessBackend.board.Board;

@Getter
@ToString
@EqualsAndHashCode
public class GameMaster {

    private final Board board;

    public GameMaster() {
        this.board = new Board();
    }
}
