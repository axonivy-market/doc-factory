pipeline {
  triggers {
    pollSCM 'H/15 * * * *'
    cron '@midnight'
  }
  agent {
    dockerfile true
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
          archiveArtifacts '**/target/*.iar, **/IvyAddOnsGuide/target/*.zip'
        }
      }
    }
  }
}

