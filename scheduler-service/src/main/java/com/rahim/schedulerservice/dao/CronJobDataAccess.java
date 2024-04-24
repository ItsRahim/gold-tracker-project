package com.rahim.schedulerservice.dao;

/**
 * @author Rahim Ahmed
 * @created 24/04/2024
 */
public class CronJobDataAccess {

    private CronJobDataAccess() {}

    public static final String TABLE_NAME = "rgts.cron_jobs";
    public static final String COL_CRON__JOB_ID = "cj_id";
    public static final String COL_CRON_JOB_NAME = "cj_name";
    public static final String COL_CRON_JOB_PURPOSE = "cd_purpose";
    public static final String COL_CRON_JOB_SCHEDULE = "cj_schedule";
    public static final String COL_CRON_JOB_MICROSERVICE_AFFECTED = "cj_microservice_affected";
    public static final String COL_CRON_JOB_IS_ACTIVE = "cj_is_active";

}
