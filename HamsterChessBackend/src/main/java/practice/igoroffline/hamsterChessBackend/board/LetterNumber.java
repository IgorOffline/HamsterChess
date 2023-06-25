package practice.igoroffline.hamsterChessBackend.board;

import practice.igoroffline.hamsterChessBackend.main.Messages;

public class LetterNumber {

    public static String getLetter(int letter) {
        return switch (letter) {
            case 0 -> "A";
            case 1 -> "B";
            case 2 -> "C";
            case 3 -> "D";
            case 4 -> "E";
            case 5 -> "F";
            case 6 -> "G";
            case 7 -> "H";
            default -> throw new IllegalArgumentException(Messages.UNKNOWN_LETTER);
        };
    }

    public static Letter getLetterEnum(int letter) {
        return switch (letter) {
            case -1 -> Letter.L;
            case 0 -> Letter.A;
            case 1 -> Letter.B;
            case 2 -> Letter.C;
            case 3 -> Letter.D;
            case 4 -> Letter.E;
            case 5 -> Letter.F;
            case 6 -> Letter.G;
            case 7 -> Letter.H;
            case 8 -> Letter.R;
            default -> throw new IllegalArgumentException(Messages.UNKNOWN_LETTER);
        };
    }

    public static String getNumber(int number) {
        return switch (number) {
            case 0 -> "1";
            case 1 -> "2";
            case 2 -> "3";
            case 3 -> "4";
            case 4 -> "5";
            case 5 -> "6";
            case 6 -> "7";
            case 7 -> "8";
            default -> throw new IllegalArgumentException(Messages.UNKNOWN_NUMBER);
        };
    }

    public static Number2 getNumberEnum(int number) {
        return switch (number) {
            case -1 -> Number2.NMinus1;
            case 0 -> Number2.N1;
            case 1 -> Number2.N2;
            case 2 -> Number2.N3;
            case 3 -> Number2.N4;
            case 4 -> Number2.N5;
            case 5 -> Number2.N6;
            case 6 -> Number2.N7;
            case 7 -> Number2.N8;
            case 8 -> Number2.N99;
            default -> throw new IllegalArgumentException(Messages.UNKNOWN_NUMBER);
        };
    }

    public static Number2 getNumberEnumReverse(int number) {
        return switch (number) {
            case -1 -> Number2.N99;
            case 0 -> Number2.N8;
            case 1 -> Number2.N7;
            case 2 -> Number2.N6;
            case 3 -> Number2.N5;
            case 4 -> Number2.N4;
            case 5 -> Number2.N3;
            case 6 -> Number2.N2;
            case 7 -> Number2.N1;
            case 8 -> Number2.NMinus1;
            default -> throw new IllegalArgumentException(Messages.UNKNOWN_NUMBER);
        };
    }

    public static boolean isEnumLegal(Letter letter) {
        return letter.index >= 0 && letter.index < 8;
    }

    public static boolean isEnumLegal(Number2 number) {
        return number.index >= 0 && number.index <= 7;
    }
}
