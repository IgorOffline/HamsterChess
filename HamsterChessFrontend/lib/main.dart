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

  Future<Album> fetchAlbum() async {
    final response = await http
        .get(Uri.parse('https://jsonplaceholder.typicode.com/albums/1'));

    if (response.statusCode == 200) {
      // If the server did return a 200 OK response,
      // then parse the JSON.
      return Album.fromJson(jsonDecode(response.body));
    } else {
      // If the server did not return a 200 OK response,
      // then throw an exception.
      throw Exception('Failed to load album');
    }
  }
}

class Album {
  final int userId;
  final int id;
  final String title;

  const Album({
    required this.userId,
    required this.id,
    required this.title,
  });

  factory Album.fromJson(Map<String, dynamic> json) {
    return Album(
      userId: json['userId'],
      id: json['id'],
      title: json['title'],
    );
  }

  @override
  String toString() {
    return '$userId,$id-$title';
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
            FloatingActionButton(
                onPressed: () => _reset(board),
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
              child: _gridTile(index, board)));
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

  Widget _gridTile(int index, Board board) {
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

  void _plus(Board board) {
    setState(() {
      board.widthHeight += 11;
    });
    print('_plus ${board.widthHeight}');
  }

  void _minus(Board board) {
    setState(() {
      board.widthHeight -= 11;
    });
    print('_minus ${board.widthHeight}');
  }

  void _info(Board board) {
    // board.fetchAlbum().then((album) => print('album: $album')).onError((error, stackTrace) => print('album error: $error'));

    print(
        '_info: indexBoardSquare (${board.indexSquare.length}): ${board.indexSquare}');
  }

  void _reset(Board board) {
    const enrichedBoardJson =
        '{"enrichedBoard":{"0":{"letter":"A","number":"N8","piece":"NONE","pieceColor":"NONE"},"1":{"letter":"B","number":"N8","piece":"NONE","pieceColor":"NONE"},"2":{"letter":"C","number":"N8","piece":"NONE","pieceColor":"NONE"},"3":{"letter":"D","number":"N8","piece":"NONE","pieceColor":"NONE"},"4":{"letter":"E","number":"N8","piece":"NONE","pieceColor":"NONE"},"5":{"letter":"F","number":"N8","piece":"NONE","pieceColor":"NONE"},"6":{"letter":"G","number":"N8","piece":"NONE","pieceColor":"NONE"},"7":{"letter":"H","number":"N8","piece":"NONE","pieceColor":"NONE"},"8":{"letter":"A","number":"N7","piece":"NONE","pieceColor":"NONE"},"9":{"letter":"B","number":"N7","piece":"NONE","pieceColor":"NONE"},"10":{"letter":"C","number":"N7","piece":"NONE","pieceColor":"NONE"},"11":{"letter":"D","number":"N7","piece":"NONE","pieceColor":"NONE"},"12":{"letter":"E","number":"N7","piece":"NONE","pieceColor":"NONE"},"13":{"letter":"F","number":"N7","piece":"NONE","pieceColor":"NONE"},"14":{"letter":"G","number":"N7","piece":"NONE","pieceColor":"NONE"},"15":{"letter":"H","number":"N7","piece":"NONE","pieceColor":"NONE"},"16":{"letter":"A","number":"N6","piece":"NONE","pieceColor":"NONE"},"17":{"letter":"B","number":"N6","piece":"NONE","pieceColor":"NONE"},"18":{"letter":"C","number":"N6","piece":"NONE","pieceColor":"NONE"},"19":{"letter":"D","number":"N6","piece":"NONE","pieceColor":"NONE"},"20":{"letter":"E","number":"N6","piece":"NONE","pieceColor":"NONE"},"21":{"letter":"F","number":"N6","piece":"NONE","pieceColor":"NONE"},"22":{"letter":"G","number":"N6","piece":"NONE","pieceColor":"NONE"},"23":{"letter":"H","number":"N6","piece":"NONE","pieceColor":"NONE"},"24":{"letter":"A","number":"N5","piece":"NONE","pieceColor":"NONE"},"25":{"letter":"B","number":"N5","piece":"NONE","pieceColor":"NONE"},"26":{"letter":"C","number":"N5","piece":"NONE","pieceColor":"NONE"},"27":{"letter":"D","number":"N5","piece":"NONE","pieceColor":"NONE"},"28":{"letter":"E","number":"N5","piece":"NONE","pieceColor":"NONE"},"29":{"letter":"F","number":"N5","piece":"NONE","pieceColor":"NONE"},"30":{"letter":"G","number":"N5","piece":"NONE","pieceColor":"NONE"},"31":{"letter":"H","number":"N5","piece":"NONE","pieceColor":"NONE"},"32":{"letter":"A","number":"N4","piece":"NONE","pieceColor":"NONE"},"33":{"letter":"B","number":"N4","piece":"NONE","pieceColor":"NONE"},"34":{"letter":"C","number":"N4","piece":"NONE","pieceColor":"NONE"},"35":{"letter":"D","number":"N4","piece":"NONE","pieceColor":"NONE"},"36":{"letter":"E","number":"N4","piece":"KING","pieceColor":"WHITE"},"37":{"letter":"F","number":"N4","piece":"NONE","pieceColor":"NONE"},"38":{"letter":"G","number":"N4","piece":"NONE","pieceColor":"NONE"},"39":{"letter":"H","number":"N4","piece":"NONE","pieceColor":"NONE"},"40":{"letter":"A","number":"N3","piece":"NONE","pieceColor":"NONE"},"41":{"letter":"B","number":"N3","piece":"NONE","pieceColor":"NONE"},"42":{"letter":"C","number":"N3","piece":"NONE","pieceColor":"NONE"},"43":{"letter":"D","number":"N3","piece":"NONE","pieceColor":"NONE"},"44":{"letter":"E","number":"N3","piece":"NONE","pieceColor":"NONE"},"45":{"letter":"F","number":"N3","piece":"NONE","pieceColor":"NONE"},"46":{"letter":"G","number":"N3","piece":"NONE","pieceColor":"NONE"},"47":{"letter":"H","number":"N3","piece":"NONE","pieceColor":"NONE"},"48":{"letter":"A","number":"N2","piece":"NONE","pieceColor":"NONE"},"49":{"letter":"B","number":"N2","piece":"NONE","pieceColor":"NONE"},"50":{"letter":"C","number":"N2","piece":"NONE","pieceColor":"NONE"},"51":{"letter":"D","number":"N2","piece":"NONE","pieceColor":"NONE"},"52":{"letter":"E","number":"N2","piece":"NONE","pieceColor":"NONE"},"53":{"letter":"F","number":"N2","piece":"NONE","pieceColor":"NONE"},"54":{"letter":"G","number":"N2","piece":"NONE","pieceColor":"NONE"},"55":{"letter":"H","number":"N2","piece":"NONE","pieceColor":"NONE"},"56":{"letter":"A","number":"N1","piece":"NONE","pieceColor":"NONE"},"57":{"letter":"B","number":"N1","piece":"NONE","pieceColor":"NONE"},"58":{"letter":"C","number":"N1","piece":"NONE","pieceColor":"NONE"},"59":{"letter":"D","number":"N1","piece":"NONE","pieceColor":"NONE"},"60":{"letter":"E","number":"N1","piece":"NONE","pieceColor":"NONE"},"61":{"letter":"F","number":"N1","piece":"NONE","pieceColor":"NONE"},"62":{"letter":"G","number":"N1","piece":"NONE","pieceColor":"NONE"},"63":{"letter":"H","number":"N1","piece":"NONE","pieceColor":"NONE"}}}';

    final Map<String, dynamic> decode = jsonDecode(enrichedBoardJson);
    final enrichedBoardMap = decode['enrichedBoard'] as Map<String, dynamic>;
    setState(() {
      enrichedBoardMap.forEach((keyIndex, valueSquare) {
        final square = board.indexSquare[int.parse(keyIndex)]!;
        // short test version
        if (valueSquare['piece'] == 'NONE') {
          square.piece = Piece.none;
          square.pieceColor = PieceColor.none;
        } else {
          square.piece = Piece.king;
          square.pieceColor = PieceColor.white;
        }
      });
    });

    print('_reset');
  }
}
