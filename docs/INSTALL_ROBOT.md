# Robot Installation Guide

This guide provides instructions for installing and configuring the GR Platform software (or a similar ROS2-based system) on a **headless robot** running Ubuntu 22.04. It includes setup steps for ROS2 Humble, networking, autostart services, and more.

---

## Table of Contents

1. [Prerequisites](#1-prerequisites)
2. [Initial System Setup](#2-initial-system-setup)
3. [Install ROS2 Humble](#3-install-ros2-humble)
4. [Clone and Build the Project](#4-clone-and-build-the-project)
5. [Headless Configuration](#5-headless-configuration)
6. [Network Configuration](#6-network-configuration)
7. [Remote Management](#7-remote-management)
8. [Troubleshooting](#8-troubleshooting)

---

## 1. Prerequisites

- **Hardware**:
  - Jetson Orin Nano or Jetson AGX Orin (8GB or higher recommended)
  - Alternatively Raspberry Pi 5 (8GB or higher)
  - At least 32GB storage

- **Software**:
  - **Ubuntu 22.04** (64-bit)
  - SSH server (for headless operation)
  - **ROS2 Humble** or later

---

## 2. Initial System Setup

1. **Install Ubuntu 22.04** on your robot computer (Jetson, Raspberry Pi, etc.).
2. **Enable SSH** (if not done during installation):
   ```bash
   sudo apt update
   sudo apt install -y openssh-server
   sudo systemctl enable ssh
   sudo systemctl start ssh
   ```
3. **Update Packages**:
   ```bash
   sudo apt update
   sudo apt upgrade -y
   ```

---

## 3. Install ROS2 Humble

1. **Set up sources and keys**:
   ```bash
   sudo apt update && sudo apt install -y curl gnupg2 lsb-release
   sudo curl -sSL https://raw.githubusercontent.com/ros/rosdistro/master/ros.key | sudo apt-key add -
   sudo sh -c 'echo "deb http://packages.ros.org/ros2/ubuntu $(lsb_release -cs) main" > /etc/apt/sources.list.d/ros2-latest.list'
   ```

2. **Install ROS2**:
   ```bash
   sudo apt update
   sudo apt install -y ros-humble-desktop
   ```

3. **Source ROS2**:
   ```bash
   echo "source /opt/ros/humble/setup.bash" >> ~/.bashrc
   source ~/.bashrc
   ```

4. **ROS2 Dependencies**:
   ```bash
   sudo apt install -y python3-rosdep python3-colcon-common-extensions
   sudo rosdep init
   rosdep update
   ```

---

## 4. Clone and Build the Project

1. **Clone repository**:
   ```bash
   mkdir -p ~/gr_platform2/colcon_ws/src
   cd ~/gr_platform2/colcon_ws/src
   git clone https://github.com/Curiosity-AI-team/OrcaRL2.git --recursive
   ```

2. **Install Dependencies**:
   ```bash
   cd ~/gr_platform2/colcon_ws
   rosdep install --from-paths src --ignore-src -r -y
   ```

3. **Build the Workspace**:
   ```bash
   export MAKEFLAGS="-j2"  # Adjust if you have more cores
   colcon build --symlink-install --cmake-args -DCMAKE_BUILD_TYPE=Release
   ```

4. **Add Workspace to Bashrc**:
   ```bash
   echo "source ~/gr_platform2/colcon_ws/install/setup.bash" >> ~/.bashrc
   source ~/.bashrc
   ```

---

## 5. Headless Configuration

To run your robot software automatically at startup, create a **systemd service**:

1. **Create Service File**:
   ```bash
   sudo nano /etc/systemd/system/gr_platform.service
   ```
2. **Add Service Configuration**:
   ```ini
   [Unit]
   Description=GR Platform Robot Service
   After=network.target

   [Service]
   Type=simple
   User=robotuser
   ExecStart=/bin/bash -c 'source /opt/ros/humble/setup.bash && source ~/gr_platform2/colcon_ws/install/setup.bash && ros2 launch operation desktop_operation.launch.py'
   Restart=on-failure

   [Install]
   WantedBy=multi-user.target
   ```
   Replace `robotuser` with the correct username.

3. **Enable and Start**:
   ```bash
   sudo systemctl daemon-reload
   sudo systemctl enable gr_platform.service
   sudo systemctl start gr_platform.service
   ```

4. **Check Service Status**:
   ```bash
   sudo systemctl status gr_platform.service
   ```

---

## 6. Network Configuration

Ensure the robot is on the same network or configured with a static IP if needed. You can manage network settings via `nmtui` or `nmcli`. SSH into the robot using:
```bash
ssh robotuser@<robot-ip>
```

---

## 7. Remote Management

- **SSH Keys**: For secure, password-less login, follow the steps in [SSH Connection Setup](HardwareHelper.md#3-ssh-connection-setup).
- **Monitoring**: Use tools like `top`, `htop`, or `jtop` (for Jetson) to monitor system performance.

---

## 8. Troubleshooting

- **View service logs**:
  ```bash
  sudo journalctl -u gr_platform.service -f
  ```
- **Check active ROS2 nodes**:
  ```bash
  ros2 node list
  ```
- **Rebuild workspace** (if you made changes):
  ```bash
  cd ~/gr_platform2/colcon_ws
  colcon build --symlink-install
  sudo systemctl restart gr_platform.service
  ```

---

Go back to the [README](README.md).