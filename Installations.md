```bash
git clone --recursive https://github.com/Curiosity-AI-team/orca_robot.git
./ros2-humble-desktop-main.sh 
source /opt/ros/humble/setup.bash
sudo apt install python3-colcon-common-extensions
git clone https://github.com/introlab/rtabmap.git rtabmap
git clone --branch ros2 https://github.com/introlab/rtabmap_ros.git rtabmap_ros
rosdep update
rosdep install --from-paths src --ignore-src -r -y
export MAKEFLAGS="-j1"

sudo apt-get install gparted ros-humble-gazebo-ros-pkgs ros-humble-gazebo-ros2-control ros-humble-turtle-tf2-py ros-humble-tf2-tools ros-humble-tf-transformations  ros-humble-navigation2 ros-humble-ros-testing ros-humble-nav2-bringup ros-humble-realsense2-camera ros-humble-imu-filter-madgwick ros-humble-libpointmatcher ros-humble-gtsam ros-humble-ros-testing ros-humble-nav2-bringup ros-humble-realsense2-camera  ros-humble-nav2-bringup ros-humble-imu-filter-madgwick ros-humble-libpointmatcher ros-humble-libpointmatcher ros-humble-gtsam ros-humble-rmw-cyclonedds-cpp ros-humble-ros2-control ros-humble-ros2-controllers ros-humble-gazebo-ros2-control -y

sudo apt install python3-pip xterm
pip install flask
# pip install socketio # this will produce error!
pip install flask_cors
pip install flask_socketio
pip install fastapi

sudo apt-get install ros-humble-vision* -y
sudo apt-get install ros-humble-octomap-* -y
sudo apt-get install ros-humble-turtlebot3* -y
sudo apt-get install -y ros-humble-rmf*
source ~/orca_robot/colcon_ws/install/setup.bash

colcon build --symlink-install --cmake-args -DCMAKE_BUILD_TYPE=Release
source install/setup.bash 
sudo apt install nvidia-cuda-toolkit
pip3 install torch==1.10.0+cu113 torchvision==0.11.1+cu113 torchaudio==0.10.0+cu113 -f https://download.pytorch.org/whl/cu113/torch_stable.html
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/cuda/lib64
f
ros2 launch orca_navigation turtlebot3_world.launch.py
ros2 launch orca_navigation cartographer.launch.py use_sim_time:=True
ros2 launch orca_navigation navigation2.launch.py use_sim_time:=True

ros2 launch orca_control launch_sim.launch.py

ros2 launch orca_rtabmap orca_rtabmap_slam.launch.py

ros2 launch orca_navigation navigation2.launch.py use_sim_time:=True



```