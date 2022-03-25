package cycling;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class CyclingPortal implements CyclingPortalInterface {

    private HashMap<Integer, Race> races = new HashMap<Integer, Race>();
    private HashMap<Integer, Stage> stages = new HashMap<Integer, Stage>();
    private HashMap<Integer, Team> teams = new HashMap<Integer, Team>();
    private HashMap<Integer, Rider> riders = new HashMap<Integer, Rider>();
    private HashMap<Integer, Segment> segments = new HashMap<Integer, Segment>();

    @Override
    public void removeRaceByName(String name) throws NameNotRecognisedException {
        for (Race race : races.values()) {
            if (race.getName() == name) {
                races.remove(race.getId());
            }
        }
    }

    @Override
    public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
//        A ranked list of riders' IDs sorted ascending by the sum of their
//	         adjusted elapsed times in all stages of the race. That is, the first
//	         in this list is the winner (least time). An empty list if there is no
//	         result for any stage in the race.
        Race race = races.get(raceId);
        Stage[] raceStages = Arrays.stream(race.getStages()).mapToObj(stageId -> this.stages.get(stageId)).toArray(Stage[]::new);

        ArrayList<Rider> riderRankings = raceStages[0].getStageRidersRanking();
        raceStages[0].getRiderPoints();
        for (int i=0; i<riderRankings.size()-1; i++) {
            for (int j=0; j<riderRankings.size()-i-1;j++){
                if (riderRankings.get(j).getRaceGCPoints(race) > riderRankings.get(j+1).getRaceGCPoints(race)) {
                    Collections.swap(riderRankings, j, j+1);
                }
            }
        }
        int[] rank = new int[riderRankings.size()];
        for (int i=0; i<riderRankings.size(); i++) {
            rank[i] = riderRankings.get(i).getId();
        }



        return rank;
    }

    @Override
    public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {

        return new LocalTime[0];
    }

    @Override
    public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
//        A list of riders' points (i.e., the sum of their points in all stages
//        of the race), sorted by the total elapsed time. An empty list if
//	      there is no result for any stage in the race. These points should
//        match the riders returned by {@link #getRidersGeneralClassificationRank(int)}.

        Race race = races.get(raceId);
        Stage[] raceStages = Arrays.stream(race.getStages()).mapToObj(stageId -> this.stages.get(stageId)).toArray(Stage[]::new);
        raceStages[0].getRiderPoints();

        ArrayList<Rider> riderRankings = raceStages[0].getStageRidersRanking();

        for (int i = 0; i < riderRankings.size() - 1; i++) {
            for (int j = 0; j < riderRankings.size() - i - 1; j++) {
                if (riderRankings.get(j).getTotalElapsedTime(race).compareTo(riderRankings.get(j).getTotalElapsedTime(race)) > 0) {
                    Collections.swap(riderRankings, j, j + 1);
                }
            }
        }

        ArrayList<Integer> points = new ArrayList<>();
        for (Rider rider : riderRankings) {
            points.add(rider.getRaceGCPoints(race));
        }
        int[] rank = new int[points.size()];
        for (int i=0; i<points.size(); i++) {
            rank[i] = points.get(i);
        }
//        array of riders total scores
//        sort the array
//        reverse the array
//        return the array
        return new int[0];
    }

    @Override
    public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
        Race race = races.get(raceId);

//        Stage stage = stages.get(stageId);
//        Rider rider = riders.get(riderId);
//        stage.setResultsForSegment();
//        return stage.getMountainPointsForRider(rider);
        return new int[0];
    }

    @Override
    public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
        return new int[0];
    }

    @Override
    public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
        return new int[0];
    }

    @Override
    public int[] getRaceIds() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i: races.keySet()) {
            list.add(i);
        }

        int[] raceIds = list.stream().mapToInt(i -> i).toArray();
        return raceIds;
    }

    @Override
    public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {

        Race race = new Race(name, description);
        races.put(race.getId(), race);
        return race.getId();
    }

    @Override
    public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
        Race race = races.get(raceId);
        return race.getDetails();
    }

    @Override
    public void removeRaceById(int raceId) throws IDNotRecognisedException {
        races.remove(raceId);
    }

    @Override
    public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
        Race race = races.get(raceId);
        return race.getNumStages();
    }

    @Override
    public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime, StageType type) throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
        Race race = races.get(raceId);
        Stage stage = new Stage(stageName, description, length, type, startTime);
        race.addStage(stage);
        stages.put(stage.getStageId(), stage);
        return stage.getStageId();
    }

    @Override
    public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
        Race race = races.get(raceId);
        return race.getStages();
    }

    @Override
    public double getStageLength(int stageId) throws IDNotRecognisedException {
        Stage stage = stages.get(stageId);
        return stage.getLength();
    }

    @Override
    public void removeStageById(int stageId) throws IDNotRecognisedException {
        stages.remove(stageId);
    }

    @Override
    public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient, Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
        CategorizedClimb categorizedClimb = new CategorizedClimb(stageId, location, type, averageGradient, length);
        Stage stage = stages.get(stageId);
        stage.addCategorizedClimbToStage(categorizedClimb);
        return categorizedClimb.getSegmentId();
    }

    @Override
    public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
        Stage stage = stages.get(stageId);
        IntermediateSprint intermediateSprint = new IntermediateSprint(stageId, location);
        stage.addIntermediateSprintToStage(intermediateSprint);
        return intermediateSprint.getSegmentId();
    }

    @Override
    public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {

        Segment segment  = segments.get(segmentId);
        Stage stage = stages.get(segment.getStageId());


        this.segments.remove(segmentId);
        stage.removeSegment(segmentId);

    }

    @Override
    public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
        Stage stage = stages.get(stageId);
        stage.concludePrepartion();
    }

    @Override
    public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
        Stage stage = stages.get(stageId);
        ArrayList<Segment> stageSegments = stage.getSegments();
        int[] segmentIds = new int[stageSegments.size()];
        for (int i = 0; i<stageSegments.size(); i++) {
            segmentIds[i] = stageSegments.get(i).getSegmentId();
        }
        return segmentIds;
    }

    @Override
    public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
        Team team = new Team(name, description);
        teams.put(team.getId(), team);
        return team.getId();
    }

    @Override
    public void removeTeam(int teamId) throws IDNotRecognisedException {
        this.teams.remove(teamId);
    }

    @Override
    public int[] getTeams() {
        ArrayList<Integer> teamIds = new ArrayList<>();
        for(int id : teams.keySet()) {
            teamIds.add(id);
        }
        int[] intIds = new int[teamIds.size()];
        for (int i=0; i<teamIds.size(); i++){
            intIds[i] = teamIds.get(i);
        }
        return intIds;
    }

    @Override
    public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
        Team team = teams.get(teamId);
        return team.getRiders();
    }

    @Override
    public int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException, IllegalArgumentException {
        Rider rider = new Rider(teamID, name, yearOfBirth);
        riders.put(rider.getId(), rider);
        return rider.getId();
    }

    @Override
    public void removeRider(int riderId) throws IDNotRecognisedException {
        riders.remove(riderId);
    }

    @Override
    public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints) throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException, InvalidStageStateException {
        Stage stage =stages.get(stageId);
        Rider rider = riders.get(riderId);

        rider.setStageResults(stage, checkpoints);
        stage.setRider(rider);


//        add points to rider by the stage type and the place in the rank
    }

    @Override
    public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
        LocalTime[] arr = riders.get(riderId).getStageResults(stages.get(stageId));

        return arr;
    }

    @Override
    public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
        Stage stage = stages.get(stageId);
        Rider rider = riders.get(riderId);
        stage.calculateAdjustedElapsedTime();
        return LocalTime.MIDNIGHT.plus(rider.getAdjustedElapsedTime(stage).toSeconds(), ChronoUnit.SECONDS);

    }

    @Override
    public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
        Stage stage = stages.get(stageId);
        Rider rider = riders.get(riderId);
        rider.deleteStageResults(stage);
    }

    @Override
    public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
        ArrayList<Rider> stageRiders = stages.get(stageId).getStageRidersRanking();
        int[] rankings = new int[stageRiders.size()];
        for (int i=0; i<stageRiders.size(); i++) {
            rankings[i] = stageRiders.get(i).getId();
        }

        return rankings;
    }

    @Override
    public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
        return new LocalTime[0];
    }

    @Override
    public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
        Stage stage = stages.get(stageId);
        return stage.getRiderPoints();
    }

    @Override
    public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
        Stage stage = stages.get(stageId);
        ArrayList<Integer> mountainPoints = new ArrayList<>();
        stage.setResultsForSegment();
        for (Rider rider : stage.getStageRidersRanking()) {
            mountainPoints.add(stage.getMountainPointsForRider(rider));
        }
        int[] points = new int[mountainPoints.size()];
        for (int i=0; i<mountainPoints.size(); i++){
            points[i] = mountainPoints.get(i);
        }
        return points;
    }

    @Override
    public void eraseCyclingPortal() {
        // clear all the hashMaps int all classes
        segments.clear();
        stages.clear();
        teams.clear();
        riders.clear();
        races.clear();
    }

    @Override
    public void saveCyclingPortal(String filename) throws IOException {
        IdDatabase idDatabase = new IdDatabase(races, stages, teams, riders, segments);

        FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(idDatabase);
        out.close();
        fileOut.close();
//        copy all  hashmaps to the id database
//        serialize this id database
    }

    @Override
    public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
//        deserialize idDatabase
//        set hashmaps to the equivalent hashmaps of the id database
        IdDatabase idDatabase = null;

        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        idDatabase = (IdDatabase) in.readObject();
        in.close();
        fileIn.close();

        this.races = idDatabase.races;
        this.stages = idDatabase.stages;
        this.riders = idDatabase.riders;
        this.teams = idDatabase.teams;
        this.segments = idDatabase.segments;

    }
//    public int getMountainPoints(int stageId, int riderId) {
//        Stage stage = stages.get(stageId);
//        Rider rider = riders.get(riderId);
//        stage.setResultsForSegment();
//        return stage.getMountainPointsForRider(rider);
////        for (Rider rider : stage.getStageRiders()) {
////            System.out.println(stage.getSegmentPointsForRider(rider) + ", " + rider.getId());
////        }
//
//
//
//    }
}
