##!/bin/bash
#echo "Stopping existing backend service..."
#sudo pkill -f 'java -jar'
#!/bin/bash
#!/bin/bash
#!/bin/bash
echo "Stopping and removing existing backend container..."
CONTAINER_NAME="rushr-backend"

# Stop running container if exists
if [ "$(sudo docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Stopping running container..."
    sudo docker stop $CONTAINER_NAME
fi

# Remove the old container
if [ "$(sudo docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    echo "Removing old container..."
    sudo docker rm $CONTAINER_NAME
fi
