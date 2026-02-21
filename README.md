# Evaluador de Prestamos

Aplicacion de consola construida con Spring Boot que evalua solicitudes de prestamo aplicando reglas de negocio basadas en puntaje de credito, deudas e historial de empleo.

## Requisitos

- Java 17
- Maven (incluido via wrapper `mvnw`)

## Compilacion y ejecucion

### Compilar

```bash
.\mvnw compile
```

### Ejecutar la aplicacion

```bash
.\mvnw spring-boot:run
```

La aplicacion solicita los siguientes datos por consola:

| Campo | Descripcion |
|---|---|
| Nombre | Nombre del solicitante |
| Edad | Edad en anos (minimo 18) |
| Puntaje de credito | Valor entre 0 y 850 |
| Tiene deudas | `si` o `no` |
| Anos de empleo | Numero entero >= 0 |
| Ingreso mensual | Valor mayor a 0 |
| Monto solicitado | Maximo 5x el ingreso mensual |

Al finalizar muestra uno de tres resultados: `APPROVED`, `REJECTED` o `MANUAL_REVIEW`.

### Ejecutar los tests

```bash
.\mvnw test
```

Los tests corren sin levantar el evaluador de consola. Al ejecutar los tests se activa el perfil `test`, lo que desactiva el `CommandLineRunner`.

### Compilar y empaquetar

```bash
.\mvnw package
```

Genera el JAR en `target/demo-0.0.1-SNAPSHOT.jar`.

---

## Estructura del proyecto

```
src/
├── main/java/com/u1/demo/
│   ├── DemoApplication.java   # Entry point + CommandLineRunner (perfil !test)
│   ├── Applicant.java         # Modelo de dominio con invariantes
│   ├── LoanEvaluator.java     # Logica de evaluacion con pre/postcondiciones
│   └── LoanDecision.java      # Enum: APPROVED, REJECTED, MANUAL_REVIEW
└── test/java/com/u1/demo/
    ├── LoanEvaluatorTest.java  # 11 tests unitarios
    └── DemoApplicationTests.java # Test de carga del contexto Spring
```

---

## Decisiones de diseno

### 1. Invariantes en `Applicant`

El modelo `Applicant` verifica sus propias invariantes en el constructor y en cada setter mediante `verifyInvariant()`. Esto garantiza que nunca exista un `Applicant` en estado invalido en memoria, independientemente de quien lo construya. Las reglas son:

- Nombre no nulo ni en blanco
- Edad minima de 18 anos
- Puntaje de credito entre 0 y 850
- Anos de empleo >= 0
- Ingreso mensual > 0

### 2. Precondiciones y postcondiciones en `LoanEvaluator`

`LoanEvaluator.evaluateLoan()` implementa diseno por contrato:

- **Precondiciones** (`validatePreconditions`): el solicitante no puede ser nulo, el monto debe ser mayor a cero y no puede superar 5 veces el ingreso mensual. Si alguna falla, se lanza `IllegalArgumentException`.
- **Postcondiciones** (`validatePostconditions`): verifican que la decision devuelta sea consistente con las reglas de negocio. Actuan como red de seguridad ante futuros cambios en la logica. Si fallan, se lanza `IllegalStateException`.

### 3. `LoanEvaluator` como clase utilitaria final

`LoanEvaluator` es `final` con constructor privado. No tiene estado ni dependencias inyectadas. La logica de evaluacion es pura: dado el mismo `Applicant` y monto, siempre devuelve el mismo resultado, lo que facilita el testing unitario sin mocks.

### 4. Tabla de decision

Las reglas de negocio estan documentadas en `decisionTable.md` y se implementan directamente en `LoanEvaluator`:

| Condicion | Resultado |
|---|---|
| Edad < 18 | Rechazado (invariante) |
| Monto > 5x ingreso | Rechazado (precondicion) |
| Puntaje >= 700, sin deudas | APPROVED |
| Puntaje >= 700, con deudas | MANUAL_REVIEW |
| Puntaje < 500 | REJECTED |
| Puntaje 500–699, empleo > 2 anos | MANUAL_REVIEW |
| Puntaje 500–699, empleo <= 2 anos | REJECTED |

### 5. Separacion entre ejecucion y tests

El `CommandLineRunner` que lanza el evaluador por consola esta anotado con `@Profile("!test")`. Los tests de contexto (`DemoApplicationTests`) activan `@ActiveProfiles("test")`, lo que impide que el runner se registre como bean durante la ejecucion de tests. Asi los tests corren sin bloqueos de entrada por consola.
