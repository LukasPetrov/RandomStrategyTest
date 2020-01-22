package singlejartest;


import com.dukascopy.api.Instrument;
import com.dukascopy.api.LoadingProgressListener;
import com.dukascopy.api.system.ISystemListener;
import com.dukascopy.api.system.ITesterClient;
import com.dukascopy.api.system.TesterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

public class TestMainRepeater {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static GUI gui;
    private static long strategyId;
    private static ITesterClient client;
    private static String jnlpUrl = "http://platform.dukascopy.com/demo/jforex.jnlp";
    private static String userName = "DEMO3TiMJp";
    private static String password = "TiMJp";
    private static String reportsFileLocation = "C:\\Users\\lukas\\Desktop\\Exit\\Reports\\report.html";
    private static int loopCount = 0;
    private static int openingDeposit = 1000;
    private static int SL = 0;
    private static int TP = 0;

    public static void setListOfParameters(ArrayList<ArrayList<Integer>> listOfParameters) {
        TestMainRepeater.listOfParameters = listOfParameters;
    }

//    public static ArrayList<ArrayList<Integer>> getListOfParameters() {
//        return listOfParameters;
//    }

    private static ArrayList<ArrayList<Integer>> listOfParameters = new ArrayList<>(1);


    public static void main(String[] args) {

        DataCube.createDataCube();

        // start dialog and get parameters for test
        gui = new GUI();


    }

    public static void refreshListOfParameters(){
    }

    public static void startStrategy() throws Exception {
        client = TesterFactory.getDefaultInstance();

        setSystemListener();
        tryToConnect();
        subscribeToInstruments();
        client.setInitialDeposit(DataCube.getInstrument().getSecondaryJFCurrency(), openingDeposit);

        loadData();

        // set next pair of parameters
        SL = listOfParameters.get(0).get(loopCount);
        TP = listOfParameters.get(1).get(loopCount);


        //LOGGER.info("Starting strategy");
        // run strategy
        client.startStrategy(new Strategy(SL, TP, false), getLoadingProgressListener());
    }

    private static void setSystemListener() {
        client.setSystemListener(new ISystemListener() {
            @Override
            public void onStart(long processId) {
                //LOGGER.info("Strategy started: " + processId);
                strategyId = processId;
            }

            @Override
            public void onStop(long processId) {
                if (SL <= listOfParameters.get(0).size()) {
                    //LOGGER.info("Strategy started: " + processId);
                    strategyId = processId;

                    // loop counter
                    loopCount ++;

                    // creating report
                    File reportFile = new File(reportsFileLocation);
                    try {
                        client.createReport(processId, reportFile);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }

                    //start strategy again
                    if (client.getStartedStrategies().size() == 0) {
                        try {

                            startStrategy();
                        } catch (Exception a) {

                        }
                    }
                }
            }

            @Override
            public void onConnect() {
                LOGGER.info("Connected");
            }

            @Override
            public void onDisconnect() {
                //tester doesn't disconnect
            }
        });
    }

    private static void tryToConnect() throws Exception {
        LOGGER.info("Connecting...");
        //connect to the server using jnlp, user name and password
        //connection is needed for data downloading
        client.connect(jnlpUrl, userName, password);

        //wait for it to connect
        int i = 10; //wait max ten seconds
        while (i > 0 && !client.isConnected()) {
            Thread.sleep(1000);
            i--;
        }
        if (!client.isConnected()) {
            LOGGER.error("Failed to connect Dukascopy servers");
            System.exit(1);
        }
    }

    private static void subscribeToInstruments() {
        //set instruments that will be used in testing
        Set<Instrument> instruments = new HashSet<>();
        instruments.add(DataCube.getInstrument());
        LOGGER.info("Subscribing instruments...");
        client.setSubscribedInstruments(instruments);
    }

    private static void loadData() throws InterruptedException, java.util.concurrent.ExecutionException {
        //load data
        LOGGER.info("Downloading data");
        Future<?> future = client.downloadData(null);
        client.setDataInterval(ITesterClient.DataLoadingMethod.DIFFERENT_PRICE_TICKS, DataCube.getDateFrom().getTime(), DataCube.getDateTo().getTime());
        //wait for downloading to complete
        future.get();
    }

    public static LoadingProgressListener getLoadingProgressListener() {
        return new LoadingProgressListener() {
            @Override
            public void dataLoaded(long startTime, long endTime, long currentTime, String information) {
                LOGGER.info(information);
            }

            @Override
            public void loadingFinished(boolean allDataLoaded, long startTime, long endTime, long currentTime) {
            }

            @Override
            public boolean stopJob() {
                return false;
            }
        };
    }

    public static void printToConsoleTextArea(String string){
        gui.printToConsoleTextArea(string);
    }


    public static int getTP() {
        return TP;
    }

    public static int getOpeningDeposit() {
        return openingDeposit;
    }

    public static void setOpeningDeposit(int openingDeposit) {
        TestMainRepeater.openingDeposit = openingDeposit;
    }

    public static int getLoopCounter() {
        return loopCount;
    }

    public static int getSL() {
        return SL;
    }

    public static void setSL(int SL) {
        TestMainRepeater.SL = SL;
    }

}

