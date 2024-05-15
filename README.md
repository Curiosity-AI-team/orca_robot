# orca_robot

## Clone the project

git clone https://github.com/Curiosity-AI-team/orca_robot.git --recursive

## Install OpenCV with CUDA:

Check the JETSON with jtop

The default shell script parameter is:

opencv: 4.8.0
cuda 10.2
cuDNN: 8.2
Jetpack 4.6

```shell
cd ~/orca_robot/nano_build_opencv
./build_opencv.sh
```
Specifying an OpenCV version (git branch)
```shell
./build_opencv.sh 4.8.0
```


## Install ROS

```bash
sudo apt update
```
```bash
sudo apt install git python3-pip python3-schedule -y
```
```bash
cd ~/orca_robot/ROS1-installation/
chmod +x ROS1-installation/ROS.sh
sudo ./ROS1-installation/ROS.sh
```
```bash
echo "source /opt/ros/noetic/setup.bash" >> ~/.bashrc
source ~/.bashrc
source /opt/ros/noetic/setup.bash
sudo apt-get install build-essential python3-rosdep libpcl1 -y
```

## Install the Project

```bash
cd ~/orca_robot/catkin_ws/
source /opt/ros/noetic/setup.bash
sudo rosdep init
```
```bash
rosdep update
rosdep install --from-paths src --ignore-src -y
```

```bash
catkin_make -j3
```