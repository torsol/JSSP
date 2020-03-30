package app;

import models.Model;
import utils.IOManager;
import utils.Scheduler;

public class PSO {
    public static void main(String[] args) throws Exception {
        IOManager manager = new IOManager();
        Model model = new Model();
        manager.parseFile("test_data/4.txt", model);
        model.generateJobObjects();
        Scheduler scheduler = new Scheduler();
    }
}