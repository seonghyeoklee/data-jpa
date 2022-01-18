# 프로젝트 생성

스프링 부트 스타터

[Spring Initializr](https://start.spring.io/)

### 프로젝트 스펙

- Project `Gradle Project`
- Language `Java`
- `Spring boot 2.6.2`
- Packaging `Jar`
- `Java 11`
- **Dependencies**

  `Spring Web`

  `Spring Data JPA`

  `H2 Database`

  `Lombok`


# Querydsl 설정과 검증

### 스프링 부트 2.6 이상, Querydsl 5.0 지원 방법

최근 `스프링 부트 2.6` 이상 버전에서는 `Querydsl 5.0`을 사용한다. 변경된 사항에 따라 `build.gradle` 설정을 변경해야 한다.

- `querydsl-jpa`, `querydsl-apt` 를 추가하고 버전을 명시해야 한다.
- 변경사항
    - **PageableExecutionUtils 클래스 사용 패키지 변경**
        - 기능이 Deprecated 된 것은 아니고, 사용 패키지 위치가 변경되었다. 기존 위치를 신규 위치로
          변경하면 문제 없이 사용할 수 있다.
    - **Querydsl fetchResults(), fetchCount() Deprecated(향후 미지원)**
        - Querydsl은 향후 fetchCount() , fetchResult() 를 지원하지 않기로 결정했다.

```java
buildscript {
	ext {
		queryDslVersion = "5.0.0"
	}
}

plugins {
	id 'org.springframework.boot' version '2.6.2'
	id 'io.spring.dependency-management' version '1.0.11.RELEASE'
	//querydsl 추가
	id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
	id 'java'
}

group = 'study'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'

	//querydsl 추가
	implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
	annotationProcessor "com.querydsl:querydsl-apt:${queryDslVersion}"

	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
	useJUnitPlatform()
}

//querydsl 추가 시작
def querydslDir = "$buildDir/generated/querydsl"

querydsl {
	jpa = true
	querydslSourcesDir = querydslDir
}

sourceSets {
	main.java.srcDir querydslDir
}

configurations {
	querydsl.extendsFrom compileClasspath
}

compileQuerydsl {
	options.annotationProcessorPath = configurations.querydsl
}
```

- *만약 2.6 이하 버전이라면*

    ```java
    plugins {
    	id 'org.springframework.boot' version ‘2.2.2.RELEASE'
    	id 'io.spring.dependency-management' version '1.0.8.RELEASE'
    	//querydsl 추가
    	id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
    	id 'java'
    }
    
    group = 'study'
    version = '0.0.1-SNAPSHOT'
    sourceCompatibility = '1.8'
    
    configurations {
    	compileOnly {
    		extendsFrom annotationProcessor
    	}
    }
    
    repositories {
    	mavenCentral()
    }
    
    dependencies {
    	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    	implementation 'org.springframework.boot:spring-boot-starter-web'
    
    	//querydsl 추가
    	implementation 'com.querydsl:querydsl-jpa'
    
    	compileOnly 'org.projectlombok:lombok'
    	runtimeOnly 'com.h2database:h2'
    	annotationProcessor 'org.projectlombok:lombok'
    	testImplementation('org.springframework.boot:spring-boot-starter-test') {
    		exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    	}
    }
    
    test {
    	useJUnitPlatform()
    }
    
    //querydsl 추가 시작
    def querydslDir = "$buildDir/generated/querydsl"
    
    querydsl{
    	jpa = true
      querydslSourcesDir = querydslDir
    }
    
    sourceSets{
    	main.java.srcDir querydslDir
    }
    
    configurations{
    	querydsl.extendsFrom compileClasspath
    }
    
    compileQuerydsl{
    	options.annotationProcessorPath = configurations.querydsl
    }
    ```


gradle 설정이 완료되었다면

- `Gradle` - `Tasks` - `other` - `compileQuerydsl`
- `./gradlew clean compileQuerydsl`

실행하여 `Querydsl query type`을 생성한다. 생성된 것을 보면 Q~ 라고 자동으로 생성된 클래스를 확인할 수 있다. 클래스 생성이 되지 않았다면 `build.gradle` 파일을 다시 살펴보자.

*~~파일 내용을 살펴봐도  아직 이해가 되지 않는다.~~*

💡 Q타입은 컴파일 시점에 자동 생성되므로 버전관리(git)에 포함하지 않는 것이 좋다. 앞서 설정에서 생성 위치를 gradle build 폴더 아래 생성되도록 했기 때문에 이 부분도 자연스럽게 해결된다. (대부분 gradle build 폴더를 git에 포함하지 않는다.)


제대로 동작하는지 테스트 코드로 살펴보자. Q타입 객체를 이용하여 쿼리를 작성한 모습이다.

`selectFrom(qHello)` Q타입 객체를 사용하여 `fetchOne` 하나의 결과를 가져오는 모습이다. `JPA`는 객체의 `동일성(identity)`을 보장한다. 따라서 영속성 컨텍스트에서 관리되고 있던 `Hello`는 조회된 결과와 동일하다.

### Test 실행 및 검증

```java
@Test
void contextLoads() {
	Hello hello = new Hello();
	em.persist(hello);

	JPAQueryFactory query = new JPAQueryFactory(em);
	QHello qHello = new QHello("h");

	Hello result = query
			.selectFrom(qHello)
			.fetchOne();

	assertThat(result).isEqualTo(hello);
	assertThat(result.getId()).isEqualTo(hello.getId());
}
```

# **H2 데이터베이스 설치**

개발이나 테스트 용도로 가볍고 편리한 DB, 웹 화면 제공한다.

[H2 Database Engine](https://www.h2database.com)

데이터베이스를 다운로드 하고 파일을 실행한다.

💡 권한때문에 실행되지 않는다면 실행파일 위치에서 `chmod 755 h2.sh`


- `jdbc:h2:~/querydsl` 최초 한번 실행한다.
- `~/querydsl.mv.db` 생성 확인
- 이후 부터는 `jdbc:h2:tcp://localhost/~/querydsl` 이렇게 접속한다.

# **스프링 부트 설정 JPA, DB**

`application.yml` 파일 생성

```yaml
spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/querydsl
    username: sa
    password:
    driver-class-name: org.h2.Driver

  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
				format_sql: true

logging.level:
  org.hibernate.SQL: debug
```

- `ddl-auto: create`
    - 애플리케이션 실행 시점에 테이블을 drop 하고, 다시 생성한다.


💡 `org.hibernate.SQL` 옵션은 logger를 통해 하이버네이트 실행 SQL을 남긴다.
하지만 `show_sql` 옵션은 System.out 에 하이버네이트 실행 SQL을 남긴다.
따라서 `org.hibernate.SQL` 옵션을 사용한다.


### **쿼리 파라미터 로그 남기기**

[GitHub - gavlyukovskiy/spring-boot-data-source-decorator: Spring Boot integration with p6spy, datasource-proxy, flexy-pool and spring-cloud-sleuth](https://github.com/gavlyukovskiy/spring-boot-data-source-decorator)

```yaml
implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.8’
```

- 로그에 다음을 추가하기 org.hibernate.type : SQL 실행 파라미터를 로그로 남긴다.


💡 쿼리 파라미터를 로그로 남기는 외부 라이브러리는 시스템 자원을 사용하므로, 개발 단계에서는 편하게 사용해도 된다. 하지만 운영시스템에 적용하려면 꼭 성능테스트를 하고 사용하는 것이 좋다.


# 도메인 모델 설계

학습에 사용하는 모델은 매우 단순하다. `Member`, `Team` 두 엔티티를 연관관계를 맺어 `querydsl`을 학습해겠다.

- `Member`

    ```java
    @Entity
    @Getter @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString(of = {"id", "username", "age"})
    public class Member {
    
        @Id @GeneratedValue
        @Column(name = "member_id")
        private Long id;
    
        private String username;
    
        private int age;
    
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "team_id")
        private Team team;
    
    }
    ```

- `Team`

    ```java
    @Entity
    @Getter @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString(of = {"id", "name"})
    public class Team {
    
        @Id @GeneratedValue
        @Column(name = "team_id")
        private Long id;
    
        private String name;
    
        @OneToMany(mappedBy = "team")
        private List<Member> members = new ArrayList<>();
    
        public Team(String name) {
            this.name = name;
        }
    
    }
    ```


멤버와 팀은 연관관계를 가지고 있다. 멤버는 하나의 팀을 가지고 있다. 하나의 팀에는 여러 멤버를 가지고 있을 수 있다. 따라서 `@ManyToOne`, `@OneToMany` 으로 연관관계를 설정하고 `FetchType.LAZY` 지연로딩 설정을 변경했다. `FetchType.EAGER` 즉시로딩은 쿼리의 조인이 발생하면 N+1 문제가 발생할 수 있고 불필요한 쿼리가 추가로 발생하기 때문에 모든 설정은 지연로딩으로 처리할 것이다.

## JPQA vs Querydsl

테스트를 통해 Querydsl을 사용하는 간단한 방법과 이유를 살펴보겠다. 먼저 `JPQL`을 사용하는 테스트다.

```java
@Test
void startJPQL() {
  Member findByJPQL = em.createQuery(
                  "select m from Member m " +
                          "where m.username = :username", Member.class)
          .setParameter("username", "member1")
          .getSingleResult();

	assertThat(findByJPQL.getUsername()).isEqualTo("member1");
}
```

`username` 이 일치하는 멤버를 조회하고 있다. 여기서 제일 문제가 되는 부분이 무엇일까? `JPQL`을 작성하려면 문자열로 작성해야 한다. 문자열은 컴파일 단계에서 오류를 발견할 수 없다. 사소한 띄어쓰기로 예외가 발생할 수 있다. 실제로 코드가 동작하는 순간까지 에러를 발견할 수 없을 것이다. 개발자 입장에서는 컴파일 단계에서 에러를 알아낼 수 있다면 정말정말 좋을텐데 말이다. 물론 테스트 코드를 통해 견고한 애플리케이션을 만들어야 한다. 그럼 `Querydsl` 테스트를 살펴보자.

```java
@Test
void startQuerydsl() {
  QMember m = new QMember("m");

  Member findMember = jpaQueryFactory
          .select(m)
          .from(m)
          .where(m.username.eq("member1"))
          .fetchOne();

	assertThat(findMember.getUsername()).isEqualTo("member1");
}
```

동일한 결과를 가져오는 테스트 코드다. `Querydsl`은 쿼리에서 사용하는 명령어를 자바코드로 작성할 수 있다. 이는 쿼리를 자바 컴파일러가 검증할 수 있다는 것이다.


💡 `JPAQueryFactory`를 필드로 제공하면 동시성 문제는 어떻게 될까? 동시성 문제는 `JPAQueryFactory`를 생성할 때 제공하는 `EntityManager(em)`에 달려있다. 스프링 프레임워크는 여러 쓰레드에서 동시에 같은 `EntityManager`에 접근해도, 트랜잭션 마다 별도의 영속성 컨텍스트를 제공하기 때문에, 동시성 문제는 걱정하지 않아도 된다.



## **기본 Q-Type 활용**

### **Q클래스 인스턴스를 사용하는 2가지 방법**

```java
QMember qMember = new QMember("m"); //별칭 직접 지정
QMember qMember = QMember.member; //기본 인스턴스 사용
```

하지만 기본 인스턴스를 static import와 함께 사용하는 것을 권장한다.

### Querydsl에서 사용되는 JPQL이 궁금할 때?


💡 spring.jpa.properties.hibernate.use_sql_comments: true



## 검색조건 쿼리

```java
@Test
void search() {
  Member findMember = jpaQueryFactory
          .selectFrom(member)
          .where(member.username.eq("member1")
                  .and(member.age.eq(10)))
          .fetchOne();

	assertThat(findMember.getUsername()).isEqualTo("member1");
}
```

검색조건은 메서드 체인으로 연결할 수 있고 모든 검색 조건을 제공하고 있다.

| member.username.eq("member1") | username = 'member1' |
| --- | --- |
| member.username.ne("member1") | username != 'member1' |
| member.username.eq("member1").not() | username != 'member1' |
| member.username.isNotNull() | 이름이 is not null |
| member.age.in(10, 20) | age in (10,20) |
| member.age.notIn(10, 20) | age not in (10, 20) |
| member.age.between(10,30) | between 10, 30 |
| member.age.goe(30) | age >= 30 |
| member.age.gt(30) | age > 30 |
| member.age.loe(30) | age <= 30 |
| member.age.lt(30) | age < 30 |
| member.username.like("member%") | like 검색 |
| member.username.contains("member") | like ‘%member%’ 검색 |
| member.username.startsWith("member") | like ‘member%’ 검색 |

## **AND 조건을 파라미터로 처리**

```java
@Test
void searchAndParam() {
  Member findMember = jpaQueryFactory
            .selectFrom(member)
            .where(
                member.username.eq("member1"),
                member.age.eq(10)
            )
            .fetchOne();
	
	assertThat(findMember.getUsername()).isEqualTo("member1");
}
```

`where()` 에 파라미터로 검색조건을 추가하면 `AND` 조건이 추가된다. 이경우 null 값은 무시한다.

## **결과 조회**

- `fetch` : 리스트 조회, 데이터 없으면 빈 리스트 반환
- `fetchOne` : 단 건 조회
    - 결과가 없으면 : null
    - 결과가 둘 이상이면 : `com.querydsl.core.NonUniqueResultException`
- `fetchFirst` : limit(1).fetchOne()
- `fetchResults` : 페이징 정보 포함, total count 쿼리 추가 실행
- `fetchCount` : count 쿼리로 변경해서 count 수 조회

## 정렬

- `desc()`, `asc()` : 일반 정렬
- `nullsLast()`, `nullsFirst()` : null 데이터 순서 부여

## 페이징

실무에서 페이징 쿼리를 작성할 때, 데이터를 조회하는 쿼리는 여러 테이블을 조인해야 하지만, count 쿼리는 조인이 필요 없는 경우도 있다. 그런데 이렇게 자동화된 count 쿼리는 원본 쿼리와 같이 모두 조인을 해버리기 때문에 성능이 안나올 수 있다. count 쿼리에 조인이 필요없는 성능 최적화가 필요하다면, count 전용 쿼리를 별도로 작성해야 한다.

## **집합**

```java
@Test
void aggregation() {
    List<Tuple> result = queryFactory
        .select(member.count(),
                member.age.sum(),
                member.age.avg(),
                member.age.max(),
                member.age.min())
        .from(member)
        .fetch();
	
    Tuple tuple = result.get(0);
    assertThat(tuple.get(member.age.sum())).isEqualTo(100);
    assertThat(tuple.get(member.age.avg())).isEqualTo(25);
    assertThat(tuple.get(member.age.max())).isEqualTo(40);
}
```

## **조인 기본 조인**

### **기본 조인**

조인의 기본 문법은 첫 번째 파라미터에 조인 대상을 지정하고, 두 번째 파라미터에 별칭(alias)으로 사용할 Q 타입을 지정하면 된다.

- join(조인 대상, 별칭으로 사용할 Q타입)

```java
@Test
void join() {
  List<Member> result = queryFactory
          .selectFrom(member)
          .join(member.team,team)
          .where(team.name.eq("teamA"))
          .fetch();

	assertThat(result)
            .extracting("username")
            .containsExactly("member1", "member2");
}
```

- `join()` , `innerJoin()` : 내부 조인(inner join)
- `leftJoin()` : left 외부 조인(left outer join)
- `rightJoin()` : rigth 외부 조인(rigth outer join)
- `JPQL`의 `on`과 성능 최적화를 위한 `fetch` 조인 제공

### **세타 조인**

연관관계가 없는 필드로 조인한다.

```java
@Test
void theta_join() {
  em.persist(new Member("teamA"));
  em.persist(new Member("teamB"));

  List<Member> result = queryFactory
          .select(member)
          .from(member,team)
          .where(member.username.eq(team.name))
          .fetch();

	assertThat(result)
            .extracting("username")
            .containsExactly("teamA", "teamB");
}
```

- `from`절에 여러 엔티티를 선택해서 세타 조인
- 외부 조인 불가능 다음에 설명할 조인 `on`을 사용하면 외부 조인 가능

## **조인 on절**

- ON절을 활용한 조인(JPA 2.1부터 지원)
    - 조인 대상 필터링

        ```java
        @Test
        void join_on_filtering() {
            List<Tuple> result = queryFactory
                    .select(member,team)
                    .from(member)
                    .leftJoin(member.team,team).on(team.name.eq("teamA"))
                    .fetch();
        
            for (Tuple tuple : result) {
                System.out.println("tuple = " + tuple);
            }
        }
        ```
    
    - 연관관계 없는 엔티티 외부조인

        ```java
        @Test
        void join_on_no_relation() {
            em.persist(new Member("teamA"));
            em.persist(new Member("teamB"));
            em.persist(new Member("teamC"));
        
            List<Tuple> result = queryFactory
                    .select(member,team)
                    .from(member)
                    .leftJoin(team).on(member.username.eq(team.name))
                    .fetch();
        
            for (Tuple tuple : result) {
                System.out.println("tuple = " + tuple);
            }
        }
        ```

        - 하이버네이트 5.1부터 on 을 사용해서 서로 관계가 없는 필드로 외부 조인하는 기능이 추가되었다.
        - 문법을 잘 봐야 한다. `leftJoin()` ****부분에 일반 조인과 다르게 엔티티 하나만 들어간다.
            - 일반조인: leftJoin(member.team, team)
            - on조인: from(member).leftJoin(team).on(xxx)

## **조인 페치 조인**

SQL조인을 활용해서 연관된 엔티티를 SQL 한번에 조회하는 기능이다. 주로 성능 최적화에 사용하는 방법이다.

```java
@Test
void fetchJoinUse() {
    em.flush();
    em.clear();

    Member findMember = queryFactory
            .selectFrom(member)
            .join(member.team,team).fetchJoin()
            .where(member.username.eq("member1"))
            .fetchOne();

    boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
		assertThat(loaded).as("페치 조인 적용").isTrue();
}
```

- 즉시로딩으로 Member, Team SQL 쿼리 조인으로 한번에 조회한다.
- `join()`, `leftJoin()` 등 조인 기능 뒤에 `fetchJoin()` 이라고 추가하면 된다.

## **서브 쿼리**

```java
@Test
void subQuery() {
    QMember memberSub = new QMember("memberSub");

    List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.eq(
                    JPAExpressions
                            .select(memberSub.age.max())
                            .from(memberSub)
            ))
            .fetch();

		assertThat(result)
            .extracting("age")
            .containsExactly(40);
}
```

```java
@Test
void subQueryGoe() {
    QMember memberSub = new QMember("memberSub");

    List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.goe(
                    JPAExpressions
                            .select(memberSub.age.avg())
                            .from(memberSub)
            ))
            .fetch();

		assertThat(result)
		            .extracting("age")
		            .containsExactly(30, 40);
}
```

```java
@Test
void subQueryIn() {
    QMember memberSub = new QMember("memberSub");

    List<Member> result = queryFactory
            .selectFrom(member)
            .where(member.age.in(
                    JPAExpressions
                            .select(memberSub.age)
                            .from(memberSub)
                            .where(memberSub.age.gt(10))
            ))
            .fetch();

		assertThat(result)
		            .extracting("age")
		            .containsExactly(20, 30, 40);
}
```

```java
@Test
void selectSubQuery() {
    QMember memberSub = new QMember("memberSub");

    List<Tuple> result = queryFactory
            .select(member.username,
                    JPAExpressions
                            .select(memberSub.age.avg())
                            .from(memberSub)
            )
            .from(member)
            .fetch();

    for (Tuple tuple : result) {
        System.out.println("tuple = " + tuple);
    }
}
```


💡 JPA JPQL 서브쿼리의 한계점으로 from 절의 서브쿼리(인라인 뷰)는 지원하지 않는다. 당연히 Querydsl 도 지원하지 않는다. 하이버네이트 구현체를 사용하면 select 절의 서브쿼리는 지원한다. Querydsl도 하이버네이트 구현체를 사용하면 select 절의 서브쿼리를 지원한다.



## Case문

```java
@Test
void basicCase() {
    List<String> result = queryFactory
            .select(member.age
                    .when(10).then("10")
                    .when(20).then("20")
                    .otherwise("00")
            )
            .from(member)
            .fetch();

    for (String s : result) {
        System.out.println("s = " + s);
    }
}
```

```java
@Test
void complexCase() {
    List<String> result = queryFactory
            .select(
                    new CaseBuilder()
                            .when(member.age.between(0, 20)).then("0~20")
                            .when(member.age.between(21, 30)).then("21~30")
                            .otherwise("00")
            )
            .from(member)
            .fetch();

    for (String s : result) {
        System.out.println("s = " + s);
    }
}
```

## **상수, 문자 더하기**

```java
@Test
void constant() {
    List<Tuple> result = queryFactory
            .select(member.username, Expressions.constant("A"))
            .from(member)
            .fetch();

    for (Tuple tuple : result) {
        System.out.println("tuple = " + tuple);
    }
}
```

```java
@Test
void concat() {
    List<String> result = queryFactory
            .select(member.username.concat("_").concat(member.age.stringValue()))
            .from(member)
            .where(member.username.eq("member1"))
            .fetch();

    for (String s : result) {
        System.out.println("s = " + s);
    }
}
```

💡 `member.age.stringValue()` 부분이 중요한데, 문자가 아닌 다른 타입들은 `stringValue()` 로
문자로 변환할 수 있다. 이 방법은 `ENUM`을 처리할 때도 자주 사용한다.
