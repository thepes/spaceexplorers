package org.copains.spaceexplorer.backend.tools;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 05/07/2016.
 */

public class NameGenerator {

    private static String first[] = {"Loup", "Lion", "Rat", "Tigre", "Faucon", "Dragon", "Chat", "Souris"};
    private static String last[] = {"Sauvage","Rebele","Gris","Violent","Invincible","Timide","Geant"};

    public static final String generateName() {
        Random rnd = new Random(Calendar.getInstance().getTimeInMillis());
        int start = rnd.nextInt(first.length);
        int end = rnd.nextInt(last.length);
        StringBuilder sb = new StringBuilder();
        sb.append(first[start]).append(last[end]);
        for (int i = 0 ; i < 5 ; i++) {
            int rndNum = rnd.nextInt(10);
            sb.append(rndNum);
        }
        return sb.toString();
    }

}
