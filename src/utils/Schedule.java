package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Model;

public class Schedule {

    static int[][] technicalMatrix = Model.technicalMatrix;
    static int[][] processingMatrix = Model.processingMatrix;

    List<Integer> indices = new ArrayList<Integer>();
    List<Integer> nextAvailableMachineTime = new ArrayList<Integer>();
    List<Integer> nextAvailableJobTime = new ArrayList<Integer>();
    int makespan = 0;

    public Schedule() {
        for (int i = 0; i < technicalMatrix.length; i++) {
            indices.add(-1);
            nextAvailableMachineTime.add(0);
            nextAvailableJobTime.add(0);
        }
    }

    public void addOperationFromJob(int currentJob) {
        int currentMachine;
        int currentProcedureLength;
        this.indices.set(currentJob, indices.get(currentJob) + 1);
        currentMachine = technicalMatrix[currentJob][this.indices.get(currentJob)];
        currentProcedureLength = processingMatrix[currentJob][this.indices.get(currentJob)];

        if (this.nextAvailableMachineTime.get(currentMachine) >= this.nextAvailableJobTime.get(currentJob)) {
            this.nextAvailableMachineTime.set(currentMachine,
                    this.nextAvailableMachineTime.get(currentMachine) + currentProcedureLength);
            this.nextAvailableJobTime.set(currentJob, nextAvailableMachineTime.get(currentMachine));
        } else {
            this.nextAvailableJobTime.set(currentJob, nextAvailableJobTime.get(currentJob) + currentProcedureLength);
            this.nextAvailableMachineTime.set(currentMachine, nextAvailableJobTime.get(currentJob));
        }
        this.makespan = Collections.max(nextAvailableJobTime);

    }

    public int newPossibleMakespan(int currentJob) {
        int potentionalMakespan = 0;
        int currentMachine;
        int currentProcedureLength;
        // this.indices.set(currentJob, indices.get(currentJob) + 1);
        currentMachine = technicalMatrix[currentJob][this.indices.get(currentJob) + 1];
        currentProcedureLength = processingMatrix[currentJob][this.indices.get(currentJob) + 1];

        if (this.nextAvailableMachineTime.get(currentMachine) >= this.nextAvailableJobTime.get(currentJob)) {
            potentionalMakespan = this.nextAvailableMachineTime.get(currentMachine) + currentProcedureLength;
        } else {
            potentionalMakespan = this.nextAvailableJobTime.get(currentJob) + currentProcedureLength;
        }
        int newMakespan = potentionalMakespan > this.makespan ? potentionalMakespan: this.makespan;
        
        return newMakespan;
    }
}