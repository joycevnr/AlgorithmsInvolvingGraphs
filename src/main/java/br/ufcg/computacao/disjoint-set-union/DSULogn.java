package br.ufcg.computacao.disjoint-set-union;

public class DSULogn {
    private int[] parent;
    private int[] size;
    
    public DSULogn(int n){
        this.parent = new int[n];
        this.size = new int[n];
    }

    public void makeSet(int v){
        this.parent[v] = v;
        this.size[v] = 1;
    }

    public void unionSet(int a, int b){
        a = findSet(a);
        b = findSet(b);
        if(a != b){
            if(this.size[a] < this.size[b]){
                int aux = a;
                a = b;
                b = aux;
            }
            this.parent[b] = a;
            this.size[a] += this.size[b];
        }
    }

    public int findSet(int v){
        if(this.parent[v] == v)
            return v;
        return this.parent[v] = findSet(this.parent[v]);
    }

}

