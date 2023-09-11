Write a program to recognize line patterns in a given set of points.

# Point data type. 
Create an immutable data type Point that represents a point in the plane

# Brute force. 
Write a program BruteCollinearPoints.java that examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments. To check whether the 4 points p, q, r, and s are collinear, check whether the three slopes between p and q, between p and r, and between p and s are all equal.

A faster, sorting-based solution:

# FastCollinearPoints.

Should include each maximal line segment containing 4 (or more) points exactly once. For example, if 5 points appear on a line segment in the order p→q→r→s→t, then do not include the subsegments p→s or q→t.
