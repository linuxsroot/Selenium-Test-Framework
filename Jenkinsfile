pipeline {
	agent any

	tools {
		maven 'Maven-3.9.12'
	}

	stages {

		stage('Build') {
			steps {
				bat 'mvn clean install'
			}
		}

		stage('Test') {
			steps {
				bat 'mvn test'
			}
		}

		stage('Reports') {
			steps {
				publishHTML(target: [
					reportDir: 'src/test/resources/ExtentReport',
					reportFiles: 'ExtentReport.html',
					reportName: 'Extent Spark Report'
				])
			}
		}
	}

	post {

		always {
			archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html', fingerprint: true
			junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
		}

		success {
			emailext (
				to: 'pinkukumar.127.0.0.1@gmail.com',
				subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
				mimeType: 'text/html',
				attachLog: true,
				body: """<p>Build Successful</p>"""
			)
		}

		failure {
			emailext (
				to: 'pinkukumar.127.0.0.1@gmail.com',
				subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
				mimeType: 'text/html',
				attachLog: true,
				body: """<p>Build Failed</p>"""
			)
		}
	}
}
