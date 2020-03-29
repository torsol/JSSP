package app;

import java.util.ArrayList;
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
        int maxIterations = 100;
        int antAmount = 100;
        double alpha = 0.1;
        double beta = 0.1;
        double pheromoneInitialValue = 0.5;
        double evaporation = 0.1;

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
                }
                if (antCurrentMakespan < localShortestLength) {
                    localShortestLength = antCurrentMakespan;
                    localBestSolution = ant.scheduledOperations;
                }
            }

            updatePheromoneMatrix(localBestSolution, localShortestLength, globalShortestLength, model, evaporation);

            iterationCount++;
        }

        System.out.println(globalShortestLength);
        System.out.println(globalBestSolution);
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

    }
}