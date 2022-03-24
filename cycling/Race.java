package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Race {

    private int id;
    private String name;
    private String description;

    private int[] teams;
    private int[] riders;
    private int[] GCResults;
    private int[] pointsResults;
    private int[] mountainResults;
    private int totalLength;
    private static int counter=100;
    private static int numberOfStages;
    private HashMap<Integer,Stage> stages= new HashMap<Integer,Stage>();

    public Race(String name, String description) {
        this.id = counter++;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDetails() {
        return this.name + " " + this.description;
    }

    public int getNumStages() {
        return stages.size();
    }

    public void addStage(Stage stage) {
        stages.put(stage.getStageId(), stage);
        stage.setRaceId(id);
    }
    public int getId() { return this.id; }

    public int[] getStages(){
        ArrayList<Integer> arr= new ArrayList<Integer>();
        for (int i: stages.keySet()){
            arr.add(i);
        }
        int[] stageIds = arr.stream().mapToInt(i -> i).toArray();

        return stageIds;
    }


}
