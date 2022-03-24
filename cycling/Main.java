package cycling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) throws InvalidNameException, IllegalNameException, IDNotRecognisedException, InvalidLengthException, InvalidStageStateException, InvalidLocationException, InvalidStageTypeException, DuplicatedResultException, InvalidCheckpointsException {
//        LocalTime first = LocalTime.of(10, 11, 10);
//        LocalTime last = LocalTime.of(10, 20, 10);
//
//        int milliseconds = (int)  first.until(last, ChronoUnit.MILLIS);
//        int hours = milliseconds / (60*60*1000);
//        milliseconds = milliseconds - hours*(60*60*1000);
//        int minutes = milliseconds / (60*1000);
//        milliseconds = milliseconds - minutes*(60*1000);
//        int seconds = milliseconds / 1000;
//        milliseconds = milliseconds - seconds*1000;
//
//        LocalTime elapsedTime = LocalTime.of(hours, minutes, seconds, milliseconds);
//
//
//        System.out.println(elapsedTime);

        CyclingPortal g = new CyclingPortal();
        System.out.println( g.createRace("penis", "hard"));
        LocalDateTime f = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0, 0));
        LocalTime t = LocalTime.of(10, 10, 0);
        LocalTime newt = LocalTime.of(10, 20, 0);
        System.out.println( g.addStageToRace(100, "idk", "good morning", 9.7, f, StageType.FLAT));
        g.createTeam("teamname", "mashallah");
        System.out.println( g.createRider(100, "omar", 1997));
        g.addCategorizedClimbToStage(100, 5.1, SegmentType.C1, 22.1, 5.0);
        g.addIntermediateSprintToStage(100, 90);
        g.registerRiderResultsInStage(100, 100, t, newt);

        for (LocalTime time : g.getRiderResultsInStage(100, 100)) {
            System.out.println(time);
        }


    }
}
