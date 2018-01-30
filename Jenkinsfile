@Library('ivy-jenkins-shared-libraries') _

pipeline {
  agent {
    dockerfile true
  }
  stages {
    stage('build') {
      steps {
        script {
          maven cmd: 'clean install'
        }
      }
    }
  }
}
