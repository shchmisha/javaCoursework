package cycling;

import java.io.Serializable;
import java.util.HashMap;

public class Team implements Serializable {
    String name;
    String description;
    private int teamID;
    static int n = 100;
    HashMap<Integer, Rider> riders = new HashMap<Integer, Rider>();

    public Team(String name, String description){
        this.name = name;
        this.description = description;
        this.teamID = n++;

    }

    public String getName() {
        return this.name;
    }



    public int getId() {
        return this.teamID;
    }

    public int[] getRiders() {
        int[] teamRiders = new int[this.riders.size()];
        int counter = 0;
        for (int id: riders.keySet()) {
            teamRiders[counter] = id;
            counter++;
        }
        return teamRiders;
    }
}
