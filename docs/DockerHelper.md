# Docker Helper

This document explains how to install and manage Docker on Ubuntu, along with basic usage examples. It also covers forcefully removing images in cases where Docker complains about multiple tags. If you need to integrate Docker containers into your **GR Platform** workflow, or you want to containerize parts of your robotic system, follow the steps below.

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [System Update](#2-system-update)
3. [Install Required Packages](#3-install-required-packages)
4. [Add Docker GPG Key](#4-add-docker-gpg-key)
5. [Set Up Docker Repository](#5-set-up-docker-repository)
6. [Install Docker Engine](#6-install-docker-engine)
7. [Verify Installation](#7-verify-installation)
8. [Manage Docker as a Non-root User](#8-manage-docker-as-a-non-root-user)
9. [Enable Docker at Boot](#9-enable-docker-at-boot)
10. [Basic Docker Usage](#10-basic-docker-usage)
11. [Force Remove Images](#11-force-remove-images)
12. [Next Steps and References](#12-next-steps-and-references)

---

## 1. Introduction

Docker is a powerful platform to **build, ship, and run** applications inside lightweight containers. It can be especially useful for robotics workflows, where you may want to isolate certain dependencies or quickly deploy on multiple machines.

This guide assumes you are using **Ubuntu 22.04** (the same OS recommended in [INSTALL_DESKTOP.md](INSTALL_DESKTOP.md) and [INSTALL_ROBOT.md](INSTALL_ROBOT.md)).

---

## 2. System Update

Always begin by updating your package index:

```bash
sudo apt update
```

*(You can also run `sudo apt upgrade -y` if you’d like to upgrade existing packages.)*

---

## 3. Install Required Packages

Install packages necessary for Docker:

```bash
sudo apt install apt-transport-https ca-certificates curl software-properties-common -y
```

---

## 4. Add Docker GPG Key

Download Docker’s official GPG key and store it in your keyring:

```bash
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | \
  sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
```

---

## 5. Set Up Docker Repository

Next, add the **stable** Docker repository to your system’s sources:

```bash
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] \
  https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" \
  | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

Then update your package list again:

```bash
sudo apt update
```

---

## 6. Install Docker Engine

Install Docker’s command-line and daemon packages:

```bash
sudo apt install docker-ce docker-ce-cli containerd.io -y
```

---

## 7. Verify Installation

Run the classic `hello-world` test container:

```bash
sudo docker run hello-world
```

If everything is successful, you’ll see a message confirming Docker is up and running.

---

## 8. Manage Docker as a Non-root User

To avoid using `sudo` for every Docker command, add your user to the **docker** group:

```bash
sudo usermod -aG docker $USER
```

Either **log out and back in** or run:

```bash
newgrp docker
```

to apply the new group settings immediately.

---

## 9. Enable Docker at Boot

Make Docker start automatically whenever your system boots:

```bash
sudo systemctl enable docker
```

---

## 10. Basic Docker Usage

You can now download and run containers from Docker Hub. For example, to run the latest Ubuntu image in interactive mode:

```bash
docker run -it ubuntu /bin/bash
```

When you exit the container, it will stop by default (unless you specify otherwise).

---

## 11. Force Remove Images

If you encounter an error stating an image is “tagged in multiple repositories,” you must force Docker to remove the image. For example, if the image ID is `a0eec34f4b62`:

```bash
docker rmi --force a0eec34f4b62
```

Verify removal:

```bash
docker images
```

The removed image should no longer be listed.

---

Go back to the [README](../README.md).