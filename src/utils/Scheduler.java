package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.time.SimpleTimePeriod;

import models.Model;

public class Scheduler {

    static int[][] technicalMatrix = Model.technicalMatrix;
    static int[][] processingMatrix = Model.processingMatrix;


    public int calculateMakespan(List<Integer> operationOrder){
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
        return Collections.max(currentJobLength);
    }

    public int calculateGanttSchedule(List<Integer> operationOrder){
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

    static TaskSeries createDataseriesFromSchedule(List<Integer> scheduledOperations, int makespan) {

        TaskSeries series1 = new TaskSeries("Schedule");
        // Loops over every machine in the schedule
        for (int i = 1; i < Model.technicalMatrix[0].length + 1; i++) {
            Task machine = new Task("Machine " + i, new SimpleTimePeriod(0, makespan));
            series1.add(machine);
        }

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
        for(int i=0; i<scheduledOperations.size(); i++){

            currentJob = scheduledOperations.get(i);
            indices.set(currentJob, indices.get(currentJob)+1);
            currentMachine = technicalMatrix[currentJob][indices.get(currentJob)];
            currentProcedureLength = processingMatrix[currentJob][indices.get(currentJob)];

            if(nextAvailableMachineTime.get(currentMachine) >= currentJobLength.get(currentJob)){
                series1.get(currentMachine).addSubtask(new Task(""+currentJob, new SimpleTimePeriod(nextAvailableMachineTime.get(currentMachine), nextAvailableMachineTime.get(currentMachine)+currentProcedureLength)));
                //printTask(new Task(""+currentJob, new SimpleTimePeriod(nextAvailableMachineTime.get(currentMachine), nextAvailableMachineTime.get(currentMachine)+currentProcedureLength)), currentJob, currentMachine);
                nextAvailableMachineTime.set(currentMachine, nextAvailableMachineTime.get(currentMachine)+currentProcedureLength);
                currentJobLength.set(currentJob, nextAvailableMachineTime.get(currentMachine)+currentProcedureLength);
            }
            else{
                series1.get(currentMachine).addSubtask(new Task(""+currentJob, new SimpleTimePeriod(currentJobLength.get(currentJob), currentJobLength.get(currentJob)+currentProcedureLength)));
                //printTask(new Task(""+currentJob, new SimpleTimePeriod(nextAvailableMachineTime.get(currentMachine), nextAvailableMachineTime.get(currentMachine)+currentProcedureLength)), currentJob, currentMachine);
                nextAvailableMachineTime.set(currentMachine, currentJobLength.get(currentJob)+currentProcedureLength);
                currentJobLength.set(currentJob, currentJobLength.get(currentJob)+currentProcedureLength);
            }


        }
        return series1;
    }

    public static void printTask(Task task, int currentJob, int currentMachine){
        System.out.println("[Job:Machine]" + "[" + currentJob + ":" + currentMachine + "]" + "[Start:End]" + "[" + task.getDuration().getStart().getTime() + ":" + task.getDuration().getEnd().getTime() + "]");
    }





}

