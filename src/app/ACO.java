package app;

import models.Model;
import utils.IOManager;

public class ACO {
    public static void main(String[] args) throws Exception {
        IOManager manager = new IOManager();
        Model model = new Model();
        manager.parseFile("test_data/1.txt", model);
    }
}