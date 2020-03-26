package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import models.Model;

public class IOManager{

    public void parseFile(String fileName, Model model) throws FileNotFoundException {

        Scanner scan = new Scanner(new File(fileName));
        Scanner stringScanner;

        //Scan the first line of the input file to obtain the jobs and machines
        stringScanner = new Scanner(scan.nextLine());
        int jobs = stringScanner.nextInt();
        int machines = stringScanner.nextInt();
        model.setJobs(jobs);
        model.setMachines(machines);

        // Technical matrix consists of machine order of each job
        int[][] technicalMatrix = new int[jobs][machines];

        // Processing time matrix for each equivalent job/machine combo
        int[][] processingMatrix = new int[jobs][machines]; 

        for(int i=0; i<jobs; i++){
            stringScanner = new Scanner(scan.nextLine());
            for(int j=0; j<machines*2; j++){
                if(j%2==0){
                    technicalMatrix[i][j/2] = stringScanner.nextInt();
                }
                else{
                    processingMatrix[i][j/2] = stringScanner.nextInt();
                }
            }
        }
        model.setTechnicalMatrix(technicalMatrix);
        model.setProcessingMatrix(processingMatrix);
        scan.close();
    }
}