# Keyboard Height Emitter Plugin for Flutter

Credit to the keyboard_height_plugin author at https://github.com/nschairer/keyboard_height_plugin/tree/main

This fork fixes the issue where you can only use it on a single route at a time, pushing a new route with the plugin would silently drop the listener on the route under it.

`keyboard_height_emitter` is a Flutter plugin for iOS and Android that provides the keyboard size before the keyboard animation occurs for showing or hiding it. This helps eliminate lag when positioning widgets around the keyboard, such as placing a TextField above the keyboard.

## Installation

To install `keyboard_height_emitter`, add it to your `pubspec.yaml` file under the `dependencies` section:

```yaml
dependencies:
  keyboard_height_emitter: ^0.0.1
```

## Usage

To use the `keyboard_height_emitter`, first import it in your Dart file:

```dart
import 'package:keyboard_height_emitter/keyboard_height_emitter.dart';
```

Next, create a stateful widget and declare a variable to store the keyboard height and create an instance of the `KeyboardHeightEmitter`:

```dart
class _HomePageState extends State<HomePage>; {
  double _keyboardHeight = 0;
  final KeyboardHeightEmitter _keyboardHeightEmitter = KeyboardHeightEmitter();
  // ... rest of code ...
}
```

Then, initialize the `KeyboardHeightEmitter` in your `initState` method and listen for changes in the keyboard height:

```dart
@override
void initState() {
  super.initState();
  _keyboardHeightEmitter.onKeyboardHeightChanged((double height) {
    setState(() {
      _keyboardHeight = height;
    });
  });
}
```

Use the `_keyboardHeight` variable to position your widgets around the keyboard. 

## Example

For a complete example on how to use the `keyboard_height_emitter`, please refer to the [`example`](example) directory in the repository.

## Contributing

If you encounter any issues or have suggestions for improvements, feel free to open an issue or submit a pull request on the project's GitHub repository.

## License

This plugin is licensed under the [BSD 3-Clause License](LICENSE).
