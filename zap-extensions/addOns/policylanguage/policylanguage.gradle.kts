

version = "1"
description = "Load policies and rules from Domain Specific Language (DSL) specified in txt file."

zapAddOn {
    addOnName.set("Policy language")
    zapVersion.set("2.9.0")

    manifest {
        author.set("Group 19")
    }
}

dependencies {
    testImplementation(project(":testutils"))
}
