package com.isolationtax.demo;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * These tests turn "we enforce Lateral Isolation" from a claim in a
 * README into something that fails a build if it stops being true.
 *
 * v1 and v2 deliberately do NOT have an equivalent passing rule — that
 * absence is the whole point of the comparison. Try writing the same
 * rule for v1direct or v2facadeunenforced and watch it fail, because
 * OrderServiceDirect and OrderServiceBypass both reach the shared
 * subsystems package directly with nothing to stop them.
 */
class ArchitectureRulesTest {

    private final com.tngtech.archunit.core.domain.JavaClasses importedClasses =
            new ClassFileImporter().importPackages("com.isolationtax.demo");

    @Test
    void v3_subsystem_classes_must_only_be_used_within_their_own_package() {
        ArchRule rule = classes()
                .that().resideInAPackage("..v3facadeenforced..")
                .and().haveSimpleNameEndingWith("Subsystem")
                .should().onlyBeAccessed().byAnyPackage("..v3facadeenforced..");

        rule.check(importedClasses);
    }

    @Test
    void v3_gateway_is_the_only_public_entry_point_into_its_package() {
        ArchRule rule = classes()
                .that().resideInAPackage("..v3facadeenforced..")
                .and().arePublic()
                .should().haveSimpleName("OrderGateway")
                .orShould().haveSimpleName("OrderControllerV3");

        rule.check(importedClasses);
    }
}
