package br.ufcg.computacao.disjoint-set-union;

public class DSU {
     
    private int[] parent;
    
    public DSU(int n){
        this.parent = new int[n];
    }

    public void makeSet(int v){
        this.parent[v] = v;
    }

    public void unionSet(int a, int b){
        a = findSet(a);
        b = findSet(b);
        if(a != b){
            this.parent[b] = a;
        }
    }

    public int findSet(int v){
        if(this.parent[v] == v)
            return v;
        return findSet(this.parent[v]);
    }

}
