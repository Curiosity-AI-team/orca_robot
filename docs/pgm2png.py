import cv2

input = cv2.imread("/home/vboxuser/orca_robot/colcon_ws/src/OrcaRL2/simulation/rmf_demos/rmf_demos_maps/maps/cave/my_map.pgm", -1)
cv2.imwrite("out.png", input)
print("Done!")