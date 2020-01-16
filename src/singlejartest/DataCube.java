package singlejartest;

import com.dukascopy.api.IOrder;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.Period;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create "DataCube" which is 3D field of array lists
 */
public class DataCube {
    private static ArrayList<Object> firstLevel = new ArrayList<>();

    private static int StrategyNameIndex;
    private static int param1Index;
    private static int param2Index;
    private static int finalDepositIndex;
    private static int dailyEquityIndex;
    private static int dateFromIndex;
    private static int dateToIndex;
    private static int inputMa_1Index;
    private static int inputMa_2Index;
    private static int inputInstrumentIndex;
    private static int inputPeriodIndex;
    private static int inputOpeningDepositIndex;
    private static int bestResultsIndex;
    private static int ordersIndex;

    public static void createDataCube(){
        // int represents location at firstLevel ArrayList
        StrategyNameIndex = 0;      addData1D(new ArrayList<String>());                // 0 Strategy name
        param1Index = 1;            addData1D(new ArrayList<Integer>());               // 1 param 1
        param2Index = 2;            addData1D(new ArrayList<Integer>());               // 2 param 2
        finalDepositIndex = 3;      addData1D(new ArrayList<Double>());                // 3 final deposit
        dailyEquityIndex = 4;      addData1D(new ArrayList<ArrayList<Double>>());     // 4 daily equity
        dateFromIndex = 5;              addData1D(new Date());          // 5 date from
        dateToIndex = 6;                addData1D(new Date());          // 6 date to
        inputMa_1Index = 7;            addData1D(new short[0]);        // 7 ma_1 from user input
        inputMa_2Index = 8;            addData1D(new short[0]);        // 8 ma_2 from user input
        inputInstrumentIndex = 9;      addData1D(new Object());        // 9 instrument from user input
        inputPeriodIndex = 10;          addData1D(new Object());        // 10 period from user input
        inputOpeningDepositIndex = 11;  addData1D(new Object());        // 11 opening deposit from user input
        bestResultsIndex = 12;          addData1D(new ArrayList<Integer>());     // 12 list of the best strategies
        ordersIndex = 13;               addData1D(new ArrayList<ArrayList<IOrder>>());     // 13 list of all orders
    }

    public static void addStrategyName(String name){
        addData2D(StrategyNameIndex, name);
    }
    public static void addParam1(int param){
        addData2D(param1Index, param);
    }
    public static void addParam2(int param){
        addData2D(param2Index, param);
    }
    public static void addFinalDeposit(Double deposit){
        addData2D(finalDepositIndex, deposit);
    }
    public static void addDailyEquity(int strategyNumber, Double balance){
        addData3D(dailyEquityIndex, strategyNumber, balance);
    }
    public static void addBestResultsIndex(int param){
        addData2D(bestResultsIndex, param);
    }
    public static void addOrder(int strategy, IOrder order){
        addData3D(ordersIndex, strategy, order);
    }

    public static void setOrder(int strategy, IOrder order) {
        addData3D(ordersIndex, strategy, order);
    }
    public static void setDateFrom(Date dateFrom){
        setData1D(dateFromIndex, dateFrom);
    }
    public static void setDateTo(Date dateTo){
        setData1D(dateToIndex, dateTo);
    }
    public static void setMa_1(short ma_1){
        setData1D(inputMa_1Index, ma_1);
    }
    public static void setMa_2(short ma_2){
        setData1D(inputMa_2Index, ma_2);
    }
    public static void setInstrument(Instrument instrument){
        setData1D(inputInstrumentIndex, instrument);
    }
    public static void setPeriod(Period period){
        setData1D(inputPeriodIndex, period);
    }
    public static void setOpeningDeposit(int openingDeposit){
        setData1D(inputOpeningDepositIndex, openingDeposit);
    }

    public static String getStrategyName(int strategyIndex){
        return getData(StrategyNameIndex, strategyIndex);
    }
    public static Object getStrategyName(){
        return getData(StrategyNameIndex);
    }
    public static int getParam1(int strategyIndex){
        return getData(param1Index, strategyIndex);
    }
    public static int getParam2(int strategyIndex){
        return getData(param2Index, strategyIndex);
    }
    public static Double getFinalDeposit(int strategyIndex){
        return getData(finalDepositIndex, strategyIndex);
    }
    public static Object getFinalDepositList(){
        return getData(finalDepositIndex);
    }
    public static Double getDailyEquity(int strategyIndex, int dayIndex){
        return (Double) getData(dailyEquityIndex, strategyIndex, dayIndex);
    }
    public static List getDailyEquity(int strategy){
        return (List) getData(dailyEquityIndex, strategy);
    }
    public static List getDailyEquity(){
        return (List) getData(dailyEquityIndex);
    }
    public static Date getDateFrom(){
        return (Date) getData(dateFromIndex);
    }
    public static Date  getDateTo(){
        return (Date) getData(dateToIndex);
    }
    public static short  getMa_1(){
        return (short) getData(inputMa_1Index);
    }
    public static short  getMa_2(){
        return (short) getData(inputMa_2Index);
    }
    public static Instrument  getInstrument(){
        return (Instrument) getData(inputInstrumentIndex);
    }
    public static Period  getPeriod(){
        return (Period) getData(inputPeriodIndex);
    }
    public static Object  getOpeningDeposit(){
        return  getData(inputOpeningDepositIndex);
    }
    public static int getBestResults(int strategyIndex){
        return getData(bestResultsIndex, strategyIndex);
    }
    public static Object getBestResults(){
        return getData(bestResultsIndex);
    }
    public static ArrayList<IOrder> getOrders(int strategy){
        return (ArrayList<IOrder>) getData(ordersIndex, strategy);
    }

    /**
     * Return the value from position x, y, z.
     *
     * @param  x  1st Dimension
     * @param  y  2nd Dimension
     * @param  z  3th Dimension
     */
    private static <T> T getData(int x, int y, int z){

        // load first level
        ArrayList<ArrayList<T>> secondLevel = (ArrayList<ArrayList<T>>) firstLevel.get(x);

        // load second list
        ArrayList<T> thirdLevel = secondLevel.get(y);

        // load item
        T value = thirdLevel.get(z);

        return value;
    }

    /**
     * Return the value from position x, y.
     *
     * @param  x  1st Dimension
     * @param  y  2nd Dimension
     */
    private static <T> T getData(int x, int y){

        // load first level
        ArrayList<T> secondLevel = (ArrayList<T>) firstLevel.get(x);

        // load second list
        T value = secondLevel.get(y);

        return value;
    }


    /**
     * Return Object from position x.
     *
     * @param  x  1st Dimension
     */
    private static Object  getData(int x){

        // load second value
        Object value = (Object) firstLevel.get(x);

        return value;
    }


    /**
     * Add the value to position x, y.
     *
     * @param  x  1st Dimension
     * @param  y  2nd Dimension
     * @param  value add this value
     */
    private static void addData3D(int x, int y, Object value){
        //try if list exists if not (error) create new and then add new value
        try{
            // load first level
            ArrayList<Object> secondLevel = (ArrayList<Object>) firstLevel.get(x);

            // load second list

            ArrayList<Object> thirdLevel = (ArrayList<Object>) secondLevel.get(y);
            thirdLevel.add(value);

            // overwrite original list
            secondLevel.set(y, thirdLevel);
            firstLevel.set(x, secondLevel);
        }catch(Exception e){
            // create new list to solve the error
            addData2D(x, new ArrayList<Integer>());

            // load first level
            ArrayList<Object> secondLevel = (ArrayList<Object>) firstLevel.get(x);

            // load second list

            ArrayList<Object> thirdLevel = (ArrayList<Object>) secondLevel.get(y);
            thirdLevel.add(value);

            // overwrite original list
            secondLevel.set(y, thirdLevel);
            firstLevel.set(x, secondLevel);
        }
    }


    /**
     * Add the value to position x.
     *
     * @param  x  1st Dimension
     * @param  value add this value
     */
    private static void addData2D(int x, Object value){

        // load first level
        ArrayList<Object> secondLevel = (ArrayList<Object>) firstLevel.get(x);

        // load second list
        secondLevel.add(value);

        // overwrite original list

        firstLevel.set(x, secondLevel);
    }

    /**
     * Add new list.
     *
     * @param  list add this list
     */
    private static void addData1D(Object list){
        firstLevel.add(list);
    }


    /**
     * Set/Replace list to x.
     *
     * @param  x 1st Dimension
     * @param  list add this list
     */
    private static void setData1D(int x, Object list){
        firstLevel.set(x, list);
    }

    /**
     * Set/Replace value in the list in the 3th Dimension to x.
     *
     * @param  x 1st Dimension
     * @param  y 2nd Dimension
     * @param  z 3th Dimension
     * @param  value add this list
     */
    private static void setData3D(int x, int y, int z, Object value){
        //try if list exists if not (error) create new and then add new value
        try{
            // load first level
            ArrayList<Object> secondLevel = (ArrayList<Object>) firstLevel.get(x);

            // load second list
            ArrayList<Object> thirdLevel = (ArrayList<Object>) secondLevel.get(y);
            thirdLevel.set(z, value);

            // overwrite original list
            secondLevel.set(y, thirdLevel);
            firstLevel.set(x, secondLevel);
        }catch(Exception e){
            // create new list to solve the error
            addData2D(x, new ArrayList<Integer>());

            // load first level
            ArrayList<Object> secondLevel = (ArrayList<Object>) firstLevel.get(x);

            // load second list
            ArrayList<Object> thirdLevel = (ArrayList<Object>) secondLevel.get(y);
            thirdLevel.set(z, value);

            // overwrite original list
            secondLevel.set(y, thirdLevel);
            firstLevel.set(x, secondLevel);
        }
    }
}
