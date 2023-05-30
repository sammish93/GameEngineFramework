Text-Based Adventure Game Engine
============

A Java framework intended to facilitate the development of text-based adventure games. The game can be run via a terminal or a Swing GUI.

# Getting Started

### Prerequisites

JDK 17 or newer.

### Installing

This repository contains a .jar file, though it currently isn't published on Maven Central.

If you wish to create an additional .jar file based off any changes you have made, and have both git and maven installed then:
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
1. Navigate to a main() method in your desired java project and import the project using `import GameEngine;`
2. Create a GameEngine variable using `var game = GameEngine.create();`
3. Run the game by using `game.run();`

Note that you will receive a message in the terminal informing you that no encounters are present. This is because the framework doesn't provide any encounters, only the means for other developers to create them. Listed below is a link to code to an an example of a small game created using TBAGE.

# Additional Information

## Javadoc
Javadoc documentation can be found [here](https://htmlpreview.github.io/?https://github.com/sammish93/GameEngineFramework/blob/main/javadoc/index.html).

## Example Code
An example of the code required (together with descriptive comments) to create a simple game featuring many of the framework's functionality can be found [here](/example/Main.java).

## Frequently Asked Questions

Why did you choose to write this framework using Java?
- This framework is a project for my Computer Science degree, and Java is the language I’m most comfortable with at the time of writing. Given I have concrete deadlines to meet, I feel that getting comfortable with a new language would be an inefficient use of my time considering the focus of this project is on planning, rather than implementing the code.

Why have you chosen to use Java Swing as a GUI instead of, say, JavaFX?
- Many consider Java Swing to be easier to learn than JavaFX, thus lowering the barrier to entry for those wishing to extend the GUI. Text-based adventures are much less resource intensive than many other games, especially those with 3D model rendering, so optimisation and performance weren’t much of a concern. Since the game engine can also can build games that can be played exclusively through a terminal, the capability to extend the framework to allow for other GUIs is entirely possible.

Why should I choose your framework instead of an established game engine such as Unity or Unreal?
- These engines can be intimidating for some, but great for others. This framework’s scope is much more focused and is easier to use. It also provides a straightforward template with much of the work already done. It’s great for those who wish to create their own story elements and see them come to life without the tedium of game systems such as inventory management, weighted probability algorithms, or game rendering.

Could I use your framework to create a genre of game that isn’t a text-based adventure?
- You could, but it might take a bit of extra work. The framework is extensible and many systems are in place. A dungeon-crawler or point-and-click game would definitely be feasible, but more physics-based games such racers and platformers would take a lot of work, and other game engines would be more practical.

Do you plan to roll out more functionality and features in future versions of the framework?
- I have several ideas of what I want to include in future releases. A few of these are:
  1. A GUI complete with forms and buttons that a non-programmer can use to create rudimentary encounters following a set template.
  2. Functionality for an ‘overworld’ style map containing node locations, each of which is a set of encounters from another game’s permutation.
  3. Persistent saving of a player character’s progression, statistics, etc.
  4. Functionality for animated artwork. Examples of use include for non-player character sprites, spell effects, and landscape scenes.
  
### Credits

Images used by the GUI are made by [Upklyak](https://www.freepik.com/author/upklyak) from [Freepik](https://www.freepik.com).
