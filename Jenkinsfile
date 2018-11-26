pipeline {
  triggers {
    pollSCM 'H/15 * * * *'
    cron '@midnight'
  }
  agent {
    dockerfile true
  }
  options {
    buildDiscarder(logRotator(artifactNumToKeepStr: '5'))
  }
  stages {
    stage('build') {
      steps {
        script {
          if (env.BRANCH_NAME == 'master'){
            script { maven cmd: '-s settings.xml deploy -U' }
          } else {
            script { maven cmd: '-s settings.xml verify -U' }
          }
          junit '**/target/surefire-reports/**/*.xml' 
          archiveArtifacts '**/target/*.iar, **/IvyAddOnsGuide/target/*.zip'
        }
      }
    }
  }
}

