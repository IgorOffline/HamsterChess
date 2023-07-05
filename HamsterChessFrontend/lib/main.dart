import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:chess_vectors_flutter/chess_vectors_flutter.dart';
import 'package:http/http.dart' as http;

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  static const String _title = 'Flutter';

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: _title,
      home: Scaffold(
        appBar: AppBar(title: const Text(_title)),
        body: const MyStatefulWidget(),
      ),
    );
  }
}

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
  rook("R");

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
  Map<int, List<int>> legalMoves = {}; // indexLegalFrom + indicesLegalTo

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

  Widget getWidgetForSquare(BoardSquare square) {
    var pieceSize = (widthHeight / size) * 0.62;
    if (square.pieceColor == PieceColor.white) {
      if (square.piece == Piece.king) {
        return WhiteKing(size: pieceSize);
      }
    } else if (square.pieceColor == PieceColor.black) {
      if (square.piece == Piece.king) {
        return BlackKing(size: pieceSize);
      }
    }

    throw UnimplementedError('Square piece unknown');
  }

  bool canMove(int fromIndex, int toIndex) {
    return (toIndex == fromIndex + 1) || (toIndex == fromIndex - 1);
  }

  void move(int fromIndex, int toIndex) {
    var fromSquare = indexSquare[fromIndex]!;
    var toSquare = indexSquare[toIndex]!;
    toSquare.piece = fromSquare.piece;
    toSquare.pieceColor = fromSquare.pieceColor;
    fromSquare.piece = Piece.none;
    fromSquare.pieceColor = PieceColor.none;
  }

  Future<BoardB> fetchReset() async {
    final response = await http.get(Uri.parse('http://localhost:8080/reset'));

    if (response.statusCode == 200) {
      return BoardB.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('fetchReset failed');
    }
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

class MyStatefulWidget extends StatefulWidget {
  const MyStatefulWidget({super.key});

  @override
  State<MyStatefulWidget> createState() => _MyStatefulWidgetState();
}

class _MyStatefulWidgetState extends State<MyStatefulWidget> {
  final board = Board();

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
        child: Column(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: <Widget>[
          Container(
            width: board.widthHeight,
            height: board.widthHeight,
            margin:
                const EdgeInsets.only(left: 10, top: 10, right: 0, bottom: 0),
            decoration: BoxDecoration(
                border: Border.all(color: Colors.black, width: 2.0)),
            child: GridView.builder(
              gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: board.size,
              ),
              itemBuilder: (context, index) =>
                  _itemBuilder(context, index, board),
              itemCount: board.size * board.size,
              physics: const NeverScrollableScrollPhysics(),
            ),
          ),
          Row(children: <Widget>[
            FloatingActionButton(
                onPressed: () => _plus(),
                tooltip: '+',
                child: const Icon(Icons.add)),
            FloatingActionButton(
                onPressed: () => _minus(),
                tooltip: '-',
                child: const Icon(Icons.remove)),
            FloatingActionButton(
                onPressed: () => _info(),
                tooltip: 'info',
                child: const Text('info')),
            FloatingActionButton(
                onPressed: () => _reset(),
                tooltip: 'reset',
                child: const Text('reset')),
          ])
        ]));
  }

  Widget _itemBuilder(BuildContext context, int index, Board board) {
    return GridTile(
        child: DragTarget<int>(builder: (
      BuildContext context,
      List<dynamic> accepted,
      List<dynamic> rejected,
    ) {
      return Container(
          decoration: BoxDecoration(
              color: _color(index, board),
              border: Border.all(color: Colors.black, width: 0.5)),
          child: Draggable<int>(
              data: index,
              feedback: Container(
                color: Colors.transparent,
                height: 100,
                width: 100,
                child: Icon(Icons.directions_run),
              ),
              childWhenDragging: Container(
                  height: 100.0,
                  width: 100.0,
                  color: Colors.transparent,
                  child: Icon(Icons.directions_run)),
              child: _gridTile(index)));
    }, onWillAccept: (int? data) {
      if (data != null) {
        print('onWillAccept dataFrom: $data, indexTo: $index');
        return board.canMove(data, index);
      }

      return false;
    }, onAccept: (int data) {
      print('onAccept dataFrom: $data, indexTo: $index');
      setState(() {
        board.move(data, index);
      });
    }));
  }

  Color _color(int index, Board board) {
    var squareColorFlag = false;
    for (var i = 0; i < board.size * board.size; i++) {
      if (i % board.size == 0) {
        squareColorFlag = !squareColorFlag;
      }
      if (i == index) {
        return squareColorFlag ? Color(0xFFF0D9B5) : Color(0xFFB58863);
      }
      squareColorFlag = !squareColorFlag;
    }

    throw IndexError(index, board.size * board.size);
  }

  Widget _gridTile(int index) {
    final square = board.indexSquare[index]!;
    if (square.piece == Piece.none) {
      return Column(children: <Widget>[
        Text('${square.letter}${square.number}', textScaleFactor: 0.75),
      ]);
    } else {
      return Column(children: <Widget>[
        Text('${square.letter}${square.number}', textScaleFactor: 0.75),
        board.getWidgetForSquare(square),
      ]);
    }
  }

  void _plus() {
    setState(() {
      board.widthHeight += 11;
    });
    print('_plus ${board.widthHeight}');
  }

  void _minus() {
    setState(() {
      board.widthHeight -= 11;
    });
    print('_minus ${board.widthHeight}');
  }

  void _info() {
    print(
        '_info: indexBoardSquare (${board.indexSquare.length}): ${board.indexSquare}');
  }

  void _reset() {
    board
        .fetchReset()
        .then((boardB) => _resetInner(boardB))
        .onError((error, stackTrace) => print('reset error: $error'));
    print('_reset');
  }

  void _resetInner(BoardB boardB) {
    setState(() {
      for (var boardBItem in boardB.board) {
        final square = board.indexSquare[boardBItem.index]!;
        if (boardBItem.piece == 'NONE') {
          square.piece = Piece.none;
          square.pieceColor = PieceColor.none;
        } else if (boardBItem.piece == 'KING') {
          square.piece = Piece.king;
          _setPieceColor(square, boardBItem);
        } else if (boardBItem.piece == 'ROOK') {
          square.piece = Piece.rook;
          _setPieceColor(square, boardBItem);
        }
      }
    });
  }

  void _setPieceColor(BoardSquare square, BoardBItem boardBItem) {
    if (boardBItem.pieceColor == 'WHITE') {
      square.pieceColor = PieceColor.white;
    } else {
      square.pieceColor = PieceColor.black;
    }
  }
}
