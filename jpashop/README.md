## 개요
- JPA의 연관관계 개념을 이해하기 위한 샘플 프로젝트를 만들어보는 것, 주의사항을 기록하는 것이 목표이다.
- **웹 어플리케이션 개발을 위한 프로젝트 환경설정**
- **요구사항 분석 및 설계**

### 프로젝트 생성
- [스프링 부트 스타터](https://start.spring.io/)
- 작성일 기준(2021.12.26) 환경설정
> *Project* : `Gradle Project`
> *Spring Boot* : `2.6.2`
> *Packaging* : `Jar`
> *Java* : `11`
> *Dependencies* : `web`, `thymeleaf`, `jpa`, `h2`, `lombok`, `validation`

#### View 환경 설정
- resources : `templates/ + {ViewName} + .html`

> `spring-boot-devtools` 라이브러리를 추가하면, html 파일을 컴파일만 해주면 서버 재시작 없이 view 파일 변경이 가능하다.

#### H2 데이터베이스 설치
- 개발용, 테스트용으로 사용하기 좋은 DB
- query 파라미터 log 남기기 위한 라이브러리
  - `implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.0'`

## 도메인
- 도메인 설계시 주의사항은 무엇일까?
- 우선, 요구사항을 분석하여 기능개발 목록을 작성한다.
- 회원, 주문, 상품이라는 예시가 존재한다. 회원은 여러 상품을 주문할 수 있고, 여러 상품을 선택할 수 있으므로 주문과 상품은 다대다 관계이다. 하지만 다대다 관계는 관계형 데이터베이스에서 사용하지 않기 때문에 일대다, 다대일 관계로 풀어낸다.

```java
@Entity
@Getter
public class Member {

  @Id @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  private String name;

  @Embedded
  private Address address;

  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();
}
```

- 이름과 임베디드 타입인 `Address` 그리고 `orders` 리스트를 가진다.
- `private List<Order> orders = new ArrayList<>();`
  - 컬렉션은 필드에서 바로 초기화 하는 것이 안전하다.

```java
@Entity
@Table(name = "orders")
@Getter
public class Order {

  @Id @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  private LocalDateTime orderDate;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;
}
```

- `@Table(name = "orders")`
  - order는 예약어이기 때문에 테이블명을 변경
- `@ManyToOne`, `@OneToOne`
  - `fetch = FetchType.LAZY`
  - 모든 연관관계는 지연로딩으로 설정한다.
  - 즉시로딩(`EAGER`)은 예측이 어렵고, 어떤 `SQL`이 실행될지 추적하기 어렵다. 특히 `JPQL`을 실행할 때 N+1 문제가 자주 발생한다.
  - 연관된 엔티티를 함께 DB에서 조회해야 하면, `fetch join` 또는 `엔티티 그래프` 기능을 사용한다.
- `@Enumerated(EnumType.STRING)`
  - 무조건 `STRING` 옵션을 선택해야 한다. 데이터베이스에 enum 값 그대로 저장되어야 추후 상태가 변경되더라도 지속적으로 사용 가능한다.

```java
@Embeddable
@Getter
public class Address {
  private String city;
  private String street;
  private String zipcode;

  protected Address() {
  }

  public Address(String city, String street, String zipcode) {
    this.city = city;
    this.street = street;
    this.zipcode = zipcode;
  }
}
```

- `@Embeddable`
  - 값 타입은 변경 불가능하게 설계해야 한다. 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들고 기본 생성자를 `protected`로 생성한다.

## 정리
- `Member`, `Order`
  - 일대다, 다대일의 양방향 관계다. 이러한 경우 연관관계의 주인을 설정해야 한다.
  - 보통 외래키가 있는 엔티티를 연관관계의 주인으로 정하는 것이 좋다. 만약 반대의 경우라면 불필요한 업데이트 쿼리가 발생하는 등의 성능 이슈가 존재한다.

> `Setter`는 모두 제공하지 않고, 꼭 필요한 별도의 메서드를 제공하는게 가장 이상적이다. 변경이 필요하다면 비즈니스 메소드를 별도로 제공해야 한다.