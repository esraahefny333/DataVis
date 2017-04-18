package grad.proj.webApp.firstApp.recommendation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ninja
 */
public class VisExtract {

    private String tableName;
    private String x_axis;
    private String y_axis;
    private String aggregateFunction;
    private String selectorOnDimension;
    private String SpecificAttribute;
    private String firstOperator;
    private String firstSelector;
    private String secondOperator;
    
    private String secondSelector;

    private VisData vd;
    private SqLiteHelper sql;
    private ResultSet rs;

    public VisExtract(String tableName, String xAxis, String yAxis, String aggregate, String selectorOnDimension, String attribute, String firstOperator, String firstSelector, String secondOperator, String secondSelector) throws ClassNotFoundException, SQLException {

        this.tableName = tableName;
        this.x_axis = xAxis;//D
        this.y_axis = yAxis;//M
        this.aggregateFunction = aggregate;//F
        this.selectorOnDimension = selectorOnDimension;
        this.SpecificAttribute = attribute;
        this.firstOperator = firstOperator;
        this.firstSelector = firstSelector;
        this.secondOperator = secondOperator;
        this.secondSelector = secondSelector;

        this.sql = new SqLiteHelper();     
    }

    private String concat(String... s) {
        StringBuilder b = new StringBuilder();
        for (String x : s) {
            b.append(x);
        }
        return b.toString();
    }

    public ArrayList<VisData> getMainVis() {
        /* 
            Expected Distribution: //Run the query on the entire data - not spacifying any selection
                                     this is is case the user specify only one selector
                    
            Actual Distribution://Run the same query on the subset of data we are interested in
                                  this os case he give only one selector 
         */

        vd = new VisData();
        vd.x_axis = this.x_axis;
        vd.y_axis = this.aggregateFunction + "[" + this.y_axis + "]";

        /*
        e.g:
        "SELECT x-axis, aggregate(y-axis),CASE WHEN Specific-attribute '=' first-selector THEN 1 WHEN Specific-attribute '=' second-selector THEN 2 END as g1 
         FROM table-name WHERE y-axis '=' selector-on-dimension GROUP BY x-axis, g1"
        */
        
        if (this.selectorOnDimension.equals("")) {

            rs = sql.executeQuery(concat("SELECT ", this.x_axis, ",", this.aggregateFunction, "(", this.y_axis, ")",
                    ",", "CASE WHEN ", this.SpecificAttribute, this.firstOperator, this.firstSelector, " THEN 1",
                    " WHEN ", this.SpecificAttribute, this.secondOperator, this.secondSelector, " THEN 2 END as g1",
                    " FROM ", this.tableName,
                    " GROUP BY ", this.x_axis, ",", "g1")
            );

        } else {

            rs = sql.executeQuery(concat("SELECT ", this.x_axis, ",", this.aggregateFunction, "(", this.y_axis, ")",
                    "," + "CASE WHEN ", this.SpecificAttribute + this.firstOperator + this.firstSelector, " THEN 1",
                    " WHEN ", this.SpecificAttribute, this.secondOperator, this.secondSelector, " THEN 2 END as g1",
                    " FROM ", this.tableName,
                    " WHERE ", this.y_axis, "=", this.selectorOnDimension,
                    " GROUP BY ", this.x_axis, ",", "g1")
            );
        }

        try {
            while (rs.next()) {

                if (rs.getString(3) != null) {

                    if (rs.getString(3).equals("1")) {
                        vd.recomQuery1.put(rs.getString(1), rs.getString(2));
                    } else if (rs.getString(3).equals("2")) {
                        vd.recomQuery2.put(rs.getString(1), rs.getString(2));
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(VisExtract.class.getName()).log(Level.SEVERE, null, ex);
        }

        VisCharts.mainVisList.add(vd);
        return  VisCharts.mainVisList;
    }

    //Try all:  D * F * M
    public  ArrayList<VisData> getRecommendations_bruteForce() {

        for (int i = 0; i < VisBuilder.dimentionAttributeArray.length; i++) {

            for (int j = 0; j < VisBuilder.aggregateArray.length; j++) {

                for (int k = 0; k < VisBuilder.MeasureAttributeArray.length; k++) {

                    vd = new VisData();
                    vd.x_axis = VisBuilder.dimentionAttributeArray[i];
                    vd.y_axis = VisBuilder.aggregateArray[j] + "(" + VisBuilder.MeasureAttributeArray[k] + ")";

                    rs = sql.executeQuery(concat("SELECT ", VisBuilder.dimentionAttributeArray[i], ",", VisBuilder.aggregateArray[j], "(", VisBuilder.MeasureAttributeArray[k], ")",
                            "," + "CASE WHEN ", this.SpecificAttribute + this.firstOperator, this.firstSelector, " THEN 1",
                            " WHEN ", this.SpecificAttribute, this.secondOperator, this.secondSelector, " THEN 2 END as g1",
                            " FROM ", this.tableName,
                            " GROUP BY ", VisBuilder.dimentionAttributeArray[i], ",", "g1")
                    );

                    try {

                        while (rs.next()) {

                            if (rs.getString(3) != null) {

                                if (rs.getString(3).equals("1")) {
                                    vd.recomQuery1.put(rs.getString(1), rs.getString(2));
                                } else if (rs.getString(3).equals("2")) {
                                    vd.recomQuery2.put(rs.getString(1), rs.getString(2));
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(VisExtract.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    VisCharts.recommendationList_bruteForce.add(vd);
                    
                }
            }
        }

        euclidean_metric(VisCharts.recommendationList_bruteForce);
        //manhattan_metric(this.recommendationList_bruteForce);
        rank(VisCharts.recommendationList_bruteForce);
        return VisCharts.recommendationList_bruteForce;
    }

    public  ArrayList<VisData> getRecommendation_optimized() {

        String query = "";
        int countCaseConditionPlace = VisBuilder.dimentionAttributeArray.length + (VisBuilder.aggregateArray.length * VisBuilder.MeasureAttributeArray.length) + 1;
        int countColumns = 0;

        ArrayList<String> status = new ArrayList<String>();
        ArrayList<String> row;

        status.add("");
        query = concat(query, "SELECT ");

        for (int i = 0; i < VisBuilder.dimentionAttributeArray.length; i++) {
            query = concat(query, VisBuilder.dimentionAttributeArray[i], ",");
            status.add(concat("D-", VisBuilder.dimentionAttributeArray[i]));
            for (int j = 0; j < VisBuilder.aggregateArray.length; j++) {
                for (int k = 0; k < VisBuilder.MeasureAttributeArray.length; k++) {
                    vd = new VisData();
                    vd.x_axis = VisBuilder.dimentionAttributeArray[i];
                    vd.y_axis = VisBuilder.aggregateArray[j] + "(" + VisBuilder.MeasureAttributeArray[k] + ")";
                    VisCharts.recommendationList_optimized.add(vd);
                    if (i == VisBuilder.dimentionAttributeArray.length - 1) {
                        query = concat(query, VisBuilder.aggregateArray[j], "(", VisBuilder.MeasureAttributeArray[k], ")", ",");
                        status.add(concat("A-", VisBuilder.aggregateArray[j], "(", VisBuilder.MeasureAttributeArray[k], ")"));
                    }
                }
            }
        }

        query = concat(query, "CASE WHEN ", this.SpecificAttribute + this.firstOperator, this.firstSelector, " THEN 1",
                " WHEN ", this.SpecificAttribute, this.secondOperator, this.secondSelector, " THEN 2 END as g1",
                " FROM ", this.tableName, " GROUP BY ");

        status.add(concat("C-", "coniditon"));

        for (int i = 0; i < VisBuilder.dimentionAttributeArray.length; i++) {
            query = concat(query, VisBuilder.dimentionAttributeArray[i], ",");
        }

        query = concat(query, "g1");

        /*
        e.g:
        rs = sql.executeQuery("SELECT state,race,SUM(Charge),COUNT(Charge),CASE WHEN gender='M' THEN 1 WHEN gender='F' THEN 2 END as g1 "
                                + "FROM Traffic_Violations "
                                + "GROUP BY state,race,g1" );
        */
        rs = sql.executeQuery(query);

        try {
            while (rs.next()) {

                row = new ArrayList<String>();

                if (rs.getString(countCaseConditionPlace) != null) {

                    countColumns = 0;

                    while (countColumns < countCaseConditionPlace) {

                        countColumns++;
                        row.add(concat(status.get(countColumns), "-", rs.getString(countColumns)));
                    }
                }

                for (int i = 0; i < row.size(); i++) {

                    String xAxisTag, yAxisTag, xValue, yValue;

                    String[] rowSplitArray = row.get(i).split("-");

                    if (rowSplitArray[0].equals("D")) {

                        xAxisTag = rowSplitArray[1];
                        xValue = rowSplitArray[2];

                        for (int j = i; j < row.size(); j++) {

                            String[] rowSplitArray2 = row.get(j).split("-");

                            if (rowSplitArray2[0].equals("D")) {
                                continue;
                            } else if (rowSplitArray2[0].equals("A")) {

                                yAxisTag = rowSplitArray2[1];
                                yValue = rowSplitArray2[2];

                                for (int k = 0; k < VisCharts.recommendationList_optimized.size(); k++) {
                                    if (VisCharts.recommendationList_optimized.get(k).x_axis.equals(xAxisTag)
                                            && VisCharts.recommendationList_optimized.get(k).y_axis.equals(yAxisTag)) {

                                        if (row.get(countCaseConditionPlace - 1).split("-")[2].equals("1")) {
                                            String returnedValue = (VisCharts.recommendationList_optimized.get(k).recomQuery1.get(xValue));
                                            if (returnedValue == null) {
                                                VisCharts.recommendationList_optimized.get(k).recomQuery1.put(xValue, yValue);
                                            } else {
                                                String newValue = String.valueOf(Double.valueOf(VisCharts.recommendationList_optimized.get(k).recomQuery1.get(xValue)) + Double.valueOf(yValue));
                                                VisCharts.recommendationList_optimized.get(k).recomQuery1.put(xValue, newValue);
                                            }

                                        } else {
                                            String returnedValue = (VisCharts.recommendationList_optimized.get(k).recomQuery2.get(xValue));
                                            if (returnedValue == null) {
                                                VisCharts.recommendationList_optimized.get(k).recomQuery2.put(xValue, yValue);
                                            } else {
                                                String newValue = String.valueOf(Double.valueOf(VisCharts.recommendationList_optimized.get(k).recomQuery2.get(xValue)) + Double.valueOf(yValue));
                                                VisCharts.recommendationList_optimized.get(k).recomQuery2.put(xValue, newValue);
                                            }
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(VisExtract.class.getName()).log(Level.SEVERE, null, ex);
        }

        euclidean_metric(VisCharts.recommendationList_optimized);
        rank(VisCharts.recommendationList_optimized);
        
        return VisCharts.recommendationList_optimized;
    }

    //update the total_devation entry in each object
    private void euclidean_metric(ArrayList<VisData> myList) {
        /*  
                     Euclidean Distance
            d(p,q)=sqrt( (p1-q1)^2 + (p2-q2)^2 + (p2-q2)^2 + .... )
         
         */

        ArrayList arrayOfValues = new ArrayList();
        String pKey, qValueString;
        double pValue, qValue;

        for (int i = 0; i < myList.size(); i++) {

            for (Map.Entry iterator : myList.get(i).recomQuery2.entrySet()) {

                pKey = (String) iterator.getKey();
                pValue = Double.valueOf((String) iterator.getValue());

                qValueString = myList.get(i).recomQuery1.get(pKey);

                if (qValueString == null) {
                    qValue = 0;
                } else {
                    qValue = Double.valueOf(qValueString);
                }

                myList.get(i).totalDeviation += Math.pow(pValue - qValue, 2);
                arrayOfValues.add(pKey);
            }

            for (Map.Entry iterator : myList.get(i).recomQuery1.entrySet()) {
                if (arrayOfValues.contains((String) iterator.getKey())) {
                    continue;
                }
                myList.get(i).totalDeviation += Math.pow(Double.valueOf((String) iterator.getValue()), 2);
            }

            myList.get(i).totalDeviation = Math.sqrt(myList.get(i).totalDeviation);
        }
    }

    private void manhattan_metric(ArrayList<VisData> myList) {
        /*  
                     manhattan Distance 
            d(p,q)=abs (p1-q1) + abs(p2-q2) + abs(p3-q3) 
         
         */

        ArrayList arrayOfValues = new ArrayList();
        String pKey, qValueString;
        double pValue, qValue;

        for (int i = 0; i < myList.size(); i++) {

            for (Map.Entry iterator : myList.get(i).recomQuery2.entrySet()) {

                pKey = (String) iterator.getKey();
                pValue = Double.valueOf((String) iterator.getValue());

                qValueString = myList.get(i).recomQuery1.get(pKey);

                if (qValueString == null) {
                    qValue = 0;
                } else {
                    qValue = Double.valueOf(qValueString);
                }

                myList.get(i).totalDeviation += Math.abs(qValue - pValue);
                arrayOfValues.add(pKey);
            }

            for (Map.Entry iterator : myList.get(i).recomQuery1.entrySet()) {
                if (arrayOfValues.contains((String) iterator.getKey())) {
                    continue;
                }
                myList.get(i).totalDeviation += Math.abs(Double.valueOf((String) iterator.getValue()));
            }

        }
    }

    //rank visualizations according to Deviations 
    private void rank(ArrayList<VisData> myList) {
        Collections.sort(myList);
    }

    //Show visualizations
    public void showVis() {

        System.out.println();
        System.out.println("***************My main visualization**************** ");
        System.out.println();

        for (int j = 0; j < VisCharts.mainVisList.size(); j++) {

            System.out.println("First Query: ");
            System.out.println();

            for (Map.Entry iterator : VisCharts.mainVisList.get(j).recomQuery1.entrySet()) {

                System.out.println("**X-axis** ( " + VisCharts.mainVisList.get(j).x_axis + " ): " + iterator.getKey() + " **Y-axis** ( " + VisCharts.mainVisList.get(j).y_axis + " ): " + iterator.getValue());

            }

            System.out.println();
            System.out.println("Second Query: ");
            System.out.println();

            for (Map.Entry iterator : VisCharts.mainVisList.get(j).recomQuery2.entrySet()) {

                System.out.println("**X-axis** ( " + VisCharts.mainVisList.get(j).x_axis + " ): " + iterator.getKey() + " **Y-axis** ( " + VisCharts.mainVisList.get(j).y_axis + " ): " + iterator.getValue());

            }

        }

        System.out.println();
        System.out.println("***************Recommendations**************** ");
        System.out.println();

        for (int i = 0; i < VisCharts.recommendationList_bruteForce.size(); i++) {

            System.out.println();
            System.out.println("recommendation (" + (i + 1) + ")");
            System.out.println();

            System.out.println("Deviation: " + VisCharts.recommendationList_bruteForce.get(i).totalDeviation);
            System.out.println();

            System.out.println("First query: ");
            System.out.println();
            for (Map.Entry iterator : VisCharts.recommendationList_bruteForce.get(i).recomQuery1.entrySet()) {

                System.out.println("**X-axis** ( " + VisCharts.recommendationList_bruteForce.get(i).x_axis + " ): " + iterator.getKey() + " **Y-axis** ( " + VisCharts.recommendationList_bruteForce.get(i).y_axis + " ): " + iterator.getValue());

            }

            System.out.println();
            System.out.println("Second query: ");
            System.out.println();

            for (Map.Entry iterator : VisCharts.recommendationList_bruteForce.get(i).recomQuery2.entrySet()) {

                System.out.println("**X-axis** ( " + VisCharts.recommendationList_bruteForce.get(i).x_axis + " ): " + iterator.getKey() + " **Y-axis** ( " + VisCharts.recommendationList_bruteForce.get(i).y_axis + " ): " + iterator.getValue());

            }

            System.out.println();
        }

    }

    //Show visualizations
    public void showVis2() {

        System.out.println();
        System.out.println("***************Recommendations**************** ");
        System.out.println();

        for (int i = 0; i < VisCharts.recommendationList_optimized.size(); i++) {

            System.out.println();
            System.out.println("recommendation (" + (i + 1) + ")");
            System.out.println();

            System.out.println("Deviation: " + VisCharts.recommendationList_optimized.get(i).totalDeviation);
            System.out.println();

            System.out.println("First query: ");
            System.out.println();
            for (Map.Entry iterator : VisCharts.recommendationList_optimized.get(i).recomQuery1.entrySet()) {

                System.out.println("**X-axis** ( " + VisCharts.recommendationList_optimized.get(i).x_axis + " ): " + iterator.getKey() + " **Y-axis** ( " + VisCharts.recommendationList_optimized.get(i).y_axis + " ): " + iterator.getValue());

            }

            System.out.println();
            System.out.println("Second query: ");
            System.out.println();

            for (Map.Entry iterator : VisCharts.recommendationList_optimized.get(i).recomQuery2.entrySet()) {

                System.out.println("**X-axis** ( " + VisCharts.recommendationList_optimized.get(i).x_axis + " ): " + iterator.getKey() + " **Y-axis** ( " + VisCharts.recommendationList_optimized.get(i).y_axis + " ): " + iterator.getValue());

            }

            System.out.println();
        }

    }

}
