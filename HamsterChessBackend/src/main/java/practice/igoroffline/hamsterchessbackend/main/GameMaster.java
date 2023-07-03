package practice.igoroffline.hamsterchessbackend.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import practice.igoroffline.hamsterchessbackend.board.Board;
import practice.igoroffline.hamsterchessbackend.board.EnrichedBoard;

@Getter
@ToString
@EqualsAndHashCode
public class GameMaster {

    private final Board board;
    private final EnrichedBoard enrichedBoard;

    public GameMaster() {
        this.board = new Board();
        this.enrichedBoard = new EnrichedBoard(board);
    }
}
