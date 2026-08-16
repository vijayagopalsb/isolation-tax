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


//package com.isolation.archunit;
//
//import com.tngtech.archunit.core.domain.JavaClasses;
//import com.tngtech.archunit.core.importer.ClassFileImporter;
//import com.tngtech.archunit.core.importer.ImportOption;
//import com.tngtech.archunit.junit.AnalyzeClasses;
//import com.tngtech.archunit.junit.ArchTest;
//import com.tngtech.archunit.lang.ArchRule;
//
//import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
//import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
//
//@AnalyzeClasses(packages = {
//        "com.isolation.dsl",
//        "com.isolation.servicea",
//        "com.isolation.serviceb",
//        "com.isolation.servicec",
//        "com.isolation.serviced",
//        "com.isolation.gateway"
//}, importOptions = ImportOption.DoNotIncludeTests.class)
//public class ArchitectureRulesTest {
//
//    // 1. Services must not depend on each other
//    @ArchTest
//    public static final ArchRule services_should_not_depend_on_each_other =
//            noClasses()
//                .that().resideInAnyPackage(
//                        "com.isolation.servicea..",
//                        "com.isolation.serviceb..",
//                        "com.isolation.servicec..",
//                        "com.isolation.serviced..")
//                .should().dependOnClassesThat().resideInAnyPackage(
//                        "com.isolation.servicea..",
//                        "com.isolation.serviceb..",
//                        "com.isolation.servicec..",
//                        "com.isolation.serviced..")
//                .because("Services must be laterally isolated");
//
//    // 2. Gateway must not depend on any service internals (only via HTTP)
//    @ArchTest
//    public static final ArchRule gateway_should_not_depend_on_services =
//            noClasses()
//                .that().resideInAPackage("com.isolation.gateway..")
//                .should().dependOnClassesThat().resideInAnyPackage(
//                        "com.isolation.servicea..",
//                        "com.isolation.serviceb..",
//                        "com.isolation.servicec..",
//                        "com.isolation.serviced..")
//                .because("Gateway communicates with services over HTTP, not direct Java dependencies");
//
//    // 3. DSL core must not depend on any service or gateway
//    @ArchTest
//    public static final ArchRule dsl_core_should_not_depend_on_services_or_gateway =
//            noClasses()
//                .that().resideInAPackage("com.isolation.dsl..")
//                .should().dependOnClassesThat().resideInAnyPackage(
//                        "com.isolation.servicea..",
//                        "com.isolation.serviceb..",
//                        "com.isolation.servicec..",
//                        "com.isolation.serviced..",
//                        "com.isolation.gateway..")
//                .because("DSL core is a shared library and must not depend on services");
//
//    // 4. Services must follow package naming convention
//    @ArchTest
//    public static final ArchRule services_should_have_correct_package_prefix =
//            classes()
//                .that().resideInAPackage("com.isolation.servicea..")
//                .should().resideInAPackage("..servicea..")
//                .andShould().resideInAPackage("..servicea..")
//                // We can add more specific checks, but this is illustrative
//                .because("Service A classes must be under com.isolation.servicea");
//}