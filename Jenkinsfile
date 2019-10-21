pipeline {
  triggers {
    pollSCM 'H/5 * * * *'
    cron '0 20 * * *'
  }
  
  agent {
    label 'docker'
  }
  
  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '5'))
  }

  stages {
    stage('build') {
      steps {
        script {
          docker.build('maven-build', '-f Dockerfile build').inside {
            if (env.BRANCH_NAME == 'master'){
              script { maven cmd: '-s settings.xml deploy -U -Dmaven.test.failure.ignore=true' }
            } else {
              script { maven cmd: '-s settings.xml verify -U -Dmaven.test.failure.ignore=true' }
            }
            junit '**/target/surefire-reports/**/*.xml'
            archiveArtifacts '**/target/*.iar, **/IvyAddOnsGuide/target/*.zip'
          }
        }
      }
    }
    stage('doc') {
      steps {
        script {
          docker.image('axonivy/build-container:read-the-docs-1.1').inside {
            sh "make -C /doc-build html BASEDIR='${env.WORKSPACE}/doc'"
          }
          archiveArtifacts 'doc/build/html/**/*'
        }
      }
    }
  }
}
