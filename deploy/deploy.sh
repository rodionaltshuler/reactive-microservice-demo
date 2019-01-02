#!/usr/bin/env bash
JAR_FILE=demo-0.0.1-SNAPSHOT.jar
SERVER_URL=ec2-52-59-255-103.eu-central-1.compute.amazonaws.com
#Copy jar to instance
scp -i ~/.aws/ec2/instance1.pem ../build/libs/$JAR_FILE ubuntu@$SERVER_URL:~/
#Run server
ssh -i ~/.aws/ec2/instance1.pem ubuntu@$SERVER_URL 'PORT=8080 java -jar $JAR_FILE'
