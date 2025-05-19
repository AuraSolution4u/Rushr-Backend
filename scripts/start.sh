##!/bin/bash
#echo "Starting backend service..."
#cd /opt/rushr-backend
#nohup java -jar target/*.jar > app.log 2>&1 &
#!/bin/bash
#!/bin/bash
echo "Starting new backend container..."
CONTAINER_NAME="rushr-backend"
IMAGE_NAME="rushr-backend:latest"

cd /opt/rushr-backend

# Build Docker image
sudo docker build -t $IMAGE_NAME .

# Run new container
sudo docker run -d --name $CONTAINER_NAME -p 8080:8080 $IMAGE_NAME