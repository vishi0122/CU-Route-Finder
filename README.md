# CU Campus Route Finder
### DAA + Java Project | Chandigarh University

---

## 📌 Project Overview
A Java-based navigation system for Chandigarh University campus that implements
four graph traversal and pathfinding algorithms from the DAA (Design and Analysis
of Algorithms) course. The system provides both a **Console Interface** and a
**Graphical User Interface (GUI)** with visual graph rendering.

---

## 🎯 Algorithms Implemented

| Algorithm | Type | Best For |
|-----------|------|----------|
| **Dijkstra** | Greedy / Priority Queue | Shortest distance path |
| **BFS** | Graph Traversal | Fewest number of stops |
| **DFS** | Graph Traversal | Deep exploration |
| **A\*** | Heuristic Search | Fast shortest path |

---

## 🗺️ Campus Locations (15 Nodes)
1. Main Gate
2. Admin Block
3. UIET Block
4. UIET Lab
5. Library
6. CSE Block
7. MBA Block
8. Punjabi Block
9. Sports Complex
10. Boys Hostel
11. Girls Hostel
12. Cafeteria
13. Medical Center
14. Auditorium
15. Research Block

---

## 📁 Project Structure
```
RouteFinderCU/
├── src/
│   ├── Main.java          → Entry point
│   ├── Graph.java         → Campus graph (nodes + weighted edges)
│   ├── Dijkstra.java      → Dijkstra's Algorithm
│   ├── BFSDFSAlgo.java    → BFS and DFS Algorithms
│   ├── AStarAlgo.java     → A* Algorithm
│   ├── ConsoleApp.java    → Console UI
│   └── GraphGUI.java      → Swing GUI with visual graph
├── run.bat                → Windows compile & run
├── run.sh                 → Linux/Mac compile & run
└── README.md
```

---

## 🚀 How to Run

### Windows:
```
Double-click run.bat
OR
javac -d out src\*.java
java -cp out Main
```

### Linux / Mac:
```
chmod +x run.sh && ./run.sh
OR
javac -d out src/*.java
java -cp out Main
```

### Launch GUI Directly:
```
java -cp out Main gui
```

---

## 📊 DAA Concepts Covered
- **Graph Representation**: Weighted undirected graph using adjacency list
- **Greedy Algorithm**: Dijkstra's shortest path
- **BFS**: Level-by-level traversal using Queue
- **DFS**: Deep traversal using Stack / Recursion
- **A\***: Heuristic-based optimal search (f = g + h)
- **Priority Queue**: Used in Dijkstra and A*
- **Time Complexity**:
  - Dijkstra: O((V + E) log V)
  - BFS: O(V + E)
  - DFS: O(V + E)
  - A*: O(E log V) with admissible heuristic

---

## 👨‍💻 Developer
- **Name**: Vishal Kumar
- **Roll No**: 24BCS10668
- **Section**: 605/A
- **Course**: B.Tech CSE — 4th Semester
- **University**: Chandigarh University
- **Subject**: Design and Analysis of Algorithms (DAA)
