pipeline {
  agent {
    dockerfile true
  }
  stages {
    stage('build') {
      steps {
        script {
          maven cmd: '-s settings.xml clean install'
        }
      }
    }
  }
}
