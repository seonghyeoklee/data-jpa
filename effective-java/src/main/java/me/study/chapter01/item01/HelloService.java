package me.study.chapter01.item01;

public interface HelloService {

    String hello();

    // 정적 메소드
    static String hi() {
        prepare();
        return "hi";
    }

    static String hi2() {
        prepare();
        return "hi";
    }

    // private 메소드를 생성할 수 있다.
    static private void prepare() {
    }

    // 기본 메소드
    default String bye() {
        return "bye";
    }

}
