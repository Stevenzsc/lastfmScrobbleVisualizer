package com.zsc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        BufferedReader reader;
        ArrayList<String[]> artists = new ArrayList<>();
        HashSet<String> artistNames = new HashSet<>();


        try {
            reader = new BufferedReader(new FileReader(
                    "D:/Projects/dataProcess/test.txt"));
            String line = reader.readLine();
            while (line != null) {
                // System.out.println(line);
                String name = line.split("\\*@\\*")[0].trim();
                String date = line.split("\\*@\\*")[1].trim();
                String[] item = {name, date};
                artists.add(item);
                artistNames.add(name);


                // read next line
                line = reader.readLine();
            }
            reader.close();

            //printAllNames(artistNames);
            System.out.println("Start to generate...");
            ArrayList<ArrayList<String>> artistCount = processArtists(artists);
            ArrayList<String> allName = getAllNames(artistNames);
            ArrayList<ArrayList<String>> allCount = new ArrayList<>();
            boolean flag = false;
            for (int i = 0; i < allName.size(); i++) {
                for (int k = 0; k < artistCount.size(); k++) {
                    ArrayList<String> temp = new ArrayList<>();
                    for (int j = 0; j < artistCount.get(k).size(); j++) {
                        // ArrayList<String> temp = new ArrayList<>();
                        if (artistCount.get(k).get(j).split("=")[0].equals(allName.get(i))) {
                            temp.add(allName.get(i));
                            temp.add(artistCount.get(k).get(j).split("=")[1]);
                            allCount.add(temp);
                            //temp.clear();
                            flag = true;
                        }
                    }
                    if (!flag) {
                        temp.add(allName.get(i));
                        temp.add("0");
                        allCount.add(temp);
                        // temp.clear();
                    }
                    flag = false;
                }


            }
            //allCount
            Integer retain = 0;
            boolean retainFlag = false;
            for (int i = 0; i < allCount.size(); i++) {
                //System.out.print(allCount.get(i).get(1) + " ");
                String crt = allCount.get(i).get(1).trim();
                Integer in = Integer.valueOf(crt);

                //add weight
//                if (i > 0 && i + 1 < allCount.size() && (i + 1) % artistCount.size() != 0) {
//                    //Integer last = Integer.valueOf(allCount.get(i - 1).get(1).trim());
//                    Integer next = Integer.valueOf(allCount.get(i + 1).get(1).trim());
//
//                    //in = Integer.valueOf(crt);
//                    if (in - next > 0) {
//                        in = in - (in - next) / 6;
//                        retain = in;
//                        retainFlag = true;
//                    }
//                    if (in < 0) {
//                        in = 0;
//                    }
//
//                } else {
//                    retainFlag = false;
//                }
//                if (retainFlag) {
//                    in = retain;
//
//                }
                System.out.print(String.format("%6s", String.valueOf(in)));
                if (i != 0 && (i + 1) % artistCount.size() == 0) {
                    System.out.println(" ");
                }
            }
            System.out.println(" ");
            long endTime = System.currentTimeMillis();
            System.out.println("Time spent: " + (endTime - startTime) + "ms");
            System.out.println("done!!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getAllNames(HashSet<String> artistNames) {
        ArrayList<String> an = new ArrayList<>();
        Iterator it = artistNames.iterator();

        while (it.hasNext()) {
            an.add((String) it.next());
        }

        return an;
    }

    private static void printAllNames(HashSet<String> artistNames) {

        Iterator it = artistNames.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
        }


    }

    /**
     * @param artists
     * @return
     */
    private static ArrayList<ArrayList<String>> processArtists(ArrayList<String[]> artists) {
        ArrayList<String[]> dayCount = new ArrayList<>();
        ArrayList<String> dayCountName = new ArrayList<>();
        ArrayList<ArrayList<String[]>> monthCount = new ArrayList<>();
        ArrayList<ArrayList<String>> monthCountName = new ArrayList<>();

        ArrayList<ArrayList<String>> results = new ArrayList<>();
        String currentDate = "no date";
        for (int i = 0; i < artists.size(); i++) {
            if (currentDate.equals(artists.get(i)[1]) || i == 0) {
                dayCount.add(artists.get(i));
            } else {
                monthCount.add(new ArrayList<>(dayCount));
                dayCount.clear();
            }
            currentDate = artists.get(i)[1];
        }
        monthCount.add(new ArrayList<>(dayCount));
        dayCount.clear();

        // transfer data
        currentDate = "no date";
        for (int i = 0; i < artists.size(); i++) {
            if (currentDate.equals(artists.get(i)[1]) || i == 0) {
                dayCountName.add(artists.get(i)[0]);
            } else {
                monthCountName.add(new ArrayList<>(dayCountName));
                dayCountName.clear();
            }
            currentDate = artists.get(i)[1];
        }
        monthCountName.add(new ArrayList<>(dayCountName));
        dayCountName.clear();

        for (ArrayList<String> ss : monthCountName) {
            ArrayList<String> result = new ArrayList<>();
            String[] array = ss.toArray(new String[0]);
            String[] arr = array;
            Arrays.stream(arr)
                    .collect(Collectors.groupingBy(s -> s))
                    .forEach((k, v) -> {
                        result.add(k + "=" + v.size());
                        //System.out.println(k + "==" + v.size());
                    });
            //System.out.println("-----------------");
            results.add(result);
        }
        return results;
    }
}