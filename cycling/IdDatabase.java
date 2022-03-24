package cycling;

import java.io.Serializable;
import java.util.HashMap;

public class IdDatabase implements Serializable {
    HashMap<Integer, Race> races = new HashMap<Integer, Race>();
    HashMap<Integer, Stage> stages = new HashMap<Integer, Stage>();
    HashMap<Integer, Team> teams = new HashMap<Integer, Team>();
    HashMap<Integer, Rider> riders = new HashMap<Integer, Rider>();
    HashMap<Integer, Segment> segments = new HashMap<Integer, Segment>();

    public IdDatabase(HashMap<Integer, Race> races, HashMap<Integer, Stage> stages, HashMap<Integer, Team> teams, HashMap<Integer, Rider> riders, HashMap<Integer, Segment> segments) {
        this.races = races;
        this.stages = stages;
        this.teams = teams;
        this.riders = riders;
        this.segments = segments;
    }
}
