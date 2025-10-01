🚀 Exercise 1: Design Pattern Demos

Located in the src/exercise1/patterns directory, this exercise provides six isolated, console-based simulations, each dedicated to demonstrating a single design pattern.
Patterns Demonstrated

Category
	

Pattern
	

Goal
	

Implementation File

Creational
	

Singleton
	

Ensures a single instance of a critical system (e.g., Mission Control) exists throughout the application.
	

SingletonSimulation.java

Creational
	

Factory Method
	

Defines an interface for creating an object, but lets subclasses decide which class to instantiate (e.g., creating different types of Spacecraft).
	

FactorySimulation.java

Structural
	

Composite
	

Allows clients to treat individual objects and groups of objects uniformly (e.g., handling status reports for individual modules and entire space station segments).
	

CompositeSimulation.java

Behavioral
	

Strategy
	

Defines a family of algorithms, encapsulates each one, and makes them interchangeable (e.g., applying different propulsion methods to a rocket).
	

StrategySimulation.java

Behavioral
	

Command
	

Encapsulates a request as an object, allowing parameterization of clients with different requests, queueing of requests, or logging (e.g., issuing commands like Launch or Fast-Forward).
	

CommandSimulation.java

Behavioral
	

Observer
	

Defines a one-to-many dependency so that when one object changes state, all its dependents are notified (e.g., a Space Weather Station notifying individual Ship Modules of a solar flare).
	

ObserverSimulation.java
