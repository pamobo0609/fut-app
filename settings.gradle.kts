plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "app"
include("src:functionalTest:functionalTest")
findProject(":src:functionalTest:functionalTest")?.name = "functionalTest"
include("fut-app.functionaltest")
include("src:functionalTest:functionalTest")
findProject(":src:functionalTest:functionalTest")?.name = "functionalTest"
