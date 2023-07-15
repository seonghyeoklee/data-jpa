package com.example.mysql.domain.post.service;

import com.example.mysql.domain.post.dto.PostCommand;
import com.example.mysql.domain.post.entity.Post;
import com.example.mysql.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostWriteService {
    private final PostRepository postRepository;

    public Long create(PostCommand command) {
        Post post = Post.builder()
                .memberId(command.memberId())
                .contents(command.content())
                .build();

        return postRepository.save(post).getId();
    }

    public void likePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.increaseLikeCount();
        postRepository.save(post);
    }
}
