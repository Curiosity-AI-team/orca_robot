### CUDA withg pytorcvh installation (optional)

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