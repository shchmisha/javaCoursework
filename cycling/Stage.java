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
    private StageType stageType;
    private HashMap<Integer, Segment> segmentsMap = new HashMap<Integer, Segment>();
    private ArrayList<Rider> riders =new ArrayList<>();
    StageState stageState;

    private int[] HCPointsArr = new int[] {20, 15, 12, 10, 8, 6, 4, 2};
    private int[] OneCPointsArr = new int[] {10, 8, 6, 4, 2, 1};
    private int[] TwoCPointsArr = new int[] {5, 3, 2, 1};
    private int[] ThreeCPointsArr = new int[] {2, 1};


    public Stage (String stageName, String description, double length, StageType type, LocalDateTime startTime){

        this.name=stageName;
        this.description=description;
        this.length=length;
        this.id= number++;
        this.stageState= StageState.Preparing;
        this.startTime = startTime;
        this.stageType = type;
    }

    public ArrayList<Rider> getStageRiders() {
        return this.riders;
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
            for (int j=0; j<riders.size()-i-1;j++) {
                if (riders.get(j).getElapsedTime(this).compareTo(riders.get(j+1).getElapsedTime(this)) > 0 ) {
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

    public int getRiderRanking(int riderId) {
        ArrayList<Rider> rankings = this.getStageRidersRanking();
        int index = 0;
        for (int i=0; i<rankings.size();i++) {
            if (rankings.get(i).getId() == riderId) {
                index = i;
            }
        }
        return index;
    }

    public StageType getType() {
        return this.stageType;
    }

    public int[] getRiderPoints() {
        int[] points = new int[riders.size()];
        for (int i=0; i<riders.size(); i++) {
            this.getStageRidersRanking().get(i).setSprintPoints(this);
            points[i] = this.getStageRidersRanking().get(i).getRiderPointsForStage(this);
        }
        return points;
    }

    public void setResultsForSegment() {
        for (Segment segment : segmentsMap.values()) {
            ArrayList<Rider> riderRankings = riders;

            for (int i = 0; i < riderRankings.size() - 1; i++) {
                for (int j = 0; j < riderRankings.size() - i - 1; j++) {
                    if (riderRankings.get(j).getSegmentResult(segment).compareTo(riderRankings.get(j + 1).getSegmentResult(segment)) > 0) {
                        Collections.swap(riderRankings, j, j + 1);
                    }
                }
            }

            for (int i = 0; i < riderRankings.size(); i++) {
                switch (segment.type) {
                    case HC:
                        switch (i) {
                            case 0:
                                riderRankings.get(i).setSegmentPoints(segment, HCPointsArr[i]);
                                break;
                            case 1:
                                riderRankings.get(i).setSegmentPoints(segment, HCPointsArr[i]);
                                break;
                            case 2:
                                riderRankings.get(i).setSegmentPoints(segment, HCPointsArr[i]);
                                break;
                            case 3:
                                riderRankings.get(i).setSegmentPoints(segment, HCPointsArr[i]);
                                break;
                            case 4:
                                riderRankings.get(i).setSegmentPoints(segment, HCPointsArr[i]);
                                break;
                            case 5:
                                riderRankings.get(i).setSegmentPoints(segment, HCPointsArr[i]);
                                break;
                            case 6:
                                riderRankings.get(i).setSegmentPoints(segment, HCPointsArr[i]);
                                break;
                            case 7:
                                riderRankings.get(i).setSegmentPoints(segment, HCPointsArr[i]);
                                break;
                            default:
                                riderRankings.get(i).setSegmentPoints(segment, 0);
                        }
                    case C1:
                        switch (i) {
                            case 0:
                                riderRankings.get(i).setSegmentPoints(segment, OneCPointsArr[i]);
                                break;
                            case 1:
                                riderRankings.get(i).setSegmentPoints(segment, OneCPointsArr[i]);
                                break;
                            case 2:
                                riderRankings.get(i).setSegmentPoints(segment, OneCPointsArr[i]);
                                break;
                            case 3:
                                riderRankings.get(i).setSegmentPoints(segment, OneCPointsArr[i]);
                                break;
                            case 4:
                                riderRankings.get(i).setSegmentPoints(segment, OneCPointsArr[i]);
                                break;
                            case 5:
                                riderRankings.get(i).setSegmentPoints(segment, OneCPointsArr[i]);
                                break;
                            default:
                                riderRankings.get(i).setSegmentPoints(segment, 0);
                        }
                    case C2:
                        switch (i) {
                            case 0:
                                riderRankings.get(i).setSegmentPoints(segment, TwoCPointsArr[i]);
                                break;
                            case 1:
                                riderRankings.get(i).setSegmentPoints(segment, TwoCPointsArr[i]);
                                break;
                            case 2:
                                riderRankings.get(i).setSegmentPoints(segment, TwoCPointsArr[i]);
                                break;
                            case 3:
                                riderRankings.get(i).setSegmentPoints(segment, TwoCPointsArr[i]);
                                break;
                            default:
                                riderRankings.get(i).setSegmentPoints(segment, 0);
                        }
                    case C3:
                        switch (i) {
                            case 0:
                                riderRankings.get(i).setSegmentPoints(segment, ThreeCPointsArr[i]);
                                break;
                            case 1:
                                riderRankings.get(i).setSegmentPoints(segment, ThreeCPointsArr[i]);
                                break;
                            default:
                                riderRankings.get(i).setSegmentPoints(segment, 0);
                        }
                    case C4:
                        switch (i) {
                            case 0:
                                riderRankings.get(i).setSegmentPoints(segment, 1);
                                break;
                            default:
                                riderRankings.get(i).setSegmentPoints(segment, 0);
                        }
                }
            }
        }
    }

}
