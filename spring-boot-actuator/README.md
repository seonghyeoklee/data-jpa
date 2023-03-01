## 프로덕션 준비 기능

전투에서 실패한 지휘관은 용서할 수 있지만 경계에서 실패하는 지휘관은 용서할 수 없다라는 말이있다. 장애는 언제든지 발생할 수 있지만 지속적인 모니터링이 중요하다는 의미이다.

개발자는 기능 요구사항 뿐만 아니라 서비스를 실제 운영 단계에 올리게 되면 개발자들이 해야하는 또 다른 중요한 업무가 있다. 바로 서비스에 문제가 없는지 모니터링하고 지표들을 심어서 감시하는 활동들이다.
운영 환경에서 서비스할 때 필요한 이런 기능들을 프로덕션 준비 기능이라 한다. 쉽게 이야기해서 프로덕션을 운영에 배포할 때 준비해야 하는 비 기능적 요소들을 뜻한다.

- 지표(metric), 추적(trace), 감사(auditing)
- 모니터링

액추에이터는 시스템을 움직이거나 제어하는 데 쓰이는 기계 장치라는 뜻이다.

Spring boot에서는 액추에이터를 쉽게 사용할 수 있도록 기능을 제공하고 마이크로미터, 프로메테우스, 그라파나와 같은 모니터링 시스템과 쉽게 연동할 수 있도록 기능을 제공한다.

```groovy
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

Application 실행 후 http://localhost:8080/actuator 

#### 실행 결과

```json
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/actuator",
      "templated": false
    },
    "health-path": {
      "href": "http://localhost:8080/actuator/health/{*path}",
      "templated": true
    },
    "health": {
      "href": "http://localhost:8080/actuator/health",
      "templated": false
    }
  }
}
```
