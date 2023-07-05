package practice.igoroffline.hamsterchessbackend.legal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import practice.igoroffline.hamsterchessbackend.board.PieceColor;
import practice.igoroffline.hamsterchessbackend.board.Square;
import practice.igoroffline.hamsterchessbackend.main.GameMaster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString
public class LegalMoves {

    private Map<Square, List<Square>> legalMoves;

    public void calculate(GameMaster gameMaster) {
        throw new UnsupportedOperationException();
    }
}
