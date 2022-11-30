package com.attest.ict;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.attest.ict");

        noClasses()
            .that()
            .resideInAnyPackage("com.attest.ict.service..")
            .or()
            .resideInAnyPackage("com.attest.ict.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.attest.ict.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
