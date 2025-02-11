# GR Platform Manual

This document explains how to run simulations, launch SLAM, and start navigation using the GR Platform or a similar ROS2-based setup. It also includes instructions for connecting to external fleet management or tasks.

---

## Table of Contents

1. [Running the Simulation](#1-running-the-simulation)
2. [Launching SLAM](#2-launching-slam)
3. [Launching Navigation](#3-launching-navigation)
4. [LLM Management](#4-llm-management)
5. [Fleet Server and Client](#5-fleet-server-and-client)
6. [SLAM and Localization Pipeline](#6-slam-and-localization-pipeline)
7. [Submitting Tasks](#7-submitting-tasks)
8. [Additional Commands](#8-additional-commands)

---

## 1. Running the Simulation

### 1.1 Launch Simulation

```bash
ros2 launch boldbot_sim boldbot_sim_nav.launch.py
```
*(Adjust package names/launch files to your actual setup.)*

---

## 2. Launching SLAM

Start the `orca_rtabmap_slam_rgbd.launch.py` (or your own SLAM package) with simulation time:

```bash
ros2 launch orca_rtabmap orca_rtabmap_slam_rgbd.launch.py use_sim_time:=true qos:=2 localization:=false
```

---

## 3. Launching Navigation

Assuming you have already built your navigation package:

```bash
ros2 launch orca_navigation navigation2.launch.py use_sim_time:=True
```

*(If your navigation package is named differently, adjust accordingly.)*

---

## 4. LLM Management

For large language model tasks or conversation-based logic:

```bash
ros2 launch task_publisher rasa_task_publisher.launch.py
```

This typically listens for external triggers or conversation tasks.

---

## 5. Fleet Server and Client

### 5.1 Server

```bash
ros2 launch orca_free_fleet_server server.launch.xml map:=new_map5
```

### 5.2 Client

```bash
ros2 launch orca_free_fleet_client client.launch.xml
```

---

## 6. SLAM and Localization Pipeline

1. **Start Mapping**:
   ```bash
   ros2 launch orca_rtabmap orca_rtabmap_slam_rgbd.launch.py use_sim_time:=true qos:=2
   ```
2. **Save Map**:
   ```bash
   ros2 run nav2_map_server map_saver_cli -f my_map -t /map
   ```
3. **Generate Fleet Data** (if using fleet mgmt):
   ```bash
   python3 ~/Orca_Robot/docs/pgm2png.py
   traffic-editor
   colcon build
   ```
4. **Run in Localization Mode**:
   ```bash
   ros2 launch orca_rtabmap orca_rtabmap_slam_rgbd.launch.py use_sim_time:=true qos:=2 localization:=true
   ros2 launch orca_navigation navigation2.launch.py use_sim_time:=True
   ```
5. **Start Fleet Client & Server**:
   ```bash
   ros2 launch orca_free_fleet_client client.launch.xml
   ros2 launch orca_free_fleet_server server.launch.xml map:=new_map5
   ```
6. **Launch Task Publisher**:
   ```bash
   ros2 launch task_publisher rasa_task_publisher.launch.py
   ```

---

## 7. Submitting Tasks

### 7.1 Patrol Task

You can submit a patrol task to the server:
```bash
ros2 run rmf_demos_tasks dispatch_patrol -p coe lounge -n 3 --use_sim_time
```

### 7.2 Delivery Task

```bash
ros2 run rmf_demos_tasks dispatch_delivery -p pantry -ph coke_dispenser -d hardware_2 -dh coke_ingestor --use_sim_time
```

You can also explore:

```
https://open-rmf.github.io/rmf-panel-js/
https://app.foxglove.dev/
```

---

## 8. Additional Commands

### 8.1 Playing a Recorded Bag

```bash
ros2 bag play rosbag2_2024_09_23-21_20_27/rosbag2_2024_09_23-21_20_27_0.db3
```

### 8.2 Launch Navigation with Rover Simulation

```bash
ros2 launch rover_simulation launch_nav.launch.py
```

### 8.3 Terminate Gazebo Processes

```bash
killall -9 gzserver
killall -9 gzclient
```

---

Go back to the [README](../README.md).