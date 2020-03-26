package models;

public class Model{

    int jobs;
    int machines;

    int[][] technicalMatrix;
    int[][] processingMatrix;


	public void setJobs(int jobs) {
        this.jobs = jobs;
	}

	public void setMachines(int machines) {
        this.machines = machines;
	}

	public void setTechnicalMatrix(int[][] technicalMatrix) {
        this.technicalMatrix = technicalMatrix;
	}

	public void setProcessingMatrix(int[][] processingMatrix) {
        this.processingMatrix = processingMatrix;
	}



}