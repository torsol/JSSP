package models;

public class Job{


    static int[][] technicalMatrix = Model.technicalMatrix;
    static int[][] processingMatrix = Model.processingMatrix;
    int index;
    int operationIndex = 0;

    public Job(int index){
        this.index = index;
    }

    public Node getNextNode(){
        return new Node(index, operationIndex, technicalMatrix[index][operationIndex], processingMatrix[index][operationIndex]);
    }

    public boolean hasNextNode(){
        return operationIndex < technicalMatrix[0].length;
    }

    public void advance(){
        operationIndex++;
    }

    public Job copySelf(){
        return new Job(this.index);
    }






}