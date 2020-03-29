package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Model;

public class Scheduler {

    static int[][] technicalMatrix = Model.technicalMatrix;
    static int[][] processingMatrix = Model.processingMatrix;


    public int calculateMakespan(List<Integer> operationOrder){
        System.out.println(operationOrder);
        List<Integer> indices = new ArrayList<Integer>();
        List<Integer> nextAvailableMachineTime = new ArrayList<Integer>();
        List<Integer> currentJobLength = new ArrayList<Integer>();
        for(int i=0; i<technicalMatrix.length; i++){
            indices.add(-1);
            nextAvailableMachineTime.add(0);
            currentJobLength.add(0);
        }


        int currentMachine;
        int currentJob;
        int currentProcedureLength;
        for(int i=0; i<operationOrder.size(); i++){
            currentJob = operationOrder.get(i);
            indices.set(currentJob, indices.get(currentJob)+1);
            currentMachine = technicalMatrix[currentJob][indices.get(currentJob)];
            currentProcedureLength = processingMatrix[currentJob][indices.get(currentJob)];

            if(nextAvailableMachineTime.get(currentMachine) >= currentJobLength.get(currentJob)){
                nextAvailableMachineTime.set(currentMachine, nextAvailableMachineTime.get(currentMachine)+currentProcedureLength);
                currentJobLength.set(currentJob, nextAvailableMachineTime.get(currentMachine)+currentProcedureLength);
            }
            else{
                nextAvailableMachineTime.set(currentMachine, currentJobLength.get(currentJob)+currentProcedureLength);
                currentJobLength.set(currentJob, currentJobLength.get(currentJob)+currentProcedureLength);
            }


        }
        System.out.println(currentJobLength);
        System.out.println(indices);
        return Collections.max(currentJobLength);
    }


    
}

