#!/bin/bash
# Add this script to user data while creating a launch instance
yum update -y

# Installing and enabling docker services
yum install docker -y
systemctl start docker.service
systemctl enable docker.service

# Login and pull the latest docker image from GitHub
# !!! Replace USERNAME with your GitHub username !!!
# !!! Replace YOUR_GITHUB_ACCESS_TOKEN with your GitHub access token to image registry !!!
echo YOUR_GITHUB_ACCESS_TOKEN | sudo docker login ghcr.io -u USERNAME --password-stdin
# !!! Replace TAG with your image tag !!!
sudo docker pull ghcr.io/Ego1437/liuqu-backend:TAG

# Launch backend instance via docker run
# !!! Replace TAG with your image tag !!!
sudo docker run --name=springboot-liuqu -d --restart=always --add-host host.docker.internal:host-gateway -p 8083:8080 ghcr.io/Ego1437/liuqu-backend:TAG
