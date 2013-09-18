'''
Created on Sep 7, 2013

@author: msameh
'''
import numpy as np
arr = np.array([1, 2, 3, 4, 5.6, 7, 8, 9, 10], float)
mat = np.array([[1, 2, 3, 4], [5, 6, 7, 8]], float)

print arr.reshape((3, 3)).transpose().flatten()
print arr[:, None]
print np.newaxis
print np.arange(0, 7, 2, dtype=float)
print np.zeros_like(arr, dtype=float)
print np.eye(5, k=2, dtype=float)
print 9 + arr
for x in arr:
    print x
print np.prod(arr) == arr.prod()
print arr.std()
print arr.argmax()
arr.sort()
print arr
print arr.clip(1, 7)
# print np.logical_and(arr, mat)
print np.array([0, 1, 2]) == 0
print np.NaN
print np.Inf
print arr[arr > 4]
print arr[np.logical_xor(arr > 5, arr < 9)]
print arr[[1, 1, 1, 1, 0]]

a = np.array([[1, 2, 3], [3, 4, 5]])
b = np.array([[6, 5, 4], [8, 9, 5]])
print a.transpose().dot(b)