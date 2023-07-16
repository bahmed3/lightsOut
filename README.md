# lightsOut

## Objective 
The objective of this project was to build an interactive Lights Out game with an automated solve feature using Java. The game includes an intuitive GUI where users can interact with the game's grid, and buttons for actions such as "All On", "All Off", "Random", "Step", and "Undo".

## Technologies 
The key technology used in this project is Java, with a strong emphasis on its Swing library for creating the game's Graphical User Interface (GUI). The codebase also leverages Java's core features, such as event handling, inheritance, anonymous classes, and more.

## Strategy
Here are the key strategies employed in this project:

### GUI Creation
Java's Swing library was used to create a graphical user interface for the game. The interface includes a grid representing the game's board, and several buttons for user interaction. Custom UI was built using the BasicButtonUI class and overriding its installUI and paint methods. The JPanel class was used to create panels to contain the buttons and game's grid, and these were styled and organized using various layout managers and methods.

### Event Handling
Java's event handling model was used to create responsive buttons and a grid that responds to mouse clicks. For each button, an ActionListener was attached that triggers a corresponding action. The grid itself responds to mouse clicks by flipping the state of the corresponding light.

### Game Logic
The game's logic is encapsulated within the LightsOut and GridLoc classes. LightsOut contains the methods that define the game's behavior, such as switching a light's state and checking if a light is on or off. GridLoc represents a specific location in the game's grid and provides methods to retrieve and compare grid locations.

### Data Structures and Object-Oriented Programming
The project uses advanced data structures like BitSet for efficient representation and manipulation of the game state, and Queue for implementing the breadth-first search. It showcases object-oriented principles like encapsulation and immutability (in GridLoc class).

### Breadth-First Search
The breadth-first search algorithm is used to implement the automated solve feature. This algorithm traverses the game's state tree breadth-first to find a sequence of moves that results in a solved game state. The BreadthFirstLightsOut class contains the implementation of this algorithm.

## Lessons Learned
This project was a valuable experience in creating a GUI with Java Swing, encapsulating game logic in dedicated classes, using event handling for interactivity, applying object-oriented principles, and employing advanced data structures. The use of breadth-first search to provide an automated solution was particularly educational, demonstrating how algorithms can be used in practical applications.

## How to View
To view and interact with the game, you'll need to have Java installed on your system. Once Java is installed, you can run the main method in the Main class. This will launch the GUI of the game, allowing you to play and interact with the features of the Lights Out game.
