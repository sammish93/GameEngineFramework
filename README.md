Text-Based Adventure Game Engine
============

A Java framework intended to facilitate the development of text-based adventure games. The game can be run via a terminal or a Swing GUI.

Getting Started
---------------

### Prerequisites

JDK 17 or newer.

### Installing

This repository currently has no .jar file compiled, nor is it currently published on Maven Central.

If you wish to create a .jar file and have both git and maven installed then:
1. Nagivate to your desired directory
2. Open up a command window and type - `git clone http://github.com/sammish93/GameEngineFramework.git`
3. Then change to the cloned directory - `cd GameEngineFramework`
4. Compile the repository into a .jar - `mvn package`

Assuming you have the .jar file then simply immport as an external dependency.

In IntelliJ you can do this by:
1. Open up your desired project
2. Click on `File` -> `Project Structure` -> `Modules`
3. In the opened window, click on `Modules`, then `Add` or the `+` symbol -> `JAR or Directories` and select the .jar

### Usage

How to start your first game:
1. Navigate to a main() method in your desired java project and import the project using `import no.hiof.samuelcd.tbage.GameEngine;`
2. Create a GameEngine variable using `var game = GameEngine.create();`
3. Run the game by using `game.run();`

