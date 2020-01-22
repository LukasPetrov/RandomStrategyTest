package singlejartest;

import java.util.ArrayList;

public class Tools {
    public static class SLTPGenerator {

        static ArrayList<Integer> AL = new ArrayList<Integer>();
        static ArrayList<Integer> BL = new ArrayList<Integer>();


        private static int numOfStrategies =0;

        /**
         * vrati seznam parametru pro SL a TP
         * @return list of parameters for SL and TP in 2d array
         */
        public static ArrayList<ArrayList<Integer>> listOfParameters(){
            numOfStrategies =0;

            int param1 = 10;
            int param2 = 5;
            for (int A = 1; A <= param1; A++){
                for (int B = 1; B <= param2; B++){

                    AL.add(A);
                    BL.add(B * A);

                    numOfStrategies++;
                }
            }
            //    System.out.println(" ");
            System.out.println(AL);
            System.out.println(BL);
            System.out.println("final Number is: " + numOfStrategies);

            // join two arrays and return two dimensional array of all parameters
            ArrayList<ArrayList<Integer>> finalList = new ArrayList<>(2);
            finalList.add(AL);
            finalList.add(BL);

            return finalList;
        }

        public static int getNumOfStrategies() {
            return numOfStrategies;
        }

        // tester
        public static void main(String[] arg) {
            listOfParameters();
        }
    }

}
