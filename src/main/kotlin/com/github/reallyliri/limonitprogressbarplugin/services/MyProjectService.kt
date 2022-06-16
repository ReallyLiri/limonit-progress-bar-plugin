package com.github.reallyliri.limonitprogressbarplugin.services

import com.intellij.openapi.project.Project
import com.github.reallyliri.limonitprogressbarplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
