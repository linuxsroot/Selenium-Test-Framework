pipeline {
	agent any

	tools {
		maven 'maven-3.9.9'
	}

	environment {
		COMPOSE_PATH    = "${WORKSPACE}\\docker"          // where docker-compose.yml exists
		SELENIUM_GRID   = "true"
		GRID_STATUS_URL = "http://localhost:4444/status"  // Jenkins & Docker on same machine
	}

	stages {

		stage('Checkout') {
			steps {
				cleanWs()
				git branch: 'uiAutomation', url: 'https://github.com/linuxsroot/Selenium-Test-Framework.git'
			}
		}

		stage('Start Selenium Grid via Docker Compose') {
			steps {
				script {
					echo "Starting Selenium Grid with Docker Compose..."
					bat "docker compose -f \"${env.COMPOSE_PATH}\\docker-compose.yml\" up -d"

					echo "Waiting for Selenium Grid to be ready..."
					bat """
                    powershell -NoProfile -Command ^
                      "$u='${env.GRID_STATUS_URL}'; ^
                       $ok=$false; ^
                       for($i=0;$i -lt 30;$i++){ ^
                         try{ ^
                           $r=Invoke-WebRequest -UseBasicParsing -Uri $u -TimeoutSec 2; ^
                           if($r.StatusCode -eq 200){ $ok=$true; break } ^
                         } catch {} ^
                         Start-Sleep -Seconds 2; ^
                       } ^
                       if(-not $ok){ throw 'Selenium Grid not ready at ' + $u }"
                    """
				}
			}
		}

		stage('Build + Test') {
			steps {
				bat 'mvn clean test -DseleniumGrid=true'
			}
		}

		stage('Reports') {
			steps {
				publishHTML(target: [
					reportDir: 'src/test/resources/ExtentReport',
					reportFiles: 'SparkReport.html',
					reportName: 'Extent Report',
					keepAll: true,
					alwaysLinkToLastBuild: true,
					allowMissing: true
				])
			}
		}
	}

	post {
		always {
			echo "Stopping Selenium Grid (always)..."
			bat "docker compose -f \"${env.COMPOSE_PATH}\\docker-compose.yml\" down --remove-orphans"

			archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html', fingerprint: true
			junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
		}

		success {
			emailext(
				to: 'pinkukumar.127.0.0.1@gmail.com',
				subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
				mimeType: 'text/html',
				attachLog: true,
				body: """
                <html><body>
                <p>Hello Team,</p>
                <p>The latest Jenkins build has completed successfully.</p>
                <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                <p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS</b></span></p>
                <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                <p><b>Extent Report:</b> <a href="http://localhost:8080/job/${env.JOB_NAME}/HTML_20Extent_20Report/">Click here</a></p>
                <p>Best regards,<br/><b>Automation Team</b></p>
                </body></html>
                """
			)
		}

		failure {
			emailext(
				to: 'pinkukumar.127.0.0.1@gmail.com',
				subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
				mimeType: 'text/html',
				attachLog: true,
				body: """
                <html><body>
                <p>Hello Team,</p>
                <p>The latest Jenkins build has <b style="color: red;">FAILED</b>.</p>
                <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                <p><b>Build Status:</b> <span style="color: red;"><b>FAILED &#10060;</b></span></p>
                <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                <p><b>Extent Report (if available):</b> <a href="http://localhost:8080/job/${env.JOB_NAME}/HTML_20Extent_20Report/">Click here</a></p>
                <p>Best regards,<br/><b>Automation Team</b></p>
                </body></html>
                """
			)
		}
	}
}
