package com.example.postservice.service;

import com.example.postservice.dto.PostReactionDTO;
import com.example.postservice.entity.PostReaction;
import com.example.postservice.mapper.PostReactionMapper;
import com.example.postservice.repository.PostReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostReactionServiceImpl implements PostReactionService {
    @Autowired
    PostReactionRepository reactionRepository;
    @Autowired
    PostReactionMapper reactionMapper;

    @Override
    public Optional<PostReactionDTO> reactToPost(PostReactionDTO postReactionDTO) {
        Optional<PostReaction> optional = reactionRepository.findByPostIdAndUserId(
                postReactionDTO.getPostId(),
                postReactionDTO.getUserId()
        );
        if (optional.isPresent()){
            return Optional.ofNullable(reactionMapper.postReactionToDTO(optional.get()));
        }

        PostReaction postReaction = reactionMapper.postReactionToEntity(postReactionDTO);
        return Optional.ofNullable(reactionMapper.postReactionToDTO(
                reactionRepository.save(postReaction)
        ));
    }
}
