package com.example.mysql.domain.post;

import com.example.mysql.domain.post.entity.Post;
import com.example.mysql.domain.post.repository.PostRepository;
import com.example.mysql.util.PostFixtureFactory;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsertTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void bulkInsert() {
        EasyRandom easyRandom = PostFixtureFactory.get(3L, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Post> posts = IntStream.range(0, 10_000 * 100)
                .parallel()
                .mapToObj(value -> easyRandom.nextObject(Post.class))
                .toList();
        stopWatch.stop();
        System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeSeconds());

        StopWatch queryStopWatch = new StopWatch();
        queryStopWatch.start();
        postRepository.bulkInsert(posts);
        queryStopWatch.stop();

        System.out.println("쿼리 실행 시간 : " + queryStopWatch.getTotalTimeSeconds());
    }
}
