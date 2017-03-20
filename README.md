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

Download the [latest release jar](https://github.com/oberien/adb-remote-control/releases/download/v0.1.0/adb-remote-control.jar).

From the command line run `java -jar adb-remote-control.jar`

# Config

If needed you can create a config file called `config.properties`.
Currently the following keys are supported:

* `adbPath`: The path to the `adb` binary.
* `timeout`: The time between having received a screenshot and taking the next one.

## License

Licensed under either of

 * Apache License, Version 2.0, ([LICENSE-APACHE](LICENSE-APACHE) or http://www.apache.org/licenses/LICENSE-2.0)
 * MIT license ([LICENSE-MIT](LICENSE-MIT) or http://opensource.org/licenses/MIT)

at your option.
