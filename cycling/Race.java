package cycling;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Race implements Serializable {

    private int id;
    private String name;
    private String description;
    private int totalLength;
    private static int counter=100;
    private static int numberOfStages;
    private HashMap<Integer,Stage> stages= new HashMap<Integer,Stage>();

    public Race(String name, String description) {
        this.id = counter++;
        this.name = name;
        this.description = description;
    }

    /**
     * Get the race name
     *
     * @return race name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the race details
     *
     * @return race details
     */
    public String getDetails() {
        return this.name + " " + this.description;
    }

    public int getNumStages() {
        return stages.size();
    }

    /**
     * Get the riders finished position in a a stage.
     * <p>
     * The state of this MiniCyclingPortalInterface must be unchanged if any
     * exceptions are thrown.
     *
     * @param stage The stage to be stored in the hashmap.
     */
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


    public ArrayList<Stage> getRaceStages() {
        ArrayList<Stage> arr = new ArrayList<>();
        for (Stage stage : stages.values()) {
            arr.add(stage);
        }
        return arr;
    }


}
