# 영업시간(openingHours) 처리 방법

## 1. 문제 상황

`openingHours` 필드로 들어오는 JSON 데이터의 구조가 아래와 같이 3가지 이상의 비일관적인 형태로 들어옵니다.

- **Case 1:** `{"매일": "10:00 - 18:00"}`
- **Case 2:** `{"월": "10:00 - 19:00", "화": "10:00 - 19:00", ...}`
- **Case 3:** `{"목": {"time": "09:00 - 19:00", "break": "14:00 - 14:40"}, "금": "정기휴무", ...}`

이러한 비일관적인 데이터를 Java DTO에서 일관된 형태로 처리하기 위한 전체적인 흐름을 정리합니다.

## 2. 해결 흐름

**Client (JSON) -> Controller (DTO) -> Service (Logic) -> Repository -> DB (Entity)**

핵심은 Jackson의 `Custom Deserializer`를 사용하여 Controller에서 DTO를 받는 시점에 이미 파싱을 완료하고, 서비스 계층에서는 정제된 데이터만 다루도록 하는 것입니다.

## 3. 구현 단계

### 3.1. Custom Deserializer를 이용한 JSON 파싱

#### 3.1.1. 데이터 구조 정의 (`OperatingInfo.java`)

어떤 형태의 JSON이든 최종적으로 변환될 표준 Java 클래스를 정의합니다.

```java
public class OperatingInfo {
    private String time;      // 영업시간 ("09:00 - 19:00") 또는 전체 텍스트 ("정기휴무")
    private String breakTime; // 휴게시간 ("14:00 - 14:40")

    // Constructors, Getters, Setters...
}
```

#### 3.1.2. Deserializer 구현 (`OpeningHoursDeserializer.java`)

비일관적인 JSON 구조를 `Map<String, OperatingInfo>` 형태로 변환하는 핵심 로직입니다.

```java
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.yourproject.dto.OperatingInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OpeningHoursDeserializer extends JsonDeserializer<Map<String, OperatingInfo>> {

    @Override
    public Map<String, OperatingInfo> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Map<String, OperatingInfo> result = new HashMap<>();
        JsonNode rootNode = jp.getCodec().readTree(jp);
        Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String day = field.getKey();
            JsonNode valueNode = field.getValue();
            OperatingInfo operatingInfo = new OperatingInfo();

            if (valueNode.isTextual()) {
                operatingInfo.setTime(valueNode.asText());
            } else if (valueNode.isObject()) {
                JsonNode timeNode = valueNode.get("time");
                JsonNode breakNode = valueNode.get("break");

                if (timeNode != null && timeNode.isTextual()) {
                    operatingInfo.setTime(timeNode.asText());
                }
                if (breakNode != null && breakNode.isTextual()) {
                    operatingInfo.setBreakTime(breakNode.asText());
                }
            }
            result.put(day, operatingInfo);
        }
        return result;
    }
}
```

#### 3.1.3. DTO에 적용

`@JsonDeserialize` 어노테이션을 사용하여 위에서 만든 Deserializer를 지정합니다.

```java
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class YourRequestDto {
    // ... other fields

    @JsonDeserialize(using = OpeningHoursDeserializer.class)
    private Map<String, OperatingInfo> openingHours;

    // Getters, Setters
}
```
**※ `OpeningHoursDeserializer`는 스프링 빈이 아니므로 `@Component` 등의 어노테이션이 필요 없습니다.**

### 3.2. 서비스 계층에서의 데이터 변환 및 저장

#### 3.2.1. 데이터베이스 엔티티 (`StoreOperationHours.java`)

`store_operation_hours` 테이블과 매핑될 JPA 엔티티입니다.

```java
@Entity
@Table(name = "store_operation_hours")
public class StoreOperationHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private Integer dayOfWeek; // 0=일, 1=월, ..., 6=토

    @Column(nullable = false)
    private boolean isOpen;

    private LocalTime openTime;
    private LocalTime closeTime;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;

    // Getters, Setters, etc.
}
```

#### 3.2.2. 서비스 로직 (`StoreService.java`)

DTO의 `Map<String, OperatingInfo>`를 `List<StoreOperationHours>` 엔티티로 변환하고 저장하는 핵심 비즈니스 로직입니다.

```java
@Service
public class StoreService {

    // ... Repositories
    
    @Transactional
    public void createStore(YourRequestDto dto) {
        // 1. Store 정보 저장
        Store savedStore = storeRepository.save(...);

        // 2. 영업시간 정보 처리 및 저장
        Map<String, OperatingInfo> openingHoursMap = dto.getOpeningHours();
        if (openingHoursMap != null && !openingHoursMap.isEmpty()) {
            List<StoreOperationHours> hoursList = convertToOperationHours(savedStore, openingHoursMap);
            hoursRepository.saveAll(hoursList);
        }
    }

    private List<StoreOperationHours> convertToOperationHours(Store store, Map<String, OperatingInfo> openingHoursMap) {
        List<StoreOperationHours> hoursList = new ArrayList<>();
        // 시간 포맷이 "HH:mm"이 아닐 경우를 대비하여 유연하게 처리할 수 있지만, 여기서는 고정 포맷으로 가정합니다.
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Map.Entry<String, OperatingInfo> entry : openingHoursMap.entrySet()) {
            String dayKey = entry.getKey();
            OperatingInfo info = entry.getValue();

            if ("매일".equals(dayKey)) {
                for (int i = 0; i <= 6; i++) { // 0(일)부터 6(토)까지
                    hoursList.add(parseDayInfo(store, i, info, timeFormatter));
                }
            } else {
                int dayOfWeek = convertDayKeyToDayOfWeek(dayKey);
                hoursList.add(parseDayInfo(store, dayOfWeek, info, timeFormatter));
            }
        }
        return hoursList;
    }

    private StoreOperationHours parseDayInfo(Store store, int dayOfWeek, OperatingInfo info, DateTimeFormatter formatter) {
        StoreOperationHours operationHours = new StoreOperationHours();
        operationHours.setStore(store);
        operationHours.setDayOfWeek(dayOfWeek);

        String time = info.getTime();
        if (time == null || time.trim().isEmpty() || time.contains("휴무")) {
            operationHours.setIsOpen(false);
        } else {
            operationHours.setIsOpen(true);
            String[] times = time.split(" - ");
            if (times.length == 2) {
                try {
                    operationHours.setOpenTime(LocalTime.parse(times[0].trim(), formatter));
                    operationHours.setCloseTime(LocalTime.parse(times[1].trim(), formatter));
                } catch (Exception e) {
                    // 시간 파싱 실패 시 로그를 남기고, 해당 요일은 비영업 처리 또는 예외 처리를 할 수 있습니다.
                    operationHours.setIsOpen(false);
                }
            }
        }

        String breakTime = info.getBreakTime();
        if (breakTime != null && !breakTime.trim().isEmpty()) {
            String[] times = breakTime.split(" - ");
            if (times.length == 2) {
                 try {
                    operationHours.setBreakStartTime(LocalTime.parse(times[0].trim(), formatter));
                    operationHours.setBreakEndTime(LocalTime.parse(times[1].trim(), formatter));
                } catch (Exception e) {
                    // 브레이크 타임 파싱 실패 시 로그만 남기고 넘어갈 수 있습니다.
                }
            }
        }
        return operationHours;
    }

    private int convertDayKeyToDayOfWeek(String dayKey) {
        switch (dayKey) {
            case "일":
            case "일요일":
                return 0;
            case "월":
            case "월요일":
                return 1;
            case "화":
            case "화요일":
                return 2;
            case "수":
            case "수요일":
                return 3;
            case "목":
            case "목요일":
                return 4;
            case "금":
            case "금요일":
                return 5;
            case "토":
            case "토요일":
                return 6;
            default:
                // 잘못된 요일 키가 들어올 경우 예외 발생
                throw new IllegalArgumentException("Invalid day key: " + dayKey);
        }
    }
}
```

### 3.3. 컨트롤러

컨트롤러는 `@RequestBody`로 DTO를 받아 서비스에 전달하는 역할만 수행합니다.

```java
@RestController
public class StoreController {

    private final StoreService storeService;
    // ... constructor

    @PostMapping("/stores")
    public ResponseEntity<Void> createStore(@RequestBody YourRequestDto requestDto) {
        storeService.createStore(requestDto);
        return ResponseEntity.ok().build();
    }
}
```
