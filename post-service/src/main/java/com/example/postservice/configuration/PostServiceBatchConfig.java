package com.example.postservice.configuration;

import com.example.postservice.batch.processor.PostProcessor;
import com.example.postservice.dto.PostDTO;
import com.example.postservice.entity.Post;
import com.example.postservice.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class PostServiceBatchConfig {
    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;
    @Autowired
    private PostRepository postRepository;

    @Bean
    RepositoryItemReader<Post> repositoryPostReader(){
        HashMap<String, Direction> sorts = new HashMap<>();
        sorts.put("postId", Direction.ASC);
        RepositoryItemReader<Post> repositoryPostReader = new RepositoryItemReader<>();
        repositoryPostReader.setRepository(postRepository);
        repositoryPostReader.setMethodName("findAll");
        repositoryPostReader.setSort(sorts);
        return repositoryPostReader;
    }

    @Bean
    PostProcessor postProcessor(){
        return new PostProcessor();
    }

    @Bean
    FlatFileItemWriter<PostDTO> flatFilePostWriter(){
        FlatFileItemWriter<PostDTO> flatFilePostWriter = new FlatFileItemWriter<>();
        flatFilePostWriter.setResource(new FileSystemResource("src/main/resources/file/post.csv"));
        flatFilePostWriter.setHeaderCallback(new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write("Post ID,Creator ID,Status Content,Attachment URL,Created Time,Updated Time,Comments Count,Reactions Count");
            }
        });
        flatFilePostWriter.setLineAggregator(postLineAggregator());
        return flatFilePostWriter;
    }

    private LineAggregator<PostDTO> postLineAggregator() {
        BeanWrapperFieldExtractor<PostDTO> fieldExtractor = new BeanWrapperFieldExtractor<PostDTO>();
        fieldExtractor.setNames(new String[]{"postId", "userId", "statusContent", "attachmentUrl", "createdTime", "updatedTime","commentsCount","postReactionsCount"});

        DelimitedLineAggregator<PostDTO> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);
        return lineAggregator;
    }

    @Bean
    Step step1(){
        return new StepBuilder("post-to-csv",jobRepository)
                .<Post,PostDTO>chunk(10,transactionManager)
                .reader(repositoryPostReader())
                .processor(postProcessor())
                .writer(flatFilePostWriter())
                .build();
    }

    @Bean
    public Job postToCsvJob() {
        return new JobBuilder("postToCsvJob", jobRepository)
                .flow(step1())
                .end()
                .build();
    }
}
