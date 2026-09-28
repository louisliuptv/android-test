/**
 * Precompiled [emptyapp.android.hilt.gradle.kts][Emptyapp_android_hilt_gradle] script plugin.
 *
 * @see Emptyapp_android_hilt_gradle
 */
public
class Emptyapp_android_hiltPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Emptyapp_android_hilt_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
