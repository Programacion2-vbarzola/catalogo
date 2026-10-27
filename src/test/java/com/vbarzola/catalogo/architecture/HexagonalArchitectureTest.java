package com.vbarzola.catalogo.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Verifies that the hexagonal architecture constraints hold:
 *
 * 1. The domain layer (model, ports, services, DTOs, mappers) must NOT depend on any adapter.
 * 2. Input adapters (REST, Kafka) must NOT depend on output adapters (persistence).
 *
 * This guarantees the domain core is framework-independent and testable in isolation,
 * and that REST/Kafka adapters are not tightly coupled to persistence concerns.
 */
@AnalyzeClasses(packages = "com.vbarzola.catalogo", importOptions = DoNotIncludeTests.class)
public class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule domain_must_not_depend_on_adapters = noClasses()
        .that()
        .resideInAPackage("com.vbarzola.catalogo.domain..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("com.vbarzola.catalogo.adapter..")
        .as("Domain layer must not depend on any adapter");

    @ArchTest
    static final ArchRule in_adapters_must_not_depend_on_out_adapters = noClasses()
        .that()
        .resideInAPackage("com.vbarzola.catalogo.adapter.in..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("com.vbarzola.catalogo.adapter.out..")
        .as("Input adapters must not depend on output adapters");

    @ArchTest
    static final ArchRule professional_domain_must_not_depend_on_infrastructure = noClasses()
        .that()
        .resideInAPackage("com.vbarzola.catalogo.professional.domain..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("com.vbarzola.catalogo.professional.infrastructure..")
        .as("professional domain layer must not depend on professional infrastructure");

    @ArchTest
    static final ArchRule professional_application_must_not_depend_on_infrastructure = noClasses()
        .that()
        .resideInAPackage("com.vbarzola.catalogo.professional.application..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("com.vbarzola.catalogo.professional.infrastructure..")
        .as("professional application layer must not depend on professional infrastructure");

    @ArchTest
    static final ArchRule professional_web_must_not_depend_on_persistence = noClasses()
        .that()
        .resideInAPackage("com.vbarzola.catalogo.professional.infrastructure.web..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("com.vbarzola.catalogo.professional.infrastructure.persistence..")
        .as("professional web adapters must not depend on professional persistence adapters");

    @ArchTest
    static final ArchRule schedule_domain_must_not_depend_on_infrastructure = noClasses()
        .that()
        .resideInAPackage("com.vbarzola.catalogo.schedule.domain..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("com.vbarzola.catalogo.schedule.infrastructure..")
        .as("schedule domain layer must not depend on schedule infrastructure");

    @ArchTest
    static final ArchRule schedule_application_must_not_depend_on_infrastructure = noClasses()
        .that()
        .resideInAPackage("com.vbarzola.catalogo.schedule.application..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("com.vbarzola.catalogo.schedule.infrastructure..")
        .as("schedule application layer must not depend on schedule infrastructure");

    @ArchTest
    static final ArchRule schedule_web_must_not_depend_on_persistence = noClasses()
        .that()
        .resideInAPackage("com.vbarzola.catalogo.schedule.infrastructure.web..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("com.vbarzola.catalogo.schedule.infrastructure.persistence..")
        .as("schedule web adapters must not depend on schedule persistence adapters");
}
