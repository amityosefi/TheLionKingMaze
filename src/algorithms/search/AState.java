package algorithms.search;

import java.io.Serializable;

public abstract class AState implements Serializable {
    protected int cost;
    protected AState camefrom;

    public AState() {
        this.camefrom=null;
        this.cost=0;
    }

    public abstract boolean equals(AState a);
    protected void setCameFrom(AState a) {
        this.camefrom = a;
    }
    public AState getCameFrom() {
        return this.camefrom;
    }
    protected void setCost(int cost) {
        this.cost=cost;
    }
    public int getCost() {
        return cost;
    }
    public String toString() {
        return "";
    }
}