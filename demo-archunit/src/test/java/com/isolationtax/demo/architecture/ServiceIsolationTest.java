package com.isolationtax.demo.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Test;

/**
 * This is the article's central claim, made testable: Service A cannot
 * reach Service C directly (or B, or any other sibling). If someone
 * later adds "just one" direct call between two services - exactly the
 * temptation the article opens with - this test fails, naming both
 * services involved.
 */
class ServiceIsolationTest {

    @Test
    void servicesDoNotDependOnEachOther() {
        JavaClasses classes = new ClassFileImporter()
                .importPackages("com.isolationtax.demo.services");

        ArchRule rule = SlicesRuleDefinition.slices()
                .matching("com.isolationtax.demo.services.(*)..")
                .should().notDependOnEachOther();

        rule.check(classes);
    }
}
