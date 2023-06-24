import 'dart:developer';
import 'dart:math' as math;

import 'package:flutter/material.dart';
import 'package:chess_vectors_flutter/chess_vectors_flutter.dart';

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

class BoardMovement {
  BoardSquare from;
  BoardSquare to;

  BoardMovement(this.from, this.to);

  @override
  String toString() {
    return '$from, $to';
  }
}

class Board {
  int size = 8;
  double widthHeight = 441;
  List<BoardSquare> squares = [];
  GlobalKey key1 = GlobalKey();

  Board() {
    for (var j = 0; j < size; j++) {
      for (var i = 0; i < size; i++) {
        final letter = BoardLetter.values.elementAt(i);
        final number = BoardNumber.values.reversed.elementAt(j);
        if (letter == BoardLetter.e && number == BoardNumber.n3) {
          squares
              .add(BoardSquare(letter, number, Piece.king, PieceColor.white));
        } else {
          squares.add(BoardSquare(letter, number, Piece.none, PieceColor.none));
        }
      }
    }
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

  String calculateDragAndDropTargetSquare() {
    final RenderBox renderBox =
        key1.currentContext?.findRenderObject() as RenderBox;
    final Offset renderBoxOffset = renderBox.localToGlobal(Offset.zero);
    final Size renderBoxSize = renderBox.size;

    final double squareSize = renderBoxSize.width / size;
    final double squareSizeHalf = squareSize / 2;

    print('AppBar().preferredSize.height: ${AppBar().preferredSize.height}');

    return '';
  }
}

class MyStatefulWidget extends StatefulWidget {
  const MyStatefulWidget({super.key});

  @override
  State<MyStatefulWidget> createState() => _MyStatefulWidgetState();
}

class _MyStatefulWidgetState extends State<MyStatefulWidget> {
  Board board = Board();

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
        child: Column(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: <Widget>[
          Container(
            key: board.key1,
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
                onPressed: () => _plus(board),
                tooltip: '+',
                child: const Icon(Icons.add)),
            FloatingActionButton(
                onPressed: () => _minus(board),
                tooltip: '-',
                child: const Icon(Icons.remove)),
            FloatingActionButton(
                onPressed: () => _info(board),
                tooltip: 'info',
                child: const Text('info')),
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
                  child: Icon(Icons.account_balance)),
              child: _gridTile(index, board)));
    }, onWillAccept: (int? data) {
      if (data != null) {
        //log('onWillAccept: $data');
      }
      return true;
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

  Widget _gridTile(int index, Board board) {
    final square = board.squares.elementAt(index);
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

  void _plus(Board board) {
    setState(() {
      board.widthHeight += 11;
    });
    debugPrint('_plus ${board.widthHeight}');
  }

  void _minus(Board board) {
    setState(() {
      board.widthHeight -= 11;
    });
    debugPrint('_minus ${board.widthHeight}');
  }

  void _info(Board board) {
    debugPrint('_info ${board.key1}');

    final RenderBox renderBox =
        board.key1.currentContext?.findRenderObject() as RenderBox;

    final Size size = renderBox.size; // or _widgetKey.currentContext?.size
    print('Size: ${size.width}, ${size.height}');

    final Offset offset = renderBox.localToGlobal(Offset.zero);
    print('Offset: ${offset.dx}, ${offset.dy}');
    print(
        'Position: ${(offset.dx + size.width) / 2}, ${(offset.dy + size.height) / 2}');

    board.calculateDragAndDropTargetSquare();
  }
}
