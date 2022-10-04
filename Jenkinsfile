pipeline {
  agent any

  triggers {
    cron '@midnight'
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '2'))
  }

  parameters {
    string(
       name: 'engineListUrl',
       description: 'Engine to use for build',
       defaultValue: 'https://jenkins.ivyteam.io/job/core_product/job/master/lastSuccessfulBuild/'
    )
  }

  stages {
    stage('build') {
      steps {
        script {
          docker.image('axonivy/build-container:read-the-docs-2').inside {
            sh "make -C /doc-build html BASEDIR='${env.WORKSPACE}/doc-factory-doc'"
          }
          archiveArtifacts 'doc-factory-doc/build/html/**/*'

          def random = (new Random()).nextInt(10000000)
          def networkName = "build-" + random
          def seleniumName = "selenium-" + random
          def ivyName = "ivy-" + random
          sh "docker network create ${networkName}"
          try {
            docker.image("selenium/standalone-firefox:4").withRun("-e START_XVFB=false --shm-size=2g --name ${seleniumName} --network ${networkName}") {
              docker.build('maven', ".").inside("--name ${ivyName} --network ${networkName}") {
                def phase = env.BRANCH_NAME == 'master' ? 'deploy -DaltDeploymentRepository=nexus.axonivy.com::https://nexus.axonivy.com/repository/maven-releases/ -DaltSnapshotDeploymentRepository=nexus.axonivy.com::https://nexus.axonivy.com/repository/maven-snapshots/' : 'verify'
                maven cmd: "clean ${phase} -Divy.engine.version=[9.2.0,] -Dmaven.test.failure.ignore=true -Divy.compiler.warnings=false -Divy.engine.list.url=${params.engineListUrl} -Dtest.engine.url=http://${ivyName}:8080 -Dselenide.remote=http://${seleniumName}:4444/wd/hub"
              }
            }
          } finally {
            sh "docker network rm ${networkName}"
          }
          archiveArtifacts '**/target/*.iar'
          archiveArtifacts artifacts: '**/target/selenide/reports/**/*', allowEmptyArchive: true
          archiveArtifacts 'doc-factory-doc/target/*.zip'
          junit '**/target/*-reports/**/*.xml'
          recordIssues tools: [eclipse(), sphinxBuild()], unstableTotalAll: 1
        }
      }
    }
  }
}
