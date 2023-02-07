package com.example.mysql.controller;

import com.example.mysql.domain.post.dto.DailyPostCount;
import com.example.mysql.domain.post.dto.DailyPostCountRequest;
import com.example.mysql.domain.post.dto.PostCommand;
import com.example.mysql.domain.post.entity.Post;
import com.example.mysql.domain.post.service.PostReadService;
import com.example.mysql.domain.post.service.PostWriteService;
import com.example.mysql.utils.CursorRequest;
import com.example.mysql.utils.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostWriteService postWriteService;
    private final PostReadService postReadService;

    @PostMapping("/posts")
    public Long create(PostCommand command) {
        return postWriteService.create(command);
    }

    @GetMapping("/posts/daily-post-counts")
    public List<DailyPostCount> getDailyPostCount(DailyPostCountRequest request) {
        return postReadService.getDailyPostCount(request);
    }

    @GetMapping("/posts/members/{memberId}")
    public Page<Post> getPosts(@PathVariable Long memberId, Pageable pageable) {
        return postReadService.getPosts(memberId, pageable);
    }

    @GetMapping("/posts/members/{memberId}/by-cursor")
    public PageCursor<Post> getPosts(@PathVariable Long memberId, CursorRequest cursorRequest) {
        return postReadService.getPosts(memberId, cursorRequest);
    }
}
