package com.rahim.schedulerservice.repository;

import com.rahim.schedulerservice.dao.CronJobDataAccess;
import com.rahim.schedulerservice.model.CronJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 24/04/2024
 */
@Repository
public interface CronJobRepository extends JpaRepository<CronJob, Integer> {

    @Query(value = "SELECT "
            + CronJobDataAccess.COL_CRON_JOB_NAME
            + ","
            + CronJobDataAccess.COL_CRON_JOB_SCHEDULE
            + " FROM "
            + CronJobDataAccess.TABLE_NAME
            + " WHERE "
            + CronJobDataAccess.COL_CRON_JOB_IS_ACTIVE
            + " = 'true'", nativeQuery = true)
    List<CronJob> getCronJobSchedule();

}
