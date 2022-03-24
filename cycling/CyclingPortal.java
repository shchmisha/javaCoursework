package cycling;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
        return new int[0];
    }

    @Override
    public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
        return new LocalTime[0];
    }

    @Override
    public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
        return new int[0];
    }

    @Override
    public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
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
        return LocalTime.now();
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
        return new int[0];
    }

    @Override
    public void eraseCyclingPortal() {

    }

    @Override
    public void saveCyclingPortal(String filename) throws IOException {

    }

    @Override
    public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {

    }

    public int getMountainPoints(int stageId, int riderId) {
        Rider rider = riders.get(riderId);
        Stage stage = stages.get(stageId);
        return rider.getMountainPoints(stage);
    }
}
