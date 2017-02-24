dbEnvt=sqlserver

getCount=select COUNT(*) cnt from DMS.dbo.<<tableName>>;
getColumnInfo=USE [DMS] select COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION, DATETIME_PRECISION, IS_NULLABLE from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = '<<tableName>>'
getrows=select <<columnName>> from DMS.dbo.<<tableName>>;
insertRecords=INSERT INTO DMS.dbo.<<tableName>> (<<columnName>>) VALUES (<<columnValues>>);

getThirdPartyFileInfo=SELECT TOP 1 ReportStartDate, ReportEndDate, FileName, FileRecordCount, FileStatusID FROM [DMS].[dbo].[ThirdPartyReportFileRequest_Queue] where FileTypeId = $fileTypeID$ order by ThirdPartyReportFileRequestQueueID desc;

getMeasurementCountCreativeDelivery=SELECT sum([impressions]) as total_impressions,sum([cost]) as total_cost ,sum([clicks]) as total_clicks FROM [NAN_STATS].[dbo].[impression_creatives_stats] (nolock) where campaign_ID > 1000000 and convert(date, dateadd(dd, date_id,'1999-12-31'), 101) between '$START_DATE$' and '$END_DATE$'
getMeasurementCountCreativeConversion=SELECT sum([click_based]) as total_click_based_conversion, (count(1) - sum([click_based])) as total_view_based_conversion FROM [NAN_STATS].[dbo].[conversion_stats_date] (nolock) where campaign_ID > 1000000 and convert(date, dateadd(dd, date_id,'1999-12-31'), 101) between '$START_DATE$' and '$END_DATE$'
getMeasurementCountTraitConversion=SELECT sum([click_based]) as total_click_based_conversion, (count(1) - sum([click_based])) as total_view_based_conversion FROM [NAN_STATS].[dbo].[conversion_stats_date] (NOLOCK) WHERE campaign_id > 1000000  AND convert(date, dateadd(dd, date_id,'1999-12-31'), 101) BETWEEN '$START_DATE$' and '$END_DATE$'
getMeasurementCountTraitDelivery=SELECT sum([impressions]) as total_impressions,sum([cost]) as total_cost ,sum([clicks]) as total_clicks FROM [NAN_STATS].[dbo].[impression_categories_stats] (nolock) where campaign_ID > 1000000 and convert(date, dateadd(dd, date_id,'1999-12-31'), 101) between '$START_DATE$' and '$END_DATE$'
getMeasurementCountDomainConversion=SELECT sum([click_based]) as total_click_based_conversion, (count(1) - sum([click_based])) as total_view_based_conversion FROM [NAN_STATS].[dbo].[conversion_stats_date] (NOLOCK) WHERE campaign_id > 1000000  AND convert(date, dateadd(dd, date_id,'1999-12-31'), 101) BETWEEN '$START_DATE$' and '$END_DATE$'
getMeasurementCountDomainDelivery=SELECT sum([impressions]) as total_impressions,sum([cost]) as total_cost ,sum([clicks]) as total_clicks FROM [NAN_STATS].[dbo].[impression_creatives_stats] (nolock) where campaign_ID > 1000000 and convert(date, dateadd(dd, date_id,'1999-12-31'), 101) between '$START_DATE$' and '$END_DATE$'

getCreativeMeasurementByColumn=SELECT da.[$ColumnName$],sum([impressions]) AS total_impressions,sum([cost]) AS total_cost,sum([clicks]) AS total_clicks FROM [NAN_STATS].[dbo].[impression_creatives_stats] cs (nolock) inner join [DMS].[dbo].[Display2_Advertisers] da on da.Legacy_ID = cs.Advertiser_ID where campaign_ID > 1000000 and convert(date, dateadd(dd, date_id,'1999-12-31'), 101) between '$START_DATE$' and '$END_DATE$' group by da.[$ColumnName$]
getCountCreativeConversionLevelForAdvertiser=select [advertiser_id] AS advertiser_id  ,total_click_based_conversion ,COALESCE(conversions, 0)-COALESCE(total_click_based_conversion,0) as total_view_based_conversion from ( SELECT da.[advertiser_id] , count(1) as Conversions, sum(click_based) as total_click_based_conversion FROM [NAN_STATS].[dbo].[conversion_stats_date] cs (nolock) inner join [DMS].[dbo].[Display2_Advertisers] da on da.Legacy_ID = cs.Advertiser_ID where campaign_ID > 1000000 and convert(date, dateadd(dd, date_id,'1999-12-31'), 101) between '$START_DATE$' and '$END_DATE$' group by da.[advertiser_id]) a
getCountCreativeDeliveryLevel=SELECT $ColumnName$ AS $ColumnName$,sum([impressions]) AS total_impressions,sum([cost]) AS total_cost,sum([clicks]) AS total_clicks FROM [NAN_STATS].[dbo].[impression_creatives_stats] (nolock) where campaign_ID > 1000000 and convert(date, dateadd(dd, date_id,'1999-12-31'), 101) between '$START_DATE$' and '$END_DATE$' group by $ColumnName$
getCountCreativeConversionLevel=select $ColumnName$ AS $ColumnName$ ,total_click_based_conversion ,COALESCE(conversions, 0)-COALESCE(total_click_based_conversion,0) as total_view_based_conversion from ( SELECT $ColumnName$ , count(1) as Conversions, sum(click_based) as total_click_based_conversion FROM [NAN_STATS].[dbo].[conversion_stats_date] (nolock) where campaign_ID > 1000000 and convert(date, dateadd(dd, date_id,'1999-12-31'), 101) between '$START_DATE$' and '$END_DATE$' group by $ColumnName$) a
getCreativeMeasurementCampaign=SELECT ct.[$ColumnName$] ,sum(ic.[impressions]) AS total_impressions,sum(ic.[cost]) AS total_cost,sum(ic.[clicks]) AS total_clicks FROM [NAN_STATS].[dbo].[impression_creatives_stats] (nolock) ic INNER JOIN [DMS].[dbo].[Display2_Campaign_Targets] ct ON ic.Campaign_ID = ct.Campaign_Target_ID where ic.campaign_ID > 1000000 and convert(date, dateadd(dd, ic.date_id,'1999-12-31'), 101) between '$START_DATE$' and '$END_DATE$' group by ct.[$ColumnName$]
getCountCreativeConversionLevelForCampaign=SELECT [Campaign_ID] AS Campaign_ID ,total_click_based_conversion ,COALESCE(conversions, 0)-COALESCE(total_click_based_conversion,0) as total_view_based_conversion from ( SELECT ct.[Campaign_ID] , count(1) as Conversions, sum(click_based) as total_click_based_conversion FROM [NAN_STATS].[dbo].[conversion_stats_date] (nolock) ic INNER JOIN [DMS].[dbo].[Display2_Campaign_Targets] ct ON ic.Campaign_ID = ct.Campaign_Target_ID where ic.campaign_ID > 1000000 and convert(date, dateadd(dd, ic.date_id,'1999-12-31'), 101) between '$START_DATE$' and '$END_DATE$' group by ct.[Campaign_ID] ) a



getCountTraitDeliveryLevelByAdvertiser=SELECT da.[$ColumnName$] as $ColumnName$ ,SUM(a.impressions) as total_impressions,SUM(a.cost) as total_cost,SUM(a.clicks) as total_clicks FROM NAN_STATS..impression_categories_stats a (NOLOCK) inner join [DMS].[dbo].[Display2_Advertisers] da on da.Legacy_ID = a.[$ColumnName$] WHERE convert(date, dateadd(dd, a.date_id,'1999-12-31'), 101) BETWEEN '$START_DATE$' and '$END_DATE$' AND a.campaign_id > 1000000 GROUP BY da.[$ColumnName$]
getCountTraitDeliveryLevelByCampaign=SELECT ct.[$ColumnName$] AS $ColumnName$,SUM(a.impressions) as total_impressions,SUM(a.cost) as total_cost,SUM(a.clicks) as total_clicks FROM NAN_STATS..impression_categories_stats a (NOLOCK) INNER JOIN [DMS].[dbo].[Display2_Campaign_Targets] ct ON a.[$ColumnName$] = ct.Campaign_Target_ID WHERE convert(date, dateadd(dd, a.date_id,'1999-12-31'), 101) BETWEEN '$START_DATE$' and '$END_DATE$' AND a.[$ColumnName$] > 1000000 GROUP BY ct.[$ColumnName$]