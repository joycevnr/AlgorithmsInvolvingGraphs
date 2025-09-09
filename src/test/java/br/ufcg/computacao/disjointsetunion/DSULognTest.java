package br.ufcg.computacao.disjointsetunion;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class DSULognTest{

    private DSULogn dsu;

    @BeforeEach
    public void setUp() {
        dsu = new DSULogn(7);
        dsu.makeSet(0);
        dsu.makeSet(1);
        dsu.makeSet(2);
        dsu.makeSet(3);
        dsu.makeSet(4);
        dsu.makeSet(5);
        dsu.makeSet(6);
    }

    @Test
    void testMakeSet() {
        assertEquals(0, dsu.findSet(0));
        assertEquals(1, dsu.findSet(1));
        assertEquals(2, dsu.findSet(2));
        assertEquals(3, dsu.findSet(3));
        assertEquals(4, dsu.findSet(4));
        assertEquals(5, dsu.findSet(5));
        assertEquals(6, dsu.findSet(6));
    }

    @Test
    void testUnionSet(){
        dsu.unionSet(0, 1);
        dsu.unionSet(2,3);
        dsu.unionSet(4,5);
        assertEquals(0, dsu.findSet(1));
        assertEquals(2, dsu.findSet(3));
        assertEquals(4, dsu.findSet(5));
        
        dsu.unionSet(1, 2);
        assertEquals(0, dsu.findSet(3));

        dsu.unionSet(5, 6);
        assertEquals(4, dsu.findSet(6));

        dsu.unionSet(6,0);
        assertEquals(0, dsu.findSet(5));
        assertEquals(0, dsu.findSet(6));
    }
}