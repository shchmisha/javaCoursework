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
        for (Race i : races.values()) {
            if (i.getName() != name) {
                throw new NameNotRecognisedException("The name " + name + " isn't recognised");
            }
        }
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
        for (int i : races.keySet()) {
            if (i != raceId) {
                throw new IDNotRecognisedException("The id " + raceId + " isn't recognised");
            }
        }
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
//        A list of riders' times sorted by the sum of their adjusted elapsed
//        times in all stages of the race. An empty list if there is no result
//        for any stage in the race. These times should match the riders
        for (int i : races.keySet()) {
            if (i != raceId) {
                throw new IDNotRecognisedException("The id " + raceId + " isn't recognised");
            }
        }
        Race race = races.get(raceId);
        Stage[] raceStages = Arrays.stream(race.getStages()).mapToObj(stageId -> this.stages.get(stageId)).toArray(Stage[]::new);
        ArrayList<Rider> riderRankings = raceStages[0].getStageRidersRanking();
        raceStages[0].getRiderPoints();
        for (int i=0; i<raceStages.length; i++) {
            raceStages[i].calculateAdjustedElapsedTime();
        }
        for (int i=0; i<riderRankings.size()-1; i++) {
            for (int j=0; j<riderRankings.size()-i-1;j++){
                if (riderRankings.get(j).getTotalAdjustedElapsedTime(race).compareTo(riderRankings.get(j+1).getTotalAdjustedElapsedTime(race)) > 0) {
                    Collections.swap(riderRankings, j, j+1);
                }
            }
        }

        LocalTime[] rank = new LocalTime[riderRankings.size()];
        for (int i=0; i<riderRankings.size(); i++) {
            rank[i] = LocalTime.MIDNIGHT.plus(riderRankings.get(i).getTotalElapsedTime(race).toSeconds(), ChronoUnit.SECONDS);
        }


        return rank;
    }

    @Override
    public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
        for (int i : races.keySet()) {
            if (i != raceId) {
                throw new IDNotRecognisedException("The id " + raceId + " isn't recognised");
            }
        }
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
        return rank;
    }

    @Override
    public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
        for (int i : races.keySet()) {
            if (i != raceId) {
                throw new IDNotRecognisedException("The id " + raceId + " isn't recognised");
            }
        }
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
            points.add(rider.getMountainPoints(race));
        }
        int[] mntnpoints = new int[points.size()];
        for (int i=0; i<points.size(); i++) {
            mntnpoints[i] = points.get(i);
        }
//        Stage stage = stages.get(stageId);
//        Rider rider = riders.get(riderId);
//        stage.setResultsForSegment();
//        return stage.getMountainPointsForRider(rider);
        return mntnpoints;
    }

    @Override
    public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
//        A ranked list of riders' IDs sorted descending by the sum of their
//        points in all stages of the race. That is, the first in this list is
//        the winner (more points). An empty list if there is no result for any
//        stage in the race.
        for (int i : races.keySet()) {
            if (i != raceId) {
                throw new IDNotRecognisedException("The id " + raceId + " isn't recognised");
            }
        }
        Race race = races.get(raceId);
        Stage[] raceStages = Arrays.stream(race.getStages()).mapToObj(stageId -> this.stages.get(stageId)).toArray(Stage[]::new);
        raceStages[0].getRiderPoints();
        ArrayList<Rider> raceRiders = raceStages[0].getStageRidersRanking();
        HashMap<Rider, Integer> ridersPoints = new HashMap<>();

        for (Rider rider : raceRiders) {
            int[] riderPoints = getRidersPointsInRace(raceId);
            int total = 0;
            for (int i=0; i<riderPoints.length; i++) {
                total = total + riderPoints[i];
            }
            ridersPoints.put(rider, total);
        }


        for (int i = 0; i < raceRiders.size() - 1; i++) {
            for (int j = 0; j < raceRiders.size() - i - 1; j++) {
                if (ridersPoints.get(raceRiders.get(j)) > ridersPoints.get(raceRiders.get(j+1))) {
                    Collections.swap(raceRiders, j, j + 1);
                }
            }
        }

        Collections.reverse(raceRiders);

        int[] points = new int[raceRiders.size()];
        for (int i=0; i<raceRiders.size(); i++) {
            points[i] = raceRiders.get(i).getId();
        }

        return points;
    }

    @Override
    public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
//        A ranked list of riders' IDs sorted descending by the sum of their
//	      mountain points in all stages of the race. That is, the first in this
//	      list is the winner (more points). An empty list if there is no result
//	      for any stage in the race.
        for (int i : races.keySet()) {
            if (i != raceId) {
                throw new IDNotRecognisedException("The id " + raceId + " isn't recognised");
            }
        }
        Race race = races.get(raceId);
        Stage[] raceStages = Arrays.stream(race.getStages()).mapToObj(stageId -> this.stages.get(stageId)).toArray(Stage[]::new);
        raceStages[0].getRiderPoints();
        ArrayList<Rider> raceRiders = raceStages[0].getStageRidersRanking();
        HashMap<Rider, Integer> ridersPoints = new HashMap<>();

        for (Rider rider : raceRiders) {
            int[] riderPoints = getRidersMountainPointsInRace(raceId);
            int total = 0;
            for (int i=0; i<riderPoints.length; i++) {
                total = total + riderPoints[i];
            }
            ridersPoints.put(rider, total);
        }


        for (int i = 0; i < raceRiders.size() - 1; i++) {
            for (int j = 0; j < raceRiders.size() - i - 1; j++) {
                if (ridersPoints.get(raceRiders.get(j)) > ridersPoints.get(raceRiders.get(j+1))) {
                    Collections.swap(raceRiders, j, j + 1);
                }
            }
        }

        Collections.reverse(raceRiders);

        int[] points = new int[raceRiders.size()];
        for (int i=0; i<raceRiders.size(); i++) {
            points[i] = raceRiders.get(i).getId();
        }

        return points;
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
        for (Race i : races.values()){
            if (i.getName() == name){
                throw new IllegalNameException(name + " is already there");
            }
        }
        if (name.length()<1 | name.length()>30){
            throw new InvalidNameException("Invalid name");
        }
        if (name == null){
            throw new InvalidNameException("Cannot accept null");
        }
        Race race = new Race(name, description);
        races.put(race.getId(), race);
        return race.getId();
    }

    @Override
    public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
        for (int i : races.keySet()) {
            if (i != raceId) {
                throw new IDNotRecognisedException("ID wasn't found");
            }
        }
        Race race = races.get(raceId);
        return race.getDetails();
    }

    @Override
    public void removeRaceById(int raceId) throws IDNotRecognisedException {
        for (int i : races.keySet()) {
            if (i != raceId) {
                throw new IDNotRecognisedException("ID wasn't found");
            }
        }
        races.remove(raceId);
    }

    @Override
    public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
        for (int i : races.keySet()) {
            if (i != raceId) {
                throw new IDNotRecognisedException("ID wasn't found");
            }
        }
        Race race = races.get(raceId);
        return race.getNumStages();
    }

    @Override
    public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime, StageType type) throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
        Race race = races.get(raceId);
        for (int i : races.keySet()) {
            if (i != raceId) {
                throw new IDNotRecognisedException("raceId: " + raceId + " doesn't exist");
            }
        }
        for (Stage n : stages.values()) {
            if (n.getName() == stageName) {
                throw new IllegalNameException(stageName + " is already in the system");
            }
            if (stageName == null | stageName.length() > 30 | stageName.length() < 1)
                ;
            {

                throw new InvalidNameException(stageName + " isn't valid");
            }
        }

        for (Stage l : stages.values()) {
            if (l.getLength() < 5)
                ;
            {
                throw new InvalidLengthException("Invalid length");
            }
        }
        Stage stage = new Stage(stageName, description, length, type, startTime);
        race.addStage(stage);
        stages.put(stage.getStageId(), stage);
        return stage.getStageId();
    }

    @Override
    public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
        for (int i : races.keySet()) {
            if (i != raceId) {
                throw new IDNotRecognisedException("ID wasn't found");
            }
        }
        Race race = races.get(raceId);
        return race.getStages();
    }

    @Override
    public double getStageLength(int stageId) throws IDNotRecognisedException {
        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException("ID wasn't found");
            }
        }
        Stage stage = stages.get(stageId);
        return stage.getLength();
    }

    @Override
    public void removeStageById(int stageId) throws IDNotRecognisedException {
        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException("ID wasn't found");
            }
        }
        stages.remove(stageId);
    }

    @Override
    public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient, Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException(stageId + " doesn't exist");
            }
        }

        for (Stage l : stages.values()) {
            for (Segment i : segments.values()) {
                if (i.getLocation() > l.getLength())
                    ;
                {
                    throw new InvalidLocationException(location + " is out of bounds");
                }
            }

        }
        for (Integer l : stages.keySet()) {
            if (l == stageId) {
                for (Stage i : stages.values()) {
                    if (i.getType() == StageType.TT) {
                        throw new InvalidStageTypeException("Invalid stage type");

                    }
                }
            }
        }
        CategorizedClimb categorizedClimb = new CategorizedClimb(stageId, location, type, averageGradient, length);
        Stage stage = stages.get(stageId);
        if (stage.stageState == StageState.waiting_for_results) {
            throw new InvalidStageStateException("Invalid stage state");
        }
        stage.addCategorizedClimbToStage(categorizedClimb);
        return categorizedClimb.getSegmentId();
    }

    @Override
    public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException(stageId + " doesn't exist");
            }
        }
        for (Stage l : stages.values()) {
            for (Segment i : segments.values()) {
                if (i.getLocation() > l.getLength())
                    ;
                {
                    throw new InvalidLocationException(location + " is out of bounds");
                }
            }
        }
        for (Integer l : stages.keySet()) {
            if (l == stageId) {
                for (Stage i : stages.values()) {
                    if (i.getType() == StageType.TT) {
                        throw new InvalidStageTypeException("Invalid stage type");

                    }
                }
            }
        }
        Stage stage = stages.get(stageId);
        IntermediateSprint intermediateSprint = new IntermediateSprint(stageId, location);
        stage.addIntermediateSprintToStage(intermediateSprint);
        return intermediateSprint.getSegmentId();
    }

    @Override
    public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {
        for (int i : segments.keySet()) {
            if (i != segmentId) {
                throw new IDNotRecognisedException(segmentId + "doesn't exist");
            }
        }

        Segment segment  = segments.get(segmentId);
        Stage stage = stages.get(segment.getStageId());


        this.segments.remove(segmentId);
        stage.removeSegment(segmentId);

    }

    @Override
    public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException(stageId +" doesn't exist");
            }
        }
        Stage stage = stages.get(stageId);
        stage.concludePrepartion();
    }

    @Override
    public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException(stageId + " doesn't exist");
            }
        }
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
        if (name.length()<1 || name.length()>30){
            throw new InvalidNameException("Illegal name: Must be greater than 0 characters and 30 or less");
        }
        if (name == null){
            throw new InvalidNameException("Cannot accept null");
        }
        for (Team i : teams.values()){
            if (i.getName() == name){
                throw new IllegalNameException(name + " already exists");
            }
        }
        Team team = new Team(name, description);
        teams.put(team.getId(), team);
        return team.getId();
    }

    @Override
    public void removeTeam(int teamId) throws IDNotRecognisedException {
        for (int i : teams.keySet()) {
            if (i != teamId) {
                throw new IDNotRecognisedException(teamId + " doesn't exist");
            }
        }
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
        for (int i : teams.keySet()) {
            if (i != teamId) {
                throw new IDNotRecognisedException(teamId + " doesn't exist");
            }
        }
        Team team = teams.get(teamId);
        return team.getRiders();
    }

    @Override
    public int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException, IllegalArgumentException {
        if (name == null | yearOfBirth < 1990) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        for (int i : teams.keySet()) {
            if (i != teamID) {
                throw new IDNotRecognisedException(teamID + " doesn't exist");
            }
        }
        Rider rider = new Rider(teamID, name, yearOfBirth);
        riders.put(rider.getId(), rider);
        return rider.getId();
    }

    @Override
    public void removeRider(int riderId) throws IDNotRecognisedException {
        for (int i : riders.keySet()) {
            if (i != riderId) {
                throw new IDNotRecognisedException(riderId + "doesn't exist");
            }
        }
        riders.remove(riderId);
    }

    @Override
    public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints) throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException, InvalidStageStateException {
        Stage stage =stages.get(stageId);
        Rider rider = riders.get(riderId);
        if (rider.getStageResults().containsKey(stageId)) {
            throw new DuplicatedResultException(riderId + " has a result already");
        }
        if (stages.keySet() == null) {
            throw new IDNotRecognisedException(stageId + " doesn't exist");
        }
        if (stage.stageState != StageState.waiting_for_results) {
            throw new InvalidStageStateException("Invalid stage state");
        }
        if (riders.keySet() == null) {
            throw new IDNotRecognisedException(riderId + " doesn't exist");
        }

        rider.setStageResults(stage, checkpoints);
        stage.setRider(rider);
    }

    @Override
    public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
        LocalTime[] arr = riders.get(riderId).getStageResults(stages.get(stageId));
        if (stages.keySet() == null) {
            throw new IDNotRecognisedException(stageId + " doesn't exist");
        }
        if (riders.keySet() == null) {
            throw new IDNotRecognisedException(riderId + " doesn't exist");
        }
        return arr;
    }

    @Override
    public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
        for (int i : riders.keySet()) {
            if (!this.riders.containsKey(riderId)) {
                throw new IDNotRecognisedException(riderId + " doesn't exist");
            }
        }

        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException(stageId + "doesn't exist");
            }
        }
        Stage stage = stages.get(stageId);
        Rider rider = riders.get(riderId);
        stage.calculateAdjustedElapsedTime();
        return LocalTime.MIDNIGHT.plus(rider.getAdjustedElapsedTime(stage).toSeconds(), ChronoUnit.SECONDS);

    }

    @Override
    public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
        for (int i : riders.keySet()) {
            if (i != riderId) {
                throw new IDNotRecognisedException(riderId + " doesn't exist");
            }
        }
        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException(stageId + " doesn't exist");
            }
        }
        Stage stage = stages.get(stageId);
        Rider rider = riders.get(riderId);
        rider.deleteStageResults(stage);
    }

    @Override
    public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException(stageId + " doesn't exist");
            }
        }
        ArrayList<Rider> stageRiders = stages.get(stageId).getStageRidersRanking();
        int[] rankings = new int[stageRiders.size()];
        for (int i=0; i<stageRiders.size(); i++) {
            rankings[i] = stageRiders.get(i).getId();
        }

        return rankings;
    }

    @Override
    public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException(stageId + " doesn't exist");
            }
        }
        Stage stage = stages.get(stageId);
        ArrayList<Rider> stageRiders = stage.getStageRidersRanking();

        stage.calculateAdjustedElapsedTime();

        for (int i = 0; i < stageRiders.size() - 1; i++) {
            for (int j = 0; j < stageRiders.size() - i - 1; j++) {
                if (stageRiders.get(j).getAdjustedElapsedTime(stage).compareTo(stageRiders.get(j+1).getAdjustedElapsedTime(stage)) > 0) {
                    Collections.swap(stageRiders, j, j + 1);
                }
            }
        }

        LocalTime[] times = new LocalTime[stageRiders.size()];
        for (int i=0; i<stageRiders.size();i++) {
            times[i] = LocalTime.MIDNIGHT.plus(stageRiders.get(i).getAdjustedElapsedTime(stage).toSeconds(), ChronoUnit.SECONDS);
        }
        return times;
    }

    @Override
    public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException(stageId + " doesn't exist");
            }
        }
        Stage stage = stages.get(stageId);
        return stage.getRiderPoints();
    }

    @Override
    public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
        for (int i : stages.keySet()) {
            if (i != stageId) {
                throw new IDNotRecognisedException(stageId + " doesn't exist");
            }
        }
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
