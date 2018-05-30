pipeline {
  triggers {
    cron '@midnight'
  }
  agent {
    docker {
      image 'maven:3.5.2-jdk-8'
    }
  }
  stages {
    stage('build') {
      when {
        branch 'master'
      }
      steps {
        script {
          maven cmd: '-s settings.xml clean deploy'
        }
      }
    }
  }
}
