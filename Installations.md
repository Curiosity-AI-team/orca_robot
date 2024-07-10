```bash
git clone --recursive https://github.com/Curiosity-AI-team/orca_robot.git
./ros2-foxy-desktop-main.sh 
source /opt/ros/foxy/setup.bash
sudo apt install python3-colcon-common-extensions
git clone https://github.com/introlab/rtabmap.git rtabmap
git clone --branch ros2 https://github.com/introlab/rtabmap_ros.git rtabmap_ros
rosdep update
rosdep install --from-paths src --ignore-src -r -y
export MAKEFLAGS="-j1"

sudo apt-get install gparted -y
sudo apt-get install ros-foxy-gazebo-ros-pkgs ros-foxy-gazebo-ros2-control -y
sudo apt-get install ros-foxy-turtle-tf2-py ros-foxy-tf2-tools ros-foxy-tf-transformations -y
sudo apt-get install ros-foxy-vision* -y
sudo apt install ros-foxy-navigation2 ros-foxy-octomap-* ros-foxy-ros-testing ros-foxy-nav2-bringup ros-foxy-realsense2-camera ros-foxy-imu-filter-madgwick ros-foxy-libpointmatcher ros-foxy-ament-python ros-foxy-gtsam  -y
sudo apt install ros-foxy-octomap-* -y
sudo apt install ros-foxy-ros-testing -y
sudo apt install ros-foxy-nav2-bringup -y
sudo apt install ros-foxy-realsense2-camera -y
sudo apt install ros-foxy-nav2-bringup -y
sudo apt install ros-foxy-imu-filter-madgwick -y
sudo apt install ros-foxy-libpointmatcher -y
sudo apt install ros-foxy-ament-python -y
sudo apt install ros-foxy-libpointmatcher -y
sudo apt install ros-foxy-gtsam -y

colcon build --symlink-install --cmake-args -DCMAKE_BUILD_TYPE=Release
source install/setup.bash 
sudo apt install nvidia-cuda-toolkit
pip3 install torch==1.10.0+cu113 torchvision==0.11.1+cu113 torchaudio==0.10.0+cu113 -f https://download.pytorch.org/whl/cu113/torch_stable.html
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/cuda/lib64


```