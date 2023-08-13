package practice.igoroffline.hamsterchessbackend.piece.movement;

import practice.igoroffline.hamsterchessbackend.board.Square;

import java.util.List;

public record MovementAttackOpponentCheck(List<Square> movementSquares, List<Square> attackSquares, boolean opponentsKingInCheck) {
}
