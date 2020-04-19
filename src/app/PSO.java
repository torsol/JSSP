package app;

import java.util.List;

import org.jfree.ui.RefineryUtilities;

import models.Bird;
import models.Model;
import utils.IOManager;
import utils.Scheduler;
import utils.Visualizer;

public class PSO {
    public static void main(String[] args) throws Exception {
        IOManager manager = new IOManager();
        Model model = new Model();
        manager.parseFile("test_data/7.txt", model);
        model.generateJobObjects();
        Scheduler scheduler = new Scheduler();

        // variables
        int maxIterations = 1000;
        int birdAmount = 1000;
        double positionLowerBound = 0.0;
        double positionUpperBound = 1.0;
        double velocityLowerBound = -1.0;
        double velocityUpperBound = 1.0;
        double inertia = 0.90;
        double accelerationWeigthLocal = 2.0;
        double accelerationWeightGlobal = 2.0;
        int iterationCount = 0;
        int printEveryIteration = 1;

        boolean earlyStopping = true;
        //int threshold = 62; //1.txt 56  62.72
        //int threshold = 1186; //2.txt 1059  1186.08
        //int threshold = 1429; //3.txt 1276 1429.10
        //int threshold = 1265; //4.txt 1130 1265.60
        //int threshold = 1625; //5.txt 1451 1625.12 (1000/1000/0.9)
        //int threshold = 1927; //6.txt 1721 1927.52  //TODO NOT ACHEIVED: 2220 1.5p
        int threshold = 1094; //7.txt 977 1094.24   //TODO NOT ACHEIVED 1180 1.5p

        // Generate birds
        List<Bird> birds = model.generateBirds(birdAmount);

        int globalShortestLength = Integer.MAX_VALUE;
        List<Integer> globalBestSolution = null;
        List<Double> globalBestPosition = null;

        int birdCurrentMakespan = Integer.MAX_VALUE;
        List<Integer> birdCurrentSolution = null;

        // Initialize the birds
        List<Integer> jobOperationOrder = model.getJobOperationOrder();
        for (Bird bird : birds) {
            bird.initializeBird(jobOperationOrder, positionLowerBound, positionUpperBound, velocityLowerBound,
                    velocityUpperBound);
        }

        while (iterationCount < maxIterations) {

            for (Bird bird : birds) {
               birdCurrentSolution = bird.getOrder();
               birdCurrentMakespan = scheduler.calculateMakespan(birdCurrentSolution);
               if(birdCurrentMakespan <= bird.getBestMakespan()){
                   bird.setBestMakespan(birdCurrentMakespan);
                   bird.setLocalBestSolution();
               } 
               if(birdCurrentMakespan <= globalShortestLength){
                   globalShortestLength = birdCurrentMakespan;
                   globalBestSolution = birdCurrentSolution;
                   globalBestPosition = bird.getLocalBestPosition();
                   System.out.println("Iteration: " + iterationCount + " new global found = " + birdCurrentMakespan);
               }
           }
           // calculate new velocities and positions
           for(Bird bird: birds){
               bird.updatePositionVelocity(globalBestPosition, inertia, accelerationWeigthLocal, accelerationWeightGlobal);
           }
            iterationCount++;


            if(globalShortestLength<=threshold && earlyStopping){
                System.out.println("Stoppped early at iteration " + iterationCount + " with makespan = " + globalShortestLength);
                break;
            }

            if(iterationCount%printEveryIteration==0){
                System.out.println("Iteration: " + iterationCount + "/" + maxIterations);
            }
        }
        Visualizer visualizer = new Visualizer("PSO Gantt", globalBestSolution, globalShortestLength, model.getJobs());
        visualizer.pack();
        RefineryUtilities.centerFrameOnScreen(visualizer);
        visualizer.setVisible(true);

    }
}