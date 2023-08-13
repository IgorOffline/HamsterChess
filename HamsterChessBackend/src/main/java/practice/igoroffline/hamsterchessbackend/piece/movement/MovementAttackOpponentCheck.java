package practice.igoroffline.hamsterchessbackend.piece.movement;

import practice.igoroffline.hamsterchessbackend.board.Square;

import java.util.List;
import java.util.Optional;

public record MovementAttackOpponentCheck(List<Square> movementSquares, Optional<List<Square>> attackSquares, boolean opponentsKingInCheck) {
}
