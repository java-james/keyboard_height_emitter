import 'dart:async';
import 'package:flutter/services.dart';

typedef KeyboardHeightCallback = void Function(double height);

class KeyboardHeightEmitter {
  static const EventChannel _keyboardHeightEventChannel = EventChannel(
    'keyboardHeightEventChannel',
  );

  static final Stream<double> _keyboardHeightStream =
      _keyboardHeightEventChannel
          .receiveBroadcastStream()
          .map((event) => event as double)
          .asBroadcastStream();

  StreamSubscription<double>? _subscription;

  void onKeyboardHeightChanged(KeyboardHeightCallback callback) {
    _subscription = _keyboardHeightStream.listen(callback);
  }

  void dispose() {
    _subscription?.cancel();
  }
}
