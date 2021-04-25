import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

open class PancakeInferLicenseTask : DefaultTask() {

	@Input
	var sourceFile = File(project.rootDir, "LICENSE")

	@Input
	var targetFile = File(project.buildDir, "resources/main/net/tassia/pancake/LICENSE")



	@TaskAction
	fun inferLicense() {
		require(sourceFile.exists())
		targetFile.parentFile.mkdirs()
		Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
	}

}