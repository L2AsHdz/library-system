pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Iniciando la fase de build...'
                sh './mvnw clean package'
                echo 'Build completado.'
            }
        }
        stage('Test') {
            steps {
                echo 'Iniciando la fase de test...'
                sh './mvnw test'
                echo 'Test completado.'
            }
        }
        stage('Package') {
            steps {
                echo 'Iniciando la fase de empaquetado...'
                sh './mvnw package'
                echo 'Empaquetado completado.'
            }
        }
    }
    post {
        success {
            echo 'Pipeline completado con éxito.'
        }
        failure {
            echo 'Pipeline falló.'
        }
    }
}
