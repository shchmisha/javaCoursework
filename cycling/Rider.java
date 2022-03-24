package cycling;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Rider {
    private static final long serialVersionUID=1;
    private int teamID;
    String name;
    int yearOfBirth;
    static int count=100;
    private int riderId;
    private HashMap<Stage, Duration> stageResults = new HashMap<>();
    private HashMap<Segment, Duration> segmentResults = new HashMap<>();
    private HashMap<Stage, Integer> stagePoints = new HashMap<>();
//    private HashMap<>



    public Rider(int teamID, String name, int yearOfBirth) throws IllegalArgumentException {
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.riderId = count++;
    }

    public int getYearOfBirth() {
        return this.yearOfBirth;
    }

    public int getId() {
        return this.riderId;
    }

    public int getTeamId() {
        return this.teamID;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return name + teamID + yearOfBirth;
    }

    public void setTeamId(int teamID){
        this.teamID=teamID;
    }

    public void setStageResults(Stage stage, LocalTime... checkpoints) {
        LocalTime first = stage.getStartTime().toLocalTime();
        LocalTime last = checkpoints[checkpoints.length-1];
        Duration result = Duration.between(first, last);
        this.stageResults.put(stage, result);

        ArrayList<Segment> segments = stage.orderSegments();

        ArrayList<Duration> checkpointsDuration = new ArrayList<>();
        LocalTime prev = stage.getStartTime().toLocalTime();

        for (int i=0; i<checkpoints.length; i++) {

            Duration segmentResult = Duration.between(prev, checkpoints[i]);
            checkpointsDuration.add(segmentResult);
            prev = checkpoints[i];
        }

        for (int i=0; i<checkpoints.length; i++) {

            Segment curSegment = segments.get(i);
            this.segmentResults.put(curSegment, checkpointsDuration.get(i));
        }

    }

    public LocalTime[] getStageResults(Stage stage) {
        ArrayList<Segment> segments = stage.getSegments();
        LocalTime[] results = new LocalTime[segments.size()+1];

        for (int i=0; i<segments.size();i++) {
            results[i] = LocalTime.MIDNIGHT.plus(segmentResults.get(segments.get(i)).toSeconds(), ChronoUnit.SECONDS);
        }

        results[segments.size()] = LocalTime.MIDNIGHT.plus(stageResults.get(stage).toSeconds(), ChronoUnit.SECONDS);
        return results;
    }

    public void deleteStageResults(Stage stage) {
        this.stageResults.remove(stage);
    }

    public Duration getElapsedTime(int stageId) {
        return this.stageResults.get(stageId);
    }

    public boolean riderInStage(int stageId) {
        return this.stageResults.containsKey(stageId);
    }

    public void setRiderPointsForStage(Stage stage, int points) {
        this.stagePoints.put(stage, points);
    }

    public int getRiderPointsForStage(Stage stage) {
        return this.stagePoints.get(stage);
    }


}
