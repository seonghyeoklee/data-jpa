package com.example.mysql.util;

import com.example.mysql.domain.member.entity.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class MemberFixtureFactory {

    public static Member create() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        return new EasyRandom(parameters).nextObject(Member.class);
    }

    public static Member create(Long seed) {
        EasyRandomParameters parameters = new EasyRandomParameters().seed(seed);
        return new EasyRandom(parameters).nextObject(Member.class);
    }
}
