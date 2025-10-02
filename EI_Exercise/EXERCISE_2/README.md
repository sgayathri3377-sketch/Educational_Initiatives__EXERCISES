#  EXERCISE 2: Console-Based Rocket Launch Simulator

This directory contains **Exercise 2**, which is a comprehensive **Console-Based Rocket Launch Simulator** application. The simulator is a practical case study in building maintainable, robust, and optimized software by heavily relying on advanced **Design Patterns**.

## Focus Areas
* **Maintainable Architecture:** Using design patterns to achieve high cohesion and low coupling.
* **State Management:** Implementing robust state transitions for the rocket launch sequence.
* **Command Execution:** Decoupling the invoker (Mission Control) from the receiver (Rocket Systems).

## Implemented Design Patterns

This simulator utilizes the following four core design patterns to structure its logic:

| Pattern | Role in the Simulator | Description |
| :--- | :--- | :--- |
| **Singleton** | `MissionControlCenter` | Ensures that only one instance of the central control system exists throughout the simulation. |
| **Command** | `StartChecksCommand`, `LaunchCommand` | Encapsulates various requests (like pre-launch checks and the final launch) as objects, enabling parameterized calls and logging. |
| **State** | `PreLaunchState`, `AscentStage1`, `AscentStage2` | Defines a set of states for the rocket and allows the rocket object to alter its behavior when its internal state changes. |
| **Strategy** | `PropulsionStrategy` (e.g., Ion, Chemical) | Defines a family of algorithms (propulsion methods) and makes them interchangeable, allowing Mission Control to select the best one at runtime. |

## How to Run the Simulator

1.  Navigate to the `EXERCISE 2/RocketSimulator` directory.
2.  Compile the project files.
3.  Execute the main class to start the console simulation.
4.  Follow the on-screen prompts from Mission Control to run pre-launch checks and initiate the final countdown and launch sequence.

