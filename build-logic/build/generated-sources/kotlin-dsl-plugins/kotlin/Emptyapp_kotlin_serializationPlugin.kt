/**
 * Precompiled [emptyapp.kotlin.serialization.gradle.kts][Emptyapp_kotlin_serialization_gradle] script plugin.
 *
 * @see Emptyapp_kotlin_serialization_gradle
 */
public
class Emptyapp_kotlin_serializationPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Emptyapp_kotlin_serialization_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
