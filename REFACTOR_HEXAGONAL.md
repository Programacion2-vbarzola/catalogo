# Refactor Hexagonal — catalogo

## Estructura de referencia (repo `hexagonal/`)

Antes de planificar, estas son las convenciones exactas que establece el ejemplo del profesor:

| Elemento           | Convención del ejemplo                                            | Ejemplo concreto              |
| ------------------ | ----------------------------------------------------------------- | ----------------------------- |
| Paquete de ports   | `domain/ports/in/`, `domain/ports/out/`                           | (plural `ports`)              |
| Port in            | una interface por operación                                       | `CreateProductUseCase`        |
| Port out           | sin sufijo "Port"                                                 | `ProductRepository`           |
| UseCase impl       | `@Component`, inyecta port out                                    | `CreateProductUseCaseImpl`    |
| Service (facade)   | `@Service`, delega a use cases                                    | `ProductService`              |
| Exception          | en `application/exception/`                                       | `ProductException`            |
| Persistence mapper | `@Component`, manual, `toEntity()` / `toDomainModel()`            | `ProductMapper`               |
| JPA repo           | `JpaProductRepository extends JpaRepository<ProductEntity, Long>` | sin implementar port out      |
| Adapter            | `@Component`, implementa port out                                 | `JpaProductRepositoryAdapter` |
| Web DTO input      | `*Request`                                                        | `ProductRequest`              |
| Web DTO output     | `*Response`                                                       | `ProductResponse`             |
| Web mapper         | `@Component`, `toDomain(Request)` / `toResponse(Domain)`          | `ProductDtoMapper`            |
| Controller         | inyecta `*Service` + `*DtoMapper`                                 | `ProductController`           |

---

## Estructura objetivo completa

```
src/main/java/com/vbarzola/catalogo/
│
├── professional/
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Professional.java
│   │   │   └── ProfessionalCategory.java
│   │   └── ports/
│   │       ├── in/
│   │       │   ├── CreateProfessionalUseCase.java
│   │       │   ├── UpdateProfessionalUseCase.java
│   │       │   ├── PartialUpdateProfessionalUseCase.java
│   │       │   ├── GetProfessionalByIdUseCase.java
│   │       │   ├── GetAllProfessionalsUseCase.java
│   │       │   ├── DeleteProfessionalUseCase.java
│   │       │   ├── SearchProfessionalsUseCase.java
│   │       │   ├── CreateProfessionalCategoryUseCase.java
│   │       │   ├── UpdateProfessionalCategoryUseCase.java
│   │       │   ├── PartialUpdateProfessionalCategoryUseCase.java
│   │       │   ├── GetProfessionalCategoryByIdUseCase.java
│   │       │   ├── GetAllProfessionalCategoriesUseCase.java
│   │       │   └── DeleteProfessionalCategoryUseCase.java
│   │       └── out/
│   │           ├── ProfessionalRepository.java
│   │           └── ProfessionalCategoryRepository.java
│   ├── application/
│   │   ├── service/
│   │   │   ├── ProfessionalService.java
│   │   │   └── ProfessionalCategoryService.java
│   │   ├── usecases/
│   │   │   ├── CreateProfessionalUseCaseImpl.java
│   │   │   ├── UpdateProfessionalUseCaseImpl.java
│   │   │   ├── PartialUpdateProfessionalUseCaseImpl.java
│   │   │   ├── GetProfessionalByIdUseCaseImpl.java
│   │   │   ├── GetAllProfessionalsUseCaseImpl.java
│   │   │   ├── DeleteProfessionalUseCaseImpl.java
│   │   │   ├── SearchProfessionalsUseCaseImpl.java
│   │   │   ├── CreateProfessionalCategoryUseCaseImpl.java
│   │   │   ├── UpdateProfessionalCategoryUseCaseImpl.java
│   │   │   ├── PartialUpdateProfessionalCategoryUseCaseImpl.java
│   │   │   ├── GetProfessionalCategoryByIdUseCaseImpl.java
│   │   │   ├── GetAllProfessionalCategoriesUseCaseImpl.java
│   │   │   └── DeleteProfessionalCategoryUseCaseImpl.java
│   │   └── exception/
│   │       ├── ProfessionalException.java
│   │       └── ProfessionalCategoryException.java
│   └── infrastructure/
│       ├── persistence/
│       │   ├── entity/
│       │   │   ├── ProfessionalEntity.java
│       │   │   └── ProfessionalCategoryEntity.java
│       │   ├── mapper/
│       │   │   ├── ProfessionalMapper.java
│       │   │   └── ProfessionalCategoryMapper.java
│       │   ├── repository/
│       │   │   ├── JpaProfessionalRepository.java
│       │   │   └── JpaProfessionalCategoryRepository.java
│       │   └── adapter/
│       │       ├── JpaProfessionalRepositoryAdapter.java
│       │       └── JpaProfessionalCategoryRepositoryAdapter.java
│       └── web/
│           ├── controller/
│           │   ├── ProfessionalController.java
│           │   └── ProfessionalCategoryController.java
│           ├── dto/
│           │   ├── ProfessionalRequest.java
│           │   ├── ProfessionalResponse.java
│           │   ├── ProfessionalCategoryRequest.java
│           │   └── ProfessionalCategoryResponse.java
│           └── mapper/
│               ├── ProfessionalDtoMapper.java
│               └── ProfessionalCategoryDtoMapper.java
│
├── schedule/
│   ├── domain/
│   │   ├── model/
│   │   │   ├── WeeklySchedule.java
│   │   │   └── enumeration/
│   │   │       └── CatalogDayOfWeek.java
│   │   └── ports/
│   │       ├── in/
│   │       │   ├── CreateWeeklyScheduleUseCase.java
│   │       │   ├── UpdateWeeklyScheduleUseCase.java
│   │       │   ├── PartialUpdateWeeklyScheduleUseCase.java
│   │       │   ├── GetWeeklyScheduleByIdUseCase.java
│   │       │   ├── GetAllWeeklySchedulesUseCase.java
│   │       │   ├── GetWeeklySchedulesByProfessionalUseCase.java
│   │       │   └── DeleteWeeklyScheduleUseCase.java
│   │       └── out/
│   │           └── WeeklyScheduleRepository.java
│   ├── application/
│   │   ├── service/
│   │   │   └── WeeklyScheduleService.java
│   │   ├── usecases/
│   │   │   ├── CreateWeeklyScheduleUseCaseImpl.java
│   │   │   ├── UpdateWeeklyScheduleUseCaseImpl.java
│   │   │   ├── PartialUpdateWeeklyScheduleUseCaseImpl.java
│   │   │   ├── GetWeeklyScheduleByIdUseCaseImpl.java
│   │   │   ├── GetAllWeeklySchedulesUseCaseImpl.java
│   │   │   ├── GetWeeklySchedulesByProfessionalUseCaseImpl.java
│   │   │   └── DeleteWeeklyScheduleUseCaseImpl.java
│   │   └── exception/
│   │       └── WeeklyScheduleException.java
│   └── infrastructure/
│       ├── persistence/
│       │   ├── entity/
│       │   │   └── WeeklyScheduleEntity.java
│       │   ├── mapper/
│       │   │   └── WeeklyScheduleMapper.java
│       │   ├── repository/
│       │   │   └── JpaWeeklyScheduleRepository.java
│       │   └── adapter/
│       │       └── JpaWeeklyScheduleRepositoryAdapter.java
│       └── web/
│           ├── controller/
│           │   └── WeeklyScheduleController.java
│           ├── dto/
│           │   ├── WeeklyScheduleRequest.java
│           │   └── WeeklyScheduleResponse.java
│           └── mapper/
│               └── WeeklyScheduleDtoMapper.java
│
├── sync/
│   ├── domain/
│   │   ├── model/
│   │   │   ├── CatalogSnapshot.java          ← value object, sin persistencia
│   │   │   └── CatalogSyncStatus.java        ← POJO puro
│   │   └── ports/
│   │       ├── in/
│   │       │   └── SyncFullCatalogUseCase.java
│   │       └── out/
│   │           ├── CatedraSnapshotClient.java
│   │           └── CatalogSyncStatusRepository.java
│   ├── application/
│   │   ├── service/
│   │   │   └── CatalogSyncService.java       ← facade
│   │   └── usecases/
│   │       └── SyncFullCatalogUseCaseImpl.java
│   └── infrastructure/
│       ├── persistence/
│       │   ├── entity/
│       │   │   └── CatalogSyncStatusEntity.java
│       │   ├── mapper/
│       │   │   └── CatalogSyncStatusMapper.java
│       │   ├── repository/
│       │   │   └── JpaCatalogSyncStatusRepository.java
│       │   └── adapter/
│       │       └── JpaCatalogSyncStatusRepositoryAdapter.java
│       ├── external/
│       │   └── catedra/
│       │       ├── CatedraSnapshotRestAdapter.java
│       │       └── dto/
│       │           └── CatedraSnapshotResponse.java
│       └── messaging/
│           └── CatalogKafkaConsumer.java
│
├── user/
│   ├── domain/
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   └── Authority.java
│   │   └── ports/
│   │       ├── in/
│   │       │   ├── CreateUserUseCase.java
│   │       │   ├── UpdateUserUseCase.java
│   │       │   ├── GetUserByLoginUseCase.java
│   │       │   ├── GetAllUsersUseCase.java
│   │       │   ├── DeleteUserUseCase.java
│   │       │   └── ActivateUserUseCase.java
│   │       └── out/
│   │           ├── UserRepository.java
│   │           └── AuthorityRepository.java
│   ├── application/
│   │   ├── service/
│   │   │   └── UserService.java              ← facade
│   │   ├── usecases/
│   │   │   ├── CreateUserUseCaseImpl.java
│   │   │   ├── UpdateUserUseCaseImpl.java
│   │   │   ├── GetUserByLoginUseCaseImpl.java
│   │   │   ├── GetAllUsersUseCaseImpl.java
│   │   │   ├── DeleteUserUseCaseImpl.java
│   │   │   └── ActivateUserUseCaseImpl.java
│   │   ├── exception/
│   │   │   └── UserException.java
│   │   └── dto/
│   │       ├── AdminUserDTO.java
│   │       ├── UserDTO.java
│   │       └── PasswordChangeDTO.java
│   └── infrastructure/
│       ├── persistence/
│       │   ├── entity/
│       │   │   ├── UserEntity.java
│       │   │   └── AuthorityEntity.java
│       │   ├── mapper/
│       │   │   └── UserMapper.java
│       │   ├── repository/
│       │   │   ├── JpaUserRepository.java
│       │   │   └── JpaAuthorityRepository.java
│       │   └── adapter/
│       │       └── JpaUserRepositoryAdapter.java
│       └── web/
│           ├── controller/
│           │   ├── AuthenticateController.java
│           │   ├── AccountResource.java
│           │   ├── UserResource.java
│           │   ├── PublicUserResource.java
│           │   └── AuthorityResource.java
│           ├── dto/
│           │   └── (LoginVM, ManagedUserVM, KeyAndPasswordVM, etc.)
│           └── errors/
│               └── (BadRequestAlertException, ExceptionTranslator, etc.)
│
├── config/                        ← sin cambios
├── security/                      ← sin cambios; DomainUserDetailsService usa UserEntity
├── aop/                           ← sin cambios
├── management/                    ← sin cambios
│
│   ← paquetes a eliminar al finalizar:
├── domain/
├── adapter/
├── service/
├── broker/
└── web/rest/
```

---

## Plan de modificaciones

### FASE 1 — Feature `professional/`

**Impacto:** el más alto. Requiere crear entidades separadas, split de DTOs en Request/Response, creación de adapter classes donde hoy no existen, y descomposición de interfaces en una por operación.

---

#### 1.1 — Modelos de dominio (limpiar @Entity)

**`professional/domain/model/ProfessionalCategory.java`**
Mover de `domain/model/ProfessionalCategory.java`. Eliminar `@Entity`, `@Table`, `@Cache`, `@Column`, `@Id`, `@NotNull`, `@Size`, `implements Serializable`. Agregar Lombok (`@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`). Resultado: POJO con campos `id`, `name`, `description`, `enabled`, `createdAt`, `updatedAt`.

**`professional/domain/model/Professional.java`**
Mover de `domain/model/Professional.java`. Mismo proceso. El campo `ProfessionalCategory category` se mantiene como referencia a POJO (sin `@ManyToOne`). Agregar Lombok.

---

#### 1.2 — Ports in (una interface por operación)

Reemplaza las actuales `ProfessionalUseCase` y `ProfessionalCategoryUseCase` (que tenían múltiples métodos) por interfaces de método único.

**`professional/domain/ports/in/CreateProfessionalUseCase.java`**

```java
public interface CreateProfessionalUseCase {
  Professional createProfessional(Professional professional);
}

```

**`professional/domain/ports/in/UpdateProfessionalUseCase.java`**

```java
public interface UpdateProfessionalUseCase {
  Optional<Professional> updateProfessional(Long id, Professional professional);
}

```

**`professional/domain/ports/in/PartialUpdateProfessionalUseCase.java`**

```java
public interface PartialUpdateProfessionalUseCase {
  Optional<Professional> partialUpdateProfessional(Professional professional);
}

```

**`professional/domain/ports/in/GetProfessionalByIdUseCase.java`**

```java
public interface GetProfessionalByIdUseCase {
  Optional<Professional> getProfessionalById(Long id);
}

```

**`professional/domain/ports/in/GetAllProfessionalsUseCase.java`**

```java
public interface GetAllProfessionalsUseCase {
  Page<Professional> getAllProfessionals(Pageable pageable);
  Page<Professional> getAllProfessionalsWithEagerRelationships(Pageable pageable);
}

```

**`professional/domain/ports/in/DeleteProfessionalUseCase.java`**

```java
public interface DeleteProfessionalUseCase {
  void deleteProfessional(Long id);
}

```

**`professional/domain/ports/in/SearchProfessionalsUseCase.java`**

```java
public interface SearchProfessionalsUseCase {
  Page<Professional> search(String name, Long categoryId, Boolean enabled, Pageable pageable);
}

```

Ídem para `ProfessionalCategory`: `CreateProfessionalCategoryUseCase`, `UpdateProfessionalCategoryUseCase`, `PartialUpdateProfessionalCategoryUseCase`, `GetProfessionalCategoryByIdUseCase`, `GetAllProfessionalCategoriesUseCase`, `DeleteProfessionalCategoryUseCase`.

---

#### 1.3 — Port out (renombrar, sin sufijo "Port")

**`professional/domain/ports/out/ProfessionalRepository.java`**
Mover de `domain/port/out/ProfessionalRepositoryPort.java`. Renombrar la interface a `ProfessionalRepository`. Cambiar firmas para usar `Professional` (POJO) en vez de la entidad JPA.

**`professional/domain/ports/out/ProfessionalCategoryRepository.java`**
Mover de `domain/port/out/ProfessionalCategoryRepositoryPort.java`. Renombrar a `ProfessionalCategoryRepository`.

---

#### 1.4 — Entidades JPA

**`professional/infrastructure/persistence/entity/ProfessionalCategoryEntity.java`**
Copiar el contenido actual de `domain/model/ProfessionalCategory.java` (que tiene todas las anotaciones JPA). Renombrar la clase a `ProfessionalCategoryEntity`. Mantener `@Entity`, `@Table("professional_category")`, `@Cache`, `@Column`, etc.

**`professional/infrastructure/persistence/entity/ProfessionalEntity.java`**
Copiar el contenido actual de `domain/model/Professional.java`. Renombrar a `ProfessionalEntity`. La relación `ProfessionalCategory category` pasa a ser `ProfessionalCategoryEntity category` con `@ManyToOne`.

---

#### 1.5 — Mappers de persistencia (Entity ↔ Domain)

Siguiendo el patrón de `ProductMapper`: `@Component`, sin MapStruct, métodos `toEntity()` y `toDomainModel()`.

**`professional/infrastructure/persistence/mapper/ProfessionalCategoryMapper.java`**

```java
@Component
public class ProfessionalCategoryMapper {
    public ProfessionalCategoryEntity toEntity(ProfessionalCategory domain) { ... }
    public ProfessionalCategory toDomainModel(ProfessionalCategoryEntity entity) { ... }
}
```

**`professional/infrastructure/persistence/mapper/ProfessionalMapper.java`**

```java
@Component
@RequiredArgsConstructor
public class ProfessionalMapper {

  private final ProfessionalCategoryMapper categoryMapper;

  public ProfessionalEntity toEntity(Professional domain) {
    return ProfessionalEntity.builder()
      .id(domain.getId())
      .firstName(domain.getFirstName())
      .lastName(domain.getLastName())
      .enabled(domain.getEnabled())
      .createdAt(domain.getCreatedAt())
      .updatedAt(domain.getUpdatedAt())
      .category(categoryMapper.toEntity(domain.getCategory()))
      .build();
  }

  public Professional toDomainModel(ProfessionalEntity entity) {
    return Professional.builder()
      .id(entity.getId())
      .firstName(entity.getFirstName())
      .lastName(entity.getLastName())
      .enabled(entity.getEnabled())
      .createdAt(entity.getCreatedAt())
      .updatedAt(entity.getUpdatedAt())
      .category(categoryMapper.toDomainModel(entity.getCategory()))
      .build();
  }
}

```

**Nota:** los mappers MapStruct actuales (`domain/mapper/ProfessionalMapper.java`, etc.) se eliminan. Son reemplazados por estos mappers manuales `@Component`, que es el patrón del ejemplo.

---

#### 1.6 — Repositorios JPA (solo JPA, sin implementar port out)

**`professional/infrastructure/persistence/repository/JpaProfessionalCategoryRepository.java`**

```java
@Repository
public interface JpaProfessionalCategoryRepository extends JpaRepository<ProfessionalCategoryEntity, Long> {
  // queries si las hay
}

```

Mover las queries del actual `adapter/out/persistence/ProfessionalCategoryJpaRepository.java`. Ya **no** implementa `ProfessionalCategoryRepositoryPort`. Actualizar el tipo en JPQL de `ProfessionalCategory` a `ProfessionalCategoryEntity`.

**`professional/infrastructure/persistence/repository/JpaProfessionalRepository.java`**

```java
@Repository
public interface JpaProfessionalRepository extends JpaRepository<ProfessionalEntity, Long> {
  Page<ProfessionalEntity> findAllWithToOneRelationships(Pageable pageable);
  Optional<ProfessionalEntity> findOneWithToOneRelationships(Long id);
  Page<ProfessionalEntity> search(String name, Long categoryId, Boolean enabled, Pageable pageable);
}

```

Mover queries del actual `adapter/out/persistence/ProfessionalJpaRepository.java`. Actualizar JPQL a `ProfessionalEntity`. Ya **no** implementa `ProfessionalRepositoryPort`.

---

#### 1.7 — Adapter classes (nueva creación — el patrón central)

El patrón exacto de `JpaProductRepositoryAdapter`:

**`professional/infrastructure/persistence/adapter/JpaProfessionalCategoryRepositoryAdapter.java`**

```java
@Component
@RequiredArgsConstructor
public class JpaProfessionalCategoryRepositoryAdapter implements ProfessionalCategoryRepository {

  private final JpaProfessionalCategoryRepository jpaRepository;
  private final ProfessionalCategoryMapper mapper;

  @Override
  public ProfessionalCategory save(ProfessionalCategory domain) {
    ProfessionalCategoryEntity entity = mapper.toEntity(domain);
    return mapper.toDomainModel(jpaRepository.save(entity));
  }

  @Override
  public Optional<ProfessionalCategory> findById(Long id) {
    return jpaRepository.findById(id).map(mapper::toDomainModel);
  }

  @Override
  public List<ProfessionalCategory> findAll() {
    return jpaRepository.findAll().stream().map(mapper::toDomainModel).toList();
  }

  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return jpaRepository.existsById(id);
  }

  @Override
  public void deleteAll() {
    jpaRepository.deleteAll();
  }
}

```

**`professional/infrastructure/persistence/adapter/JpaProfessionalRepositoryAdapter.java`**
Ídem para `ProfessionalRepository`. Los métodos `findAll(Pageable)`, `findAllWithEagerRelationships(Pageable)`, `findOneWithEagerRelationships(id)`, `search(...)` delegan al JPA repo y mapean con `mapper.toDomainModel()`.

---

#### 1.8 — Use case implementations (una clase por operación)

Patrón exacto de `CreateProductUseCaseImpl`:

**`professional/application/usecases/CreateProfessionalUseCaseImpl.java`**

```java
@Component
@RequiredArgsConstructor
public class CreateProfessionalUseCaseImpl implements CreateProfessionalUseCase {

  private final ProfessionalRepository professionalRepository;

  @Override
  public Professional createProfessional(Professional professional) {
    return professionalRepository.save(professional);
  }
}

```

**`professional/application/usecases/GetProfessionalByIdUseCaseImpl.java`**

```java
@Component
@RequiredArgsConstructor
public class GetProfessionalByIdUseCaseImpl implements GetProfessionalByIdUseCase {

  private final ProfessionalRepository professionalRepository;

  @Override
  public Optional<Professional> getProfessionalById(Long id) {
    return professionalRepository.findOneWithEagerRelationships(id);
  }
}

```

**`professional/application/usecases/UpdateProfessionalUseCaseImpl.java`**

```java
@Component
@RequiredArgsConstructor
public class UpdateProfessionalUseCaseImpl implements UpdateProfessionalUseCase {

  private final ProfessionalRepository professionalRepository;

  @Override
  public Optional<Professional> updateProfessional(Long id, Professional professional) {
    return professionalRepository
      .findById(id)
      .map(existing -> {
        professional.setId(id);
        return professionalRepository.save(professional);
      });
  }
}

```

Crear de la misma forma: `PartialUpdateProfessionalUseCaseImpl`, `GetAllProfessionalsUseCaseImpl`, `DeleteProfessionalUseCaseImpl`, `SearchProfessionalsUseCaseImpl`. Ídem para toda la serie de `ProfessionalCategory`.

**`professional/application/exception/ProfessionalException.java`**

```java
public class ProfessionalException extends RuntimeException {

  public ProfessionalException(Long id) {
    super("Could not find professional with id: " + id);
  }
}

```

---

#### 1.9 — Service facade

Patrón exacto de `ProductService`:

**`professional/application/service/ProfessionalService.java`**

```java
@Service
@RequiredArgsConstructor
public class ProfessionalService {

  private final CreateProfessionalUseCase createProfessionalUseCase;
  private final UpdateProfessionalUseCase updateProfessionalUseCase;
  private final PartialUpdateProfessionalUseCase partialUpdateProfessionalUseCase;
  private final GetProfessionalByIdUseCase getProfessionalByIdUseCase;
  private final GetAllProfessionalsUseCase getAllProfessionalsUseCase;
  private final DeleteProfessionalUseCase deleteProfessionalUseCase;
  private final SearchProfessionalsUseCase searchProfessionalsUseCase;

  public Professional createProfessional(Professional professional) {
    return createProfessionalUseCase.createProfessional(professional);
  }

  public Professional getProfessionalById(Long id) {
    return getProfessionalByIdUseCase.getProfessionalById(id).orElseThrow(() -> new ProfessionalException(id));
  }

  public Optional<Professional> updateProfessional(Long id, Professional professional) {
    return updateProfessionalUseCase.updateProfessional(id, professional);
  }

  public Optional<Professional> partialUpdateProfessional(Professional professional) {
    return partialUpdateProfessionalUseCase.partialUpdateProfessional(professional);
  }

  public Page<Professional> getAllProfessionals(Pageable pageable) {
    return getAllProfessionalsUseCase.getAllProfessionals(pageable);
  }

  public Page<Professional> getAllProfessionalsWithEagerRelationships(Pageable pageable) {
    return getAllProfessionalsUseCase.getAllProfessionalsWithEagerRelationships(pageable);
  }

  public void deleteProfessional(Long id) {
    deleteProfessionalUseCase.deleteProfessional(id);
  }

  public Page<Professional> search(String name, Long categoryId, Boolean enabled, Pageable pageable) {
    return searchProfessionalsUseCase.search(name, categoryId, enabled, pageable);
  }
}

```

**`professional/application/service/ProfessionalCategoryService.java`** — mismo patrón para `ProfessionalCategory`.

---

#### 1.10 — Web DTOs (Request / Response en vez de DTO único)

Reemplaza `ProfessionalDTO` por dos clases separadas.

**`professional/infrastructure/web/dto/ProfessionalRequest.java`**

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionalRequest {

  @NotNull
  @Size(max = 100)
  private String firstName;

  @NotNull
  @Size(max = 100)
  private String lastName;

  @NotNull
  private Boolean enabled;

  @NotNull
  private Long categoryId; // solo el ID de la categoría
}

```

**`professional/infrastructure/web/dto/ProfessionalResponse.java`**

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionalResponse {

  private Long id;
  private String firstName;
  private String lastName;
  private Boolean enabled;
  private Instant createdAt;
  private Instant updatedAt;
  private ProfessionalCategoryResponse category; // categoría anidada en el response
}

```

**`professional/infrastructure/web/dto/ProfessionalCategoryRequest.java`** — campos: `name`, `description`, `enabled`.

**`professional/infrastructure/web/dto/ProfessionalCategoryResponse.java`** — campos: `id`, `name`, `description`, `enabled`, `createdAt`, `updatedAt`.

---

#### 1.11 — Web mappers (Domain ↔ Request/Response)

Patrón exacto de `ProductDtoMapper`:

**`professional/infrastructure/web/mapper/ProfessionalCategoryDtoMapper.java`**

```java
@Component
public class ProfessionalCategoryDtoMapper {
    public ProfessionalCategory toDomain(ProfessionalCategoryRequest request) { ... }
    public ProfessionalCategoryResponse toResponse(ProfessionalCategory domain) { ... }
}
```

**`professional/infrastructure/web/mapper/ProfessionalDtoMapper.java`**

```java
@Component
@RequiredArgsConstructor
public class ProfessionalDtoMapper {

  private final ProfessionalCategoryDtoMapper categoryDtoMapper;

  public Professional toDomain(ProfessionalRequest request) {
    ProfessionalCategory category = new ProfessionalCategory();
    category.setId(request.getCategoryId()); // solo setea el ID para la relación
    return Professional.builder()
      .firstName(request.getFirstName())
      .lastName(request.getLastName())
      .enabled(request.getEnabled())
      .category(category)
      .build();
  }

  public ProfessionalResponse toResponse(Professional domain) {
    return ProfessionalResponse.builder()
      .id(domain.getId())
      .firstName(domain.getFirstName())
      .lastName(domain.getLastName())
      .enabled(domain.getEnabled())
      .createdAt(domain.getCreatedAt())
      .updatedAt(domain.getUpdatedAt())
      .category(categoryDtoMapper.toResponse(domain.getCategory()))
      .build();
  }
}

```

---

#### 1.12 — Controladores

Patrón exacto de `ProductController`. Inyecta `ProfessionalService` + `ProfessionalDtoMapper`.

**`professional/infrastructure/web/controller/ProfessionalController.java`**

```java
@RestController
@RequestMapping("/api/professionals")
@RequiredArgsConstructor
public class ProfessionalController {

  private final ProfessionalService professionalService;
  private final ProfessionalDtoMapper dtoMapper;

  @PostMapping
  public ResponseEntity<ProfessionalResponse> createProfessional(@Valid @RequestBody ProfessionalRequest request)
    throws URISyntaxException {
    Professional domain = dtoMapper.toDomain(request);
    Professional created = professionalService.createProfessional(domain);
    return ResponseEntity.created(new URI("/api/professionals/" + created.getId())).body(dtoMapper.toResponse(created));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProfessionalResponse> getProfessional(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(dtoMapper.toResponse(professionalService.getProfessionalById(id)));
    } catch (ProfessionalException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProfessionalResponse> updateProfessional(@PathVariable Long id, @Valid @RequestBody ProfessionalRequest request) {
    Professional domain = dtoMapper.toDomain(request);
    return professionalService
      .updateProfessional(id, domain)
      .map(dtoMapper::toResponse)
      .map(ResponseEntity::ok)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
  // ... getAllProfessionals, search, delete
}

```

**`professional/infrastructure/web/controller/ProfessionalCategoryController.java`** — mismo patrón.

---

#### Archivos a eliminar tras FASE 1

- `domain/model/Professional.java`, `domain/model/ProfessionalCategory.java`
- `domain/dto/ProfessionalDTO.java`, `domain/dto/ProfessionalCategoryDTO.java`
- `domain/mapper/ProfessionalMapper.java`, `domain/mapper/ProfessionalCategoryMapper.java`
- `domain/port/in/ProfessionalUseCase.java`, `domain/port/in/ProfessionalCategoryUseCase.java`, `domain/port/in/SearchProfessionalsUseCase.java`
- `domain/port/out/ProfessionalRepositoryPort.java`, `domain/port/out/ProfessionalCategoryRepositoryPort.java`
- `domain/service/ProfessionalService.java`, `domain/service/ProfessionalCategoryService.java`, `domain/service/SearchProfessionalsService.java`
- `adapter/out/persistence/ProfessionalJpaRepository.java`, `adapter/out/persistence/ProfessionalCategoryJpaRepository.java`
- `adapter/in/rest/ProfessionalResource.java`, `adapter/in/rest/ProfessionalCategoryResource.java`

---

### FASE 2 — Feature `schedule/`

**Impacto:** medio. Idéntico al patrón de FASE 1. El único punto especial es que `WeeklyScheduleEntity` referencia `ProfessionalEntity` y `WeeklyScheduleMapper` necesita inyectar `ProfessionalMapper`.

#### Puntos clave

**`schedule/domain/model/WeeklySchedule.java`**
POJO puro. El campo `Professional professional` mantiene la referencia de dominio (importando desde `professional.domain.model`). Sin `@ManyToOne`.

**`schedule/infrastructure/persistence/entity/WeeklyScheduleEntity.java`**
Copia de `domain/model/WeeklySchedule.java` con anotaciones JPA. La relación `Professional professional` pasa a ser `ProfessionalEntity professional` con `@ManyToOne`.

**`schedule/infrastructure/persistence/mapper/WeeklyScheduleMapper.java`**

```java
@Component
@RequiredArgsConstructor
public class WeeklyScheduleMapper {
    private final ProfessionalMapper professionalMapper;   // mapper del feature professional/

    public WeeklyScheduleEntity toEntity(WeeklySchedule domain) { ... }
    public WeeklySchedule toDomainModel(WeeklyScheduleEntity entity) { ... }
}
```

**`schedule/infrastructure/persistence/repository/JpaWeeklyScheduleRepository.java`**
Extiende `JpaRepository<WeeklyScheduleEntity, Long>`. Mover queries del actual `adapter/out/persistence/WeeklyScheduleJpaRepository.java`. Actualizar JPQL a `WeeklyScheduleEntity`. Ya **no** implementa `WeeklyScheduleRepositoryPort`.

**`schedule/infrastructure/persistence/adapter/JpaWeeklyScheduleRepositoryAdapter.java`**
Implementa `WeeklyScheduleRepository`. Inyecta `JpaWeeklyScheduleRepository` + `WeeklyScheduleMapper`. Mismo patrón que los adapters de professional.

**`schedule/infrastructure/web/dto/WeeklyScheduleRequest.java`**
Campos: `dayOfWeek`, `startTime`, `endTime`, `slotDurationMinutes`, `enabled`, `professionalId`.

**`schedule/infrastructure/web/dto/WeeklyScheduleResponse.java`**
Campos: `id`, `dayOfWeek`, `startTime`, `endTime`, `slotDurationMinutes`, `enabled`, `createdAt`, `updatedAt`, `ProfessionalResponse professional`.

**`schedule/infrastructure/web/mapper/WeeklyScheduleDtoMapper.java`**
`toDomain(WeeklyScheduleRequest)` y `toResponse(WeeklySchedule)`. Inyecta `ProfessionalDtoMapper`.

Use cases, UseCaseImpls, `WeeklyScheduleService` (facade), `WeeklyScheduleController` — mismo patrón que FASE 1.

#### Archivos a eliminar tras FASE 2

- `domain/model/WeeklySchedule.java`, `domain/model/enumeration/CatalogDayOfWeek.java`
- `domain/dto/WeeklyScheduleDTO.java`, `domain/mapper/WeeklyScheduleMapper.java`
- `domain/port/in/WeeklyScheduleUseCase.java`, `domain/port/in/GetWeeklySchedulesUseCase.java`
- `domain/port/out/WeeklyScheduleRepositoryPort.java`
- `domain/service/WeeklyScheduleService.java`, `domain/service/GetWeeklySchedulesService.java`
- `adapter/out/persistence/WeeklyScheduleJpaRepository.java`
- `adapter/in/rest/WeeklyScheduleResource.java`

---

### FASE 3 — Feature `sync/`

**Impacto:** medio-bajo. `CatalogSnapshot` ya es un POJO — solo actualizar imports. `CatalogSyncStatus` necesita separarse. La lógica de `SyncFullCatalogUseCaseImpl` no cambia.

#### 3.1 — Domain models

**`sync/domain/model/CatalogSnapshot.java`**
Mover de `domain/model/CatalogSnapshot.java`. Actualizar imports de `Professional`, `ProfessionalCategory`, `WeeklySchedule` a sus nuevos packages. La clase no cambia su lógica.

**`sync/domain/model/CatalogSyncStatus.java`**
Mover de `domain/model/CatalogSyncStatus.java`. Eliminar `@Entity`, `@Table`, `@Column`, `@Id`, `@NotNull`, `@Size`. Agregar Lombok. Mantener la constante `SINGLETON_ID = 1L`.

#### 3.2 — Ports

**`sync/domain/ports/in/SyncFullCatalogUseCase.java`** — mover, sin cambios.

**`sync/domain/ports/out/CatedraSnapshotClient.java`** — mover de `domain/port/out/`.

**`sync/domain/ports/out/CatalogSyncStatusRepository.java`** — mover de `domain/port/out/CatalogSyncStatusRepositoryPort.java`. Renombrar (sin sufijo "Port") a `CatalogSyncStatusRepository`.

#### 3.3 — Entidad JPA, mapper, repositorio, adapter

**`sync/infrastructure/persistence/entity/CatalogSyncStatusEntity.java`**
Copia de `domain/model/CatalogSyncStatus.java` con todas sus anotaciones JPA. Incluir la constante `SINGLETON_ID`.

**`sync/infrastructure/persistence/mapper/CatalogSyncStatusMapper.java`**

```java
@Component
public class CatalogSyncStatusMapper {
    public CatalogSyncStatusEntity toEntity(CatalogSyncStatus domain) { ... }
    public CatalogSyncStatus toDomainModel(CatalogSyncStatusEntity entity) { ... }
}
```

**`sync/infrastructure/persistence/repository/JpaCatalogSyncStatusRepository.java`**

```java
@Repository
public interface JpaCatalogSyncStatusRepository extends JpaRepository<CatalogSyncStatusEntity, Long> {
  default Optional<CatalogSyncStatusEntity> findSingleton() {
    return findById(CatalogSyncStatusEntity.SINGLETON_ID);
  }
}

```

Ya **no** implementa `CatalogSyncStatusRepositoryPort`.

**`sync/infrastructure/persistence/adapter/JpaCatalogSyncStatusRepositoryAdapter.java`**

```java
@Component
@RequiredArgsConstructor
public class JpaCatalogSyncStatusRepositoryAdapter implements CatalogSyncStatusRepository {

  private final JpaCatalogSyncStatusRepository jpaRepository;
  private final CatalogSyncStatusMapper mapper;

  @Override
  public Optional<CatalogSyncStatus> findSingleton() {
    return jpaRepository.findSingleton().map(mapper::toDomainModel);
  }

  @Override
  public CatalogSyncStatus save(CatalogSyncStatus domain) {
    return mapper.toDomainModel(jpaRepository.save(mapper.toEntity(domain)));
  }

  @Override
  public boolean existsById(Long id) {
    return jpaRepository.existsById(id);
  }
}

```

#### 3.4 — Use case, facade, adapter externo

**`sync/application/usecases/SyncFullCatalogUseCaseImpl.java`**
Mover de `domain/service/SyncFullCatalogService.java`. Renombrar la clase a `SyncFullCatalogUseCaseImpl`. Actualizar imports de los ports al nuevo package (`sync.domain.ports.out`, `professional.domain.ports.out`, `schedule.domain.ports.out`). La lógica de negocio **no cambia**.

**`sync/application/service/CatalogSyncService.java`** (facade)

```java
@Service
@RequiredArgsConstructor
public class CatalogSyncService {

  private final SyncFullCatalogUseCase syncFullCatalogUseCase;

  public void syncFullCatalog() {
    syncFullCatalogUseCase.syncFullCatalog();
  }
}

```

**`sync/infrastructure/external/catedra/CatedraSnapshotRestAdapter.java`**
Mover de `adapter/out/catedra/`. Actualizar imports. La lógica de creación de POJOs no cambia porque los domain models ya eran instanciados como objetos normales.

**`sync/infrastructure/external/catedra/dto/CatedraSnapshotResponse.java`**
Mover de `adapter/out/catedra/dto/`.

**`sync/infrastructure/messaging/CatalogKafkaConsumer.java`**
Mover de `adapter/in/kafka/CatalogKafkaConsumer.java`. Solo cambio de package.

#### Archivos a eliminar tras FASE 3

- `domain/model/CatalogSyncStatus.java`, `domain/model/CatalogSnapshot.java`
- `domain/port/in/SyncFullCatalogUseCase.java`
- `domain/port/out/CatalogSyncStatusRepositoryPort.java`, `domain/port/out/CatedraSnapshotClient.java`
- `domain/service/SyncFullCatalogService.java`
- `adapter/out/catedra/` (todo), `adapter/in/kafka/` (todo)
- `adapter/out/persistence/CatalogSyncStatusJpaRepository.java`
- `broker/` (todo — `KafkaConsumer.java`, `KafkaProducer.java`)

---

### FASE 4 — Feature `user/`

**Impacto:** alto. Integración con Spring Security. Misma complejidad que en turnosYreservas.

#### 4.1 — Domain models

**`user/domain/model/Authority.java`** — mover de `domain/Authority.java`. Eliminar `@Entity`, `@Table`. Mantener solo `String name` con Lombok.

**`user/domain/model/User.java`** — mover de `domain/User.java`. Eliminar todas las anotaciones JPA. El campo `Set<Authority>` pasa a `Set<String>` (solo los nombres de los roles). Agregar Lombok.

#### 4.2 — Ports in (siguiendo el patrón de una interface por operación)

`CreateUserUseCase`, `UpdateUserUseCase`, `GetUserByLoginUseCase`, `GetAllUsersUseCase`, `DeleteUserUseCase`, `ActivateUserUseCase`.

#### 4.3 — Entidades JPA

**`user/infrastructure/persistence/entity/AuthorityEntity.java`** — copia de `domain/Authority.java` con `@Entity`, `@Table("jhi_authority")`.

**`user/infrastructure/persistence/entity/UserEntity.java`** — copia de `domain/User.java` con todos sus `@Entity`, `@Table("jhi_user")`, `@ManyToMany` a `AuthorityEntity`, `extends AbstractAuditingEntity<Long>`.

#### 4.4 — Mapper, repositorios, adapter, services

Mismo patrón: `UserMapper` (manual, `@Component`), `JpaUserRepository extends JpaRepository<UserEntity, Long>`, `JpaUserRepositoryAdapter implements UserRepository`, `UserService` (facade con use cases).

**`security/DomainUserDetailsService.java`** — actualizar para usar `JpaUserRepository` (que devuelve `UserEntity`) en vez del antiguo `UserRepository` que devolvía `User` con @Entity.

#### 4.5 — Web layer

Mover `web/rest/` → `user/infrastructure/web/controller/`. Mover `service/dto/` → `user/application/dto/`. Mover `service/UserService.java` → `user/application/service/UserService.java`.

#### Archivos a eliminar tras FASE 4

- `domain/User.java`, `domain/Authority.java`, `domain/AbstractAuditingEntity.java`
- `service/` (todo)
- `web/rest/` (todo)
- `repository/UserRepository.java`, `repository/AuthorityRepository.java`

---

### FASE 5 — Limpieza

Eliminar paquetes que quedan vacíos tras las 4 fases:

- `domain/` (todo — debe estar vacío)
- `adapter/` (todo — debe estar vacío)
- `service/` (todo — debe estar vacío)
- `broker/` (movido a `sync/infrastructure/messaging/`)
- `web/rest/` (todo — debe estar vacío)
- `repository/` si existía algún repo que no fue movido
- Los `package-info.java` huérfanos

---

## Verificación por fase

### Verificación rápida (después de cada fase)

```bash
./mvnw compile -q
```

### Verificación de arquitectura

```bash
./mvnw test -Dtest=HexagonalArchitectureTest -q
```

Actualizar `HexagonalArchitectureTest.java` para los nuevos paquetes:

```java
@AnalyzeClasses(packages = "com.vbarzola.catalogo", importOptions = DoNotIncludeTests.class)
public class HexagonalArchitectureTest {

  @ArchTest
  static final ArchRule domain_must_not_depend_on_infrastructure = noClasses()
    .that()
    .resideInAnyPackage("..professional.domain..", "..schedule.domain..", "..sync.domain..", "..user.domain..")
    .should()
    .dependOnClassesThat()
    .resideInAnyPackage(
      "..professional.infrastructure..",
      "..schedule.infrastructure..",
      "..sync.infrastructure..",
      "..user.infrastructure.."
    );

  @ArchTest
  static final ArchRule application_must_not_depend_on_infrastructure = noClasses()
    .that()
    .resideInAnyPackage("..professional.application..", "..schedule.application..", "..sync.application..", "..user.application..")
    .should()
    .dependOnClassesThat()
    .resideInAnyPackage(
      "..professional.infrastructure..",
      "..schedule.infrastructure..",
      "..sync.infrastructure..",
      "..user.infrastructure.."
    );

  @ArchTest
  static final ArchRule in_adapters_must_not_depend_on_out_adapters = noClasses()
    .that()
    .resideInAnyPackage("..infrastructure.web..")
    .should()
    .dependOnClassesThat()
    .resideInAnyPackage("..infrastructure.persistence..", "..infrastructure.external..");
}

```

### Verificación completa

```bash
./mvnw verify
./mvnw test -Dtest="HexagonalArchitectureTest,TechnicalStructureTest"
```

### Verificación manual de endpoints

```bash
./mvnw spring-boot:run

TOKEN=$(curl -s -X POST http://localhost:8082/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin","rememberMe":false}' | jq -r '.id_token')

# Feature professional/
curl -X GET "http://localhost:8082/api/professionals?page=0&size=5" \
  -H "Authorization: Bearer $TOKEN"

curl -X GET "http://localhost:8082/api/professionals/search?enabled=true" \
  -H "Authorization: Bearer $TOKEN"

# Feature schedule/
curl -X GET "http://localhost:8082/api/weekly-schedules?page=0&size=5" \
  -H "Authorization: Bearer $TOKEN"

# Feature user/
curl -X GET http://localhost:8082/api/account \
  -H "Authorization: Bearer $TOKEN"
```

---

## Orden estricto de las fases

```
FASE 1 (professional/)
   ↓ debe compilar antes de...
FASE 2 (schedule/)     ← WeeklyScheduleEntity depende de ProfessionalEntity
   ↓ deben compilar antes de...
FASE 3 (sync/)         ← SyncFullCatalogUseCaseImpl importa ports de professional/ y schedule/
   ↓ independiente de las anteriores:
FASE 4 (user/)
   ↓
FASE 5 (limpieza)
```
