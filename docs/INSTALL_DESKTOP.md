# Desktop Installation Guide

This document provides step-by-step instructions for setting up a **desktop** environment suitable for developing, simulating, and testing the GR Platform or a similar ROS2-based project.

---

## Table of Contents

1. [Prerequisites](#1-prerequisites)
2. [Clone the Repository](#2-clone-the-repository)
3. [Install ROS Dependencies](#3-install-ros-dependencies)
4. [Build the Project Workspace](#4-build-the-project-workspace)
5. [Build Individual Packages (Optional)](#5-build-individual-packages-optional)
6. [Creating a New ROS2 Package (Optional)](#6-creating-a-new-ros2-package-optional)
7. [DDS Configuration (Optional)](#7-dds-configuration-optional)

---

## 1. Prerequisites

- **Operating System**: Ubuntu 22.04 LTS
- **ROS2 Distribution**: Humble Hawksbill
- **Python version**: 3.10 or later recommended

Ensure you have already installed:
```bash
sudo apt-get update
sudo apt-get install curl git python3-colcon-common-extensions
```
and have **ROS2 Humble** installed and sourced in your shell:
```bash
source /opt/ros/humble/setup.bash
```

---

## 2. Clone the Repository

1. Navigate to a workspace folder (e.g., `~/gr_platform2/colcon_ws/src`).
2. Clone the repository (submodules included):
   ```bash
   cd ~/gr_platform2/colcon_ws/src
   git clone https://github.com/Curiosity-AI-team/OrcaRL2.git --recursive
   ```

*(Adjust the repository URL to match your actual project if it differs.)*

---

## 3. Install ROS Dependencies

Install the necessary ROS2 packages:

```bash
sudo apt-get update
sudo apt-get install gparted \
  ros-humble-gazebo-ros-pkgs \
  ros-humble-gazebo-ros2-control \
  ros-humble-turtle-tf2-py \
  ros-humble-tf2-tools \
  ros-humble-tf-transformations \
  ros-humble-navigation2 \
  ros-humble-ros-testing \
  ros-humble-nav2-bringup \
  ros-humble-realsense2-camera \
  ros-humble-imu-filter-madgwick \
  ros-humble-libpointmatcher \
  ros-humble-gtsam \
  ros-humble-rmw-cyclonedds-cpp \
  ros-humble-ros2-control \
  ros-humble-ros2-controllers \
  ros-humble-gazebo-ros2-control -y

sudo apt-get install ros-humble-vision* -y
sudo apt-get install ros-humble-octomap-* -y
sudo apt-get install ros-humble-turtlebot3* -y
sudo apt-get install -y ros-humble-rmf*
sudo apt-get install -y ros-humble-tf2*
```

Then install any pending dependencies in the workspace:
```bash
cd ~/gr_platform2/colcon_ws
rosdep update
rosdep install --from-paths src --ignore-src -r -y
```

---

## 4. Build the Project Workspace

From the workspace root:
```bash
cd ~/gr_platform2/colcon_ws
export MAKEFLAGS="-j2"  # Adjust for your CPU core count
colcon build --symlink-install --cmake-args -DCMAKE_BUILD_TYPE=Release
```

---

## 5. Build Individual Packages (Optional)

If you only want to build a specific package (e.g., `orca_navigation`):
```bash
colcon build --packages-select orca_navigation
```

---

## 6. Creating a New ROS2 Package (Optional)

To create a new ROS2 package with `ament_cmake`:

```bash
ros2 pkg create --build-type ament_cmake --node-name <node_name> <package_name> --dependencies std_msgs
```

---

## 7. DDS Configuration (Optional)

If you require custom DDS settings (e.g., CycloneDDS), you can place a file like `cyclonedds.xml` in your workspace:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<CycloneDDS xmlns="https://cdds.io/config" ...>
  <Domain id="any">
    <General>
      <NetworkInterfaceAddress>ham0</NetworkInterfaceAddress>
      <AllowMulticast>false</AllowMulticast>
      <MaxMessageSize>65500B</MaxMessageSize>
      <FragmentSize>4000B</FragmentSize>
      <Transport>udp6</Transport>
    </General>
    <Discovery>
      <Peers>
        <Peer address="2620:9b::1921:60c"/>
        <Peer address="2620:9b::1912:a92"/>
      </Peers>
      <ParticipantIndex>auto</ParticipantIndex>
    </Discovery>
    <Tracing>
      <Verbosity>severe</Verbosity>
      <OutputFile>stdout</OutputFile>
    </Tracing>
  </Domain>
</CycloneDDS>
```

Set the environment variable so ROS2 uses this file:
```bash
export CYCLONEDDS_URI=file:///path/to/cyclonedds.xml
```

---

Go back to the [README](README.md).