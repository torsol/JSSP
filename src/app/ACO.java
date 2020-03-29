package app;

import java.util.ArrayList;
import java.util.List;

import models.Ant;
import models.Model;
import utils.IOManager;
import utils.Scheduler;
import utils.Visualizer;
import org.jfree.ui.RefineryUtilities;


public class ACO {
    public static void main(String[] args) throws Exception {
        IOManager manager = new IOManager();
        Model model = new Model();
        manager.parseFile("test_data/1.txt", model);
        model.generateJobObjects();
        Scheduler scheduler = new Scheduler();

        //variables
        int maxIterations = 100;
        int antAmount = 100;
        double alpha = 0.1;
        double beta = 0.1;

        model.setAlpha(alpha);
        model.setbeta(beta);
        model.generatePheromoneMatix();
        
        int iterationCount = 0;
        int shortestLength = Integer.MAX_VALUE;
        List<Integer> bestSolution = null;
        int currentMakespan;

        List<Ant> ants = new ArrayList<Ant>();

        while(iterationCount < maxIterations){

            //initialize ants
            ants = model.generateAnts(antAmount);

            //every ant finds a route
            for(Ant ant: ants){
                ant.findRoute();
                currentMakespan = scheduler.calculateMakespan(ant.scheduledOperations);
                if(currentMakespan<shortestLength){
                    shortestLength=currentMakespan;
                    bestSolution = ant.scheduledOperations;
                }

            }
            iterationCount ++;
        }

        System.out.println(shortestLength);
        System.out.println(bestSolution);
        Visualizer visualizer = new Visualizer("ACO Gantt", bestSolution, shortestLength, 6);
        visualizer.pack();
        RefineryUtilities.centerFrameOnScreen(visualizer);
        visualizer.setVisible(true);
    }
}