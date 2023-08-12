package practice.igoroffline.hamsterchessbackend.piece.movement;

public enum PieceMovement {
    NEXT_NUMBER, PREVIOUS_NUMBER, NEXT_LETTER, PREVIOUS_LETTER,
    PREVIOUS_LETTER_NEXT_NUMBER, NEXT_LETTER_NEXT_NUMBER, PREVIOUS_LETTER_PREVIOUS_NUMBER, NEXT_LETTER_PREVIOUS_NUMBER,
    PP_LETTER_NEXT_NUMBER, NN_NUMBER_PREVIOUS_LETTER, NN_NUMBER_NEXT_LETTER, NN_LETTER_NEXT_NUMBER,
    PP_LETTER_PREVIOUS_NUMBER, PP_NUMBER_PREVIOUS_LETTER, PP_NUMBER_NEXT_LETTER, NN_LETTER_PREVIOUS_NUMBER
}