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

    private HashMap<Stage, Integer> sprintPoints = new HashMap<>();
    private int[] flatPointsArr = new int[] {50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2};
    private int[] midMntnPointsArr = new int[] {30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
    private int[] highMntnPointsArr = new int[] {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    private int[] timeTrialPointsArr = new int[] {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    private int[] intSprintPointsArr = new int[] {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};



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

    public void deleteStageResults(Stage stage) {
        this.stageResults.remove(stage);
    }

    public Duration getElapsedTime(Stage stage) {
        return this.stageResults.get(stage);
    }

    public boolean riderInStage(int stageId) {
        return this.stageResults.containsKey(stageId);
    }

    public void setRiderPointsForStage(Stage stage, int points) {
        this.stagePoints.put(stage, points);
    }

    public int getRiderPointsForStage(Stage stage) {
        return this.sprintPoints.get(stage);
    }

    public int getPointsInStage(Stage stage) {
        return this.sprintPoints.get(stage);
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
//        this.setSprintPoints(stage);
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

    public void setSprintPoints(Stage stage) {
        StageType type = stage.getType();
        int rank = stage.getRiderRanking(this.riderId);
        switch (type) {
            case FLAT:
                sprintPoints.put(stage, flatPointsArr[rank]);
                break;
            case MEDIUM_MOUNTAIN:
                sprintPoints.put(stage, midMntnPointsArr[rank]);
                break;
            case HIGH_MOUNTAIN:
                sprintPoints.put(stage, highMntnPointsArr[rank]);
                break;
            case TT:
                sprintPoints.put(stage, timeTrialPointsArr[rank]);
                break;
        }
    }


}
