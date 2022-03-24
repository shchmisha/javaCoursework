package cycling;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Segment implements Serializable {
    public int stageId;
    public int segmentId;
    public Double location;
    public SegmentType type;
    public Double averageGradient;
    public Double length;
    public int[] results;
    public static int count = 100;

    public abstract int getSegmentId();

    public abstract double getLocation();

    public abstract int[] getResults();

    public abstract int getStageId();

}

class CategorizedClimb extends Segment{

    public CategorizedClimb(int stageId, Double location, SegmentType type, Double averageGradient, Double length) {
        this.stageId = stageId;
        this.location = location;
        this.type = type;
        this.averageGradient = averageGradient;
        this.length = length;
        segmentId = count++;
    }


    public String toString() {
        return stageId + " " + location + " " + type + " " + averageGradient + " " + length;
    }

    public String getDetails() {
        return stageId + " " + location + " " + type + " " + averageGradient + " " + length;
    }

    @Override
    public int getSegmentId() {
        return segmentId;
    }

    @Override
    public double getLocation() {
        return location;
    }

    @Override
    public int[] getResults() {
        return results;
    }

    @Override
    public int getStageId() {
        return stageId;
    }
    public void setStageId(int stageId){
        this.stageId = stageId;
    }


}

class IntermediateSprint extends Segment {

    public IntermediateSprint(int stageId, Double location) {
        this.stageId = stageId;
        this.location = location;
        this.type = SegmentType.SPRINT;
        segmentId = count++;
    }

    public String toString() {
        return stageId + " " + location;
    }

    public String getDetails() {
        return stageId + " " + location;
    }

    @Override
    public int getSegmentId() {
        return segmentId;
    }

    @Override
    public double getLocation() {
        return location;
    }

    @Override
    public int[] getResults() {
        return results;
    }

    @Override
    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId){
        this.stageId = stageId;
    }

}
