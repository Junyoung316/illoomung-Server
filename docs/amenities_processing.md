# 편의시설(amenities) 처리 방법

## 1. 문제 상황

- 클라이언트로부터 사업장에 속한 편의시설 목록을 **이름(String)의 리스트** 형태로 받습니다. (e.g., `"amenities": ["주차 가능", "Wi-Fi"]`)
- 데이터베이스에는 `stores`와 `amenities` 간의 관계를 나타내는 `store_amenity_mappings` 라는 Many-to-Many 매핑 테이블이 존재합니다.
- 이 매핑 테이블에는 이름이 아닌, 각 테이블의 **ID**가 저장되어야 합니다.

따라서, 서비스 계층에서 **편의시설 이름 목록(`List<String>`)**을 **`StoreAmenityMapping` 엔티티 목록**으로 변환하는 과정이 필요합니다.

## 2. 해결 흐름

**DTO (`List<String>`) -> Service (Logic) -> Repository -> DB (Entities & Mappings)**

## 3. 구현 단계

### 3.1. 관련 엔티티 정의

편의시설 마스터 테이블(`amenities`)과 중간 매핑 테이블(`store_amenity_mappings`)에 해당하는 엔티티입니다.

**`Amenity.java`**
```java
@Entity
@Table(name = "amenities")
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // "편의시설 명"

    // ... other fields, getters, setters
}
```

**`StoreAmenityMapping.java`**
```java
@Entity
@Table(name = "store_amenity_mappings")
public class StoreAmenityMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;

    // Constructors, Getters, Setters...
}
```

### 3.2. Repository 쿼리 메소드 추가

편의시설 이름 목록으로 모든 `Amenity` 엔티티를 한 번의 쿼리로 조회하기 위한 메소드를 `AmenityRepository`에 추가합니다. 이는 성능 최적화에 매우 중요합니다(N+1 문제 방지).

**`AmenityRepository.java`**
```java
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    // 이름 목록(amenityNames)을 받아 해당하는 Amenity 엔티티 목록을 모두 찾아 반환
    List<Amenity> findByNameIn(List<String> amenityNames);
}
```

### 3.3. 서비스 계층 로직

`StoreService`에서 편의시설 이름 목록을 받아 매핑 엔티티로 변환하고 저장하는 로직입니다.

**`StoreService.java`**
```java
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final AmenityRepository amenityRepository;
    private final StoreAmenityMappingRepository mappingRepository;

    // ... Constructor

    @Transactional
    public void createStore(YourRequestDto dto) {
        // 1. Store 엔티티 저장
        Store savedStore = storeRepository.save(...);

        // ... 다른 로직 ...

        // 2. 편의시설 정보 처리
        List<String> amenityNames = dto.getAmenities();
        if (amenityNames != null && !amenityNames.isEmpty()) {
            // 2-1. 이름 목록으로 Amenity 엔티티 목록을 한번에 조회
            List<Amenity> foundAmenities = amenityRepository.findByNameIn(amenityNames);

            // 2-2. (선택적) 요청된 편의시설이 DB에 모두 존재하는지 검증
            if (foundAmenities.size() != amenityNames.size()) {
                throw new IllegalArgumentException("존재하지 않는 편의시설 이름이 포함되어 있습니다.");
            }

            // 2-3. StoreAmenityMapping 엔티티 목록 생성
            List<StoreAmenityMapping> mappings = foundAmenities.stream()
                    .map(amenity -> new StoreAmenityMapping(savedStore, amenity))
                    .collect(Collectors.toList());

            // 2-4. 생성된 매핑 정보들을 한번에 저장
            mappingRepository.saveAll(mappings);
        }
    }
}
```

### 3.4. DTO 정의

컨트롤러가 받을 DTO에는 `List<String>` 타입의 필드가 포함됩니다.

**`YourRequestDto.java`**
```java
public class YourRequestDto {
    // ... other fields
    private List<String> amenities;

    // Getters, Setters
}
```
