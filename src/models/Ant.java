package models;

import java.util.ArrayList;
import java.util.List;

public class Ant {

    double[][] pheromoneMatrix;
    public List<Integer> scheduledOperations = new ArrayList<Integer>();
    List<Job> jobs;
    double alpha;
    double beta;

    // initial start node
    Node current;

    public Ant(double[][] pheromoneMatrix, List<Job> jobs, double alpha, double beta) {
        this.pheromoneMatrix = pheromoneMatrix;
        this.jobs = jobs;
        this.alpha = alpha;
        this.beta = beta;
        this.current = new Node(-1, -1, -1, -1);
    }

    public void findRoute() {
        // get reachable nodes
        List<Node> availableNodes = getAvailableNodes();

        while (availableNodes.size() > 0) {
            // calculate probability
            calculateProbability(current, availableNodes);
            // pick node based on probability
            Node chosen = pickNode(availableNodes);

            scheduledOperations.add(chosen.job);
            jobs.get(chosen.job).advance();

            this.current = chosen;
            availableNodes = getAvailableNodes();
        }
    }

    public List<Node> getAvailableNodes() {
        List<Node> availableNodes = new ArrayList<Node>();
        for (Job job : this.jobs) {
            if (job.hasNextNode()) {
                availableNodes.add(job.getNextNode());
            }
        }
        return availableNodes;
    }

    public void calculateProbability(Node current, List<Node> availableNodes) {
        double sum = 0;
        for (Node next : availableNodes) {
            sum += calculateProbability(current, next);
        }
        for (Node next : availableNodes) {
            next.addProbability(calculateProbability(current, next) / sum);
        }
    }

    public double calculateProbability(Node current, Node next) {

        // get pheromone for current edge
        int currentIndex = getIndexOnRowCol(current.job, current.index);
        int nextIndex = getIndexOnRowCol(next.job, next.index);
        double pheromone = pheromoneMatrix[currentIndex][nextIndex];

        // get duration for the next node
        double duration = (double) next.duration;

        return Math.pow(pheromone, alpha) * Math.pow(1 / duration, beta);
    }

    public static int getIndexOnRowCol(int job, int operation) {
        if (job == -1)
            return 0;
        return (job * (Model.processingMatrix[0].length) + operation) + 1;
    }

    public Node pickNode(List<Node> nodes) {
        double p = Math.random();
        double cummulativeProbability = 0.0;
        for (Node node : nodes) {
            cummulativeProbability += node.probability;
            if (p <= cummulativeProbability) {
                return node;
            }
        }

        // If not selected (somehow) just return the first one
        return nodes.get(0);
    }

}