package cycling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Stage {
    private int id;
    private String name;
    private String description;
    private double length;
    private Double averageGradient;
    private LocalDateTime startTime;
    private static int number=100;
    private int raceId;
    private static int numberOfStages;
    private HashMap<Integer, Segment> segmentsMap = new HashMap<Integer, Segment>();
    private ArrayList<Rider> riders =new ArrayList<>();
    StageState stageState;

    public Stage (String stageName, String description, double length, StageType type, LocalDateTime startTime){

        this.name=stageName;
        this.description=description;
        this.length=length;
        this.id= number++;
        this.stageState= StageState.Preparing;
        this.startTime = startTime;
    }

    public double getLength() {
        return length;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public int getStageId(){
        return id;
    }

    public void removeSegment(int segmentId) throws IDNotRecognisedException{
        segmentsMap.remove(segmentId);
    }

    public ArrayList<Segment> orderSegments() {
        ArrayList<Segment> segments = this.getSegments();

        for (int i=0; i<segments.size()-1; i++) {
            for (int j=0; j<segments.size()-i-1; j++) {
                if (segments.get(j).getLocation() > segments.get(j+1).getLocation()) {
                    Collections.swap(segments, j, j+1);
                }
            }
        }
        return segments;
    }

    public int addCategorizedClimbToStage(CategorizedClimb newCategorizedClimb){

        segmentsMap.put(newCategorizedClimb.getSegmentId(), newCategorizedClimb);
        newCategorizedClimb.setStageId(id);

        return newCategorizedClimb.getSegmentId();
    }
    public int addIntermediateSprintToStage(IntermediateSprint newIntermediateSprint){
        segmentsMap.put(newIntermediateSprint.getSegmentId(),newIntermediateSprint);
        newIntermediateSprint.setStageId(id);
        return newIntermediateSprint.getSegmentId();
    }

    public int getRaceId(){
        return raceId;
    }

    public void setRaceId(int raceId){
        this.raceId=raceId;
    }

    public void concludePrepartion(){
        this.stageState= StageState.waiting_for_results;

    }

    public void setRider(Rider rider) {
        this.riders.add(rider);
    }

    public ArrayList<Rider> getStageRidersRanking() {
        for (int i=0; i<riders.size()-1; i++ ) {
            for (int j=0; j<riders.size()-i-1;i++) {
                if (riders.get(j).getElapsedTime(this.id).compareTo(riders.get(j+1).getElapsedTime(this.id)) > 0 ) {
                    Collections.swap(riders, j, j+1);
                }
            }
        }
        return this.riders;
    }

    public ArrayList<Segment> getSegments() {
        ArrayList<Segment> arr = new ArrayList<>();

        for (Segment i : segmentsMap.values()) {
            arr.add(i);
        }

        return arr;
    }

}
