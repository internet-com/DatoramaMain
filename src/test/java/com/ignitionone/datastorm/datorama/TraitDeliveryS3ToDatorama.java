
package com.ignitionone.datastorm.datorama;

import au.com.bytecode.opencsv.CSVReader;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.ignitionone.datastorm.datorama.AmazonServices.S3Functions;
import com.ignitionone.datastorm.datorama.datoramaUtil.JsonParser;
import com.ignitionone.datastorm.datorama.apiUtil.APIRequestBodyGenerator;
import com.ignitionone.datastorm.datorama.apiUtil.APIUtil;
import com.ignitionone.datastorm.datorama.etl.DestinationTable;
import com.ignitionone.datastorm.datorama.etl.FileLevel;
import com.ignitionone.datastorm.datorama.etl.RecordLevel;
import com.ignitionone.datastorm.datorama.util.CSVandTextReader;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.ignitionone.datastorm.datorama.etl.DataType.VARCHAR;
import static com.ignitionone.datastorm.datorama.etl.ValidationStyle.IGNORE;
import static com.ignitionone.datastorm.datorama.etl.ValidationStyle.MATCH;
import static com.ignitionone.datastorm.datorama.etl.ValidationStyle.SUBSTRING;
import static io.restassured.path.json.JsonPath.from;


/**
 * Created by nitin.poddar on 1/13/2017.
 */

public class TraitDeliveryS3ToDatorama extends ApiBaseClass{

    private static final String REPORT_HEADER = "Compare Trait Delivery File between Amazon S3 and Datorama Stream Using API";
    private static final String REPORT_TITLE = "This test is to verify Trait Delivery file is picked and processed properly by Datorama.";
    public JsonParser parser = new JsonParser();
    String envt;
    String SOURCE_TABLE = "Trait Delivery API Response";
    String DESTINATION_TABLE = "Trait Delivery CSV File";
    S3Functions s3Functions = new S3Functions();
    AmazonS3 s3 = new AmazonS3Client();
    File traitDeliveryFile;
    String traitDeliveryFilePath;
    String Bucket_Name = "thirdpartyreporting";
    String traitDeliveryFileName = "SummarizedTraitDeliveryEventData_";
    String traitDeliveryDirectory = "Datorama/Final/EventData/Summarized/Trait/Delivery";

    @BeforeClass
    @Parameters(value = {"environment"})
    public void setUp(String environment) throws Exception {
        envt = environment;
        setUp(envt,REPORT_HEADER, REPORT_TITLE);
         //traitDeliveryFilePath = s3Functions.getFilePathFromBucket(Bucket_Name, s3, traitDeliveryFileName, traitDeliveryDirectory);
         //traitDeliveryFile=s3Functions.DownloadCSVFromS3(Bucket_Name,s3, traitDeliveryFilePath,"TraitDeliverySummarizedData");
    }

    @Test
    public void traitDeliveryToDatorama() throws Exception {

        RecordLevel recordLevel = new RecordLevel();
        extentReportUtil.logInfo("Reading Mapping between Source Table : " + SOURCE_TABLE + " and Destination Table : " + DESTINATION_TABLE);
        Map<String, DestinationTable> validate = new HashMap<>();

        String AuthResponse = APIUtil.getResponseAsString("/auth/authenticate", APIRequestBodyGenerator.getAuthRequestBody());
        String token=from(AuthResponse).get("token");

        String Resp = APIUtil.getResportAsString("/query/execBatchQuery",APIRequestBodyGenerator.getTraitDelivery("2017-01-01", "2017-01-31"), token);
        JSONObject jsonObject = parser.convertStringtoJsonObj(Resp);
        List<String> traitDeliverySrcList = parser.convertJsonToList(jsonObject);

        DestinationTable day  = new DestinationTable("Date", true, VARCHAR, MATCH);
        DestinationTable advertiserId = new DestinationTable("BU_ID", false, VARCHAR, MATCH);
        DestinationTable campaignId = new DestinationTable("Campaign_ID", false, VARCHAR, MATCH);
        DestinationTable campaign = new DestinationTable("Campaign_Name", false, VARCHAR, MATCH);
        DestinationTable campaignFlightStartDate = new DestinationTable("Campaign_Flightdate_Start", false, VARCHAR, IGNORE);
        DestinationTable campaignFlightEndDate = new DestinationTable("Campaign_Flightdate_End", false, VARCHAR, IGNORE);
        DestinationTable accountManagerId = new DestinationTable("Account_Manager_ID", false, VARCHAR, MATCH);
        DestinationTable campaignStatus = new DestinationTable("Campaign_Status", false, VARCHAR, MATCH);
        DestinationTable campaignTargetId = new DestinationTable("Campaign_Target_ID", true, VARCHAR, MATCH);
        DestinationTable campaignTargetName = new DestinationTable("Campaign_Target_Name", false, VARCHAR, MATCH);
        DestinationTable campaignTargetStartDate = new DestinationTable("Campaign_Target_Flightdate_Start", false, VARCHAR, IGNORE);
        DestinationTable campaignTargetEndDate = new DestinationTable("Campaign_Target_Flightdate_End", false, VARCHAR, IGNORE);
        DestinationTable campaignTargetStatus = new DestinationTable("Campaign_Target_Status", false, VARCHAR, MATCH);
        DestinationTable integrationName = new DestinationTable("Integration_Name", false, VARCHAR, MATCH);
        DestinationTable currencyCode = new DestinationTable("Currency_Code", false, VARCHAR, SUBSTRING);
        DestinationTable traitId = new DestinationTable("Trait_ID", true, VARCHAR, MATCH);
        DestinationTable traitName = new DestinationTable("Trait_Name", false, VARCHAR, SUBSTRING);
        DestinationTable impressions = new DestinationTable("Impressions", false, VARCHAR, SUBSTRING);
        DestinationTable clicks = new DestinationTable("Clicks", false, VARCHAR, SUBSTRING);
        DestinationTable integrationId = new DestinationTable("Integration_ID", true, VARCHAR, MATCH);
        DestinationTable cost = new DestinationTable("Rounded Cost", false, VARCHAR, SUBSTRING);
        DestinationTable advertiserSourceId = new DestinationTable("Advertiser_Source_ID", false, VARCHAR, MATCH);
        DestinationTable advertiserSource = new DestinationTable("Advertiser_Source_Name", false, VARCHAR, MATCH);

        validate.put("Day", day);
        validate.put("Advertiser ID", advertiserId);
        validate.put("Campaign ID", campaignId);
        validate.put("Campaign", campaign);
        validate.put("Campaign Flight Start Date", campaignFlightStartDate);
        validate.put("Campaign Flight End Date", campaignFlightEndDate);
        validate.put("Account Manager ID", accountManagerId);
        validate.put("Campaign Status", campaignStatus);
        validate.put("Line Item ID", campaignTargetId);
        validate.put("Line Item", campaignTargetName);
        validate.put("Line Item Flight Start Date", campaignTargetStartDate);
        validate.put("Line Item Flight End Date", campaignTargetEndDate);
        validate.put("Line Item Status", campaignTargetStatus);
        validate.put("Publisher", integrationName);
        validate.put("Currency (Original)", currencyCode);
        validate.put("Segment ID", traitId);
        validate.put("Segment", traitName);
        validate.put("Publisher ID", integrationId);
        validate.put("Trait Impressions", impressions);
        validate.put("Trait Clicks", clicks);
        validate.put("Trait Media Cost", cost);
        validate.put("Advertiser Source ID", advertiserSourceId);
        validate.put("Advertiser Source", advertiserSource);

        CSVandTextReader csvReader = new CSVandTextReader();
        //CSVReader reader = new CSVReader(new FileReader(System.getProperty("user.dir")+"/"+"SummarizedTraitDeliveryEventData.csv"), '|');
        //List <String> traitDeliveryDestList=reader.readAll();
        List <String> traitDeliveryDestList = csvReader.getCSVData(System.getProperty("user.dir")+"/"+"SummarizedTraitDeliveryEventData.csv");


        extentReportUtil.startTest("File level tests <BR> Verify Data Types <BR> Source Table : " + SOURCE_TABLE + " and Destination Table : " + DESTINATION_TABLE, "Verify Data Types for each column between Source Table : " + SOURCE_TABLE + " and Destination Table : " + DESTINATION_TABLE);
        FileLevel fileLevel= new FileLevel();
        fileLevel.verifyTableCount(traitDeliverySrcList,"Trait Delivery API Response", traitDeliveryDestList, "Trait Delivery CSV Data");

        recordLevel.verifySrcWithDestData(validate,traitDeliverySrcList,traitDeliveryDestList);
    }
    @AfterClass(alwaysRun = true)
    public void generateReport() {
        extentReportUtil.endTest();
        extentReportUtil.writeReport();
    }
}

