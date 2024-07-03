# orca_robot

## Clone the project

git clone https://github.com/Curiosity-AI-team/orca_robot.git --recursive

rosdep install --from-paths src -y --ignore-src

sudo apt-get install python3-rosdep

sudo apt-get install ros-foxy-ament-cmake


git submodule add https://github.com/introlab/rtabmap.git localization/rtabmap
git submodule add https://github.com/introlab/rtabmap_ros localization/rtabmap_ros

git submodule add https://github.com/ros-drivers/velodyne colcon_ws/src/OrcaRL2/localization/velodyne


$ git config --global user.name "John Doe" $ git config --global user.email hejhe@gmail.com