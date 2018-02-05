pipeline {
  triggers {
    pollSCM '@hourly'
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
          maven cmd: '-s settings.xml clean deploy'
        }
      }
    }
  }
}
