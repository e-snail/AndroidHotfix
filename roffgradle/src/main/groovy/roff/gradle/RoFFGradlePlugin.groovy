package roff.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class RoFFGradlePlugin implements Plugin<Project> {
  void apply(Project project) {
      project.task('startTask') << {
        println "RoFF Gradle Plugin start........."
      }

      project.task('endTask') << {
        println "RoFF Gradle Plugin end........."
      }
  }
}
