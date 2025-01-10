cmake_minimum_required(VERSION 3.5)
project(block_build)

find_package(ament_cmake REQUIRED)

message(FATAL_ERROR
  "**************************************************************
   ERROR: You are attempting to build in a forbidden directory!
   Please build only inside ~/Orca_Robot/colcon_ws/
   **************************************************************")

ament_package()
