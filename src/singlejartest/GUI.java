package singlejartest;

import com.dukascopy.api.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GUI {
    private JPanel panel1;
    private JTextField startEquityTextField;
    private JButton startButton;
    private JProgressBar progressBar1;
    private JComboBox PeriodComboBox;
    private JComboBox ParameterIncreaseSizeComboBox;
    private JTextField dateToTextField;
    private JTextField InstrumentTextField;
    private JTextField dateFromTextField;
    private JTextField ma_1;
    private JTextField ma_2;
    private JTextArea ConsoleTextArea;

    public GUI() {
        JFrame f = new JFrame("SMATester");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        $$$setupUI$$$();
        f.add(panel1);

        f.setSize(700, 1000);
        f.setVisible(true);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // load and save date from / to
                final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                try {
                    DataCube.setDateFrom(dateFormat.parse(dateFromTextField.getText() + " 00:00:00"));
                    DataCube.setDateTo(dateFormat.parse(dateToTextField.getText() + " 00:00:00"));
                } catch (ParseException a) {
                    a.printStackTrace();
                }

                // load and save myInstrument
                DataCube.setInstrument(Instrument.valueOf(InstrumentTextField.getText()));

                // load and save opening deposit
                TestMainRepeater.setOpeningDeposit(Integer.parseInt(startEquityTextField.getText()));

                // load and save period
                switch (PeriodComboBox.getSelectedIndex()) {
                    case 0:
                        DataCube.setPeriod(Period.ONE_SEC);
                        break;
                    case 1:
                        DataCube.setPeriod(Period.TEN_SECS);
                        break;
                    case 2:
                        DataCube.setPeriod(Period.ONE_MIN);
                        break;
                    case 3:
                        DataCube.setPeriod(Period.FIVE_MINS);
                        break;
                    case 4:
                        DataCube.setPeriod(Period.TEN_MINS);
                        break;
                    case 5:
                        DataCube.setPeriod(Period.FIFTEEN_MINS);
                        break;
                    case 6:
                        DataCube.setPeriod(Period.THIRTY_MINS);
                        break;
                    case 7:
                        DataCube.setPeriod(Period.ONE_HOUR);
                        break;
                    case 8:
                        DataCube.setPeriod(Period.FOUR_HOURS);
                        break;
                    case 9:
                        DataCube.setPeriod(Period.DAILY);
                        break;
                    case 10:
                        DataCube.setPeriod(Period.WEEKLY);
                        break;
                    case 11:
                        DataCube.setPeriod(Period.MONTHLY);
                        break;
                }

                // save ma_1 and ma_2 period
                DataCube.setMa_1(Short.parseShort(ma_1.getText()));
                DataCube.setMa_2(Short.parseShort(ma_2.getText()));

                // save new dateFrom
                TestMainRepeater.setSL(Integer.parseInt(ma_1.getText()));

                // clean console text area
                ConsoleTextArea.selectAll();
                ConsoleTextArea.replaceSelection("");
                ConsoleTextArea.append("" +
                        "Name\t" +
                        "Final Deposit\t" +
                        "Success rate\t" +
                        "Num Of Orders\t" +
                        "Avrg fee\t" +
                        "Biggest loss/profit\t" +
                        "Avrg Order Time");

                // get list of parameters
                TestMainRepeater.refreshListOfParameters();


                System.out.println("Date From: " + DataCube.getDateFrom());
                System.out.println("Date To: " + DataCube.getDateTo());
                System.out.println("MA 1: " + DataCube.getMa_1());
                System.out.println("MA 2: " + DataCube.getMa_2());
                System.out.println("Period: " + DataCube.getPeriod());
                System.out.println("Instrument: " + DataCube.getInstrument());
                System.out.println("Start Equity: " + TestMainRepeater.getOpeningDeposit());



                // get list of parameters
                TestMainRepeater.setListOfParameters(Tools.SLTPGenerator.listOfParameters());


                // start test
                try {
                    TestMainRepeater.startStrategy();
                } catch (Exception a) {

                }
            }
        });
    }

    public void printToConsoleTextArea(String string) {
        ConsoleTextArea.append(string);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
        startEquityTextField = new JTextField();
        startEquityTextField.setText("50000");
        startEquityTextField.setToolTipText("1000 USD e.g.");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(startEquityTextField, gbc);
        startButton = new JButton();
        startButton.setText("Start");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(startButton, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Date (MM/dd/yyyy)");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Instrument");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(label2, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Time Frame");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(label3, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Start Equity");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(label4, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Časový úsek");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(label5, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("USD");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label6, gbc);
        PeriodComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("1 Sec");
        defaultComboBoxModel1.addElement("10 Sec");
        defaultComboBoxModel1.addElement("1 Min");
        defaultComboBoxModel1.addElement("5 Min");
        defaultComboBoxModel1.addElement("10 Min");
        defaultComboBoxModel1.addElement("15 Min");
        defaultComboBoxModel1.addElement("30 Min");
        defaultComboBoxModel1.addElement("1 Hour");
        defaultComboBoxModel1.addElement("4 Hour");
        defaultComboBoxModel1.addElement("1 Day");
        defaultComboBoxModel1.addElement("1 Week");
        defaultComboBoxModel1.addElement("1 Month");
        PeriodComboBox.setModel(defaultComboBoxModel1);
        PeriodComboBox.setSelectedIndex(7);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(PeriodComboBox, gbc);
        ParameterIncreaseSizeComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("1 (1, 2, 3, 4, etc.)");
        defaultComboBoxModel2.addElement("5 (5, 10, 15, 20, etc.)");
        defaultComboBoxModel2.addElement("10 (10, 20, 30, 40, etc.)");
        defaultComboBoxModel2.addElement("20 (20, 40, 60, 80, etc.)");
        ParameterIncreaseSizeComboBox.setModel(defaultComboBoxModel2);
        ParameterIncreaseSizeComboBox.setSelectedIndex(2);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(ParameterIncreaseSizeComboBox, gbc);
        dateToTextField = new JTextField();
        dateToTextField.setText("01/02/2019");
        dateToTextField.setToolTipText("dd/MM/yyyy");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(dateToTextField, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(label7, gbc);
        dateFromTextField = new JTextField();
        dateFromTextField.setText("01/01/2019");
        dateFromTextField.setToolTipText("dd/MM/yyyy");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(dateFromTextField, gbc);
        progressBar1 = new JProgressBar();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(progressBar1, gbc);
        InstrumentTextField = new JTextField();
        InstrumentTextField.setText("EURUSD");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(InstrumentTextField, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("SMA parameters (from - to)");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel1.add(label8, gbc);
        ma_1 = new JTextField();
        ma_1.setText("50");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(ma_1, gbc);
        ma_2 = new JTextField();
        ma_2.setText("150");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(ma_2, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(label9, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane1, gbc);
        ConsoleTextArea = new JTextArea();
        ConsoleTextArea.setColumns(0);
        ConsoleTextArea.setDragEnabled(true);
        ConsoleTextArea.setEditable(true);
        ConsoleTextArea.setLineWrap(false);
        ConsoleTextArea.setRows(15);
        ConsoleTextArea.setText("");
        scrollPane1.setViewportView(ConsoleTextArea);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }


    public static class OrdersWindow {
        private static java.util.List<IOrder> data;
        // frame
        JFrame frame;
        // Table
        public static JTable table = null;
        public static DefaultTableModel model;

        OrdersWindow(){
            // Frame initiallization
            frame = new JFrame();

            // Frame Title
            frame.setTitle("Orders");
//
//        List<IOrder> list = null;
//        if (list.size() > 0){
//            // create list
//            ArrayList<String[]> data = new ArrayList<>();
//
//            // add all orders to the String[][]
//            for (IOrder order : list) {
//                // Data to be displayed in the JTable
//                data.add(new String[]{
//                        String.valueOf(order.getLabel()),
//                        String.valueOf(order.getOrderCommand()),
//                        String.valueOf(order.getAmount()),
//                        String.valueOf(order.getProfitLossInAccountCurrency()),
//                        String.valueOf(order.getProfitLossInPips()),
//                        String.valueOf(order.getCommissionInUSD())
//                });
//            }
//
//            // convert arrayList to String[][]
//            String[][] array = new String[data.size()][];
//            for (int i = 0; i < data.size(); i++) {
//                String[] row = data.get(i);
//                array[i] = row;
//            }
//        }


            // Column Names
            String[] columnNames = { "Label", "Command", "Amount", "Profit", "Profit in Pips", "Commission" };

            model = new DefaultTableModel(columnNames,0);

            // Initializing the JTable
            table = new JTable(model);

            table.setBounds(30, 40, 200, 300);

            // adding it to JScrollPane
            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane);

            // Frame Size
            frame.setSize(500, 600);

            // Frame Visible = true
            frame.setVisible(true);
        }

        /** remove all rows from the model */
        public static void clear(DefaultTableModel model){
            if(model != null) {
                if (model.getRowCount() > 0) {
                    for (int i = model.getRowCount() - 1; i > -1; i--) {
                        model.removeRow(i);
                    }
                }
            }
        }

        public static void main(String[] args){

            data = new ArrayList<>();

            new OrdersWindow();

            GUI.OrdersWindow.model.addRow(new Object[]{"Column 1", "Column 2", "Column 3"});
            GUI.OrdersWindow.model.addRow(new Object[]{"Column 1", "Column 2", "Column 3"});


            // create runnable method
            Runnable runnable = new Runnable() {
                public void run() {
                    System.out.println("Next sec");

                    // remove all rows from the model
//                    OrdersWindow.clear(OrdersWindow.model);

                    // fill model by new data
                    GUI.OrdersWindow.model.addRow(new Object[]{"Column 1", "Column 2", "Column 3"});
                }
            };

            // start runnable method
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
        }
    }

    public static class ShowChart extends ApplicationFrame {

        private ShowChart() {
            super("MATester");
            JFreeChart lineChart = ChartFactory.createLineChart(
                    "strategies",
                    "Days","Balance",
                    createDataset2(),
                    PlotOrientation.VERTICAL,
                    true,true,false);

            ChartPanel chartPanel = new ChartPanel( lineChart );
            chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
            chartPanel.setHorizontalAxisTrace(false);
            chartPanel.setVerticalAxisTrace(true);
            setContentPane( chartPanel );

//            showGraph();
        }

        private DefaultCategoryDataset createDataset( ) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

            for (int strategy = 0; strategy < DataCube.getDailyEquity().size(); strategy++) {
                for (int day = 0; day < DataCube.getDailyEquity(strategy).size(); day++) {
                    dataset.addValue(DataCube.getDailyEquity(strategy, day), "strategy_" + strategy, String.valueOf(day));
                }
            }
            return dataset;
        }
        // show 15 max strategies
        private DefaultCategoryDataset createDataset2( ) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

            try {
                for (int strategy = 0; strategy < 20; strategy++) {
                    for (int day = 0; day < DataCube.getDailyEquity(DataCube.getBestResults(strategy)).size(); day++) {
                        dataset.addValue(DataCube.getDailyEquity(DataCube.getBestResults(strategy), day), DataCube.getStrategyName(DataCube.getBestResults(strategy)), String.valueOf(day));
                    }
                }
            }catch(Exception e){

            }
            return dataset;
        }

        // show equity graph
        public static void showGraph(){
            GUI.ShowChart chart = new GUI.ShowChart();
            chart.pack();
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);
        }

    }
}
