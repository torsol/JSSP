package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bird {
    List<Triplet> data;
    List<Double> localBestPosition;
    int localBestMakespan = Integer.MAX_VALUE;

    public void initializeBird(List<Integer> operations, double positionLowerBound, double positionUpperBound,
            double velocityLowerBound, double velocityUpperBound) {
        List<Triplet> data = new ArrayList<Triplet>();

        for (Integer operation : operations) {
            Triplet triplet = new Triplet();
            triplet.setOperationIndex(operation);
            triplet.setPosition((Math.random() * (positionUpperBound - positionLowerBound)) + positionLowerBound);
            triplet.setVelocity((Math.random() * (velocityUpperBound - velocityLowerBound)) + velocityLowerBound);
            data.add(triplet);
        }
        this.data = data;

    }

    public List<Integer> getOrder() {
        List<Integer> operationOrder = new ArrayList<Integer>();
        List<Triplet> copyData = new ArrayList<Triplet>(data);
        Collections.sort(copyData);

        for (Triplet triplet : copyData) {
            operationOrder.add(triplet.getOperationIndex());
        }
        return operationOrder;
    }

    public int getBestMakespan() {
        return this.localBestMakespan;
    }

    public void setLocalBestSolution() {
        List<Double> localBestSolution = new ArrayList<Double>();
        for (Triplet triplet : data) {
            localBestSolution.add(triplet.position);

        }
        this.localBestPosition = localBestSolution;
    }

    public void setBestMakespan(int birdCurrentMakespan) {
        this.localBestMakespan = birdCurrentMakespan;
    }

    public List<Double> getLocalBestPosition() {
        return this.localBestPosition;
    }

    public void updatePositionVelocity(List<Double> globalBestPosition, double inertia, double accelerationWeigthLocal,
            double accelerationWeightGlobal) {
        //
        for (int i = 0; i < globalBestPosition.size(); i++) {
            double currentPosition = data.get(i).position;
            // update velocity
            double velocity = data.get(i).velocity * inertia
                    + accelerationWeigthLocal * Math.random() * (localBestPosition.get(i) - currentPosition)
                    + accelerationWeightGlobal * Math.random() * (globalBestPosition.get(i) - currentPosition);
            data.get(i).position = currentPosition + velocity;
        }
    }

}