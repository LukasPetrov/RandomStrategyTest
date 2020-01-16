package singlejartest;

import com.dukascopy.api.*;
import com.dukascopy.api.instrument.IFinancialInstrument;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OrdersWindow extends TimerTask {

    private static List<IOrder> data;
    private static OrdersWindow ordersWindow;
    // frame
    JFrame frame;
    // Table
    JTable table = null;

    public void uprcgrade(ArrayList list){



        // Frame initiallization
        frame = new JFrame();

        // Frame Title
        frame.setTitle("Orders");








        // adding it to JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        // Frame Size
        frame.setSize(500, 600);
        // Frame Visible = true
        frame.setVisible(true);

    }

    OrdersWindow(List<IOrder> list){
//    OrdersWindow(){

        // Frame initiallization
        frame = new JFrame();

        // Frame Title
        frame.setTitle("Orders");


        if (list.size() > 0){

            // create list
            ArrayList<String[]> data = new ArrayList<>();

            // add all orders to the String[][]
            for (IOrder order : list) {
                // Data to be displayed in the JTable
                data.add(new String[]{
                        String.valueOf(order.getLabel()),
                        String.valueOf(order.getOrderCommand()),
                        String.valueOf(order.getAmount()),
                        String.valueOf(order.getProfitLossInAccountCurrency()),
                        String.valueOf(order.getProfitLossInPips()),
                        String.valueOf(order.getCommissionInUSD())
                });
            }

            // convert arrayList to String[][]
            String[][] array = new String[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                String[] row = data.get(i);
                array[i] = row;
            }


            // Column Names
            String[] columnNames = { "Label", "Command", "Amount", "Profit", "Profit in Pips", "Commission" };

            // Initializing the JTable
            table = new JTable(array, columnNames);
            table.setBounds(30, 40, 200, 300);

        }

        // adding it to JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        // Frame Size
        frame.setSize(500, 600);
        // Frame Visible = true
        frame.setVisible(true);
    }

    // Driver  method
    public static void main(String[] args){

        data = new ArrayList<>();

        IOrder order = new IOrder() {
            @Override
            public Instrument getInstrument() {
                return Instrument.EURUSD;
            }

            @Override
            public String getLabel() {
                return "EURUSD01";
            }

            @Override
            public String getId() {
                return "001";
            }

            @Override
            public long getCreationTime() {
                return 0;
            }

            @Override
            public long getCloseTime() {
                return 0;
            }

            @Override
            public IEngine.OrderCommand getOrderCommand() {
                return IEngine.OrderCommand.BUY;
            }

            @Override
            public boolean isLong() {
                return false;
            }

            @Override
            public long getFillTime() {
                return 0;
            }

            @Override
            public double getOriginalAmount() {
                return 0;
            }

            @Override
            public double getAmount() {
                return 0.01;
            }

            @Override
            public double getRequestedAmount() {
                return 0;
            }

            @Override
            public double getOpenPrice() {
                return 0;
            }

            @Override
            public double getClosePrice() {
                return 0;
            }

            @Override
            public double getStopLossPrice() {
                return 0;
            }

            @Override
            public double getTakeProfitPrice() {
                return 0;
            }

            @Override
            public void setStopLossPrice(double price) throws JFException {

            }

            @Override
            public void setStopLossPrice(double price, OfferSide side) throws JFException {

            }

            @Override
            public void setStopLossPrice(double price, OfferSide side, double trailingStep) throws JFException {

            }

            @Override
            public void setLabel(String label) throws JFException {

            }

            @Override
            public OfferSide getStopLossSide() {
                return null;
            }

            @Override
            public double getTrailingStep() {
                return 0;
            }

            @Override
            public void setTakeProfitPrice(double price) throws JFException {

            }

            @Override
            public String getComment() {
                return null;
            }

            @Override
            public void setComment(String comment) throws JFException {

            }

            @Override
            public void setRequestedAmount(double amount) throws JFException {

            }

            @Override
            public void setOpenPrice(double price) throws JFException {

            }

            @Override
            public void setOpenPrice(double price, double slippage) throws JFException {

            }

            @Override
            public void close(double amount, double price, double slippage) throws JFException {

            }

            @Override
            public void close(double amount, double price) throws JFException {

            }

            @Override
            public void close(double amount) throws JFException {

            }

            @Override
            public void close() throws JFException {

            }

            @Override
            public State getState() {
                return null;
            }

            @Override
            public void setGoodTillTime(long goodTillTime) throws JFException {

            }

            @Override
            public long getGoodTillTime() {
                return 0;
            }

            @Override
            public void waitForUpdate(long timeoutMills) {

            }

            @Override
            public IMessage waitForUpdate(long timeout, TimeUnit unit) {
                return null;
            }

            @Override
            public IMessage waitForUpdate(State... states) throws JFException {
                return null;
            }

            @Override
            public IMessage waitForUpdate(long timeoutMills, State... states) throws JFException {
                return null;
            }

            @Override
            public IMessage waitForUpdate(long timeout, TimeUnit unit, State... states) throws JFException {
                return null;
            }

            @Override
            public double getProfitLossInPips() {
                return 24;
            }

            @Override
            public double getProfitLossInUSD() {
                return 0.75;
            }

            @Override
            public double getProfitLossInAccountCurrency() {
                return 0.75;
            }

            @Override
            public double getCommission() {
                return 0;
            }

            @Override
            public double getCommissionInUSD() {
                return 0.21;
            }

            @Override
            public List<IFillOrder> getFillHistory() {
                return null;
            }

            @Override
            public List<ICloseOrder> getCloseHistory() {
                return null;
            }

            @Override
            public boolean isOCO() {
                return false;
            }

            @Override
            public void groupToOco(IOrder second) throws JFException {

            }

            @Override
            public void ungroupOco() throws JFException {

            }

            @Override
            public IEntryOrder getEntryOrder() {
                return null;
            }

            @Override
            public IStopLossOrder getStopLossOrder() {
                return null;
            }

            @Override
            public ITakeProfitOrder getTakeProfitOrder() {
                return null;
            }

            @Override
            public IEntryOrder getGroupedEntryOrder() {
                return null;
            }

            @Override
            public IStopLossOrder getGroupedStopLossOrder() {
                return null;
            }

            @Override
            public ITakeProfitOrder getGroupedTakeProfitOrder() {
                return null;
            }

            @Override
            public boolean compare(IOrder order) {
                return false;
            }

            @Override
            public IFinancialInstrument getFinancialInstrument() {
                return null;
            }
        };

        data.add(order);
        data.add(order);
        data.add(order);

        ordersWindow = new OrdersWindow(data);


        Timer timer = new Timer();
        timer.schedule(new OrdersWindow(), 0, 1000);
    }

    @Override
    public void run() {

        IOrder order = new IOrder() {
            @Override
            public Instrument getInstrument() {
                return Instrument.EURUSD;
            }

            @Override
            public String getLabel() {
                return "EURUSD01";
            }

            @Override
            public String getId() {
                return "001";
            }

            @Override
            public long getCreationTime() {
                return 0;
            }

            @Override
            public long getCloseTime() {
                return 0;
            }

            @Override
            public IEngine.OrderCommand getOrderCommand() {
                return IEngine.OrderCommand.BUY;
            }

            @Override
            public boolean isLong() {
                return false;
            }

            @Override
            public long getFillTime() {
                return 0;
            }

            @Override
            public double getOriginalAmount() {
                return 0;
            }

            @Override
            public double getAmount() {
                return 0.01;
            }

            @Override
            public double getRequestedAmount() {
                return 0;
            }

            @Override
            public double getOpenPrice() {
                return 0;
            }

            @Override
            public double getClosePrice() {
                return 0;
            }

            @Override
            public double getStopLossPrice() {
                return 0;
            }

            @Override
            public double getTakeProfitPrice() {
                return 0;
            }

            @Override
            public void setStopLossPrice(double price) throws JFException {

            }

            @Override
            public void setStopLossPrice(double price, OfferSide side) throws JFException {

            }

            @Override
            public void setStopLossPrice(double price, OfferSide side, double trailingStep) throws JFException {

            }

            @Override
            public void setLabel(String label) throws JFException {

            }

            @Override
            public OfferSide getStopLossSide() {
                return null;
            }

            @Override
            public double getTrailingStep() {
                return 0;
            }

            @Override
            public void setTakeProfitPrice(double price) throws JFException {

            }

            @Override
            public String getComment() {
                return null;
            }

            @Override
            public void setComment(String comment) throws JFException {

            }

            @Override
            public void setRequestedAmount(double amount) throws JFException {

            }

            @Override
            public void setOpenPrice(double price) throws JFException {

            }

            @Override
            public void setOpenPrice(double price, double slippage) throws JFException {

            }

            @Override
            public void close(double amount, double price, double slippage) throws JFException {

            }

            @Override
            public void close(double amount, double price) throws JFException {

            }

            @Override
            public void close(double amount) throws JFException {

            }

            @Override
            public void close() throws JFException {

            }

            @Override
            public State getState() {
                return null;
            }

            @Override
            public void setGoodTillTime(long goodTillTime) throws JFException {

            }

            @Override
            public long getGoodTillTime() {
                return 0;
            }

            @Override
            public void waitForUpdate(long timeoutMills) {

            }

            @Override
            public IMessage waitForUpdate(long timeout, TimeUnit unit) {
                return null;
            }

            @Override
            public IMessage waitForUpdate(State... states) throws JFException {
                return null;
            }

            @Override
            public IMessage waitForUpdate(long timeoutMills, State... states) throws JFException {
                return null;
            }

            @Override
            public IMessage waitForUpdate(long timeout, TimeUnit unit, State... states) throws JFException {
                return null;
            }

            @Override
            public double getProfitLossInPips() {
                return 24;
            }

            @Override
            public double getProfitLossInUSD() {
                return 0.75;
            }

            @Override
            public double getProfitLossInAccountCurrency() {
                return 0.75;
            }

            @Override
            public double getCommission() {
                return 0;
            }

            @Override
            public double getCommissionInUSD() {
                return 0.21;
            }

            @Override
            public List<IFillOrder> getFillHistory() {
                return null;
            }

            @Override
            public List<ICloseOrder> getCloseHistory() {
                return null;
            }

            @Override
            public boolean isOCO() {
                return false;
            }

            @Override
            public void groupToOco(IOrder second) throws JFException {

            }

            @Override
            public void ungroupOco() throws JFException {

            }

            @Override
            public IEntryOrder getEntryOrder() {
                return null;
            }

            @Override
            public IStopLossOrder getStopLossOrder() {
                return null;
            }

            @Override
            public ITakeProfitOrder getTakeProfitOrder() {
                return null;
            }

            @Override
            public IEntryOrder getGroupedEntryOrder() {
                return null;
            }

            @Override
            public IStopLossOrder getGroupedStopLossOrder() {
                return null;
            }

            @Override
            public ITakeProfitOrder getGroupedTakeProfitOrder() {
                return null;
            }

            @Override
            public boolean compare(IOrder order) {
                return false;
            }

            @Override
            public IFinancialInstrument getFinancialInstrument() {
                return null;
            }
        };

        data.add(order);
        data.add(order);
        data.add(order);

        ordersWindow = new OrdersWindow(data);
    }
}
