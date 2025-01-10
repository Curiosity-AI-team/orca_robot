```bash
cd ~/Orca_Robot/colcon_ws/src/OrcaRL2
git submodule update --init --recursive
git submodule update --init --recursive --force
cd ~/Orca_Robot/colcon_ws/
```

Add required submodules:
```bash
git submodule add https://github.com/pal-robotics/aruco_ros.git localization/aruco_ros
git submodule add https://github.com/open-rmf/rmf_demos.git simulation/rmf_demos
git submodule add https://github.com/ros-drivers/velodyne colcon_ws/src/OrcaRL2/localization/velodyne
```