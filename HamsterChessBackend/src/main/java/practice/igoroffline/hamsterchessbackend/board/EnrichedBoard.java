package practice.igoroffline.hamsterchessbackend.board;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
public class EnrichedBoard {

    private Map<Integer, Square> enrichedBoard;

    public EnrichedBoard(Board board) {
        final var b = board.getBoard();
        this.enrichedBoard = b.stream().collect(Collectors.toMap(b::indexOf, square -> square));
    }
}
