# orca_robot

## Clone the project

git clone https://github.com/Curiosity-AI-team/orca_robot.git --recursive

rosdep install --from-paths src -y --ignore-src

sudo apt-get install python3-rosdep

sudo apt-get install ros-foxy-ament-cmake

colcon build --packages-select orca_navigation

git submodule add https://github.com/introlab/rtabmap.git localization/rtabmap
git submodule add https://github.com/introlab/rtabmap_ros localization/rtabmap_ros

git submodule add https://github.com/ros-drivers/velodyne colcon_ws/src/OrcaRL2/localization/velodyne


$ git config --global user.name "John Doe" $ git config --global user.email hejhe@gmail.com

Set your username: git config --global user.name "FIRST_NAME LAST_NAME"
Set your email address: git config --global user.email "MY_NAME@example.com"

killall -9 gzserver
killall -9 gzclient


ros2 pkg create --build-type ament_cmake --node-name orca_rmf orca_rmf --dependencies std_msgs
