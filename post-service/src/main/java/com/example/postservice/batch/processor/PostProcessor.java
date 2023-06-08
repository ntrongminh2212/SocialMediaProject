package com.example.postservice.batch.processor;

import com.example.postservice.dto.PostDTO;
import com.example.postservice.entity.Post;
import com.example.postservice.mapper.PostMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class PostProcessor implements ItemProcessor<Post, PostDTO> {

    @Autowired
    PostMapper postMapper;
    @Override
    public PostDTO process(Post post) throws Exception {
        return postMapper.postToDTO(post,false);
    }
}
