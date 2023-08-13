import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:chess_vectors_flutter/chess_vectors_flutter.dart';
import 'package:http/http.dart' as http;

import 'chess.dart';

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
            _gameStateVisualizer(),
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
        return _canMove(data, index);
      }

      return false;
    }, onAccept: (int data) {
      print('onAccept dataFrom: $data, indexTo: $index');
      _move(data, index);
    }));
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
        _getWidgetForSquare(square),
      ]);
    }
  }

  Widget _getWidgetForSquare(BoardSquare square) {
    var pieceSize = (board.widthHeight / board.size) * 0.62;
    if (square.pieceColor == PieceColor.white) {
      if (square.piece == Piece.king) {
        return WhiteKing(size: pieceSize);
      } else if (square.piece == Piece.rook) {
        return WhiteRook(size: pieceSize);
      } else if (square.piece == Piece.bishop) {
        return WhiteBishop(size: pieceSize);
      } else if (square.piece == Piece.knight) {
        return WhiteKnight(size: pieceSize);
      } else if (square.piece == Piece.pawn) {
        return WhitePawn(size: pieceSize);
      }
    } else if (square.pieceColor == PieceColor.black) {
      if (square.piece == Piece.king) {
        return BlackKing(size: pieceSize);
      } else if (square.piece == Piece.rook) {
        return BlackRook(size: pieceSize);
      } else if (square.piece == Piece.bishop) {
        return BlackBishop(size: pieceSize);
      } else if (square.piece == Piece.knight) {
        return BlackKnight(size: pieceSize);
      } else if (square.piece == Piece.pawn) {
        return BlackPawn(size: pieceSize);
      }
    }

    throw UnimplementedError('Square piece unknown');
  }

  bool _canMove(int fromIndex, int toIndex) {
    if (board.reset == null) {
      return (toIndex == fromIndex + 1) || (toIndex == fromIndex - 1);
    }

    for (var entry in board.reset!.legalMoves.entries) {
      for (var value in entry.value) {
        if (fromIndex == entry.key && toIndex == value) {
          return true;
        }
      }
    }

    return false;
  }

  void _move(int from, int to) {
    _fetchMove(from, to)
        .then((reset) => _resetInner(reset, false))
        .onError((error, stackTrace) => print('reset error: $error'));
    setState(() {
      var fromSquare = board.indexSquare[from]!;
      var toSquare = board.indexSquare[to]!;
      toSquare.piece = fromSquare.piece;
      toSquare.pieceColor = fromSquare.pieceColor;
      fromSquare.piece = Piece.none;
      fromSquare.pieceColor = PieceColor.none;
    });
  }

  void _resetInner(Reset reset, bool fullReset) {
    setState(() {
      board.reset = reset;
      if (fullReset) {
        for (var boardBItem in reset.boardB!.board) {
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
          } else if (boardBItem.piece == 'BISHOP') {
            square.piece = Piece.bishop;
            _setPieceColor(square, boardBItem);
          } else if (boardBItem.piece == 'KNIGHT') {
            square.piece = Piece.knight;
            _setPieceColor(square, boardBItem);
          } else if (boardBItem.piece == 'PAWN') {
            square.piece = Piece.pawn;
            _setPieceColor(square, boardBItem);
          }
        }
      }
      reset.cleanup();
    });
  }

  void _setPieceColor(BoardSquare square, BoardBItem boardBItem) {
    if (boardBItem.pieceColor == 'WHITE') {
      square.pieceColor = PieceColor.white;
    } else {
      square.pieceColor = PieceColor.black;
    }
  }

  Future<Reset> _fetchReset() async {
    final response = await http.get(Uri.parse('http://localhost:8080/reset'));

    if (response.statusCode == 200) {
      return Reset.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('fetchReset failed');
    }
  }

  Future<Reset> _fetchMove(int from, int to) async {
    final response = await http.post(Uri.parse('http://localhost:8080/move'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(<String, int>{'from': from, 'to': to}));
    if (response.statusCode == 200) {
      return Reset.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('fetchReset failed');
    }
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

  Widget _gameStateVisualizer() {
    if (board.reset != null) {
      if (board.reset!.whiteKingCheckmated) {
        return Row(children: [WhiteKing(), WhiteKing(), WhiteKing()]);
      } else if (board.reset!.blackKingCheckmated) {
        return Row(children: [BlackKing(), BlackKing(), BlackKing()]);
      } else if (board.reset!.whiteKingInCheck) {
        return Row(children: [WhiteKing(), WhiteKing()]);
      } else if (board.reset!.blackKingInCheck) {
        return Row(children: [BlackKing(), BlackKing()]);
      } else {
        return board.reset!.whiteToMove ? WhiteKing() : BlackKing();
      }
    }

    return WhitePawn();
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
        '_info: indexSquare (${board.indexSquare.length}): ${board.indexSquare}, reset: ${board.reset}');
  }

  void _reset() {
    _fetchReset()
        .then((reset) => _resetInner(reset, true))
        .onError((error, stackTrace) => print('reset error: $error'));
    print('_reset');
  }
}
