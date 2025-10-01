# 🚀 Design Patterns: A Guide to Scalable Software

Design patterns are established, reusable solutions to recurring problems in system design. They are conceptual templates that guide developers in structuring software to be **scalable, maintainable, and efficient**.

Using design patterns leads to:
* **Consistency** across codebases
* **Reusability** of solutions
* **Improved communication** among developers (common vocabulary)
* **Maintainability and flexibility** in software systems

They are typically divided into three categories: **Creational**, **Structural**, and **Behavioral** patterns.

---

## 🏗️ Structural Design Patterns

Structural patterns define how objects and classes are organized. They focus on how different parts of a system can be combined to form larger, efficient, and easy-to-modify structures.

| Pattern | Definition / Purpose | Space-Themed Use Case | Benefit |
| :--- | :--- | :--- | :--- |
| **Adapter Pattern** | Converts incompatible interfaces so systems can communicate seamlessly. | **Alien Communication:** Earth systems need to communicate with an alien transmitter with a different interface. | Allows systems with different interfaces to work together. |
| **Composite Pattern** | Allows individual modules and composite structures to be treated uniformly. | **Space Station Modules:** Modules (solar panels, labs) can be treated uniformly as a single object. | Simplifies operations by treating groups and individual objects identically. |

---

## ✨ Creational Design Patterns

Creational patterns manage how objects are created. Their purpose is to encapsulate object creation logic and promote flexibility by controlling how objects are instantiated.

| Pattern | Definition / Purpose | Space-Themed Use Case | Benefit |
| :--- | :--- | :--- | :--- |
| **Singleton Pattern** | Guarantees a single point of control by ensuring only one instance of a class exists. | **Mission Control Center:** Only one mission control exists to coordinate all space missions. | Guarantees a single, global point of access and control. |
| **Factory Pattern** | Centralizes and flexibly creates different object types without exposing instantiation logic. | **Spacecraft Factory:** Mission Control can create different spacecraft (Shuttle, Probe, Satellite) without knowing their exact classes. | Centralized, flexible creation; new spacecraft can be added easily. |

---

## 🔄 Behavioral Design Patterns

Behavioral patterns regulate how objects interact and communicate. They manage communication and responsibility among objects by defining clear interaction rules.

| Pattern | Definition / Purpose | Space-Themed Use Case | Benefit |
| :--- | :--- | :--- | :--- |
| **Observer Pattern** | Defines a one-to-many dependency, allowing objects to automatically react to changes. | **Space Weather Monitoring:** Modules (Navigation, Shields) automatically react to solar flare updates. | Any new module can subscribe to updates without changing existing code. |
| **Strategy Pattern** | Allows a spacecraft to switch behavior dynamically at runtime. | **Spacecraft Propulsion:** Switching propulsion methods dynamically (Chemical, Ion, Warp). | Mission Control can change spacecraft behavior at runtime without modifying its core code. |



# 🚀 Rocket Launch Simulator  

A simulation system that models rocket launches with multiple design patterns (Command, Facade, Observer, State, Strategy, Singleton). The system allows a user to set the orbit, perform checks, launch the rocket, and fast-forward the simulation.  

---

## 📌 Features
- **Command Pattern** → Processes user commands cleanly (avoids nested `if-else`).  
- **Facade Pattern** → User interacts with simple commands without knowing internal workings.  
- **Observer Pattern** → Sends updates to UserClient and Logger.  
- **State Pattern** → Manages rocket stages (Pre-launch, Stage 1, Stage 2).  
- **Strategy Pattern** → Handles different failure checks at each stage.  
- **Singleton Pattern** → Logger ensures a single instance for logging events.  

---

## 🖥️ User Client
The entry point for the user.  
- Accepts commands and interacts with the `CommandParser`.  
- Executes parsed commands through the `RocketLaunchSimulator`.  

### User Commands
- `set_orbit_leo` → Set orbit to LEO and build rocket.  
- `set_orbit_meo` → Set orbit to MEO and build rocket.  
- `set_orbit_geo` → Set orbit to GEO and build rocket.  
- `start_checks` → Run pre-launch system checks.  
- `launch` → Launch the rocket and begin Stage 1.  
- `fast_forward_x` → Simulate `x` seconds ahead.  

---

## ⚙️ Command Processing Flow
1. **UserClient** receives input.  
2. **CommandParser** interprets input, validates it, and creates a `Command` object.  
3. **UserClient** executes the command.  
4. **RocketLaunchSimulator** runs the necessary method.  

✅ **Syntactic Errors** (typos like `lanch`, `startchecks`) → Handled by **CommandParser**.  
✅ **Semantic Errors** (launch before checks, checks before setting orbit) → Handled by **RocketLaunchSimulator**.  

---

## 🛰️ Orbit Levels
### Approach 1
- `MISSION_PROFILES` contains all 3 orbit configurations.  
- User selects `set_orbit_leo/meo/geo`.  

### Approach 2 (Preferred)  
- `MISSION_PROFILES` contains only LEO details.  
- Expansion for MEO and GEO possible later.  

---

## 🏗️ Rocket Building
### Rocket Model
- Initial values:  
  - Fuel = 100%  
  - Altitude = 0 km  
  - Speed = 0 km/hr  
  - Stage = 0  
  - Angle = 0  
  - Horizontal Speed = 0 km/hr  

- Parameters set by **RocketBuilder**:  
  - Mass  
  - Fuel Amount  
  - Max Altitude  
  - Max Orbital Speed  
  - Max Horizontal Speed  

### Rocket Builder
- Receives input from **Mission Director**.  
- Builds the rocket with the given parameters.  

### Mission Director
- Loads orbit profiles from `MISSION_PROFILES`.  
- Builds rocket using **Rocket Builder**.  

---

## 🚀 Rocket Launch Simulator
- Handles system state and rocket execution.  
- **Start Checks**:  
  - 0.5% chance of system malfunction.  
  - If successful, mission starts.  
- **Launch**:  
  - Stage 1 → Stage 2 transition handled.  
- **Fast Forward**:  
  - Advances simulation by `x` seconds.  

---

## 🔄 State Management
- **Pre-launch**  
- **Stage 1**  
- **Stage 2**  

**Simulator States**  
- `Active`  
- `Inactive`  

**Success Conditions**  
- Reached `max_altitude` and `orbital_speed`.  

**Failure Conditions**  
- System errors or malfunctions.  

---

## ⚠️ Failure Strategy (Strategy Pattern)
- **Pre-Launch**: 0.5% chance of malfunction.  
- **Stage 1**: 1% chance of engine flameout per second.  
- **Stage 2**: 2% chance of fuel leak per second (increases burn rate).  

---

## 📝 Logger (Singleton)
- Stores all user commands + mission events with timestamps.  
- Format:  

- Logs:  
- User commands  
- Mission status changes (success, failure, stage separation, etc.)  
- Exception handling for file operations included.  

---

## 📡 Output Management
### Approach 1  
Use Observer Pattern for **both UserClient and Logger**.  

### Approach 2 (Ease of Coding)  
Send output directly to console.  

### Approach 3 (Preferred for Scalability)  
Use Observer Pattern for **UserClient** only. Logger integration can be added later.  

---

## ⚡ Exception Handling
- **CommandParser**: Handles invalid syntax.  
- **RocketLaunchSimulator**: Handles invalid command order (semantic errors).  
- **Logger**: Handles file read/write errors.  
- **Commands**: Handles invalid values in `fast_forward_x` and checks states  

---

## 📂 Design Patterns Summary
- **Command Pattern** → User command handling.  
- **Facade Pattern** → Simplified interface for the user.  
- **Observer Pattern** → Output updates to clients.  
- **State Pattern** → Rocket stages + simulator status.  
- **Singleton Pattern** → Logger.  

---

## 📊 System Architecture

```mermaid
flowchart TD
  A[UserClient] -->|Sends input| B[CommandParser]
  B -->|Creates Command| C[RocketLaunchSimulator]
  C -->|Build Rocket| D[MissionDirector]
  D -->|Configures| E[RocketBuilder]
  E -->|Builds| F[Rocket]

  C -->|Updates| G[Logger]
  C -->|Notifies| H[UserClient Output]

