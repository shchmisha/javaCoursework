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
//        System.out.println(riders);
//        System.out.println(segmentsMap.values());
        int[] HCPointsArr = new int[] {20, 15, 12, 10, 8, 6, 4, 2};

        int[] OneCPointsArr = new int[] {10, 8, 6, 4, 2, 1};
        int[] TwoCPointsArr = new int[] {5, 3, 2, 1};
        int[] ThreeCPointsArr = new int[] {2, 1};
//        for (int i=0; i<HCPointsArr.length; i++) {
//            System.out.println(HCPointsArr[i]);
//        }


        for (Segment segment : segmentsMap.values()) {
//                System.out.println(riders);
                ArrayList<Rider> segmentRiderRankings = new ArrayList<>(riders);


                for (int i = 0; i < segmentRiderRankings.size() - 1; i++) {
                    for (int j = 0; j < segmentRiderRankings.size() - i - 1; j++) {
                        if (segmentRiderRankings.get(j).getSegmentResult(segment).compareTo(segmentRiderRankings.get(j + 1).getSegmentResult(segment)) > 0) {
                            Collections.swap(segmentRiderRankings, j, j + 1);
                        }
                    }
                }
                System.out.println(segmentRiderRankings);
//                int count = 0;
                for (Rider rider : segmentRiderRankings) {
//                    rider.setSegmentPoints(segment, count++);
                    int i = segmentRiderRankings.indexOf(rider);
//                    System.out.println(i);
//                    System.out.println(OneCPointsArr[i]);
//                    System.out.println(TwoCPointsArr[i]);
                    switch (segment.type) {
                        case HC:
                            if (i<8) {

                                rider.setSegmentPoints(segment, HCPointsArr[i]);
                            }
                            else {
                                rider.setSegmentPoints(segment, 0);
                            }
                            break;
                        case C1:
                            if (i<6) {

                                rider.setSegmentPoints(segment, OneCPointsArr[i]);
                            }
                            else {
                                rider.setSegmentPoints(segment, 0);
                            }
                            break;
                        case C2:
                            if (i<4) {
                                rider.setSegmentPoints(segment, TwoCPointsArr[i]);
                            }
                            else {
                                rider.setSegmentPoints(segment, 0);
                            }
                            break;
                        case C3:
                            if (i<2) {
                                rider.setSegmentPoints(segment, ThreeCPointsArr[i]);
                            }
                            else {
                                rider.setSegmentPoints(segment, 0);
                            }
                            break;
                        case C4:
                            if (i == 0) {
                                rider.setSegmentPoints(segment, 1);
                            } else {
                                rider.setSegmentPoints(segment, 0);
                            }
                            break;
                    }
                }
        }

        for (Rider rider :riders) {
            for (Segment segment : segmentsMap.values()) {
                System.out.println(rider.getSegmentPoints(segment) + ", " + rider.getName());
            }
        }

    }

    public int getMountainPointsForRider(Rider rider) {
        int total = 0;
        int count = 0;
        for (Segment segment: segmentsMap.values()) {
            total = total + rider.getSegmentPoints(segment);
        }
        return total;
    }

}
