package com.isolation.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = {
        "com.isolation.dsl",
        "com.isolation.servicea",
        "com.isolation.serviceb",
        "com.isolation.servicec",
        "com.isolation.serviced",
        "com.isolation.gateway"
})
public class ArchitectureRulesTest {

    @ArchTest
    public static final ArchRule services_should_not_depend_on_each_other =
            noClasses()
                    .that().resideInAnyPackage(
                            "com.isolation.servicea..",
                            "com.isolation.serviceb..",
                            "com.isolation.servicec..",
                            "com.isolation.serviced..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "com.isolation.servicea..",
                            "com.isolation.serviceb..",
                            "com.isolation.servicec..",
                            "com.isolation.serviced..")
                    .because("Services must be laterally isolated");

    @ArchTest
    public static final ArchRule gateway_should_not_depend_on_services =
            noClasses()
                    .that().resideInAPackage("com.isolation.gateway..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "com.isolation.servicea..",
                            "com.isolation.serviceb..",
                            "com.isolation.servicec..",
                            "com.isolation.serviced..")
                    .because("Gateway communicates with services over HTTP, not direct Java dependencies");

    @ArchTest
    public static final ArchRule dsl_core_should_not_depend_on_services_or_gateway =
            noClasses()
                    .that().resideInAPackage("com.isolation.dsl..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "com.isolation.servicea..",
                            "com.isolation.serviceb..",
                            "com.isolation.servicec..",
                            "com.isolation.serviced..",
                            "com.isolation.gateway..")
                    .because("DSL core is a shared library and must not depend on services or gateway");
}
