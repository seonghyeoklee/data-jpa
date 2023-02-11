package com.example.mysql.application.usecase;

import com.example.mysql.domain.follow.entity.Follow;
import com.example.mysql.domain.follow.service.FollowReadService;
import com.example.mysql.domain.post.entity.Post;
import com.example.mysql.domain.post.service.PostReadService;
import com.example.mysql.utils.CursorRequest;
import com.example.mysql.utils.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTimelinePostsUseCase {
    private final FollowReadService followReadService;
    private final PostReadService postReadService;

    public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest) {
        List<Follow> follows = followReadService.getFollows(memberId);
        List<Long> memberIds = follows.stream()
                .map(Follow::getToMemberId)
                .toList();

        return postReadService.getPosts(memberIds, cursorRequest);
    }
}
