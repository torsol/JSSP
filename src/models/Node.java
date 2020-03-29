package models;

public class Node{

    int job;
    int index;
    int machine;
    int duration;

    double probability;

    public Node(int job, int index, int machine, int duration){
        this.job = job;
        this.machine = machine;
        this.duration = duration; 
        this.index = index;
    }

    public String toString(){
        return "[Job:OperationIndex]" + "[" + job +":" + index + "] [Machine:Duration]" + "[" + machine +":" + duration + "]";
    }
    public void addProbability(double probability){
        this.probability = probability;
    }
}