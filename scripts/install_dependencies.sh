##!/bin/bash
#echo "Installing dependencies..."
#sudo apt update -y
#sudo apt install -y openjdk-17-jdk
#!/bin/bash

echo "Installing dependencies..."

sudo apt update -y

sudo apt install -y docker.io

sudo systemctl start docker

sudo systemctl enable docker