package com.ignitionone.datastorm.datorama.etl;

import com.ignitionone.datastorm.datorama.util.Jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitin.poddar on 2/1/2017.
 */
public class DatoramaNanETL extends SqlBaseClass {

    public static String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String reportStartDate;
    public static String reportEndDate;
    public static int recordCount;
    public static int fileStatusID;
    public static String fileName;
    public static int total_impressions;
    public static int total_clicks;
    public static double total_cost;
    public static int total_click_based_conversion;
    public static int total_view_based_conversion;

    Jdbc jdbc = new Jdbc();

    public int getStoreProcedureCount(String sqlFile, String environment, String sqlQueryName, String oldString1, String newString1, String oldString2, String newString2) throws Exception {
        sqlSetup(sqlFile, sqlQueryName, environment);
        return jdbc.getStoreProcedureRecordCount(strSQL.replace(oldString1, newString1).replace(oldString2, newString2), connectionURL);
    }

    public int getStoreProcedureCount(String sqlFile, String environment, String sqlQueryName ) throws Exception {
        sqlSetup(sqlFile, sqlQueryName, environment);
        return jdbc.getStoreProcedureRecordCount(strSQL, connectionURL);
    }

    public void executeThirdPartyFileInfo(String sqlFile, String environment, String sqlQueryName, String oldString, String newString) throws Exception {
        sqlSetup(sqlFile, sqlQueryName, environment);
        getThirdPartyInfo(strSQL.replace(oldString, newString), connectionURL);
    }

    public void getMeasurementCount(String sqlFile, String environment, String sqlQueryName, String oldString1, String newString1, String oldString2, String newString2) throws Exception {
        sqlSetup(sqlFile, sqlQueryName, environment);
        if (sqlQueryName.contains("Delivery")){
            getMeasurementCountDelivery(strSQL.replace(oldString1, newString1).replace(oldString2, newString2), connectionURL);
        } else if (sqlQueryName.contains("Conversion")){
            getMeasurementCountConversion(strSQL.replace(oldString1, newString1).replace(oldString2, newString2), connectionURL);
        }
    }

    public void getMeasurementCount(String sqlFile, String environment, String sqlQueryName, String oldString1, String newString1, String oldString2, String newString2,String OldColumn,String NewColumn) throws Exception {
        sqlSetup(sqlFile, sqlQueryName, environment);
        if (sqlQueryName.contains("Delivery")){
            getMeasurementCountDelivery(strSQL.replace(oldString1, newString1).replace(oldString2, newString2).replace(OldColumn,NewColumn), connectionURL);
        } else if (sqlQueryName.contains("Conversion")){
            getMeasurementCountConversion(strSQL.replace(oldString1, newString1).replace(oldString2, newString2).replace(OldColumn,NewColumn), connectionURL);
        }
    }

    public void getThirdPartyInfo(String query, String connectionUrl) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

        ResultSet rs = null;
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(DRIVER).newInstance();
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                reportStartDate=rs.getDate("ReportStartDate").toString();
                reportEndDate=rs.getDate("ReportEndDate").toString();
                fileName =rs.getString("FileName").toString();
                recordCount=rs.getInt("FileRecordCount");
                fileStatusID=rs.getInt("FileStatusID");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close();
            stmt.close();
            conn.close();
        }
    }

    public void getMeasurementCountDelivery(String query, String connectionUrl) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

        ResultSet rs = null;
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(DRIVER).newInstance();
            //conn = DriverManager.getConnection(connectionUrl,"siasp", "siasp1871");
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            StringBuffer coloumn = new StringBuffer();
            while(rs.next()){
                total_impressions = rs.getInt("total_impressions");
                total_clicks = rs.getInt("total_clicks");
                total_cost = rs.getDouble("total_cost");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close();
            stmt.close();
            conn.close();
        }
    }

    public void getMeasurementCountConversion(String query, String connectionUrl) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

        ResultSet rs = null;
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(DRIVER).newInstance();
            //conn = DriverManager.getConnection(connectionUrl,"siasp", "siasp1871");
            conn = DriverManager.getConnection(connectionUrl);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            StringBuffer coloumn = new StringBuffer();
            while(rs.next()){
                total_click_based_conversion = rs.getInt("total_click_based_conversion");
                total_view_based_conversion = rs.getInt("total_view_based_conversion");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close();
            stmt.close();
            conn.close();
        }
    }
    //Calling Store Procedure by replacing two values from the SQL Query
    public List<String> getStoredProcedure(String sqlFile, String environment, String sqlQueryName, String oldString1, String newString1, String oldString2, String newString2) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        sqlSetup(sqlFile, sqlQueryName, environment);
        List<String> result = jdbc.executeStoredProcedure(strSQL.replace(oldString1, newString1).replace(oldString2, newString2), connectionURL);
        return result;
    }

    //Calling Store Procedure without any value replacement from SQL Query
    public List<String> getStoredProcedure(String sqlFile, String environment, String sqlQueryName) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        sqlSetup(sqlFile, sqlQueryName, environment);
        List<String> result = jdbc.executeStoredProcedure(strSQL, connectionURL);
        return result;
    }

    public List<String> executeQuery(String sqlFile, String environment, String sqlQueryName, String oldString1, String newString1, String oldString2, String newString2, String oldString3, String newString3) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        sqlSetup(sqlFile, sqlQueryName, environment);
        List<String> result = jdbc.executeSQL(strSQL.replace(oldString1, newString1).replace(oldString2, newString2).replace(oldString3, newString3), connectionURL);
        return result;
    }

    public static List<String> getFirstNRows(List<String> list, int numberOfRows) {
        List<String> result = new ArrayList<String>();
        for (int i=0;i<numberOfRows;i++){
            result.add(list.get(i));
        }
        return result;
    }
}
