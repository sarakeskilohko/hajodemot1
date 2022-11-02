package fi.utu.tech.assignment5;

import java.util.ArrayList;
import java.util.List;

// Käytetään tehtässä 1 muokattua GradingTask-oliota
import fi.utu.tech.common.GradingTask;
// Allokointifunktiot implementoidaan TaskAllocator-luokassa
import fi.utu.tech.common.TaskAllocator;

import fi.utu.tech.common.Submission;
import fi.utu.tech.common.SubmissionGenerator;
import fi.utu.tech.common.SubmissionGenerator.Strategy;


public class App5 {
    public static void main( String[] args )
    {
        // Otetaan funktion aloitusaika talteen suoritusajan laskemista varten
        long startTime = System.currentTimeMillis();

        // Generoidaan kasa esimerkkitehtäväpalautuksia
        List<Submission> ungradedSubmissions = SubmissionGenerator.generateSubmissions(21, 200, Strategy.STATIC);

        // Tulostetaan tiedot esimerkkipalautuksista ennen arviointia
        for (var ug : ungradedSubmissions) {
            System.out.println(ug);
        }

        // Luodaan uusi arviointitehtävä
        GradingTask gradingTask = new GradingTask();
        // Annetaan palautukset gradeAll-metodille ja saadaan arvioidut palautukset takaisin
        // tätä ei saa käyttää : List<Submission> gradedSubmissions =  gradingTask.gradeAll(ungradedSubmissions);
        /*
         * TODO: Muokkaa common-pakkauksen GradingTask-luokkaa siten,
         * että alla oleva run()-metodi (ilman argumentteja!) tarkistaa palautukset (ungradedSubmissions).
         * Yllä olevaa gt.gradeAll()-metodia ei tule enää käyttää suoraan
         * tästä main-metodista. Tarkemmat ohjeet tehtävänannossa.
         * Joudut keksimään, miten GradingTaskille voi antaa tehtävät ja miten ne siltä saa noukittua
         */

        // 1. rivillä asetetaan listaan arvostelemattomat palautukset
        // 2. rivillä ajetaan metodi
        // 3. rivillä luodaan lista arvostelluille palautuksilli, haetaan get jutulla palautukset
        gradingTask.setUngradedSubmissions(ungradedSubmissions);
        // tehdään uusi thread, jossa run ajetaan
        // threadillä kestää kauemmin olla valmis kuin koko ohjelmalla

        // Luodaan uusi arviointitehtävä
        int taskCount = 21;
        List<GradingTask> gt = TaskAllocator.allocate(ungradedSubmissions, taskCount);
        List<Thread> threads = new ArrayList<>();
        for(int i = 0; i < taskCount; i++){
            Thread thread = new Thread(gt.get(i));
            threads.add(thread);
            thread.start();
        }
        for(Thread thread : threads){
            try{
                thread.join();
            } catch(InterruptedException e){
                System.out.println("InterruptedException");
            }
        }
        List<Submission> gradedSubmissions = new ArrayList<>();
        for(GradingTask gt1 : gt){
            gradedSubmissions.addAll(gt1.getGradedSubmissions());
        }

        // Tulostetaan arvioidut palautukset
        System.out.println("------------ CUT HERE ------------");
        for (var gs : gradedSubmissions) {
            System.out.println(gs);
        }

        // Lasketaan funktion suoritusaika
        System.out.printf("Total time for grading: %d ms%n", System.currentTimeMillis()-startTime);
    }
}
