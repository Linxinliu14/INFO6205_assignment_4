/*
 * Copyright (c) 2017. Phasmid Software
 */
package edu.neu.coe.info6205.union_find;

import edu.neu.coe.info6205.util.Benchmark;
import edu.neu.coe.info6205.util.Benchmark_Timer;

import java.util.Random;

/**
 * Weighted Quick Union with Path Compression
 */
public class WQUPC {
    private final int[] parent;   // parent[i] = parent of i
    private final int[] depth;   // depth[i] = size of subtree rooted at i
    private int count;  // number of components

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     *
     * @param n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public WQUPC(int n) {
        count = n;
        parent = new int[n];
        depth = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            depth[i] = 1;
        }
    }

    public void show() {
        for (int i = 0; i < parent.length; i++) {
            System.out.printf("%d: %d, %d\n", i, parent[i], depth[i]);
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int count() {
        return count;
    }

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IllegalArgumentException unless {@code 0 <= p < n}
     */
    public int find(int p) {
        validate(p);
        int root = p;
        while (root != parent[root]) {
            root = parent[root];
        }
        while (p != root) {
            int newp = parent[p];
            parent[p] = root;
            p = newp;
        }
        return root;
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Merges the component containing site {@code p} with the
     * the component containing site {@code q}.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;
        // make smaller root point to larger one
        if (depth[rootP] < depth[rootQ]) {
            parent[rootP] = rootQ;
            depth[rootQ] += depth[rootP];
        } else {
            parent[rootQ] = rootP;
            depth[rootP] += depth[rootQ];
        }
        count--;
    }

    /**
    * Union all the element in a weighted quick union find that contains {@code n} elements
     *
     * @param n is the number of elements in the WQUPCT
    * */
    public void unionall(int n){
        for(int i=0; i<n/2; i++){
            union(i,n-1-i);
        }
    }

    /**
     * Finding all the element in a weighted quick union find that has {@code n} elements
     *
     * @param n is the number of elements in the WQUPCT
     * */
    public void findall(int n){
        for(int i=0; i<n; i++){find(i);}
    }

/**
 * Using benchmark to time how long will it takes to union all {@code nelement} elements
 * with {@code nrun} number of runs
 *
 * @param nelement is the numbers of elements in the WQUPCT
 * @param nrun is the numbers of times that the function {@code unionall} will run
 * **/
public static double unionTimer(int nrun, int nelement){
        WQUPC uf=new WQUPC(nelement);

    Benchmark<Boolean> bm = new Benchmark_Timer<>(
            "testInsertionSortTimer",
            null,
            b -> {
                uf.unionall(nelement);},
            null);
    double ret = bm.run(true,nrun);
    return ret;
}

/**
 * Using benchmark to time how long will it takes to find all elements
 * in a {@code nelement} elements WQUPC
 * with {@code nrun} numbers of runs
 *
 * @param  nrun numbers of times that {@code findall} method will run
 * @param nelement numbers of elements in a WQUPC
 * */
public static double findTimer(int nrun, int nelement){
        WQUPC uf=new WQUPC(nelement);
        Benchmark<Boolean> bm = new Benchmark_Timer<>(
            "testInsertionSortTimer",
            null,
            b -> {uf.findall(nelement);},
            null);
        double ret = bm.run(true,nrun);
        return ret;
}



public static void main(String[] args){
    int[] nRuns = new int[]{500,800,1000,1200,1600};//numbers of runs
    int n = 100;// numbers of elements

    for(int i=0; i<nRuns.length; i++){
        System.out.println(" Time to union "+n+" element is "+unionTimer(nRuns[i],n));
        System.out.println("\n");}

    for(int i=0; i<nRuns.length; i++){
        System.out.println(" Time to find all elements is "+findTimer(nRuns[i],n));
        System.out.println("\n");}
}

}
