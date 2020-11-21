

version = "1"
description = "TODO"

zapAddOn {
    addOnName.set("File Tester")
    zapVersion.set("2.9.0")

    manifest {
        author.set("Group 19")
    }
}

dependencies {
    testImplementation(project(":testutils"))
}
