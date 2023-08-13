enum BoardLetter {
  a("A"),
  b("B"),
  c("C"),
  d("D"),
  e("E"),
  f("F"),
  g("G"),
  h("H");

  const BoardLetter(this.name);

  final String name;

  @override
  String toString() {
    return name;
  }
}

enum BoardNumber {
  n1("1"),
  n2("2"),
  n3("3"),
  n4("4"),
  n5("5"),
  n6("6"),
  n7("7"),
  n8("8");

  const BoardNumber(this.name);

  final String name;

  @override
  String toString() {
    return name;
  }
}

enum Piece {
  none("_"),
  king("K"),
  rook("R"),
  bishop("B"),
  knight("N"),
  pawn("P");

  const Piece(this.name);

  final String name;

  @override
  String toString() {
    return name;
  }
}

enum PieceColor {
  none("_"),
  white("W"),
  black("B");

  const PieceColor(this.name);

  final String name;

  @override
  String toString() {
    return name;
  }
}

class BoardSquare {
  BoardLetter letter;
  BoardNumber number;
  Piece piece;
  PieceColor pieceColor;

  BoardSquare(this.letter, this.number, this.piece, this.pieceColor);

  @override
  String toString() {
    return '$piece$pieceColor-$letter$number';
  }
}

class Board {
  final size = 8;
  double widthHeight = 441;
  final Map<int, BoardSquare> indexSquare = {};
  Reset? reset;

  Board() {
    for (var j = 0; j < size; j++) {
      for (var i = 0; i < size; i++) {
        final letter = BoardLetter.values.elementAt(i);
        final number = BoardNumber.values.reversed.elementAt(j);
        final key = (j * size) + i;
        final value = _initSquare(letter, number);
        indexSquare[key] = value;
      }
    }
  }

  BoardSquare _initSquare(BoardLetter letter, BoardNumber number) {
    if (letter == BoardLetter.e && number == BoardNumber.n3) {
      return BoardSquare(letter, number, Piece.king, PieceColor.white);
    }

    return BoardSquare(letter, number, Piece.none, PieceColor.none);
  }
}

class Reset {
  BoardB? boardB;
  bool whiteToMove;
  bool whiteKingInCheck;
  bool blackKingInCheck;
  bool whiteKingCheckmated;
  bool blackKingCheckmated;
  Map<int, List<int>> legalMoves = {}; // indexLegalFrom + indicesLegalTo

  Reset(
      {required this.boardB,
      required this.whiteToMove,
      required this.whiteKingInCheck,
      required this.blackKingInCheck,
      required this.whiteKingCheckmated,
      required this.blackKingCheckmated,
      required this.legalMoves});

  factory Reset.fromJson(Map<String, dynamic> json) {
    Map<int, List<int>> resetLegalMoves = {};
    Map<String, dynamic> jsonLegalMoves =
        json['enrichedLegalMoves']['legalMoves'];
    jsonLegalMoves.forEach((key, value) {
      //print('jsonLegalMoves key: $key, value: $value'); // jsonLegalMoves key: 44, value: [43, 51, 45, 53, 52]
      var intKey = int.parse(key);
      var intValues = List<int>.from(value as List);
      resetLegalMoves.putIfAbsent(intKey, () => intValues);
    });
    return Reset(
        boardB: BoardB.fromJson(json),
        whiteToMove: json['whiteToMove'],
        whiteKingInCheck: json['whiteKingInCheck'],
        blackKingInCheck: json['blackKingInCheck'],
        whiteKingCheckmated: json['whiteKingCheckmated'],
        blackKingCheckmated: json['blackKingCheckmated'],
        legalMoves: resetLegalMoves);
  }

  @override
  String toString() {
    return 'boardB: $boardB, whiteToMove: $whiteToMove';
  }

  void cleanup() {
    boardB = null;
  }
}

class BoardB {
  List<BoardBItem> board;

  BoardB({required this.board});

  factory BoardB.fromJson(Map<String, dynamic> json) {
    List<dynamic> boardList = json['board']['board'];
    List<BoardBItem> boardItems =
        boardList.map((item) => BoardBItem.fromJson(item)).toList();
    return BoardB(board: boardItems);
  }

  @override
  String toString() {
    return '$board';
  }
}

class BoardBItem {
  String letter;
  String number;
  String piece;
  String pieceColor;
  int index;

  BoardBItem({
    required this.letter,
    required this.number,
    required this.piece,
    required this.pieceColor,
    required this.index,
  });

  factory BoardBItem.fromJson(Map<String, dynamic> json) {
    return BoardBItem(
      letter: json['letter'],
      number: json['number'],
      piece: json['piece'],
      pieceColor: json['pieceColor'],
      index: json['index'],
    );
  }

  @override
  String toString() {
    return '$index';
  }
}
