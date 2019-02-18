pipeline {
  triggers {
    pollSCM 'H/5 * * * *'
    cron '0 20 * * *'
  }
  agent {
    dockerfile true
  }
  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '5'))
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

