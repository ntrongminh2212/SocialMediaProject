package com.example.postservice.service;

import com.example.postservice.entity.Post;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.util.PostExcelReporter;
import jakarta.servlet.http.HttpServletResponse;
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

    @Autowired
    PostRepository postRepository;

    public void exportPostsByDayToExcel(String dayFrom, String dayTo, HttpServletResponse response) throws ParseException, IOException {
        response.setContentType("application/octet-stream");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");


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

//    @Scheduled()
//    public void exportPostsByDayToExcel(String dayFrom, String dayTo, HttpServletResponse response) throws ParseException, IOException {
//
//    }
}
