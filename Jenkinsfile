pipeline {
    agent {
        kubernetes {
            yaml '''
            apiVersion: v1
            kind: Pod
            spec:
              containers:
              - name: maven
                image: maven:latest
                command:
                - "sleep"
                args:
                - "9999999"
              - name: kaniko
                image: 924809052459.dkr.ecr.us-east-1.amazonaws.com/kaniko:latest
                imagePullPolicy: Always
                volumeMounts:
                - name: kaniko-cache
                  mountPath: /kaniko/.cache
                env:
                - name: AWS_REGION
                  valueFrom:
                    secretKeyRef:
                      name: ecr-login
                      key: AWS_REGION
                - name: AWS_ACCESS_KEY_ID
                  valueFrom:
                    secretKeyRef:
                      name: ecr-login
                      key: AWS_ACCESS_KEY_ID
                - name: AWS_SECRET_ACCESS_KEY
                  valueFrom:
                    secretKeyRef:
                      name: ecr-login
                      key: AWS_SECRET_ACCESS_KEY
                command:
                - sleep
                args:
                - '9999999'
                tty: true
              volumes:
              - name: kaniko-cache
                emptyDir: {}
        '''
        }
    }

    environment{
      NAMESPACE = 'budget-services'
      SERVICE_NAME = 'budget-services'
    }

    stages {
      // Kaniko container doesn't have git installed, so we need to clone using the maven container
      stage('Pull Dependencies'){
        steps{
          container('maven'){
            sh 'git clone https://github.com/My-Budget-Buddy/Budget-Buddy-Kubernetes.git'
          }
        }
      }

      // Deploy the database to the staging environment
      stage('Deploy Postgres') {
        when {
            branch 'testing-cohort'
        }
        steps {
          container('kaniko') {
            script {
              sh 'aws eks --region us-east-1 update-kubeconfig --name project3-eks'
              sh 'kubectl config current-context'
              withCredentials([
                string(credentialsId: 'STAGING_DATABASE_USER', variable: 'DATABASE_USERNAME'),
                string(credentialsId: 'STAGING_DATABASE_PASSWORD', variable: 'DATABASE_PASSWORD')])
              {
                directory('Budget-Buddy-Kubernetes/Databases'){
                  sh '''
                    chmod +x ./deploy-database.sh
                    bash ./deploy-database.sh $NAMESPACE $SERVICE_NAME $DATABASE_USERNAME $DATABASE_PASSWORD
                  '''
                }
              }
            }
          }
        }
      }

      // Push the docker image to ECR
      stage('Build and Push Docker Image for Staging') {
        when {
          branch 'testing-cohort'
        }

        steps {
          container('kaniko') {
            script {
              sh '''
                rm -rf /var/lock
                # Get the ECR login password
                export ECR_LOGIN=$(aws ecr get-login-password --region $AWS_REGION)
                if [ -z "$ECR_LOGIN" ]; then
                  echo "Failed to get ECR login password"
                  exit 1
                fi
                mkdir -p /kaniko/.docker
                echo "{\"auths\":{\"924809052459.dkr.ecr.us-east-1.amazonaws.com\":{\"auth\":\"$(echo -n AWS:$ECR_LOGIN | base64)\"}}}" > /kaniko/.docker/config.json
                /kaniko/executor --dockerfile=Dockerfile.prod --context=dir://. --destination=924809052459.dkr.ecr.us-east-1.amazonaws.com/budget-services:latest
              '''
            }
          }
        }
      }

        // stage('Build for Staging') {
        //     when {
        //         branch 'testing-cohort'
        //     }

        //     steps {
        //         container('maven') {
        //             sh 'mvn clean install -DskipTests=true -Dspring.profiles.active=build'
        //         }
        //     }
        // }

        // stage('Test and Analyze for Staging') {
        //     when {
        //         branch 'testing-cohort'
        //     }

        //     steps {
        //         container('maven') {
        //             sh 'mvn clean verify -Pcoverage -Dspring.profiles.active=test'
        //             withSonarQubeEnv('SonarCloud') {
        //                 sh '''
        //       mvn sonar:sonar \
        //           -Dsonar.projectKey=My-Budget-Buddy_Budget-Buddy-BudgetService \
        //           -Dsonar.projectName=Budget-Buddy-BudgetService \
        //           -Dsonar.java.binaries=target/classes \
        //           -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
        //       '''
        //             }
        //         }
        //     }
        // }

        
    }

    post {
        always {
            cleanWs()
        }
    }
}
