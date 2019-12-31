package singlejartest;

import java.util.ArrayList;

public class Tools {
        public static class MACrossList {

            static ArrayList<Integer> AL = new ArrayList<Integer>();
            static ArrayList<Integer> BL = new ArrayList<Integer>();


            private static int finalNumber=0;

            /**
             * vrati seznam parametru pro testovani strategie krizeni klouzavych
             * prumeru (ma1:ma2), kombinace parametru pro sma1 a sma2 se neopakuje a to ani
             * invertovane (1:2 a 2:1), a nejsou totozne (5:5)
             *
             * @param param1I sma fast ranges
             * @param param2I sma slow ranges
             * @return list of parameters for ma1 and ma2 in 2d array
             */
            public static ArrayList<ArrayList<Integer>> listOfParameters(int param1I, int param2I){
                finalNumber=0;

                int param1 = param1I;
                int param2 = param2I;
                for (int A = param1I; A <= param2; A++){
                    for (int B = param1I; B <= param2; B++){
                        boolean zapis=true;
                        //            System.out.print("\n\n" + A + " " + B + "\t\t");
                        if (A != B){ // jen kdyz nejsou stejny
                            if (BL.contains(A)){
                                aa: for (int i = 0;i < BL.size();i++){
                                    //                        System.out.print( " i : " + i +"\t");
                                    //                        System.out.println("BL("+i+") - " + BL.get(i) + "   A - " + A + "    /    AL("+i+") - " + AL.get(i) + "   B - " + B);
                                    if ((BL.get(i) == A && AL.get(i) == B) || (BL.get(i) == B && AL.get(i) == A)){
                                        //                            System.out.println("vyskytuje se v seznamu");
                                        zapis = false;
                                        break aa;
                                    }
                                }

                                if (zapis) {
                                    AL.add(A);
                                    BL.add(B);
                                    //                        System.out.println("\t" + AL);
                                    //                        System.out.println("\t" + BL);
                                    finalNumber++;
                                }
                            }else {

                                //                    System.out.println("\tzapis nove hodnoty");
                                AL.add(A);
                                BL.add(B);
                                //                    System.out.println("\t" +AL);
                                //                    System.out.println("\t" +BL);
                                finalNumber++;
                            }
                        }
                        //System.out.print("\ttotozne hodnoty\t\t A " + A + "    B " + B);
                    }
                }
                //    System.out.println(" ");
                System.out.println(AL);
                System.out.println(BL);
                System.out.println("final Number is: " + finalNumber);



                // join two arrays and return two dimensional array of all parameters
                ArrayList<ArrayList<Integer>> finalList = new ArrayList<>(2);
                finalList.add(AL);
                finalList.add(BL);

                return finalList;
            }


            public static int[] getColumn(int[][] array, int index){
                int[] column = new int[array.length];
                for(int i=0; i<column.length; i++){
                    column[i] = array[i][index];
                }
                return column;
            }

            public static int getFinalNumber() {
                return finalNumber;
            }
            public static void main(String[] arg) {
                listOfParameters(5,8);
            }
        }

}
