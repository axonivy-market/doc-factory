pipeline {
  triggers {
    pollSCM 'H/15 * * * *'
    cron '@midnight'
  }
  agent {
    docker {
      image 'maven:3.5.2-jdk-8'
    }
  }
  stages {
    stage('build') {
      steps {
        script {
          if (env.BRANCH_NAME == 'master' || 
              env.BRANCH_NAME == '7.0' || 
              env.BRANCH_NAME == '6.0') 
          {
            script { maven cmd: '-s settings.xml deploy -U' }
          } else {
            script { maven cmd: '-s settings.xml verify -U' }
          }
        }
      }
    }
  }
}

