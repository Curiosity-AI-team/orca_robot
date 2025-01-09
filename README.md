# Orca_robot

### Installation

```bash
sudo apt install python3-pip xterm git
git clone --recursive https://github.com/Curiosity-AI-team/orca_robot.git
./orca_robot/ros2_setup_scripts_ubuntu/ros2-humble-desktop-main.sh
source /opt/ros/humble/setup.bash
echo "source ~/orca_robot/colcon_ws/install/setup.bash" >> ~/.bashrc
pip install -r ~/orca_robot/colcon_ws/src/OrcaRL2/requirements.txt
```
Install ros dependencies
```bash
sudo apt-get install gparted ros-humble-gazebo-ros-pkgs ros-humble-gazebo-ros2-control ros-humble-turtle-tf2-py ros-humble-tf2-tools ros-humble-tf-transformations  ros-humble-navigation2 ros-humble-ros-testing ros-humble-nav2-bringup ros-humble-realsense2-camera ros-humble-imu-filter-madgwick ros-humble-libpointmatcher ros-humble-gtsam ros-humble-ros-testing ros-humble-nav2-bringup ros-humble-realsense2-camera  ros-humble-nav2-bringup ros-humble-imu-filter-madgwick ros-humble-libpointmatcher ros-humble-libpointmatcher ros-humble-gtsam ros-humble-rmw-cyclonedds-cpp ros-humble-ros2-control ros-humble-ros2-controllers ros-humble-gazebo-ros2-control -y
sudo apt-get install ros-humble-vision* -y
sudo apt-get install ros-humble-octomap-* -y
sudo apt-get install ros-humble-turtlebot3* -y
sudo apt-get install -y ros-humble-rmf*
sudo apt-get install -y ros-humble-tf2*
```
ros2 launch rmf_demos_gz_classic office.launch.xml
Install orca project
```bash
cd ~/orca_robot/colcon_ws/src/OrcaRL2
git submodule update --init --recursive
git submodule update --init --recursive --force

cd ~/orca_robot/colcon_ws
rosdep update
rosdep install --from-paths src --ignore-src -r -y
export MAKEFLAGS="-j2"
colcon build --symlink-install --cmake-args -DCMAKE_BUILD_TYPE=Release

```

### CUDA installation (optional)

```bash
source install/setup.bash 
sudo apt install nvidia-cuda-toolkit
pip3 install torch==1.10.0+cu113 torchvision==0.11.1+cu113 torchaudio==0.10.0+cu113 -f https://download.pytorch.org/whl/cu113/torch_stable.html
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/cuda/lib64
```