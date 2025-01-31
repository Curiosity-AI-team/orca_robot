# Hardware Setup and Configuration Guide

This guide helps you configure essential hardware and interfaces for the GR Platform rover, including network settings, SSH access, CAN, I2C, ODrive drivers, and more.

---

## Table of Contents

1. [Install FFmpeg (Optional)](#1-install-ffmpeg-optional)
2. [Network Setup (Jetson)](#2-network-setup-jetson)
3. [SSH Connection Setup](#3-ssh-connection-setup)
4. [Enable CAN and I2C Interfaces](#4-enable-can-and-i2c-interfaces)
5. [Python Dependencies](#5-python-dependencies)
6. [ODrive Installation](#6-odrive-installation)
7. [Ardupilot Installation](#7-ardupilot-installation)
8. [Firewall Configuration (UFW)](#8-firewall-configuration-ufw)
9. [Launching ROS Packages at Boot](#9-launching-ros-packages-at-boot)
10. [System Checkers and Diagnostics](#10-system-checkers-and-diagnostics)
11. [Install AI JetPack (Optional)](#11-install-ai-jetpack-optional)
12. [Install OpenCV with CUDA (Optional)](#12-install-opencv-with-cuda-optional)
13. [Debug Utilities](#13-debug-utilities)
    - [CAN Interface Debug](#can-interface-debug)
    - [System Logs](#system-logs)
    - [ODrive Check](#odrive-check)
    - [I2C Debug](#i2c-debug)
14. [Catkin Make Examples](#14-catkin-make-examples)
15. [ODrive CAN Command Reference](#15-odrive-can-command-reference)
16. [Additional Network Debug Commands](#16-additional-network-debug-commands)

---

## 1. Install FFmpeg (Optional)

Although not always required, certain packages or video-related utilities may need **ffmpeg** installed:

```bash
sudo apt install ffmpeg
```

---

## 2. Network Setup (Jetson)

### Setting a Static IP with `nmcli`

1. Identify the name of your wired connection (e.g., `"Wired connection 1"`).
2. Assign a static IP address:

   ```bash
   sudo nmcli con mod "Wired connection 1" ipv4.addresses "192.168.1.102/24" ipv4.gateway 192.168.1.1 ipv4.method "manual"
   sudo nmcli con down "Wired connection 1" && sudo nmcli con up "Wired connection 1"
   ```

### Reverting to DHCP (Automatic Configuration)

If you later need to revert to DHCP:

```bash
sudo nmcli connection modify "Wired connection 1" ipv4.method auto
sudo nmcli connection down "Wired connection 1" && sudo nmcli connection up "Wired connection 1"
```

---

## 3. SSH Connection Setup

SSH allows you to remotely log into the rover. Follow these steps:

1. **Generate an SSH key pair** on your local machine:
   ```bash
   ssh-keygen
   ```
2. **Copy your SSH public key** to the Jetson or remote host. Two common methods:

   - **Using `ssh-copy-id`**:
     ```bash
     ssh-copy-id rover@192.168.1.102
     ```
   - **Manually appending the key**:
     ```bash
     scp ~/.ssh/id_rsa.pub rover@192.168.1.102:/tmp/id_rsa.pub
     ssh rover@192.168.1.102 'cat /tmp/id_rsa.pub >> ~/.ssh/authorized_keys'
     ```

3. **Connect without password**:
   ```bash
   ssh rover@192.168.1.102
   ```

If you are on Windows, you can copy the public key using PowerShell:
```powershell
type $env:USERPROFILE\.ssh\id_rsa.pub | ssh rover@192.168.1.102 "cat >> .ssh/authorized_keys"
```

---

## 4. Enable CAN and I2C Interfaces

### 4.1 CAN Interface Installation

1. **Install SocketCAN Tools**:
   ```bash
   sudo apt-get install iproute2 can-utils
   ```
2. **Load CAN kernel modules**:
   ```bash
   sudo modprobe can-raw
   ```
3. **Verify CAN interface**:
   ```bash
   ip link show
   ```

### 4.2 I2C Interface Installation

1. **Install I2C tools**:
   ```bash
   sudo apt-get install i2c-tools
   ```
2. **Load I2C kernel modules**:
   ```bash
   sudo modprobe i2c-dev
   ```
3. **Check I2C devices**:
   ```bash
   i2cdetect -l
   ```

### 4.3 Python Libraries for CAN and I2C

- **Python-CAN**:
  ```bash
  pip install python-can
  ```
- **SMBus2** (for I2C):
  ```bash
  pip install smbus2
  ```

Example Python usage:

```python
# CAN Example
import can
bus = can.interface.Bus(channel='can0', bustype='socketcan')
message = bus.recv()
print(message)

# I2C Example
from smbus2 import SMBus
with SMBus(1) as bus:
    data = bus.read_byte_data(0x20, 0x00)
    print(f"Read data: {data}")
```

### 4.4 Enabling CAN at Boot

1. **Copy network configuration**:
   ```bash
   sudo cp ~/gr_platform/configs_root/etc/systemd/network/80-can.network /etc/systemd/network
   ```
2. **Load CAN modules**:
   ```bash
   sudo cp ./configs_root/etc/modules-load.d/can.conf /etc/modules-load.d/can.conf
   ```
3. **Unblock mttcan**:
   ```bash
   sudo nano /etc/modprobe.d/denylist-mttcan.conf
   # Comment out or remove lines blocking mttcan
   ```
4. **Enable network service**:
   ```bash
   sudo systemctl enable systemd-networkd
   sudo systemctl start systemd-networkd
   sudo systemctl status systemd-networkd
   ```

---

## 5. Python Dependencies

To install all required Python dependencies for your ROS-based rover:

```bash
sudo /usr/bin/python3 -m pip install -r ~/gr_platform/requirements.txt
```

---

## 6. ODrive Installation

If using ODrive motor controllers:

```bash
sudo apt install -y python3-wrapt
sudo apt install i2c-tools
pip3 install odrive
```

Set up UDEV rules:

```bash
sudo bash -c "curl https://cdn.odriverobotics.com/files/odrive-udev-rules.rules > /etc/udev/rules.d/91-odrive.rules && udevadm control --reload-rules && udevadm trigger"
```

ODrive config backups:

```bash
odrivetool backup-config your_file.json
odrivetool restore-config your_file.json
dev0.save_configuration()
```

---

## 7. Ardupilot Installation

### 7.1 MAVROS Installation

```bash
sudo apt-get install ros-humble-mavros*
wget https://raw.githubusercontent.com/mavlink/mavros/master/mavros/scripts/install_geographiclib_datasets.sh
chmod a+x install_geographiclib_datasets.sh
./install_geographiclib_datasets.sh
```

For convenience on desktop:

```bash
sudo apt-get install ros-humble-rqt ros-humble-rqt-common-plugins ros-humble-rqt-robot-plugins
```

### 7.2 Enable Port for Controller

```bash
sudo chmod 777 /dev/ttyACM<you number>
```

### 7.3 Assign Persistent Device Names

To assign a custom name (e.g., `ttyACM4`) to a device with vendor ID `1209:5741`:

```bash
sudo nano /etc/udev/rules.d/99-usb-serial.rules
```

Add:

```bash
SUBSYSTEM=="tty", ATTRS{idVendor}=="1209", ATTRS{idProduct}=="5740", SYMLINK+="ttyACM6"
```

Reload rules:

```bash
sudo udevadm control --reload-rules && sudo udevadm trigger
ls -l /dev/ttyACM*
```

---

## 8. Firewall Configuration (UFW)

If you are using ports 6699/udp or 7788/udp for communication, allow them:

```bash
sudo apt-get install ufw
sudo ufw allow 6699/udp
sudo ufw allow 7788/udp
```

---

## 9. Launching ROS Packages at Boot

Automate your ROS launch files using `robot_upstart` or a systemd service.

### 9.1 Example Using `robot_upstart`

1. Install:
   ```bash
   sudo apt-get install ros-humble-robot-upstart
   ```
2. Create a service for manual control:
   ```bash
   python3 gr_platform/docs/srv_up.py
   ```
3. Edit systemd service file:
   ```bash
   sudo nano /lib/systemd/system/ros-manual-control.service
   ```
   Update:

   ```ini
   [Unit]
   Description="bringup ros-manual-control"
   After=network.target

   [Service]
   Type=simple
   Environment="HOME=/home/rover"
   Environment="XDG_RUNTIME_DIR=/home/rover"
   Environment="XAUTHORITY=/home/rover/.Xauthority"
   ExecStart=/usr/sbin/ros-manual-control-start

   [Install]
   WantedBy=multi-user.target
   ```

4. Source environment in `/usr/sbin/ros-manual-control-start`:
   ```bash
   source /home/rover/gr_platform/configs_root/scripts/env.sh
   ```
5. Enable/Disable/Check the service:
   ```bash
   sudo systemctl enable ros-manual-control
   sudo systemctl start ros-manual-control

   sudo systemctl stop ros-manual-control
   sudo systemctl disable ros-manual-control

   sudo systemctl status ros-manual-control
   ```

6. Uninstall:
   ```bash
   python3 gr_platform/docs/srv_down.py
   ```

---

## 10. System Checkers and Diagnostics

Install `jetson-stats` for Jetson devices:

```bash
sudo -H pip install jetson-stats
jtop
```

---

## 11. Install AI Jetpack (Optional)

For NVIDIA Jetson devices:

```bash
sudo apt install nvidia-jetpack
```

---

## 12. Install OpenCV with CUDA (Optional)

1. Increase Git buffer (if building from source):
   ```bash
   git config --global http.postBuffer 524288000
   git config --global core.compression 0
   ```
2. Build from the `build_opencv` directory:
   ```bash
   cd ~/gr_platform/build_opencv
   ./build_opencv.sh 4.5.4
   python3 demo.py -b=5 -t=7
   ```

---

## 13. Debug Utilities

### CAN Interface Debug

```bash
# Load modules
sudo modprobe can
sudo modprobe can_raw
sudo modprobe mttcan

# Set up CAN0
sudo ip link set can0 up type can bitrate 500000
sudo chmod 777 /dev/ttyACM0
sudo ip link set up can0
sudo ip link set can0 txqueuelen 1000

# Dump test
candump can0 -xct z -n 20
# Check details
ip -details link show can0
# Bring down and up again
sudo ip link set can0 down
sudo ip link set can0 up type can bitrate 500000

# Send CAN message
cansend can0 604#2B40600000000000
```

### System Logs

```bash
tail -f /var/log/syslog
dmesg | tail
```

### ODrive Check

```bash
odrivetool
dump_errors(odrv0)
```

### I2C Debug

```bash
i2cdetect -l
i2cdump <i2cbus> <chip-address>
i2cget <i2cbus> <chip-address> <data-address>
i2cset <i2cbus> <chip-address> <data-address> <value>
```

---

## 14. Catkin Make Examples

Build a single package (plus its dependencies):

```bash
catkin_make --only-pkg-with-deps <target_package>
catkin_make -DCMAKE_BUILD_TYPE=Release -DBUILD_VGICP_CUDA=ON
```

Return to building all packages:

```bash
catkin_make -DCMAKE_BUILD_TYPE=Release -DBUILD_VGICP_CUDA=ON -DCATKIN_WHITELIST_PACKAGES=""
```

---

## 15. ODrive CAN Command Reference

Below is an example ODrive CAN command set with message IDs and data layouts:

```python
command_set = {
  'heartbeat': (0x001, [...]),
  'estop': (0x002, []),
  'get_motor_error': (0x003, [...]),
  'get_encoder_error': (0x004, [...]),
  ...
  'set_input_torque': (0x00e, [('input_torque', 'f', 1)]),
  'reboot': (0x016, []),
  'clear_errors': (0x018, []),
}
```


### **1. Verify CUDA Installation**
Check if CUDA is installed on your system:

```bash
nvcc --version
```

Expected output (example for CUDA 12.6):

```
nvcc: NVIDIA (R) Cuda compiler
release 12.6, V12.6.0
```

If you see **"Command not found"**, install CUDA:

```bash
sudo apt install nvidia-cuda-toolkit
```

---

### **2. Set CUDA Environment Variables**
Set `CUDA_TOOLKIT_ROOT_DIR` manually:

```bash
export CUDA_TOOLKIT_ROOT_DIR=/usr/local/cuda
export PATH=$CUDA_TOOLKIT_ROOT_DIR/bin:$PATH
export LD_LIBRARY_PATH=$CUDA_TOOLKIT_ROOT_DIR/lib64:$LD_LIBRARY_PATH
```

Then verify that CUDA is detected by CMake:

```bash
cmake --find-package -DNAME=CUDA -DCOMPILER_ID=GNU -DLANGUAGE=C -DMODE=EXIST
```

If CUDA is found, re-run your build:

```bash
cd ~/gr_platform2/colcon_ws
colcon build --symlink-install
```

---

### **3. Add CUDA to CMake Arguments**
If the issue persists, modify your **CMake command** to specify CUDA explicitly:

```bash
colcon build --cmake-args -DCUDA_TOOLKIT_ROOT_DIR=/usr/local/cuda
```

Alternatively, edit **CMakeLists.txt** and add:

```cmake
set(CUDA_TOOLKIT_ROOT_DIR "/usr/local/cuda" CACHE PATH "CUDA Toolkit path")
```

---

### **4. Check CUDA Version Compatibility**
Isaac ROS may require a **specific CUDA version**. Run:

```bash
dpkg -l | grep cuda
```

If the version is **too old**, upgrade CUDA:

```bash
sudo apt update
sudo apt install cuda-12-6
```

Then reboot your system:

```bash
sudo reboot
```

---

### **5. If Using Docker**
If you're using **Isaac ROS inside a Docker container**, ensure CUDA is available by running:

```bash
docker run --gpus all --rm nvcr.io/nvidia/cuda:12.6-base nvidia-smi
```

If CUDA is not available, start your container with GPU access:

```bash
docker run --gpus all -it --rm nvcr.io/nvidia/isaac_ros/ros2:humble bash
```

---

## **Final Steps**
1. **Verify CUDA installation (`nvcc --version`).**
2. **Set `CUDA_TOOLKIT_ROOT_DIR` manually (`export CUDA_TOOLKIT_ROOT_DIR=/usr/local/cuda`).**
3. **Try building again (`colcon build --cmake-args -DCUDA_TOOLKIT_ROOT_DIR=/usr/local/cuda`).**
4. **Ensure you're using a compatible CUDA version (`dpkg -l | grep cuda`).**
5. **If using Docker, add `--gpus all` when running the container.**

Let me know if you need more debugging! 🚀



### CUDA installation (optional)

```bash
source install/setup.bash 
sudo apt install nvidia-cuda-toolkit
pip3 install torch==1.10.0+cu113 torchvision==0.11.1+cu113 torchaudio==0.10.0+cu113 -f https://download.pytorch.org/whl/cu113/torch_stable.html
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/cuda/lib64
```

Test if Pytorch is with CUDA:
```bash
python3 ~/Orca_Robot/docs/cuda_test.py
```

---

Go back to the [README](README.md).