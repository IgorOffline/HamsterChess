package practice.igoroffline.hamsterchessbackend.legal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import practice.igoroffline.hamsterchessbackend.board.Square;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
public class EnrichedLegalMoves {

    private Map<Integer, List<Integer>> legalMoves;

    public EnrichedLegalMoves(LegalMoves legalMoves) {
        this.legalMoves = legalMoves.getLegalMoves().entrySet().stream().collect(
                Collectors.toMap(
                        fromIndex -> fromIndex.getKey().getIndex(),
                        toIndices -> toIndices.getValue().stream().map(Square::getIndex).collect(Collectors.toList())
                ));
    }
}
