# Git Submodule Helper

This document explains how to manage Git submodules within the **GR Platform** repository or any ROS2 workspace. Submodules allow you to include external repositories within your own project, keeping them in sync and pinned to specific versions.

---

## Table of Contents

1. [Introduction to Submodules](#1-introduction-to-submodules)
2. [Cloning with Submodules](#2-cloning-with-submodules)
3. [Initializing and Updating Submodules](#3-initializing-and-updating-submodules)
4. [Forcing a Submodule Update](#4-forcing-a-submodule-update)
5. [Adding New Submodules](#5-adding-new-submodules)
6. [Where to Go Next](#6-where-to-go-next)

---

## 1. Introduction to Submodules

**Git submodules** are nested repositories inside a parent repository. In a robotic application, you might use submodules to include driver packages (e.g., sensor drivers, navigation libraries) that are maintained elsewhere.

---

## 2. Cloning with Submodules

When you clone a repository that contains submodules, pass the `--recursive` flag to fetch submodule contents:

```bash
git clone --recursive <repository-url>
```

If you forget the `--recursive` option, you can always initialize and update submodules afterward (see below).

---

## 3. Initializing and Updating Submodules

To ensure all submodules are present and up to date:

```bash
cd ~/Orca_Robot/colcon_ws/src/OrcaRL2
git submodule update --init --recursive
```

This will fetch all submodules (and nested submodules) at the commit specified in the parent repository.

---

## 4. Forcing a Submodule Update

If submodules get out of sync or you need to override local changes, you can **force** an update:

```bash
git submodule update --init --recursive --force
```

Use this with caution, as it discards local modifications inside submodules.

---

## 5. Adding New Submodules

To add a submodule to your repository:

```bash
git submodule add <repository-url> <destination-folder>
```

For example, to add `aruco_ros`:

```bash
git submodule add https://github.com/pal-robotics/aruco_ros.git localization/aruco_ros
```

Similarly, you might add `rmf_demos` or `velodyne` packages:

```bash
git submodule add https://github.com/open-rmf/rmf_demos.git simulation/rmf_demos
git submodule add https://github.com/ros-drivers/velodyne localization/velodyne
```

*(Adjust the paths if needed to align with your workspace.)*

---

Go back to the [README](../README.md).