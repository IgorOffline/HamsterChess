package practice.igoroffline.hamsterchessbackend.legal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import practice.igoroffline.hamsterchessbackend.board.EnrichedSquare;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
public class EnrichedLegalMoves {

    private Map<EnrichedSquare, List<EnrichedSquare>> legalMoves;

    public EnrichedLegalMoves(LegalMoves legalMoves) {
        this.legalMoves = legalMoves.getLegalMoves().entrySet().stream().collect(
                Collectors.toMap(
                        entry -> new EnrichedSquare(entry.getKey()),
                        entry -> entry.getValue().stream().map(EnrichedSquare::new).collect(Collectors.toList())
                ));
    }
}
