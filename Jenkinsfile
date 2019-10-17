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
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
          script {
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
    stage('docu') {
      steps {
        script {
          docker.image('axonivy/build-container:read-the-docs-1.1').inside {
            // SPHINXOPTS='-t enableAllExtensions'
            // https://github.com/sphinx-contrib/sphinx-pretty-searchresults/issues/29
            sh "make -C /doc-build html BASEDIR='${env.WORKSPACE}/doc'"
          }
          archiveArtifacts 'doc/build/html/**/*'
        }
      }
    }
  }
}

