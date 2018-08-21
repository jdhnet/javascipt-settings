import jetbrains.buildServer.configs.kotlin.v2018_1.*
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_1.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_1.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2018.1"

project {
    defaultTemplate = RelativeId("Template")

    vcsRoot(HttpsGithubComG0t4teamcityCourseCardsRefsHeadsMaster)

    buildType(Chrome)
    buildType(id03DeployToStaging)
    buildType(FastTest)

    template(Template_1)
}

object id03DeployToStaging : BuildType({
    id("03DeployToStaging")
    name = "03.DeployToStaging"

    dependencies {
        snapshot(Chrome) {
        }
        snapshot(FastTest) {
        }
    }
})

object Chrome : BuildType({
    templates(Template_1)
    name = "Chrome"
})

object FastTest : BuildType({
    templates(Template_1)
    name = "Fast Tests"

    params {
        param("Browser", "PhantomJS")
    }

    steps {
        script {
            name = "restore npm packages"
            id = "RUNNER_6"
            scriptContent = "npm install"
        }
        script {
            name = "PhantomJS"
            id = "RUNNER_7"
            scriptContent = "npm test -- --single-run --browsers %Browser% --colors false --reporter teamcity"
        }
    }
})

object Template_1 : Template({
    id("Template")
    name = "Template"

    vcs {
        root(HttpsGithubComG0t4teamcityCourseCardsRefsHeadsMaster)
    }

    steps {
        script {
            name = "npm install"
            id = "RUNNER_6"
            scriptContent = "npm install"
        }
        script {
            name = "PhantomJS"
            id = "RUNNER_7"
            scriptContent = "npm test -- --single-run --browsers PhantomJS --colors false --reporter teamcity"
        }
    }

    triggers {
        vcs {
            id = "vcsTrigger"
            branchFilter = ""
            perCheckinTriggering = true
            enableQueueOptimization = false
        }
    }
})

object HttpsGithubComG0t4teamcityCourseCardsRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/g0t4/teamcity-course-cards#refs/heads/master"
    url = "https://github.com/g0t4/teamcity-course-cards"
})
