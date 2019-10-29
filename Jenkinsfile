pipeline {
  agent {
    label 'docker'
  }

  triggers {
    pollSCM 'H/5 * * * *'
    cron '0 20 * * *'
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '5'))
  }

  stages {
    stage('build') {
      steps {
        script {
          docker.image('axonivy/build-container:read-the-docs-1.2').inside {
            sh "make -C /doc-build html BASEDIR='${env.WORKSPACE}/doc'"
          }
          archiveArtifacts 'doc/build/html/**/*'
          recordIssues tools: [sphinxBuild()], unstableTotalAll: 1

          docker.build('maven-build', '-f Dockerfile .').inside {
            def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
            maven cmd: "clean ${phase} -Dmaven.test.failure.ignore=true"

            dir ('doc') {
              maven cmd: "clean ${phase}"
            }
          }
          archiveArtifacts '**/target/*.iar'
          junit '**/target/surefire-reports/**/*.xml'
        }
      }
    }
  }
}
