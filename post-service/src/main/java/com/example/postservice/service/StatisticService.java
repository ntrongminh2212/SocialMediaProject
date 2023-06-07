package com.example.postservice.service;

import com.example.postservice.entity.Post;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.util.PostExcelReporter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.apache.log4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class StatisticService {

    Logger logger = Logger.getLogger(StatisticService.class);
    @Autowired
    PostRepository postRepository;
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    Job job;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat datetimeFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final int SCHEDULING_TIME = 1000*60*5;
    private static final int DAY = 1000*60*60*24;

    public void exportPostsByDayBetweenToExcel(String dayFrom, String dayTo, HttpServletResponse response) throws ParseException, IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=posts_" + dayFrom + "_" + dayTo + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Post> listPost = postRepository.findAllByCreatedTimeBetween(
                dateFormatter.parse(dayFrom),
                dateFormatter.parse(dayTo)
        );

        PostExcelReporter excelExporter = new PostExcelReporter(listPost);

        excelExporter.export(response,"Post Created Between "+dayFrom+" to "+dayTo);
    }

    @Scheduled(fixedRate = SCHEDULING_TIME)
    public void exportPostsScheduling() throws ParseException, IOException {
        Date dayTo = new Date();
        Date dayFrom = new Date(dayTo.getTime()-DAY);

        List<Post> listPost = postRepository.findAllByCreatedTimeBetween(
                dateFormatter.parse(dateFormatter.format(dayFrom)),
                dateFormatter.parse(dateFormatter.format(dayTo))
        );

        PostExcelReporter excelExporter = new PostExcelReporter(listPost);
        excelExporter.export(
                "Post Created Between " + dateFormatter.format(dayFrom) + " to " + dateFormatter.format(dayTo),
                "posts_" + dateFormatter.format(dayFrom) + "_" + dateFormatter.format(dayTo) + ".xlsx"
        );
        logger.info("Excel file export at "+datetimeFormatter.format(new Date()));
    }

    public void exportPostsToCsv() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
