package models;

public class Triplet implements Comparable<Triplet>{
    int operationIndex;
    double position;
    double velocity;

    public int getOperationIndex() {
        return operationIndex;
    }

    public void setOperationIndex(int operationIndex) {
        this.operationIndex = operationIndex;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public int compareTo(Triplet o) {
        return Double.compare(this.position, o.position); }

    
}