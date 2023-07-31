# TSPAlgorithms

**Overview**

This is a summer research project based on the Circulant Traveling Salesman Problem, a special case of one of the most famous problems in computer science with applications to network design and waste minimization. The goal of the Circulant TSP is to find a minimum-cost Hamiltonian cycle given a complete graph on n vertices and a cost for each possible edge length, where edges of the same length have the same cost. Length is the shortest distance between 2 vertices, ranging from 1 to floor(n/2). For example, on a graph with 8 vertices, arranged in a circular shape and numbered 1-8, moving from vertex 1→2 and 8→1 both have a length of 1 and thus the same cost. 

In collaboration with another undergraduate and Professor Gutekunst, we focused on finding minimum-cost Hamiltonian paths instead of cycles, meaning our goal was to find a way to visit all n vertices exactly once, while using the lowest total edge costs as possible. We developed and implemented algorithms in Java to study how the number of possible solutions grows with the size of the input. We ran numerical experiments to conjecture theoretical results, and proved them using techniques from combinatorics and number theory.

**Takeaway**

Calculating the number of edge-length vectors is “easy” when an integer n can be expressed in the form p, p1 * p2, p1^k, p1^2 * p2, or p1 * p2 * p3. This means that the number of minimum-cost Hamiltonian paths in the Circulant TSP can be quickly found from a value of n that can be written in any of those ways.

**Technical Terminology: Encoding Sequences, G-Sequences, and Edge-Length Vectors (ELV)**

Encoding sequences are minimum-cost Hamiltonian paths, which can all be represented as a sequence in the form (n, _, …, _). All of the valid sequences can be formed from the integers 1 to n/2, which is the set {1, …, floor(n/2)}. To form a valid sequence, you start with n and add a number from the set and find the greatest common divisor (GCD). If the GCD of these two terms is 1, then this is the end of a valid sequence. If the GCD is not 1, then add another number from the set and take the GCD of the newly added term and the previous GCD. If the GCD is not strictly decreasing, meaning the newest GCD >= previous GCD, then that sequence is invalid. Repeat this process of only adding terms that create a strictly decreasing GCD and ending the sequence once the GCD is 1. By testing all permutations, we can find all valid sequences. 
Example: consider n = 8 which has edge-lengths {1, 2, 3, 4}. The valid encoding sequences are (8,1), (8,2,1), (8,4,1), (8,4,2,1). An invalid encoding sequence would be (8,3,1) since GCD(8,3) = 1 = GCD(3,1), so the GCD is not strictly decreasing.

From an encoding sequence, a G-Sequence can be formed. A G-Sequence always starts with n and is built similarly to encoding sequences, except the GCD of the current and previous numbers is added to the sequence instead of the number itself. Multiple encoding sequences can lead to the same G-Sequence. Since several encoding sequences map to a single G-Sequence, G-Sequences are easier to work with and were the starting point of our research.
Example: consider the encoding sequences (8, 4, 1) and (8, 4, 3) for n = 8. Since GCD(8, 4) = 4 and GCD(4,1) = GCD(4,3) = 1, the G-Sequence for both of these would be (8, 4, 1). 

Finally, edge-length vectors (ELV) can be uniquely derived from an encoding sequence, meaning every encoding sequence maps to a unique ELV, so they have equal counts. An ELV shows how many of each edge-length is used to form a minimum-cost Hamiltonian path. For every number in an encoding sequence, let t1 = n, t2 = second term, t3 = third term, etc. Given an encoding sequence, calculate t1 - GCD(t1, t2) and place it at the t2 position in a vector. Then calculate GCD(t1, t2) - GCD(t2, t3) and place it at the t3 position in a vector. Repeat this process until the whole encoding sequence is traversed. All of the values in the final ELV should sum up to n - 1 total edges.
Example: given encoding sequence (8, 4, 1)
8 - GCD(8,4) = 8 - 4 = 4 at position 4
GCD(8,4) - GCD(4, 1) = 4 - 1 = 3 at position 1
ELV = <3, 0, 0, 4>
Meaning this Hamiltonian path uses three length-1 edges and four length-4 edges.

**Files**

**GSequence**: generate all valid G-Sequences for a given n

**GreedyELVCount**: create a permutation of every possible encoding sequence and count the valid ones

**SmartELVCount**: from a list of all G-Sequences, generate and count all valid encoding sequences

**PrimeELVCount**: formulas to quickly count ELV when n = p1 * p2 (p2 > p1 ≥ 2) and n = p1^k (p1 ≥ 3) where p1, p2 are distinct prime numbers, k is an integer ≥ 1

**ComplementELVCount**: formulas to quickly count ELV by complement when n = p1^2 * p2 and n = p1 * p2 * p3 where p1, p2, p3 are distinct prime numbers

**AlgorithmTester**: check the accuracy of the formulas with the results from the smart algorithm for the same value of n
