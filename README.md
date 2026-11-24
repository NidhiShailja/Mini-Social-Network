# Mini Social Network (Java)

**PS No. 94** | Data Structures and Algorithms (DSA) | Session 2026-27

**Submitted by:** Nidhi Shailja Singh (ERP ID: RU-2025-10881)
**Programme:** B.Tech Computer Science & Engineering (Data Science)
**Guide:** Mr. Abhijeet Dutta, SME
**University:** Rungta International Skills University, Bhilai, Chhattisgarh

## About the project

A console-based mini social network written in Java (single file: `MiniSocialNetwork.java`).
Users can register, follow other users, publish posts, view a feed of the latest posts from
the people they follow, and get suggestions of people to follow.

## Data structures and concepts used

| Concept | Used for |
|---|---|
| HashMap (Hashing) | Stores users by username; prevents duplicate usernames |
| Graph (adjacency list) | Directed follow relationships (A follows B = edge A -> B) |
| Linked list | Each user's posts, newest post at the head |
| BFS (depth up to 2) | Suggests users followed by people you follow, whom you do not follow yet |
| BFS tree | Parent links recorded by BFS tell who each suggestion was found through |

## Features

1. Register user(s) - duplicate usernames are rejected
2. Follow a user - cannot follow yourself or follow the same user twice
3. Create a post - post IDs start from 1
4. View feed - posts of followed users merged, newest first
5. Suggest people to follow - BFS up to depth 2
6. List all users
7. Run demo - runs the four test cases from the problem statement

## How to run

Requirements: JDK 8 or above (JDK 17 or 21 recommended).

```
javac MiniSocialNetwork.java
java MiniSocialNetwork
```

Or open the file in VS Code (with the Extension Pack for Java) and click Run.

Choose option **7** to run all four test cases automatically.

## Sample output (Test Case 4)

```
Post 2 created.
Suggested: Karan (followed by Aman)
Riya now follows Karan.
Feed: [Post 2] Karan: Graphs rock
[Post 1] Aman: Hello DSA
```

## Files in this repository

- `MiniSocialNetwork.java` - complete source code
- `Mini_Social_Network_Report.pdf` - project report
- `README.md` - this file

## Time complexity

| Operation | Complexity |
|---|---|
| Register | O(1) average |
| Follow | O(F) |
| Create post | O(1) |
| View feed | O(P x F) |
| Suggest (BFS depth 2) | O(U + E) |
