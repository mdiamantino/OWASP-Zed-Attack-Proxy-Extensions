

version = "1"
description = "Load a JAR file containing the programmatic implementation of a set of policy rules."

zapAddOn {
    addOnName.set("Policy verifier")
    zapVersion.set("2.9.0")

    manifest {
        author.set("Group 19")
    }
}

dependencies {
    testImplementation(project(":testutils"))
}
