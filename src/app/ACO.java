package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

import models.Ant;
import models.Model;
import utils.IOManager;
import utils.Scheduler;
import utils.Visualizer;

public class ACO {
    public static void main(String[] args) throws Exception {
        IOManager manager = new IOManager();
        Model model = new Model();
        manager.parseFile("test_data/1.txt", model);
        model.generateJobObjects();
        Scheduler scheduler = new Scheduler();

        // variables
        int maxIterations = 10000;
        int antAmount = 30;
        double alpha = 0.1;
        double beta = 0.5;
        double pheromoneInitialValue = 0.5;
        double evaporation = 0.005;

        model.setAlpha(alpha);
        model.setbeta(beta);
        model.generatePheromoneMatix(pheromoneInitialValue);

        int iterationCount = 0;
        int globalShortestLength = Integer.MAX_VALUE;
        List<Integer> globalBestSolution = null;

        int localShortestLength = Integer.MAX_VALUE;
        List<Integer> localBestSolution = null;

        int antCurrentMakespan;

        List<Ant> ants = new ArrayList<Ant>();

        while (iterationCount < maxIterations) {

            localShortestLength = Integer.MAX_VALUE;
            localBestSolution = null;
            // initialize ants
            ants = model.generateAnts(antAmount);

            // every ant finds a route
            for (Ant ant : ants) {
                ant.findRoute();
                antCurrentMakespan = scheduler.calculateMakespan(ant.scheduledOperations);
                if (antCurrentMakespan < globalShortestLength) {
                    globalShortestLength = antCurrentMakespan;
                    globalBestSolution = ant.scheduledOperations;
                    System.out.println("Iteration " + iterationCount + " new global found = " + antCurrentMakespan);
                }
                if (antCurrentMakespan < localShortestLength) {
                    localShortestLength = antCurrentMakespan;
                    localBestSolution = ant.scheduledOperations;
                }
            }

            updatePheromoneMatrix(localBestSolution, localShortestLength, globalShortestLength, model, evaporation);

            //System.out.println("Iteration: " + iterationCount + "/" + maxIterations);
            iterationCount++;

        }

        System.out.println(globalShortestLength);
        System.out.println(globalBestSolution);
        //System.out.println(Arrays.deepToString(model.getPheromoneMatix()));
        Visualizer visualizer = new Visualizer("ACO Gantt", globalBestSolution, globalShortestLength, 6);
        visualizer.pack();
        RefineryUtilities.centerFrameOnScreen(visualizer);
        visualizer.setVisible(true);
    }

    public static void updatePheromoneMatrix(List<Integer> bestSolution, int makespan, int globalMakespan,
            Model model, double p) {
        double[][] pheromoneMatrix = model.getPheromoneMatix();
        for (int i = 0; i < pheromoneMatrix.length; i++) {
            for (int j = 0; j < pheromoneMatrix[0].length; j++) {
                pheromoneMatrix[i][j] = (1-p)*pheromoneMatrix[i][j];
            }

        }
        // We always start in the zero-node
        int currentIndex = 0;
        int nextIndex;

        List<Integer> indices = new ArrayList<Integer>();
        for(int i=0; i<Model.technicalMatrix.length; i++){
            indices.add(-1);
        }

        for (int currentJob: bestSolution){
           indices.set(currentJob, indices.get(currentJob)+1);
           nextIndex = Ant.getIndexOnRowCol(currentJob, indices.get(currentJob));
           pheromoneMatrix[currentIndex][nextIndex] = pheromoneMatrix[currentIndex][nextIndex] + 1/(double) makespan;
           currentIndex = nextIndex;
        }
    }
}