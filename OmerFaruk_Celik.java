// Omer Faruk Celik,2021400084,23.03.2023 this code finds the path for the people who chooses two stations and show them how to go from one
//station to another station using metrolines and visualize them on canvas
import java.util.ArrayList;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
public class OmerFaruk_Celik{

    public static void main(String[] args) throws FileNotFoundException{
        // İncludes all Metro lines names
        ArrayList<String> allMetroLines = new ArrayList();
        // Colors which is used to draw metro lines on canvas
        ArrayList<ArrayList<Integer>> metroLinesColors = new ArrayList<ArrayList<Integer>>(10);
        // All MetroStation names sorted by allMetroLines
        ArrayList<ArrayList<String> > metroStopsNames = new ArrayList<ArrayList<String> >(10);
        // All MetroStation names including stars which will write on canvas their names sorted by allMetroLines
        ArrayList<ArrayList<String> > metroStopsStarsNames = new ArrayList<ArrayList<String> >(10);
        // All MetroStation coordinates which their names including stars which will write on canvas their names  sorted by allMetroLines
        ArrayList<ArrayList<String> > metroStopsStarsCoordinates = new ArrayList<ArrayList<String> >(10);
        // All MetroStation coordinates sorted by allMetroLines
        ArrayList<ArrayList<String>> metroStopsCoordinates = new ArrayList<ArrayList<String>>(10);
        // All breakPoints
        ArrayList<String> breakPoints = new ArrayList();
        // All breakPoints including their breaks
        ArrayList<ArrayList<String>> breakPointsMetro = new ArrayList<ArrayList<String>>(7);
        FileInputStream file = new FileInputStream("coordinates.txt");
        Scanner f = new Scanner(file);
        int i = 0;
        // reading files and filling the lists excluding breakPoints
        while (i!= 10) {
            String[] line1 = f.nextLine().split(" ");
            allMetroLines.add(line1[0]);
            String[] tempLine1 = line1[1].split(",");
            ArrayList<Integer> x1 = new ArrayList<>();
            for (int j = 0;j < 3;j++) {
                x1.add(Integer.parseInt(tempLine1[j]));
            }
            metroLinesColors.add(x1);
            // creating small lists to add large lists
            ArrayList<String> a1 = new ArrayList<String>();
            ArrayList<String> a2 = new ArrayList<String>();
            ArrayList<String> a3 = new ArrayList<String>();
            ArrayList<String> a4 = new ArrayList<String>();
            String[] line2 = f.nextLine().split(" ");
            int m = 0;
            // filling small lists(metroStopsNames and coordinates and also with stars as well)
            while (m != line2.length) {
                if (line2[m].startsWith("*")) {
                    a3.add(line2[m]);
                    a4.add(line2[m+1]);
                    a1.add(line2[m++].substring(1));
                    a2.add(line2[m++]);
                }
                else {
                    a1.add(line2[m++]);
                    a2.add(line2[m++]);
                }
            }
            metroStopsStarsNames.add(a3);
            metroStopsStarsCoordinates.add(a4);
            metroStopsNames.add(a1);
            metroStopsCoordinates.add(a2);
            i++;
        }
        //filling breakPoints
        while (i != 17) {
            String[] line1 = f.nextLine().split(" ");
            breakPoints.add(line1[0]);
            ArrayList a1 = new ArrayList();
            for (int j = 1;j < line1.length;j++) {
                a1.add(line1[j]);
            }
            breakPointsMetro.add(a1);
            i++;
        }
        f.close();


        Scanner input = new Scanner(System.in);
        // taking input from users
        String city1 = input.nextLine();
        String city2 = input.nextLine();

        int city1IndexOf1 = -1;
        int city1IndexOf2 = -1;
        int city2IndexOf1 = -1;
        int city2IndexOf2 = -1;
        // indicating city coordinates in metroStopsNames
        for (int j = 0;j < metroStopsNames.size();j++) {
            if (metroStopsNames.get(j).indexOf(city1) >= 0) {
                city1IndexOf1 = j;
                city1IndexOf2 = metroStopsNames.get(j).indexOf(city1);
            }
            if (metroStopsNames.get(j).indexOf(city2)>= 0) {
                city2IndexOf1 = j;
                city2IndexOf2 = metroStopsNames.get(j).indexOf(city2);
            }

        }
        ArrayList<String> allMetroLines1 = new ArrayList();
        for (int t = 0;t <allMetroLines.size();t++) {
            allMetroLines1.add(allMetroLines.get(t));
        }
        for (int t = 0; t <breakPointsMetro.size();t++) {
            for (int y = 0; y <breakPointsMetro.get(t).size();y++) {
                if (allMetroLines1.contains(breakPointsMetro.get(t).get(y))) {
                    allMetroLines1.remove(breakPointsMetro.get(t).get(y));
                }
            }
        }




        ArrayList<String> solution = new ArrayList(); // our solution list
        // if names is not provided in this map, give message
        if ((city1IndexOf1 == -1)||(city2IndexOf1 == -1)) {
            System.out.println("The station names provided are not present in this map.");
        }

        else if (metroStopsNames.get(allMetroLines.indexOf(allMetroLines1.get(0))).contains(city1) || metroStopsNames.get(allMetroLines.indexOf(allMetroLines1.get(0))).contains(city2)) {
            System.out.println("These two stations are not connected");
        }

        else {

            int counts = 0;
            // indicating current Location by metroStopsNames list
            int currentLocation1 = city1IndexOf1;
            int currentLocation2 = city1IndexOf2;
            // to store breakPoint which we went through
            ArrayList<ArrayList<Integer>> oldBreakPoints = new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> q = new ArrayList<Integer>();
            // for the station which has more than 1 breakpoint
            for (int m = 0; m < 3; m ++) {
                q.add(m);
            }
            int counts5 = 0;
            boolean u = true;
            int counts6 = 0;
            // until come to the same line go through every breakpoints

            while (currentLocation1 != city2IndexOf1) {
                counts5 ++;

                // if it cannot find the target loop will quit
                if (counts5 > 10000) {
                    System.out.println("These two stations are not connected");
                    break;
                }


                // if we pass the last station we start again the first station
                if (currentLocation2 == metroStopsNames.get(currentLocation1).size()) {
                    currentLocation2 = 0;
                }
                // to check is currentLocation breakpoint or not
                if (isBreak(convertToName(currentLocation1,currentLocation2,metroStopsNames),breakPoints)) {
                    // this block to remove lines which is not solution lines oldBreakPoints lists
                    if (oldBreakPoints.size() != 0 ) {

                        if (currentLocation1 == oldBreakPoints.get(oldBreakPoints.size()-1).get(0) &&currentLocation2 == oldBreakPoints.get(oldBreakPoints.size()-1).get(1)){
                            int breakIndex1 = breakPoints.indexOf(metroStopsNames.get(currentLocation1).get(currentLocation2));
                            // if we go through the stations which has more than 1 breakpoint
                            if (counts == 1 && breakIndex1 == 0) {
                                oldBreakPoints.remove(oldBreakPoints.size()-1);
                                ArrayList<Integer> b = new ArrayList<Integer>();
                                int tempLocation1 = currentLocation1;
                                int tempLocation2 = currentLocation2;
                                currentLocation1 = allMetroLines.indexOf(breakPointsMetro.get(0).get(q.get(1)));

                                if (counts == 1 &&tempLocation1 == 0 && currentLocation1 == 1) {
                                    currentLocation2 = metroStopsNames.get(currentLocation1).indexOf(convertToName(tempLocation1,tempLocation2,metroStopsNames));
                                }
                                else {
                                    currentLocation2 = metroStopsNames.get(currentLocation1).indexOf(convertToName(tempLocation1,tempLocation2,metroStopsNames))+1;
                                }
                                b.add(currentLocation1);
                                b.add(currentLocation2);
                                oldBreakPoints.add(b);
                                counts++;
                                if(currentLocation1 == city2IndexOf1) {
                                    break;
                                }
                                continue;

                            }
                            // quit the lines which dont have other breakpoint and restore old location
                            oldBreakPoints.remove(oldBreakPoints.size()-1);
                            currentLocation1 = oldBreakPoints.get(oldBreakPoints.size()-1).get(0);
                            currentLocation2 = oldBreakPoints.get(oldBreakPoints.size()-1).get(1)+1;
                            oldBreakPoints.remove(oldBreakPoints.size()-1);
                            continue;


                        }
                    }

                    int breakIndex = breakPoints.indexOf(metroStopsNames.get(currentLocation1).get(currentLocation2));
                    // if it is station which has more than 1 breakpoint
                    if (breakIndex == 0) {
                        int x = breakPointsMetro.get(0).indexOf(allMetroLines.get(currentLocation1));

                        q.remove(x);

                        if (counts == 0) {
                            ArrayList<Integer> a = new ArrayList<Integer>();
                            ArrayList<Integer> b = new ArrayList<Integer>();
                            int tempLocation1 = currentLocation1;
                            int tempLocation2 = currentLocation2;
                            a.add(tempLocation1);
                            a.add(tempLocation2);
                            currentLocation1 = allMetroLines.indexOf(breakPointsMetro.get(breakIndex).get(q.get(0)));
                            currentLocation2 = metroStopsNames.get(currentLocation1).indexOf(convertToName(tempLocation1,tempLocation2,metroStopsNames));
                            b.add(currentLocation1);
                            b.add(currentLocation2);
                            oldBreakPoints.add(a);

                            oldBreakPoints.add(b);
                            if(currentLocation1 == city2IndexOf1) {
                                break;
                            }
                            counts += 1;
                        }
                        else {
                            ArrayList<Integer> a = new ArrayList<Integer>();
                            ArrayList<Integer> b = new ArrayList<Integer>();
                            int tempLocation1 = currentLocation1;
                            int tempLocation2 = currentLocation2;
                            a.add(tempLocation1);
                            a.add(tempLocation2);
                            currentLocation1 = allMetroLines.indexOf(breakPointsMetro.get(breakIndex).get(0));
                            currentLocation2 = metroStopsNames.get(currentLocation1).indexOf(convertToName(tempLocation1,tempLocation2,metroStopsNames));
                            b.add(currentLocation1);
                            b.add(currentLocation2);
                            oldBreakPoints.add(a);

                            oldBreakPoints.add(b);
                            if(currentLocation1 == city2IndexOf1) {
                                break;
                            }
                        }


                    }
                    // if it is breakstation other than station which has more than 1 breakpoint change the line and rearrange the location which is compatible for new line
                    else {
                        if (allMetroLines.indexOf(allMetroLines.get(currentLocation1)) ==allMetroLines.indexOf(breakPointsMetro.get(breakIndex).get(0))) {
                            ArrayList<Integer> a = new ArrayList<Integer>();
                            ArrayList<Integer> b = new ArrayList<Integer>();
                            int tempLocation1 = currentLocation1;
                            int tempLocation2 = currentLocation2;
                            a.add(tempLocation1);
                            a.add(tempLocation2);
                            currentLocation1 = allMetroLines.indexOf(breakPointsMetro.get(breakIndex).get(1));
                            currentLocation2 = metroStopsNames.get(currentLocation1).indexOf(convertToName(tempLocation1,tempLocation2,metroStopsNames));
                            b.add(currentLocation1);
                            b.add(currentLocation2);
                            oldBreakPoints.add(a);

                            oldBreakPoints.add(b);
                            if(currentLocation1 == city2IndexOf1) {
                                break;
                            }


                        }
                        else {
                            ArrayList<Integer> a = new ArrayList<Integer>();
                            ArrayList<Integer> b = new ArrayList<Integer>();
                            int tempLocation1 = currentLocation1;
                            int tempLocation2 = currentLocation2;
                            a.add(tempLocation1);
                            a.add(tempLocation2);
                            currentLocation1 = allMetroLines.indexOf(breakPointsMetro.get(breakIndex).get(0));
                            currentLocation2 = metroStopsNames.get(currentLocation1).indexOf(convertToName(tempLocation1,tempLocation2,metroStopsNames));
                            b.add(currentLocation1);
                            b.add(currentLocation2);
                            oldBreakPoints.add(a);

                            oldBreakPoints.add(b);
                            if(currentLocation1 == city2IndexOf1) {
                                break;
                            }
                        }

                    }




                }
                // one step one step go through all stations on the line
                currentLocation2++;

            }









            // rearranging the oldBreakPoints
            // and restore currentLocation with oldBreakPoints last element
            // and adding solution lists which we go through because oldBreakPoints have just coordinates
            //we transform coordinates into citynames and add them solution lists
            if (oldBreakPoints.size() != 0) {
                currentLocation1 = oldBreakPoints.get(oldBreakPoints.size()-1).get(0);
                currentLocation2 = oldBreakPoints.get(oldBreakPoints.size()-1).get(1);
                int o = city1IndexOf2;
                ArrayList<Integer> b3 = new ArrayList<Integer>();
                b3.add(city1IndexOf1);
                b3.add(city1IndexOf2);
                oldBreakPoints.add(0,b3);
                oldBreakPoints.remove(oldBreakPoints.size()-1);


                while ( oldBreakPoints.size() != 0) {
                    int a1 = oldBreakPoints.get(0).get(0);
                    int b1 = oldBreakPoints.get(0).get(1);
                    int b2 = oldBreakPoints.get(1).get(1);
                    while ( b1 != b2) {
                        if (b1 > b2) {
                            solution.add(metroStopsNames.get(a1).get(b1));
                            b1--;
                        }
                        if (b1 < b2) {
                            solution.add(metroStopsNames.get(a1).get(b1));
                            b1++;
                        }

                    }
                    oldBreakPoints.remove(0);
                    oldBreakPoints.remove(0);

                }
            }



            // if it is at the same line adding the station we go through until our location is the input city
            while (currentLocation1 == city2IndexOf1 &&currentLocation2 >= 0 && currentLocation2 <= metroStopsNames.get(currentLocation1).size()) {
                if (metroStopsNames.get(currentLocation1).contains(city2)) {
                    int solCod = city2IndexOf2;
                    if (solCod > currentLocation2) {
                        solution.add(metroStopsNames.get(currentLocation1).get(currentLocation2));
                        currentLocation2++;
                    }
                    if (solCod < currentLocation2) {
                        solution.add(metroStopsNames.get(currentLocation1).get(currentLocation2));
                        currentLocation2--;
                    }
                    if (solCod == currentLocation2) {
                        solution.add(metroStopsNames.get(currentLocation1).get(currentLocation2));

                        break;
                    }

                }
                else {

                }
            }
            if (solution.contains(metroStopsNames.get(0).get(14))&& solution.contains(metroStopsNames.get(3).get(1))) {
                if (solution.indexOf(metroStopsNames.get(3).get(1)) -solution.indexOf(metroStopsNames.get(0).get(14))== 1){
                    solution.add(solution.indexOf(metroStopsNames.get(0).get(14))+1,metroStopsNames.get(3).get(0));
                }

            }
            if (solution.contains(metroStopsNames.get(0).get(12)) && solution.contains(metroStopsNames.get(3).get(1))) {
                if (solution.indexOf(metroStopsNames.get(3).get(1)) -solution.indexOf(metroStopsNames.get(0).get(12))== 1){
                    solution.add(solution.indexOf(metroStopsNames.get(0).get(12))+1,metroStopsNames.get(3).get(0));
                }
            }
            if (solution.contains(metroStopsNames.get(1).get(16)) && solution.contains(metroStopsNames.get(3).get(1))){
                if (solution.indexOf(metroStopsNames.get(3).get(1)) -solution.indexOf(metroStopsNames.get(1).get(16))== 1){
                    solution.add(solution.indexOf(metroStopsNames.get(1).get(16))+1,metroStopsNames.get(3).get(0));
                }
            }
            // printing the solution
            for (String a: solution) {
                System.out.println(a);
            }

            // drawing canvas
            StdDraw.setCanvasSize(1024,482);
            StdDraw.setXscale(0,1024);
            StdDraw.setYscale(0,482);
            StdDraw.enableDoubleBuffering();
            // setting background image
            StdDraw.picture(512, 241, "background.jpg");
            // first drawing lines
            for (int j = 0;j < allMetroLines.size();j++) {

                StdDraw.setPenColor(metroLinesColors.get(j).get(0), metroLinesColors.get(j).get(1), metroLinesColors.get(j).get(2));
                StdDraw.setPenRadius(0.012);
                for (int m = 0;m < metroStopsCoordinates.get(j).size()-1;m++) {
                    String[] p1 = metroStopsCoordinates.get(j).get(m).split(",");
                    String[] p2 = metroStopsCoordinates.get(j).get(m+1).split(",");
                    StdDraw.line(Double.parseDouble(p1[0]), Double.parseDouble(p1[1]), Double.parseDouble(p2[0]), Double.parseDouble(p2[1]));

                }
            }
            // drawing stations points
            for (int j = 0;j < allMetroLines.size();j++) {
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.setPenRadius(0.01);
                for (int m = 0;m < metroStopsCoordinates.get(j).size();m++) {
                    String[] p3 = metroStopsCoordinates.get(j).get(m).split(",");
                    StdDraw.point(Double.parseDouble(p3[0]),Double.parseDouble(p3[1]));

                }
            }
            // drawing stations names which starswiths star
            StdDraw.setFont(new Font("Helvetica", Font.BOLD, 8));
            StdDraw.setPenColor(StdDraw.BLACK);
            for (int j = 0;j < allMetroLines.size();j++) {
                for ( int m = 0;m < metroStopsStarsNames.get(j).size();m++) {
                    String[] p4 = metroStopsStarsCoordinates.get(j).get(m).split(",");
                    StdDraw.text(Double.parseDouble(p4[0]), Double.parseDouble(p4[1])+5,metroStopsStarsNames.get(j).get(m).substring(1));
                }
            }
            // to set the orange points which passed we create a second solution list
            ArrayList<String> solution2= new ArrayList<String>();
            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            // setting animations
            for (int j = 0;j <solution.size();j++) {
                solution2.add(solution.get(j));
                // arranging pauseduration
                StdDraw.pause(300);
                // showing canvas because we enable double buffering
                StdDraw.show();
                // to do animations every loop we should clear the canvas and draw again
                StdDraw.clear();
                StdDraw.picture(512, 241, "background.jpg");
                // they are the sames as we did before on top
                for (int z = 0;z < allMetroLines.size();z++) {
                    StdDraw.setPenColor(metroLinesColors.get(z).get(0), metroLinesColors.get(z).get(1), metroLinesColors.get(z).get(2));
                    StdDraw.setPenRadius(0.012);
                    for (int m = 0;m < metroStopsCoordinates.get(z).size()-1;m++) {
                        String[] p1 = metroStopsCoordinates.get(z).get(m).split(",");
                        String[] p2 = metroStopsCoordinates.get(z).get(m+1).split(",");
                        StdDraw.line(Double.parseDouble(p1[0]), Double.parseDouble(p1[1]), Double.parseDouble(p2[0]), Double.parseDouble(p2[1]));

                    }
                }
                for (int z = 0;z < allMetroLines.size();z++) {
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.setPenRadius(0.01);
                    for (int m = 0;m < metroStopsCoordinates.get(z).size();m++) {
                        String[] p3 = metroStopsCoordinates.get(z).get(m).split(",");
                        StdDraw.point(Double.parseDouble(p3[0]),Double.parseDouble(p3[1]));

                    }
                }
                StdDraw.setFont(new Font("Helvetica", Font.BOLD, 8));
                StdDraw.setPenColor(StdDraw.BLACK);
                for (int z = 0;z < allMetroLines.size();z++) {
                    for ( int m = 0;m < metroStopsStarsNames.get(z).size();m++) {
                        String[] p4 = metroStopsStarsCoordinates.get(z).get(m).split(",");
                        StdDraw.text(Double.parseDouble(p4[0]), Double.parseDouble(p4[1])+5,metroStopsStarsNames.get(z).get(m).substring(1));
                    }
                }
                // to draw orange points which is passed
                for ( int g = 0; g< solution2.size()-1;g++) {
                    String[] p6 =giveCoordinates(solution2.get(g),metroStopsNames,metroStopsCoordinates).split(",");
                    StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                    StdDraw.point(Double.parseDouble(p6[0]),Double.parseDouble(p6[1]));
                }
                // at the first to start with orange points
                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                String[] p11 =giveCoordinates(solution.get(j),metroStopsNames,metroStopsCoordinates).split(",");
                StdDraw.setPenRadius(0.02);
                StdDraw.point(Double.parseDouble(p11[0]), Double.parseDouble(p11[1]));
                // showing the canvas
                StdDraw.show();


            }
            //Finish



















        }



































    }















    // giving name return coordinates
    public static String giveCoordinates(String name,ArrayList<ArrayList<String>> allMetro,ArrayList<ArrayList<String>> allCoordinates ){
        int a = -1;
        int b = -1;
        for (int i = 0;i < allMetro.size();i++) {
            for (int j = 0; j < allMetro.get(i).size();j++) {
                if (allMetro.get(i).get(j) == name) {
                    a = i;
                    b = j;
                }
            }
        }
        return allCoordinates.get(a).get(b);
    }

    // giving coordinates return names
    public static String convertToName(int a, int b,ArrayList<ArrayList<String>> metroNames){
        return metroNames.get(a).get(b);
    }

    // determining is it a breakpoint
    public static boolean isBreak(String city,ArrayList<String> myArray) {
        if (myArray.contains(city)) {
            return true;
        }
        else
            return false;
    }


    private static ArrayList<String> ArrayList() {
        // TODO Auto-generated method stub
        return null;
    }




}