# 后处理

## 文件说明
| 序号 | 文件名称 | 说明 |
| ------ | ------ | ------ |
| 1 | src/main/jave| 主目录 |
| 2 |  | 相似度比较 |
| 3 |  | 规则校验 |
| 4 |  | 全排列 |

## 详细介绍
- Levenshtein distance <br>
In information theory, linguistics and computer science, the Levenshtein distance is a string metric for measuring the difference between two sequences. Informally,
the Levenshtein distance between two words is the minimum number of single-character edits (insertions, deletions or substitutions) required to change one word into the other.
It is named after the Soviet mathematician Vladimir Levenshtein, who considered this distance in 1965.
Levenshtein distance may also be referred to as edit distance, although that term may also denote a larger family of distance metrics.[2]:32 It is closely related to pairwise string alignments.
<br>
1 Definition
Mathematically, the Levenshtein distance between two strings a , b {\displaystyle a,b} a,b (of length | a | {\displaystyle |a|} |a| and | b | {\displaystyle |b|} |b| respectively) is given by lev a , b ⁡ ( | a | , | b | ) {\displaystyle \operatorname {lev} _{a,b}(|a|,|b|)} \operatorname{lev}_{a,b}(|a|,|b|) where
    lev a , b ⁡ ( i , j ) = { max ( i , j )  if  min ( i , j ) = 0 , min { lev a , b ⁡ ( i − 1 , j ) + 1 lev a , b ⁡ ( i , j − 1 ) + 1 lev a , b ⁡ ( i − 1 , j − 1 ) + 1 ( a i ≠ b j )  otherwise. {\displaystyle \qquad \operatorname {lev} _{a,b}(i,j)={\begin{cases}\max(i,j)&{\text{ if }}\min(i,j)=0,\\\min {\begin{cases}\operatorname {lev} _{a,b}(i-1,j)+1
    \\\operatorname {lev} _{a,b}(i,j-1)+1\\\operatorname {lev} _{a,b}(i-1,j-1)+1_{(a_{i}\neq b_{j})}\end{cases}}&{\text{ otherwise.}}\end{cases}}} {\displaystyle \qquad \operatorname {lev} _{a,b}(i,j)={\begin{cases}\max(i,j)&{\text{ if }}\min(i,j)=0,\\\min {\begin{cases}\operatorname {lev} _{a,b}(i-1,j)+1\\\operatorname {lev} _{a,b}(i,j-1)+1\\\operatorname {lev} _{a,b}(i-1,j-1)+1_{(a_{i}\neq b_{j})}\end{cases}}&{\text{ otherwise.}}\end{cases}}}
where 1 ( a i ≠ b j ) {\displaystyle 1_{(a_{i}\neq b_{j})}} 1_{(a_{i}\neq b_{j})} is the indicator function equal to 0 when a i = b j
{\displaystyle a_{i}=b_{j}} a_{i}=b_{j} and equal to 1 otherwise, and lev a , b ⁡ ( i , j ) {\displaystyle \operatorname {lev} _{a,b}(i,j)} \operatorname{lev}_{a,b}(i,j) is the distance between the first i {\displaystyle i} i characters of a {\displaystyle a} a and the first j {\displaystyle j} j characters of b {\displaystyle b} b.
Note that the first element in the minimum corresponds to deletion (from a {\displaystyle a} a to b {\displaystyle b} b), the second to insertion and the third to match or mismatch, depending on whether the respective symbols are the same.
    <br>
    1.1 Example
    For example, the Levenshtein distance between "kitten" and "sitting" is 3, since the following three edits change one into the other, and there is no way to do it with fewer than three edits:
        kitten → sitten (substitution of "s" for "k")
        sitten → sittin (substitution of "i" for "e")
        sittin → sitting (insertion of "g" at the end).
        <br>
    1.2 Upper and lower bounds
    The Levenshtein distance has several simple upper and lower bounds. These include:
        It is at least the difference of the sizes of the two strings.
        It is at most the length of the longer string.
        It is zero if and only if the strings are equal.
        If the strings are the same size, the Hamming distance is an upper bound on the Levenshtein distance.
        The Levenshtein distance between two strings is no greater than the sum of their Levenshtein distances from a third string (triangle inequality).
    An example where the Levenshtein distance between two strings of the same length is strictly less than the Hamming distance is given by the pair "flaw" and "lawn". Here the Levenshtein distance equals 2 (delete "f" from the front; insert "n" at the end). The Hamming distance is 4.
    <br>
2 Applications
In approximate string matching, the objective is to find matches for short strings in many longer texts, in situations where a small number of differences is to be expected.
The short strings could come from a dictionary, for instance. Here, one of the strings is typically short, while the other is arbitrarily long. This has a wide range of applications, for instance,
spell checkers, correction systems for optical character recognition, and software to assist natural language translation based on translation memory.
The Levenshtein distance can also be computed between two longer strings, but the cost to compute it, which is roughly proportional to the product of the two string lengths, makes this impractical.
Thus, when used to aid in fuzzy string searching in applications such as record linkage, the compared strings are usually short to help improve speed of comparisons.[citation needed]
In linguistics, the Levenshtein distance is used as a metric to quantify the linguistic distance, or how different two languages are from one another.[3] It is related to mutual intelligibility, the higher the linguistic distance,
the lower the mutual intelligibility, and the lower the linguistic distance, the higher the mutual intelligibility.
3 Relationship with other edit distance metrics
There are other popular measures of edit distance, which are calculated using a different set of allowable edit operations. For instance,
    the Damerau–Levenshtein distance allows insertion, deletion, substitution, and the transposition of two adjacent characters;
    the longest common subsequence (LCS) distance allows only insertion and deletion, not substitution;
    the Hamming distance allows only substitution, hence, it only applies to strings of the same length.
    the Jaro distance allows only transposition.
Edit distance is usually defined as a parameterizable metric calculated with a specific set of allowed edit operations, and each operation is assigned a cost (possibly infinite). This is further generalized by DNA sequence alignment algorithms such as the Smith–Waterman algorithm, which make an operation's cost depend on where it is applied.
4 Computing Levenshtein distance
    4.1 Recursive
    This is a straightforward, but inefficient, recursive C implementation of a LevenshteinDistance function that takes two strings, s and t, together with their lengths, and returns the Levenshtein distance between them:
    // len_s and len_t are the number of characters in string s and t respectively
    int LevenshteinDistance(const char *s, int len_s, const char *t, int len_t)
    {
      int cost;

      /* base case: empty strings */
      if (len_s == 0) return len_t;
      if (len_t == 0) return len_s;

      /* test if last characters of the strings match */
      if (s[len_s-1] == t[len_t-1])
          cost = 0;
      else
          cost = 1;

      /* return minimum of delete char from s, delete char from t, and delete char from both */
      return minimum(LevenshteinDistance(s, len_s - 1, t, len_t    ) + 1,
                     LevenshteinDistance(s, len_s    , t, len_t - 1) + 1,
                     LevenshteinDistance(s, len_s - 1, t, len_t - 1) + cost);
    }

    This implementation is very inefficient because it recomputes the Levenshtein distance of the same substrings many times.
    A more efficient method would never repeat the same distance calculation.
    For example, the Levenshtein distance of all possible prefixes might be stored in an array d[][] where d[i][j] is the distance between the first i characters of string s and the first j characters of string t.
    The table is easy to construct one row at a time starting with row 0. When the entire table has been built, the desired distance is d[len_s][len_t]
    <br>
    4.2 Iterative with full matrix
    Computing the Levenshtein distance is based on the observation that if we reserve a matrix to hold the Levenshtein distances between all prefixes of the first string and all prefixes of the second, then we can compute the values in the matrix in a dynamic programming fashion, and thus find the distance between the two full strings as the last value computed.

    This algorithm, an example of bottom-up dynamic programming, is discussed, with variants, in the 1974 article The String-to-string correction problem by Robert A. Wagner and Michael J. Fischer.[4]

    This is a straightforward pseudocode implementation for a function LevenshteinDistance that takes two strings, s of length m, and t of length n, and returns the Levenshtein distance between them:

    function LevenshteinDistance(char s[1..m], char t[1..n]):
      // for all i and j, d[i,j] will hold the Levenshtein distance between
      // the first i characters of s and the first j characters of t
      // note that d has (m+1)*(n+1) values
      declare int d[0..m, 0..n]

      set each element in d to zero

      // source prefixes can be transformed into empty string by
      // dropping all characters
      for i from 1 to m:
          d[i, 0] := i

      // target prefixes can be reached from empty source prefix
      // by inserting every character
      for j from 1 to n:
          d[0, j] := j

      for j from 1 to n:
          for i from 1 to m:
              if s[i] = t[j]:
                substitutionCost := 0
              else:
                substitutionCost := 1
              d[i, j] := minimum(d[i-1, j] + 1,                   // deletion
                                 d[i, j-1] + 1,                   // insertion
                                 d[i-1, j-1] + substitutionCost)  // substitution

      return d[m, n]

    Two examples of the resulting matrix (hovering over a tagged number reveals the operation performed to get that number):
    		k 	i 	t 	t 	e 	n
    	0 	1 	2 	3 	4 	5 	6
    s 	1 	1 	2 	3 	4 	5 	6
    i 	2 	2 	1 	2 	3 	4 	5
    t 	3 	3 	2 	1 	2 	3 	4
    t 	4 	4 	3 	2 	1 	2 	3
    i 	5 	5 	4 	3 	2 	2 	3
    n 	6 	6 	5 	4 	3 	3 	2
    g 	7 	7 	6 	5 	4 	4 	3

    		S 	a 	t 	u 	r 	d 	a 	y
    	0 	1 	2 	3 	4 	5 	6 	7 	8
    S 	1 	0 	1 	2 	3 	4 	5 	6 	7
    u 	2 	1 	1 	2 	2 	3 	4 	5 	6
    n 	3 	2 	2 	2 	3 	3 	4 	5 	6
    d 	4 	3 	3 	3 	3 	4 	3 	4 	5
    a 	5 	4 	3 	4 	4 	4 	4 	3 	4
    y 	6 	5 	4 	4 	5 	5 	5 	4 	3
    The invariant maintained throughout the algorithm is that we can transform the initial segment s[1..i] into t[1..j] using a minimum of d[i,j] operations. At the end, the bottom-right element of the array contains the answer.
    4.3 Iterative with two matrix rows
It turns out that only two rows of the table are needed for the construction if one does not want to reconstruct the edited input strings (the previous row and the current row being calculated).

The Levenshtein distance may be calculated iteratively using the following algorithm:[5]

function LevenshteinDistance(char s[1..m], char t[1..n]):
    // create two work vectors of integer distances
    declare int v0[n + 1]
    declare int v1[n + 1]

    // initialize v0 (the previous row of distances)
    // this row is A[0][i]: edit distance for an empty s
    // the distance is just the number of characters to delete from t
    for i from 0 to n:
        v0[i] = i

    for i from 0 to m-1:
        // calculate v1 (current row distances) from the previous row v0
        // first element of v1 is A[i+1][0]
        //   edit distance is delete (i+1) chars from s to match empty t
        v1[0] = i + 1
        // use formula to fill in the rest of the row
        for j from 0 to n-1:
            // calculating costs for A[i+1][j+1]
            deletionCost := v0[j + 1] + 1
            insertionCost := v1[j] + 1
            if s[i] = t[j]:
                substitutionCost := v0[j]
            else:
                substitutionCost := v0[j] + 1
            v1[j + 1] := minimum(deletionCost, insertionCost, substitutionCost)
        // copy v1 (current row) to v0 (previous row) for next iteration
        swap v0 with v1
    // after the last swap, the results of v1 are now in v0
    return v0[n]
This two row variant is suboptimal—the amount of memory required may be reduced to one row and one word of overhead.
Hirschberg's algorithm combines this method with divide and conquer. It can compute the optimal edit sequence, and not just the edit distance, in the same asymptotic time and space bounds.
    4.4 Adaptive variant
    The dynamic variant is not the ideal implementation. An adaptive approach may reduce the amount of memory required and, in the best case, may reduce the time complexity to linear in the length of the shortest string, and, in the worst case, no more than quadratic in the length of the shortest string.
    4.5 Approximation
    The Levenshtein distance between two strings of length n can be approximated to within a factor
        ( log ⁡ n ) O ( 1 / ε ) {\displaystyle (\log n)^{O(1/\varepsilon )}} {\displaystyle (\log n)^{O(1/\varepsilon )}}
    where ε > 0 is a free parameter to be tuned, in time O(n1 + ε).
    4.6 Computational complexity
    It has been shown that the Levenshtein distance of two strings of length n cannot be computed in time O(n2 - ε) for any ε greater than zero unless the strong exponential time hypothesis is false.