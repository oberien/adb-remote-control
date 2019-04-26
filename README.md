# ADB Remote Control

ADB Remote Control allows you to control your adb-connected device from your PC.
It enables clicks and drags with the mouse and normal keyboard inputs.

To show the image of the phone's screen, a screenshot is taken regularly.
The rate can be configured (see [Config](#config)).

This project was inspired by [adbcontrol](http://marian.schedenig.name/2014/07/03/remote-control-your-android-phone-through-adb/)
and [Adb-Remote-Screen](https://github.com/MajeurAndroid/Adb-Remote-Screen), none of which were easy enough for me to
adapt to my needs.
It was completely written from scratch.

# How to Run

Download the [latest release jar](https://github.com/oberien/adb-remote-control/releases/download/v0.1.2/adb-remote-control.jar).

From the command line run `java -jar adb-remote-control.jar`

# Compiling

Go into the `src/` directory and execute `javac de/oberien/adbremotecontrol/Main.java`.
To build the `.jar` file, after compiling, execute
`jar -cvfe adb-remote-control.jar de.oberien.adbremotecontrol.Main **/*.class`.
Depending on your shell, you might need to set the correct glob options.

# Functionality

* All ASCII characters from (including) 32 to (excluding) 127 are sent as text.
  These are all printable ascii characters including lowercase, uppercase,
  numbers and normal special chars.
* Return (Enter) is sent.
* Pressing `Esc` emulates a `Back` key event.
* Pressing `Home` will be forwarded.
* `Backspace` will delete the last character.
* `Ctrl+d` emulates 500 `Backspace` presses and can be used to quickly erase
  a large text.
* `Up`, `Down`, `Left` and `Right` are working as expected.

# Config

If needed you can create a config file called `config.properties`.
Currently the following keys are supported:

* `adbPath`: The path to the `adb` binary.
* `timeout`: The time between having received a screenshot and taking the next one.

# Building

To manually build and run this project, go into the `src` directory and execute

```sh
javac de/oberien/adbremotecontrol/Main.java
java de.oberien.adbremotecontrol.Main 
```

# Troubleshooting

## Signal

In Signal screenshots are disabled by default.
This means that while having Signal open, the displayed image of the phone
won't change.
Follow [this tutorial](support.whispersystems.org/hc/en-us/articles/213191027-Can-I-take-a-screenshot-)
to enable screenshots and therefore make ADB Remote Control work with Signal.

# License

Licensed under either of

 * Apache License, Version 2.0, ([LICENSE-APACHE](LICENSE-APACHE) or http://www.apache.org/licenses/LICENSE-2.0)
 * MIT license ([LICENSE-MIT](LICENSE-MIT) or http://opensource.org/licenses/MIT)

at your option.
