pipeline {
  agent {
    label 'docker'
  }

  triggers {
    pollSCM 'H/5 * * * *'
    cron '0 20 * * *'
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '5'))
  }

  stages {
    stage('build') {
      steps {
        script {
          def currentVersion = 'dev';

          echo 'build projects'
          docker.build('maven-build', '-f Dockerfile .').inside {
            def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
            maven cmd: "clean ${phase} -Dmaven.test.failure.ignore=true"
            currentVersion = getCurrentVersion();
          }
          archiveArtifacts '**/target/*.iar'
          junit '**/target/surefire-reports/**/*.xml'

          echo 'build doc'
          docker.image('axonivy/build-container:read-the-docs-1.2').inside {
            sh "make -C /doc-build html BASEDIR='${env.WORKSPACE}/doc-factory/doc' VERSION='${currentVersion}'"
          }
          archiveArtifacts 'doc-factory/doc/build/html/**/*'
          recordIssues tools: [eclipse(), sphinxBuild()], unstableTotalAll: 1

          echo 'deploy doc'
          docker.build('maven-build', '-f Dockerfile .').inside {            
            def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
            maven cmd: "-f doc-factory/doc/pom.xml clean ${phase}"
          }
          archiveArtifacts 'doc-factory/doc/target/*.zip'
        }
      }
    }
  }
}

def getCurrentVersion() {
  def cmd = "mvn -f maven/pom.xml  help:evaluate -Dexpression=revision -q -DforceStdout"
	def value = sh (script: cmd, returnStdout: true)
	echo "version is $value"
	if (value == "null object or invalid expression") {
	  throw new Exception("could not evaluate maven revision property");
	}
	return value
}
