/*
 * Copyright (c) 2017 Dukascopy (Suisse) SA. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Dukascopy (Suisse) SA or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. DUKASCOPY (SUISSE) SA ("DUKASCOPY")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL DUKASCOPY OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF DUKASCOPY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package singlejartest;

import com.dukascopy.api.*;
import com.dukascopy.api.IEngine.OrderCommand;
import com.dukascopy.api.IIndicators.AppliedPrice;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Strategy implements IStrategy {
    private IEngine engine;
    private IConsole console;
    private IHistory history;
    private IContext context;
    private IChart chart;
    private static IIndicators indicators;
    private static IAccount account;
    private int counter = 0;
    private static int orderCounter = 0;
    private static OfferSide myOfferSide = OfferSide.BID;

    private static Instrument instrument = Instrument.EURUSD;
    private static Period period = Period.ONE_MIN;
    private Filter indicatorFilter = Filter.ALL_FLATS;
    public double amount = 0.001;
    public int stopLossPips = 50;
    public int takeProfitPips = 50;
    public int breakEvenPips = 25;
    private int smaTimePeriod_1;
    private int smaTimePeriod_2;
    private String strategyName;
    private int strategyNumber;


    boolean time0_5_ready = true;
    boolean time5_10_ready = true;
    boolean time10_15_ready = true;
    boolean time15_20_ready = true;
    boolean time20_24_ready = true;

    int time0_5 = getRandomNumber(0 * 60,5 * 60);
    int time5_10 = getRandomNumber(5 * 60,10 * 60);
    int time10_15 = getRandomNumber(10 * 60,15 * 60);
    int time15_20 = getRandomNumber(15 * 60,20 * 60);
    int time20_24 = getRandomNumber(20 * 60,24 * 60);

    boolean GUITest;
    public Strategy(int stopLossPips, int takeProfitPips, boolean GUITest){
        // set sl and tp in pips
        this.stopLossPips = stopLossPips * 10;
        this.takeProfitPips = takeProfitPips * 10;

        // create strategy name
        strategyName = stopLossPips*10 + "/" + takeProfitPips*10;
        DataCube.addStrategyName(strategyName);

        //get strategy number
        strategyNumber =0;                  /**--------------------------------------------------------------------*/

        this.GUITest = GUITest;
        //  for GUI testing DataCube is not available
        if (!GUITest) {
            Strategy.period = DataCube.getPeriod();
            instrument = DataCube.getInstrument();
        }

        // reset orderCounter for new test
        orderCounter = 0;


        //create OrdersWindow
//        new GUI.OrdersWindow();




    }

    @Override
    public void onStart(IContext context) throws JFException {
        this.engine = context.getEngine();
        this.console = context.getConsole();
        this.history = context.getHistory();
        this.context = context;
        this.account = context.getAccount();
        this.indicators = context.getIndicators();

        //  for GUI testing DataCube is not available
        if (!GUITest) {
            period = DataCube.getPeriod();
        }


        // check if chart is open if doesn't print error message if is open add indicators
        chart = context.getLastActiveChart();
        if (chart == null) {
            console.getErr().println("No chart opened!");
            return;
        }
        chart = context.getChart(instrument);



    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
        // filter right instrument and period
        if (!instrument.equals(instrument) || !period.equals(Strategy.period))
            return;

        newOrderLogic2(instrument, askBar);
        //setBreakEvent();
        storeEquity(instrument);
        //trailPosition(order, history.getLastTick(instrument));








//        List<IOrder> list = DataCube.getOrders(strategyNumber);
//            ArrayList<String[]> newDataForOrderstWindow = new ArrayList<>();
//
//        for (IOrder order : list) {
//            // Data to be displayed in the JTable
//            newDataForOrderstWindow.add(new String[]{
//                    String.valueOf(order.getLabel()),
//                    String.valueOf(order.getOrderCommand()),
//                    String.valueOf(order.getAmount()),
//                    String.valueOf(order.getProfitLossInAccountCurrency()),
//                    String.valueOf(order.getProfitLossInPips()),
//                    String.valueOf(order.getCommissionInUSD())
//            });
//
//        }


//        GUI.OrdersWindow.model.addRow(new Object[]{"Column 1", "Column 2", "Column 3"});

//        refreshOrdersWindow();
    }

    public void refreshOrdersWindow(){


        // remove all rows from the model
        GUI.OrdersWindow.clear(GUI.OrdersWindow.model);


        if (orderCounter > 0) {
            List<IOrder> list = DataCube.getOrders(strategyNumber);
            if (list.size() > 0) {
                // create list
                ArrayList<String[]> newDataForOrderstWindow = new ArrayList<>();

                // add all orders to the String[][]
                for (IOrder order : list) {
                    // Data to be displayed in the JTable
                    newDataForOrderstWindow.add(new String[]{
                            String.valueOf(order.getLabel()),
                            String.valueOf(order.getOrderCommand()),
                            String.valueOf(order.getAmount()),
                            String.valueOf(order.getProfitLossInAccountCurrency()),
                            String.valueOf(order.getProfitLossInPips()),
                            String.valueOf(order.getCommissionInUSD())
                    });

                }

                // convert arrayList to String[][]
                String[][] array = new String[newDataForOrderstWindow.size()][];
                for (int i = 0; i < newDataForOrderstWindow.size(); i++) {
                    String[] row = newDataForOrderstWindow.get(i);
                    array[i] = row;
                }

                System.out.println(array[0][0].toString()
                        + array[0][1].toString()
                        + array[0][2].toString()
                        + array[0][3].toString()
                        + array[0][4].toString()
                        + array[0][5].toString());

                for (String[] a : array) {

                    // fill model by new newDataForOrderstWindow
                    GUI.OrdersWindow.model.addRow(new Object[]{a[0], a[1], a[2], a[3], a[4], a[5]});
                }
            }
        }
    }


    @Override
    public void onStop() throws JFException {
        // close all orders
        for (IOrder order : engine.getOrders()) {
            engine.getOrder(order.getLabel()).close();
            //Data.addOrder(TestMainRepeater.getLoopCounter(), order);
        }

        // store final deposit
        DataCube.addFinalDeposit(account.getEquity());

        // show graph after last test
        if (Tools.SLTPGenerator.getNumOfStrategies()-1 == TestMainRepeater.getLoopCounter()) {

            /** get list of certain number of the best strategies */
            // create list of best results
            Map<Double, Integer> strategies = new HashMap<>();
            DataCube.getFinalDepositList();

            ArrayList<Double> list = (ArrayList<Double>)DataCube.getFinalDepositList();
            for(int i = 0; i < list.size(); i++){
                strategies.put(DataCube.getFinalDeposit(i), i);
            }


            // sort list
            SortedMap<Double, Integer> sortedCache = new TreeMap<>(Collections.reverseOrder());
            sortedCache.putAll(strategies);

            // store sorted list of best strategies to the Data
            ArrayList<Integer> list2 =  new ArrayList<>(sortedCache.values());
            for(int i = 0; i < sortedCache.size(); i++){
                DataCube.addBestResultsIndex(list2.get(i));
            }

            strategies.forEach((x,y) -> System.out.println(x + "  " + y));
            System.out.println("---------------------------------- SORTED ---------------------------------");
            //print sorted list
            ArrayList<Integer> list3 = (ArrayList<Integer>) DataCube.getBestResults();
            list3.forEach((x) -> System.out.println(x+1 + " " + DataCube.getFinalDeposit(x) ));


//            GUI.ShowChart.showGraph();

        }

        //get correct number of valid orders
        int numOfOrdersProfitLoss = 0;
        int profitOrders = 0;
        int lossOrders = 0;
        double biggestProfit = 0;
        double biggestLoss = 0;
        double avrgCommission=0;

        for (IOrder order: DataCube.getOrders(TestMainRepeater.getLoopCounter())) {
            if (order.getState().name() == "CLOSED" || order.getState().name() == "FILLED"){
                numOfOrdersProfitLoss++;
                avrgCommission += order.getCommissionInUSD();
            }
            if (order.getProfitLossInUSD() < 0) {
                lossOrders++;
                if (order.getProfitLossInUSD() < biggestLoss)
                    biggestLoss = order.getProfitLossInUSD();
            }
            if (order.getProfitLossInUSD() > 0) {
                profitOrders++;
                if (order.getProfitLossInUSD() > biggestProfit)
                    biggestProfit = order.getProfitLossInUSD();
            }
        }

        /** --------------------------------------------------------------------------------------------------------*/

        // get order duration
        double longerOrder = 0;
        double shorterOrder = 0;
        double avrgDuration=0;

//        new Date(DataCube.getOrders(0).get(2).getFillTime())

        for (IOrder order: DataCube.getOrders(TestMainRepeater.getLoopCounter())) {
            long duration = 0;
            if (order.getState().name() == "CLOSED"){

                duration = getDuration(order.getFillTime(), order.getCloseTime());

                /** --------------------------------------------------------------------------------------------------------------------MOC VYSOKY CISLA ???----------------------------------------------------*/
//                avrgDuration += duration;


                if (duration < shorterOrder) {
                    shorterOrder = duration;
                }else if (duration > longerOrder) {
                    longerOrder = duration;
                }
            }

        }

        shorterOrder = shorterOrder / 60 /60;
        longerOrder = longerOrder / 60 /60;

        /** --------------------------------------------------------------------------------------------------------*/


        DecimalFormat df = new DecimalFormat("#.##");
        String successRate = df.format((double)profitOrders/((double)numOfOrdersProfitLoss/100));
        String shorterOrderS = df.format(shorterOrder);
        String longerOrderS = df.format(longerOrder);
        avrgCommission = Double.parseDouble(df.format(avrgCommission/numOfOrdersProfitLoss));

        TestMainRepeater.printToConsoleTextArea(
                "\n" + DataCube.getStrategyName(TestMainRepeater.getLoopCounter()) +
                        "\t" + getEquity() +
                        "\t" + successRate + "%" +
                        "\t" + numOfOrdersProfitLoss +
                        "\t" + avrgCommission +
                        "\t" + biggestLoss + "/" + biggestProfit +
                        "\t\t" + shorterOrderS + "/" + longerOrderS
        );
    }

    private long getDuration(long startTime, long closeTime) {

        // get open time
        SimpleDateFormat newFormat1 = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        String formatedDateOpen = newFormat1.format(closeTime);

        // get close time
        SimpleDateFormat newFormat2 = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        String formatedDateClose = newFormat2.format(startTime);

//        Instant test = Instant.parse("1546383625238");

        System.out.println("Open  " + formatedDateOpen);
        System.out.println("Close " + formatedDateClose);
        System.out.println("Close ");

        String open = formatedDateOpen.substring(0, 10) + "T" + formatedDateOpen.substring(11)+ ".00Z";
        String close = formatedDateClose.substring(0, 10) + "T" + formatedDateClose.substring(11)+ ".00Z";

        Instant start = Instant.parse(close);
        Instant end = Instant.parse(open);

        Duration durationInSec = Duration.between(start, end);
        return durationInSec.getSeconds();
    }

    @Override
    public void onMessage(IMessage message) throws JFException {
        //if(message.getOrder() != null) printMe("order: " + message.getOrder().getLabel() + " || message content: " + message.getContent());
    }

    @Override
    public void onAccount(IAccount account) throws JFException {
    }

    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {
//        if (!instrument.equals(instrument)) {
//            return;
//        }
    }



    private double [] MA_1;
    private double [] MA_2;
    private IOrder order = null;
    boolean ma1IsBiggerNew;
    boolean ma1IsBiggerOld;
    /* creating new orders logic */
    public void newOrderLogic(Instrument instrument) throws JFException {
        IBar prevBar = history.getBar(instrument, period, OfferSide.BID, 1);
        MA_1 = indicators.sma(instrument, period, OfferSide.BID, AppliedPrice.CLOSE, smaTimePeriod_1,
                indicatorFilter, 3, prevBar.getTime(), 0);
        MA_2 = indicators.sma(instrument, period, OfferSide.BID, AppliedPrice.CLOSE, smaTimePeriod_2,
                indicatorFilter, 3, prevBar.getTime(), 0);

        // load new value
        if (MA_1[0] > MA_2[0]) {
            ma1IsBiggerNew = true;
        }else{
            ma1IsBiggerNew = false;
        }

        // load old value
        if (MA_1[2] > MA_2[2]) { ma1IsBiggerOld = true; }
        else{ ma1IsBiggerOld = false; }

        if(ma1IsBiggerNew != ma1IsBiggerOld){
            // SMA10 crossover SMA90 from UP to DOWN
            if (MA_1[0] < MA_2[0]) {
                if (engine.getOrders().size() > 0) {
                    for (IOrder orderInMarket : engine.getOrders()) {
                        //Data.addOrder(TestMainRepeater.getLoopCounter(), orderInMarket);
                        orderInMarket.close();
                    }
                }
                if ((order == null) || (order.isLong() && order.getState().equals(IOrder.State.CLOSED)) ) {
                    print("Create SELL");
                    submitOrder(OrderCommand.SELL);
                }
                ma1IsBiggerOld = false;
            }
            // SMA10 crossover SMA90 from DOWN to UP
            if (MA_1[0] > MA_2[0]) {
                if (engine.getOrders().size() > 0) {
                    for (IOrder orderInMarket : engine.getOrders()) {
                        //Data.addOrder(TestMainRepeater.getLoopCounter(), orderInMarket);
                        orderInMarket.close();
                    }
                }
                if ((order == null) || (order.isLong() && order.getState().equals(IOrder.State.CLOSED)) ) {
                    print("Create BUY");
                    submitOrder(OrderCommand.BUY);
                }
                ma1IsBiggerOld = true;
            }
        }
    }
    /* creating new orders logic */
    public void newOrderLogic2(Instrument instrument, IBar askBar) throws JFException {

        // get actual time
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        int timeResultNumber = 0;
//        System.out.println(formatter.format(date));

        try {
            askBar.getTime();
            timeResultNumber = time0_5 * 60;
            if (formatter.format(askBar.getTime()).equals(LocalTime.ofSecondOfDay(timeResultNumber).toString()) && time0_5_ready) {
                // random BUY/SELL == 1/0
                int order = getRandomNumber(0, 1);
                if (order == 1) {
                    print("Create BUY");
                    submitOrder(OrderCommand.BUY);
                } else if (order == 0) {
                    print("Create SELL");
                    submitOrder(OrderCommand.SELL);
                }

                // there can be only one order at the time
                time0_5_ready = false;

                // set previous interval to ready
                time20_24_ready = true;

                // vygeneruje nahodny cas pro dalsi casovy usek
                time5_10 = getRandomNumber(5 * 60, 10 * 60);
            }
            timeResultNumber = time5_10 * 60;
            if (formatter.format(askBar.getTime()).equals(LocalTime.ofSecondOfDay(timeResultNumber).toString()) && time5_10_ready) {
                // random BUY/SELL == 1/0
                int order = getRandomNumber(0, 1);
                if (order == 1) {
                    print("Create BUY");
                    submitOrder(OrderCommand.BUY);
                } else if (order == 0) {
                    print("Create SELL");
                    submitOrder(OrderCommand.SELL);
                }

                // there can be only one order at the time
                time5_10_ready = false;

                // set previous interval to ready
                time0_5_ready = true;

                // vygeneruje nahodny cas pro dalsi casovy usek
                time10_15 = getRandomNumber(10 * 60, 15 * 60);
            }
            timeResultNumber = time10_15 * 60;
            if (formatter.format(askBar.getTime()).equals(LocalTime.ofSecondOfDay(timeResultNumber).toString()) && time10_15_ready) {
                // random BUY/SELL == 1/0
                int order = getRandomNumber(0, 1);
                if (order == 1) {
                    print("Create BUY");
                    submitOrder(OrderCommand.BUY);
                } else if (order == 0) {
                    print("Create SELL");
                    submitOrder(OrderCommand.SELL);
                }

                // there can be only one order at the time
                time10_15_ready = false;

                // set previous interval to ready
                time5_10_ready = true;

                // vygeneruje nahodny cas pro dalsi casovy usek
                time15_20 = getRandomNumber(15 * 60, 20 * 60);
            }
            timeResultNumber = time15_20 * 60;
            if (formatter.format(askBar.getTime()).equals(LocalTime.ofSecondOfDay(timeResultNumber).toString()) && time15_20_ready) {
                // random BUY/SELL == 1/0
                int order = getRandomNumber(0, 1);
                if (order == 1) {
                    print("Create BUY");
                    submitOrder(OrderCommand.BUY);
                } else if (order == 0) {
                    print("Create SELL");
                    submitOrder(OrderCommand.SELL);
                }

                // there can be only one order at the time
                time15_20_ready = false;

                // set previous interval to ready
                time10_15_ready = true;

                // vygeneruje nahodny cas pro dalsi casovy usek
                time20_24 = getRandomNumber(20 * 60, 24 * 59);
            }
            timeResultNumber = (time20_24 * 60);
            if (formatter.format(askBar.getTime()).equals(LocalTime.ofSecondOfDay(timeResultNumber).toString()) && time20_24_ready) {
                // random BUY/SELL == 1/0
                int order = getRandomNumber(0, 1);
                if (order == 1) {
                    print("Create BUY");
                    submitOrder(OrderCommand.BUY);
                } else if (order == 0) {
                    print("Create SELL");
                    submitOrder(OrderCommand.SELL);
                }

                // there can be only one order at the time
                time20_24_ready = false;

                // set previous interval to ready
                time15_20_ready = true;

                // vygeneruje nahodny cas pro dalsi casovy usek
                time0_5 = getRandomNumber(0 * 60, 5 * 60);
            }
        }catch(Exception e){
            System.out.println("Error: " + e);
        }
    }

    private static int getRandomNumber(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private double point() throws JFException {
        return instrument.getPipValue();
    }

    public double tStop = 20.0;
    public double tStep = 1.0;
    private void trailPosition(IOrder order, ITick tick) throws JFException {
        if (order != null && order.getState().equals(IOrder.State.FILLED)) {
            if (order.isLong()) {
                double newSL = tick.getBid() - tStop*point();
                if (tick.getBid() > order.getOpenPrice() + tStop*point() && newSL >= order.getStopLossPrice() + tStep*point()) {
                    printMe("Trailing Stop for LONG position: " + order.getLabel() + "; open price = " + order.getOpenPrice() + "; old SL = " + order.getStopLossPrice() + "; new SL = " + Double.toString(tick.getBid() - tStop*point()));
                    order.setStopLossPrice(newSL);
                    order.waitForUpdate(2000);
                }
            } else {
                double newSL = tick.getAsk() + tStop*point();
                if (tick.getAsk() < order.getOpenPrice() - tStop*point() && (newSL <= order.getStopLossPrice() - tStep*point() || order.getStopLossPrice() == 0.0)) {
                    printMe("Trailing Stop for SHORT position: " + order.getLabel() + "; open price = " + order.getOpenPrice() + "; old SL = " + order.getStopLossPrice() + "; new SL = " + Double.toString(tick.getAsk() + tStop*point()));
                    order.setStopLossPrice(newSL);
                    order.waitForUpdate(2000);
                }
            }
        }
    }

    boolean dailyChecker = false;
    /* at 22 oclock store equity */
    public void storeEquity(Instrument instrument) throws JFException {
        // return hour
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("HH");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long lastTickTime = history.getLastTick(instrument).getTime();
        String hour = sdf.format(lastTickTime);

        // at 22 hours store balance
        if (hour.equals("22") && dailyChecker == false){
            dailyChecker = true;
            //System.out.println("Balance storing");
            DataCube.addDailyEquity(TestMainRepeater.getLoopCounter(), getEquity());
        }else if (hour.equals("23")){
            dailyChecker = false;
        }
    }

    private void print(String toPrint){
        console.getOut().println(toPrint);
        System.out.println(toPrint);
    }

    public static double getEquity() {
        return account.getEquity();
    }

    private void setBreakEvent(){
        try {
            for(IOrder order : engine.getOrders()){

                // if its in profit and new sl is bigger ther old one
                if(order.getProfitLossInUSD() > 0 && order.getOpenPrice() > order.getStopLossPrice()){

                    // and if is profit bigger than require value of break even
                    if( order.getProfitLossInPips() >= breakEvenPips){
                        //console.getOut().println("Order has profit of " + order.getProfitLossInPips() + " pips! Moving the stop loss to the open price." );

                        // set SL to the starting value + 5 pips
                        order.setStopLossPrice(order.getOpenPrice() + (instrument.getPipValue() * 5));

                        System.out.println("ORDER label : " + order.getLabel());
                        System.out.println("ORDER commision : " + order.getCommissionInUSD());
                        System.out.println("ORDER profit : " + order.getProfitLossInUSD());
                    }
                }
            }
        } catch (JFException e) {
            e.printStackTrace();
        }
    }

    /** @return price of certain number of pip */
    private double getPipPrice(int pips) {
        return pips * instrument.getPipValue();
    }

    private IOrder submitOrder(IEngine.OrderCommand orderCmd) throws JFException {

//        // in gui mode add vertical line with each new order
//        IChartObjectFactory factory = chart.getChartObjectFactory();
//        IVerticalLineChartObject vLine = factory.createVerticalLine("vLine",history.getTimeOfLastTick(instrument));
//        chart.add(vLine);


////         get orders from last 1000 bars
//        IBar bar1000 = history.getBar(Instrument.EURUSD, Period.ONE_MIN, OfferSide.ASK, 1000);
//        IBar bar1 = history.getBar(Instrument.EURUSD, Period.ONE_MIN, OfferSide.ASK, 0);
//        List<IOrder> previousOrders = history.getOrdersHistory(instrument, bar1000.getTime(), bar1.getTime());
//        for (IOrder order : previousOrders) {
//            console.getOut().println("Order info: " + order);
//        }





        double stopLossPrice, takeProfitPrice;

        // Calculating stop loss and take profit prices
        if (orderCmd == OrderCommand.BUY) {

            //console.getOut().println("BUY");
            stopLossPrice = history.getLastTick(instrument).getBid() - getPipPrice(stopLossPips);
            takeProfitPrice = history.getLastTick(instrument).getBid() + getPipPrice(takeProfitPips);
        } else {
            //console.getOut().println("SELL");
            stopLossPrice = history.getLastTick(instrument).getAsk() + getPipPrice(stopLossPips);
            takeProfitPrice = history.getLastTick(instrument).getAsk() - getPipPrice(takeProfitPips);
        }

//        order.setStopLossPrice(stopLossPrice);
//        order.setTakeProfitPrice(takeProfitPrice);
        IOrder order = engine.submitOrder(getLabel(instrument), instrument, orderCmd, getAmount(), 0, 0, stopLossPrice, takeProfitPrice);

//        IOrder order = engine.submitOrder(getLabel(instrument), instrument, orderCmd, getAmount(), 0, 20, 10, 30);
        DataCube.addOrder(TestMainRepeater.getLoopCounter(), order);



//        String[] data = new String[]{
//                String.valueOf(order.getLabel()),
//                String.valueOf(order.getOrderCommand()),
//                String.valueOf(order.getAmount()),
//                String.valueOf(order.getProfitLossInAccountCurrency()),
//                String.valueOf(order.getProfitLossInPips()),
//                String.valueOf(order.getCommissionInUSD())
//        };

//        Tools.OrdersWindow.model.addRow(data);


        orderCounter++;

        // Submitting an order for the specified getInstrument at the current market price
        return order;
    }

    /** return amount of lots with a risk two percents */
    private double getAmount() throws JFException {

//        // formula from internet
//        IBar bar2 = history.getBar(instrument, period, myOfferSide, 1);
//        double units = (((account.getEquity() * 0.01) / (1/ bar2.getClose())) / stopLossPips )  *(10000/1);
//        console.getOut().println((units/100000));


        // mine formula show the same ?!?!?!
        IBar bar = history.getBar(instrument, period, myOfferSide, 1);
        double equityInUSD = getEquity() * bar.getClose();
        double lots = (equityInUSD * 0.01) / stopLossPips;
        // transfer microlots to lots
        lots /= 10;
        console.getOut().println(lots);

        // 0.001 is the least possible minimum
        if (lots < 0.001){
            lots = 0.001;
        }
/** -------------------------------------------------------------------------------------------------- 0.01 ---------------- */
        return 0.01;
//        return lots;
    }

    private void printMe(Object toPrint) throws JFException {
        console.getOut().println(instrument.name() + "|| " + toPrint.toString());
    }

    private String getLabel(Instrument instrument) {
        String label = instrument.name();
        label = label + (counter++);
        label = label.toUpperCase();
        return label;
    }
}