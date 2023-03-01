## 액츄에이터

### 프로덕션 준비 기능

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

- 엔드포인트 활성화 + 엔드포인트 노출이 둘다 적용되어야 사용할 수 있다.
- application.yml - 모든 엔드포인트를 웹에 노출

```groovy
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

- application.yml - shutdown 엔드포인트 활성화

```groovy
management:
  endpoint:
    shutdown:
      enabled: true
  endpoints:
    web:
      exposure:
        include: "*"
```

- shutdown 엔드포인트는 서버를 종료시키기 때문에 기본적으로 비활성화 되어있다. 주의해서 사용하자.

### 헬스 정보

http://localhost:8080/actuator/health

```json
{
  "status": "UP"
}
```

헬스 정보를 더 자세히 보려면 다음 옵션을 지정하면 된다.

```groovy
management:
  endpoint:
    health:
      show-details: always
```

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 494384795648,
        "free": 302213918720,
        "threshold": 10485760,
        "path": "/Users/woody/recruit/study/spring-boot-actuator/.",
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

다른 컴포넌트의 상태만 확인하려면 다음 옵션을 사용하면 된다.

```groovy
management:
  endpoint:
    health:
      show-components: always
```

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

### 애플리케이션 정보

http://localhost:8080/actuator/info

```json
{
  "java": {
    "version": "17.0.5",
    "vendor": {
      "name": "Amazon.com Inc.",
      "version": "Corretto-17.0.5.8.1"
    },
    "runtime": {
      "name": "OpenJDK Runtime Environment",
      "version": "17.0.5+8-LTS"
    },
    "jvm": {
      "name": "OpenJDK 64-Bit Server VM",
      "vendor": "Amazon.com Inc.",
      "version": "17.0.5+8-LTS"
    }
  },
  "os": {
    "name": "Mac OS X",
    "version": "13.2.1",
    "arch": "aarch64"
  }
}
```

- 추가로 정보를 표현하고 싶은경우 env를 이용하여 정보를 표현할 수 있다.

```groovy
management:
  info:
    env:
      enabled: true
```

```groovy
info:
  app:
    name: hello-actuator
    company: yh
```

```json
{
  "app": {
    "name": "hello-actuator",
    "company": "yh"
  },
  "java": {
    "version": "17.0.5",
    "vendor": {
      "name": "Amazon.com Inc.",
      "version": "Corretto-17.0.5.8.1"
    },
    "runtime": {
      "name": "OpenJDK Runtime Environment",
      "version": "17.0.5+8-LTS"
    },
    "jvm": {
      "name": "OpenJDK 64-Bit Server VM",
      "vendor": "Amazon.com Inc.",
      "version": "17.0.5+8-LTS"
    }
  },
  "os": {
    "name": "Mac OS X",
    "version": "13.2.1",
    "arch": "aarch64"
  }
}
```

#### 빌드 정보

```groovy
// build.gradle
springBoot {
    buildInfo()
}
```

#### git 정보

```groovy
// build.gradle
plugins {
    id "com.gorylenko.gradle-git-properties" version "2.4.1" //git info
}
```

```groovy
management:
  info:
    git:
      mode: full
```

### 로거

- 로그를 별도로 설정하지 않으면 스프링 부트는 기본으로 info 레벨을 사용한다.
- 보통의 로컬에서 개발하면 trace, debug 레벨까지 로그를 남기지만 운영 환경에서는 info 레벨까지 로그를 남긴다. 
- 서비스 운영중에 문제가 있어서 급하게 debug나 trace 로그를 남겨서 확인해야 확인하고 싶다면 어떻게 해야할까? 일반적으로는 로깅 설정을 변경하고, 서버를 다시 시작해야 한다.
- loggers 엔드포인트를 사용하면 애플리케이션을 다시 시작하지 않고, 실시간으로 로그 레벨을 변경할 수 있다.

#### 실시간 로그 레벨 변경

POST http://localhost:8080/actuator/loggers/com.example.controller

```json
{
  "configuredLevel": "TRACE"
}
```

요청에 성공하면 204 응답이 온다. 그리고 로그 레벨을 확인하면 변경된 것을 확인할 수 있다.

### HTTP 요청 응답 기록

- HTTP 요청과 응답의 과거 기록을 확인하고 싶다면 httpexchanges 엔드포인트를 사용하면 된다.
- Application.java 빈 등록

```java
@Bean
public InMemoryHttpExchangeRepository httpExchangeRepository() {
  return new InMemoryHttpExchangeRepository();
}
```

http://localhost:8080/actuator/httpexchanges

실행해보면 지금까지 실행한 HTTP 요청과 응답 정보를 확인할 수 있다.
참고로 이 기능은 매우 단순하고 기능에 제한이 많기 때문에 개발 단계에서만 사용하고, 실제 운영 서비스에서는 모니터링 툴이나 핀포인트, Zipkin 같은 다른 기술을 사용하는 것이 좋다.

### 액츄에이터와 보안

액츄에이터가 제공하는 기능들은 우리 애플리케이션의 내부 정보를 너무 많이 노출한다. 
그래서 외부 인터넷 망이 공개된 곳에 액츄에이터의 엔드포인트를 공개하는 것은 보안상 좋은 방안이 아니다. 
액츄에이터의 엔드포인트들은 외부 인터넷에서 접근이 불가능하게 막고, 내부에서만 접근 가능한 내부망을 사용하는 것이 안전하다.

#### 액츄에이터를 다른 포트에서 실행
예를 들어서 외부 인터넷 망을 통해서 8080 포트에만 접근할 수 있고, 다른 포트는 내부망에서만 접근할 수 있다면 액츄에이터에 다른 포트를 설정하면 된다.
액츄에이터의 기능을 애플리케이션 서버와는 다른 포트에서 실행하려면 다음과 같이 설정하면 된다. 이 경우 기존 8080 포트에서는 액츄에이터를 접근할 수 없다.

```groovy
management:
  server:
    port: 9292
```

http://localhost:9292/actuator
