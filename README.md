# TSPAlgorithms

**Overview**

This is a summer research project based on the Circulant Traveling Salesman Problem, a special case of one of the most famous problems in computer science with applications to network design and waste minimization. The goal of the Circulant TSP is to find a minimum-cost Hamiltonian cycle given a complete graph on vertices labeled 1 through n arranged around a circle, where edges of the same length have the same cost. Length is measured by the shortest distance between two vertices, which is based on how many "hops" apart they are around the circle, and that distance determines their cost. For example, on a graph with 8 vertices numbered 1-8, the distances from 1→2, 2→1, and 8→1 are all the same (1), and thus the cost of traveling between these vertices would be the same."

In collaboration with another undergraduate and Professor Gutekunst, we focused on finding minimum-cost Hamiltonian paths instead of cycles when graphs had these circulant costs. Our goal was to find a way to visit all n vertices exactly once in a single path, while using the lowest total edge costs as possible. We developed and implemented algorithms in Java to study how the number of possible solutions grows with the size of the input. We ran numerical experiments to conjecture theoretical results, and proved them using techniques from combinatorics and number theory.

**Takeaway**

Calculating the number of ELV is “easy” when an integer n can be expressed in the form p, p1 * p2, p1^k, p1^2 * p2, p1 * p2 * p3, or p1^k * p2^j. This means that the number of minimum-cost Hamiltonian paths in the Circulant TSP can be quickly found from a value of n that can be written in any of those ways.

**Technical Terminology: Edge-Length Vectors (ELV), G-Sequences, and Encoding Sequences**

In a circulant instance, the cost of a Hamiltonian path depends only on how many edges of each length it uses. Let ti be the number of length-i edges used. For example, with 5 vertices the paths 1, 2, 3, 5, 4 and 2, 1, 3, 4, 5 both use 3 length-1 edges (t1 = 3) and 1 length-2 edge (t2 = 1), so they have the same cost. The edge-length vector (ELV) encodes this in the form <t1, t2, …, ti>, which is <3, 1> in this example.

Next, G-Sequences show the connectivity using cheapest edges. A G-Sequence can be represented in the form (n, _, …, _). All of the valid sequences can be formed using the integers 1 to n/2, which is the set {1, …, floor(n/2)}. To form a valid sequence, start with n and add the greatest common divisor (GCD) of n and a number from the set. If the GCD of these two terms is 1, then this is the end of a valid sequence. If the GCD is not 1, then add the GCD of another number from the set with the previous GCD. If the GCD is not strictly decreasing, meaning the newest GCD >= previous GCD, then that sequence is invalid. Repeat this process of only adding terms that create a strictly decreasing GCD and ending the sequence once the GCD is 1. By testing all permutations, we can find all valid sequences. 

**Example**: consider n = 8 which has edge-lengths {1, 2, 3, 4}. The valid G-Sequences are (8,1), (8,2,1), (8,4,1), (8,4,2,1). An invalid G-Sequence would be (8,4,2) since GCD(4, 2) = 2, so the sequence does not end in 1.

Finally, an encoding sequence encodes how many edges can be used greedily to create a minimum-cost Hamiltonian path. This means that you use as many of the cheapest edge as possible, then as many of the next cheapest edge, and so on until you’ve used the most edges possible in a path (n - 1 edges). Each encoding sequence maps to exactly one ELV, so counting encoding sequences or ELV leads to the same result. 

Encoding sequences are built similarly to G-Sequences, except the actual number from the set is added to the sequence, not the GCD of the newly added term with the previous term. Multiple encoding sequences can map to the same G-Sequence.

**Example**: consider n = 8 which has edge-lengths {1, 2, 3, 4}. The valid encoding sequences are (8,1), (8, 3), (8,2,1), (8,2,3), (8,4,1), (8,4,3), (8,4,2,1), and (8,4,2,3). Both (8,4,2,1) and (8,4,2,3) map to the G-Sequence (8,4,2,1), since GCD(3,1) = GCD(2,1) = 1. An invalid encoding sequence would be (8,3,1) since GCD(8,3) = 1 = GCD(3,1), so the GCD is not strictly decreasing.


**Files**

**AlgorithmTester**: check the accuracy of the formulas with the results from the smart algorithm for the same value of n

**ComplementELVCount**: formulas to quickly count ELV by complement when n = p1^2 * p2 and n = p1 * p2 * p3 where p1, p2, p3 are distinct prime numbers

**CubeSolver**: implementation of the "easy" algorithm that solves Circulant TSP instances such that n = p^3, a1 = p^2, a2 = p, a3 = relatively prime to n

**GreedyELVCount**: create a permutation of every possible encoding sequence and count the valid ones

**GridELVCount**: algorithm that counts ELV much faster than a permutation approach, which provides more data by solving larger values of n, when n = p1^k * p2^j (k, j > 0) 

**GSequence**: generate all valid G-Sequences for a given n

**PrimeELVCount**: formulas to quickly count ELV when n = p1 * p2 (p2 > p1 ≥ 2) and n = p1^k (p1 ≥ 3) where p1, p2 are distinct prime numbers, k is an integer ≥ 1

**SmartELVCount**: from a list of all G-Sequences, generate and count all valid encoding sequences
