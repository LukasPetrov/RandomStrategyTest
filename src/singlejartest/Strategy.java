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
    private static Period period = Period.ONE_HOUR;
    private Filter indicatorFilter = Filter.ALL_FLATS;
    public double amount = 0.001;
    public int stopLossPips = 50;
    public int takeProfitPips = 50;
    public int breakEvenPips = 25;
    private int smaTimePeriod_1;
    private int smaTimePeriod_2;
    private String strategyName;

    public Strategy(int smaTimePeriod_1, int smaTimePeriod_2, boolean GUITest){
        // smaTimePeriod1 must by smaller than 2
        if(smaTimePeriod_1 < smaTimePeriod_2){
            this.smaTimePeriod_1 = smaTimePeriod_1 * 10;
            this.smaTimePeriod_2 = smaTimePeriod_2 * 10;
        }else{
            this.smaTimePeriod_1 = smaTimePeriod_2 * 10;
            this.smaTimePeriod_2 = smaTimePeriod_1 * 10;
        }

        // create strategy name
        strategyName = smaTimePeriod_1*10 + "/" + smaTimePeriod_2*10;
        DataCube.addStrategyName(strategyName);

        //  for GUI testing DataCube is not available
        if (!GUITest) {
            period = DataCube.getPeriod();
            instrument = DataCube.getInstrument();
        }

        // reset orderCounter for new test
        orderCounter = 0;
    }

    @Override
    public void onStart(IContext context) throws JFException {
        this.engine = context.getEngine();
        this.console = context.getConsole();
        this.history = context.getHistory();
        this.context = context;
        this.account = context.getAccount();
        this.indicators = context.getIndicators();

        // check if chart is open if doesn't print error message if is open add indicators
        chart = context.getLastActiveChart();
        if (chart == null) {
            console.getErr().println("No chart opened!");
            return;
        }
        chart = context.getChart(instrument);
        chart.add(indicators.getIndicator("SMA"), new Object[]{smaTimePeriod_1});
        chart.add(indicators.getIndicator("SMA"), new Object[]{smaTimePeriod_2});
    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
        // filter right instrument and period
        if (!instrument.equals(instrument) || !period.equals(Strategy.period))
            return;

        newOrderLogic(instrument);
        setBreakEvent();
        storeEquity(instrument);
        trailPosition(order, history.getLastTick(instrument));

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
        if (Tools.MACrossList.getFinalNumber()-1 == TestMainRepeater.getLoopCounter()) {

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


            GUI.ShowChart.showGraph();

        }

        //get correct number of valid orders
        int numOfOrders = 0;
        int profitOrders = 0;
        int lossOrders = 0;
        double biggestProfit = 0;
        double biggestLoss = 0;
        double avrgCommission=0;
        for (IOrder order: DataCube.getOrders(TestMainRepeater.getLoopCounter())) {
            if (order.getState().name() == "CLOSED" || order.getState().name() == "FILLED"){
                numOfOrders++;
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



        DecimalFormat df = new DecimalFormat("#.##");
        String successRate = df.format((double)profitOrders/((double)numOfOrders/100));
        avrgCommission = Double.parseDouble(df.format(avrgCommission/numOfOrders));

        TestMainRepeater.printToConsoleTextArea(
                "\n" + DataCube.getStrategyName(TestMainRepeater.getLoopCounter()) +
                        "\t" + getEquity() +
                        "\t" + successRate + "%" +
                        "\t" + numOfOrders +
                        "\t" + avrgCommission +
                        "\t" + biggestLoss + "/" + biggestProfit
        );
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

        IOrder order = engine.submitOrder(getLabel(instrument), instrument, orderCmd, getAmount(), 0, 20, 0, 0);
        DataCube.addOrder(TestMainRepeater.getLoopCounter(), order);

        orderCounter++;

        // Submitting an order for the specified getInstrument at the current market price
        return order;
    }

    /** return amount of lots with a risk two percents */
    private double getAmount() throws JFException {

        // formula from internet
        IBar bar2 = history.getBar(instrument, period, myOfferSide, 1);
        double units = (((account.getEquity() * 0.01) / (1/ bar2.getClose())) / stopLossPips )  *(10000/1);
        console.getOut().println((units/100000));


        // mine formula show the same ?!?!?!
        IBar bar = history.getBar(instrument, period, myOfferSide, 1);
        double equityInUSD = getEquity() * bar.getClose();
        double lots = (equityInUSD * 0.01) / stopLossPips;
        // transfer microlots to lots
        lots /= 10;
        console.getOut().println(lots);
        return lots;
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