
import ProjectVersions.openosrsVersion
version = "1.0.4"

project.extra["PluginName"] = "Free Utils"
project.extra["PluginDescription"] = "Tools required for FREE Plugins to function."


tasks {
    jar {
        manifest {
            attributes(mapOf(
                    "Plugin-Version" to project.version,
                    "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                    "Plugin-Provider" to project.extra["PluginProvider"],
                    "Plugin-Description" to project.extra["PluginDescription"],
                    "Plugin-License" to project.extra["PluginLicense"]
            ))
        }
    }
}