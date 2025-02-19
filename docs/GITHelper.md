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
git config --global user.name "FIRST_NAME LAST_NAME"
git config --global user.email "MY_NAME@example.com"
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


You can generate an SSH key on your system using the following steps:

---

## **1. Generate a New SSH Key**
Open a terminal and run:

```bash
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```

- Replace `"your_email@example.com"` with your actual GitHub email.
- When prompted to enter a file to save the key, press **Enter** to use the default path (`~/.ssh/id_rsa`).
- When prompted for a passphrase, you can leave it empty (or set one for extra security).

---

## **2. Add Your SSH Key to the SSH Agent**
Start the SSH agent:

```bash
eval "$(ssh-agent -s)"
```

Then add your SSH private key to the agent:

```bash
ssh-add ~/.ssh/id_rsa
```

---

## **3. Copy Your SSH Public Key**
Run:

```bash
cat ~/.ssh/id_rsa.pub
```

Copy the output (your SSH public key), which looks something like:

```
ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEA7...
```

---

## **4. Add the SSH Key to GitHub**
1. Go to **GitHub → Settings**:  
   [🔗 GitHub SSH Keys](https://github.com/settings/keys)
   
2. Click **New SSH Key**.

3. **Title:** Name your key (e.g., "My Work PC").

4. **Key Type:** Choose **Authentication Key**.

5. **Paste the SSH Public Key** (from step 3).

6. Click **Add SSH Key**.

---

## **5. Test the SSH Connection**
Run:

```bash
ssh -T git@github.com
```

If successful, you should see:

```
Hi <your_github_username>! You've successfully authenticated, but GitHub does not provide shell access.
```

---

## **6. Change Your Git Remote to SSH**
If your Git repository is using HTTPS, update it to SSH:

```bash
git remote set-url origin git@github.com:Curiosity-AI-team/OrcaRL2.git
```

Now, try fetching:

```bash
git fetch
```

If you set up SSH correctly, authentication should work without asking for a username or password.

---

If Visual Studio (VS) still shows changes in the submodule `colcon_ws/src/rmf_demos`, it means Git is tracking the submodule commit and detecting differences. The `-dirty` status indicates that some files inside the submodule were modified.

### **Steps to Fully Ignore Submodule Changes**
#### **1. Ignore Submodule Changes Globally**
Run the following command in your repository:
```bash
git config submodule.colcon_ws/src/rmf_demos.ignore all
```
This tells Git to **completely ignore** changes inside the submodule.

If you want to apply this setting globally for all repositories:
```bash
git config --global submodule.recurse false
```

#### **2. Mark Submodule as Unchanged**
If VS still shows changes, tell Git to ignore modifications:
```bash
git update-index --assume-unchanged colcon_ws/src/rmf_demos
```
If you ever need to reset this:
```bash
git update-index --no-assume-unchanged colcon_ws/src/rmf_demos
```

#### **3. Ensure No Local Changes in the Submodule**
Navigate to the submodule directory and check for modifications:
```bash
cd colcon_ws/src/rmf_demos
git status
```
If you see any modified files, you can reset them:
```bash
git reset --hard HEAD
git clean -fd
```
Then, go back to your main repository:
```bash
cd ../../../
```

#### **4. Remove Submodule from `git status` Output**
To fully remove the submodule from showing in `git status`, run:
```bash
git diff --cached colcon_ws/src/rmf_demos
```
If you see an unwanted change, reset it:
```bash
git reset HEAD colcon_ws/src/rmf_demos
```

#### **5. Verify in VS**
Restart Visual Studio, then check if it still detects changes.

---

Let me know if you need further assistance! 🚀

---

Go back to the [README](../README.md).