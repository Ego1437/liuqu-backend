#!/bin/bash
# Add this script to user data while creating a launch instance
yum update -y

# Installing and enabling docker services
yum install docker -y
systemctl start docker.service
systemctl enable docker.service

sudo yum -y install docker
sudo systemctl start docker.service
sudo systemctl enable docker.service

# !!! REPLACE THIS WITH YOUR ECR USER CREDENTIALS !!!
export AWS_ACCESS_KEY_ID='YOUR_AWS_ACCESS_KEY'
export AWS_SECRET_ACCESS_KEY='YOUR_AWS_ACCESS_SECRET'
export AWS_DEFAULT_REGION=ap-southeast-2

# Configure AWS user for ECR
aws configure set aws_access_key_id "$AWS_ACCESS_KEY_ID" && aws configure set aws_secret_access_key "$AWS_SECRET_ACCESS_KEY" && aws configure set region "$AWS_DEFAULT_REGION"
# Login to our private ECR
aws ecr get-login-password --region ap-southeast-2 | sudo docker login --username AWS --password-stdin 992382689127.dkr.ecr.ap-southeast-2.amazonaws.com

# Launch backend instance via docker run
sudo docker run --name=springboot-liuqu -d --restart=always --add-host host.docker.internal:host-gateway -p 8083:8080 ghcr.io/Ego1437/liuqu-backend:latest
