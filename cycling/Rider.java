package cycling;

import java.io.Serializable;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Rider implements Serializable {
    private int teamID;
    String name;
    int yearOfBirth;
    static int count=100;
    private int riderId;
    private HashMap<Stage, Duration> stageResults = new HashMap<>();
    private HashMap<Segment, Duration> segmentResults = new HashMap<>();

    private HashMap<Segment, Integer> segmentPoints = new HashMap<>();

    private HashMap<Stage, Duration> adjustedElapsedTime = new HashMap<>();

    private HashMap<Stage, Integer> stagePoints = new HashMap<>();
    private int[] flatPointsArr = new int[] {50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2};
    private int[] midMntnPointsArr = new int[] {30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
    private int[] highMntnPointsArr = new int[] {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    private int[] timeTrialPointsArr = new int[] {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};



    public Rider(int teamID, String name, int yearOfBirth) throws IllegalArgumentException {
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.riderId = count++;
    }


    public HashMap<Stage, Duration> getStageResults(){
        return this.stageResults;
    }

    public void setAdjustedElapsedTime(Stage stage, Duration time) {
        this.adjustedElapsedTime.put(stage, time);
    }

    public Duration getAdjustedElapsedTime(Stage stage) {
        return this.adjustedElapsedTime.get(stage);
    }

    public int getYearOfBirth() {
        return this.yearOfBirth;
    }

    public int getId() {
        return this.riderId;
    }

    public Duration getSegmentResult(Segment segment) {
        return this.segmentResults.get(segment);
    }

    public int setSegmentPoints(Segment segment, int points) {
        this.segmentPoints.put(segment, points);
        return this.getSegmentPoints(segment);
    }

    public int getRaceGCPoints(Race race) {
        ArrayList<Stage> raceStages = race.getRaceStages();
        int total = 0;
        for (Stage stage : raceStages) {
            total = total + this.stagePoints.get(stage);
            for (Segment segment : stage.getSegments()) {
                total = total + this.segmentPoints.get(segment);
            }
        }
        return total;
    }

    public int getMountainPoints(Race race) {
        ArrayList<Stage> raceStages = race.getRaceStages();
        int total = 0;
        for (Stage stage : raceStages) {
            for (Segment segment : stage.getSegments()) {
                if (segment.type != SegmentType.SPRINT) {
                    total = total + this.segmentPoints.get(segment);
                }
            }
        }
        return total;
    }


    public Duration getTotalElapsedTime(Race race) {
        ArrayList<Stage> raceStages = race.getRaceStages();
        Duration total = Duration.ZERO;
        for (Stage stage : raceStages) {
            total = this.stageResults.get(stage).plus(total);
        }
//        return LocalTime.MIDNIGHT.plus(total.toSeconds(), ChronoUnit.SECONDS);
        return total;
    }

    public Duration getTotalAdjustedElapsedTime(Race race) {
        ArrayList<Stage> raceStages = race.getRaceStages();
        Duration total = Duration.ZERO;
        for (Stage stage : raceStages) {
            total = this.adjustedElapsedTime.get(stage).plus(total);
        }
//        return LocalTime.MIDNIGHT.plus(total.toSeconds(), ChronoUnit.SECONDS);
        return total;
    }

//    public int getRaceGCTimes(Race race) {
//        ArrayList<Stage> raceStages = race.getRaceStages();
//        LocalTime total = 0;
//        for (Stage stage : raceStages) {
//            total = total + this.sprintPoints.get(stage);
//        }
//        return total;
//    }

//    public int getRaceCGPoints(Race race) {
//        return this.raceGCPoints.get(race);
//    }

    public int getSegmentPoints(Segment segment) {
        return this.segmentPoints.get(segment);
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

//    public void setRiderPointsForStage(Stage stage, int points) {
//        this.stagePoints.put(stage, points);
//    }

    public int getRiderPointsForStage(Stage stage) {
        return this.stagePoints.get(stage);
    }

//    public int getPointsInStage(Stage stage) {
//        return this.sprintPoints.get(stage);
//    }

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

    public void setSprintPoints(Stage stage) {
        StageType type = stage.getType();
        int rank = stage.getRiderRanking(this.riderId);
        switch (type) {
            case FLAT:
                stagePoints.put(stage, flatPointsArr[rank]);
                break;
            case MEDIUM_MOUNTAIN:
                stagePoints.put(stage, midMntnPointsArr[rank]);
                break;
            case HIGH_MOUNTAIN:
                stagePoints.put(stage, highMntnPointsArr[rank]);
                break;
            case TT:
                stagePoints.put(stage, timeTrialPointsArr[rank]);
                break;
        }
    }


}
