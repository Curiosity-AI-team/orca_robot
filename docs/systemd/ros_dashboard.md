Below is an example of how to create a **systemd service** on Ubuntu that runs your `test_ros_receive.py` script **within your virtual environment** located in `/home/rover2/Orca_Robot/OrcaVA/internet_client/venv`.  

---

## 1. Create a Systemd Service File

Open (or create) a new service file:

```bash
sudo nano /etc/systemd/system/test_ros_receive.service
```

Paste in the following content (adjusting paths if necessary):

```ini
[Unit]
Description=ROS Receive Script (FastAPI with Virtual Env)
After=network.target

[Service]
Type=simple

# Run as your regular user (not root) so that file permissions match
User=rover2
Group=rover2

# Point WorkingDirectory to the directory containing your Python script
WorkingDirectory=/home/rover2/Orca_Robot/OrcaVA/app/scripts

# Use the Python interpreter from your virtual environment
ExecStart=/home/rover2/Orca_Robot/OrcaVA/internet_client/venv/bin/python /home/rover2/Orca_Robot/OrcaVA/app/scripts/test_ros_receive.py

# Automatically restart if the script crashes
Restart=always
RestartSec=3

# Make Python output unbuffered so logs appear instantly in journalctl
Environment="PYTHONUNBUFFERED=1"

[Install]
WantedBy=multi-user.target
```

> **Key Points**  
> 1. **WorkingDirectory** should be set to the folder containing `test_ros_receive.py` (or wherever you prefer).  
> 2. **ExecStart** must explicitly point to your virtual environment’s Python interpreter.  
> 3. You can add or remove environment variables as needed.

---

## 2. Reload and Enable the Service

Reload systemd to recognize the new service unit:

```bash
sudo systemctl daemon-reload
```

Enable the service at startup:

```bash
sudo systemctl enable test_ros_receive.service
```

Start the service:

```bash
sudo systemctl start test_ros_receive.service
```

---

## 3. Verify the Service Status

To check if the service is active:

```bash
systemctl status test_ros_receive.service
```

You should see an **active (running)** status if everything is working correctly.

To monitor real-time logs:

```bash
journalctl -u test_ros_receive.service -f
```

Press `Ctrl + C` to exit log monitoring.

---

## 4. Troubleshooting Tips

1. **Check File Paths**  
   - Ensure the paths in `ExecStart` and `WorkingDirectory` are correct.  
   - Verify your virtual environment is located at `/home/rover2/Orca_Robot/OrcaVA/internet_client/venv`.

2. **Verify Package Installations**  
   - Make sure you have installed `fastapi`, `pydantic`, `yaml`, and `uvicorn` in your virtual environment:
     ```bash
     source /home/rover2/Orca_Robot/OrcaVA/internet_client/venv/bin/activate
     pip install fastapi pydantic pyyaml uvicorn
     ```

3. **Check Permissions**  
   - The service runs under the user `rover2`. Verify that `rover2` has access to the script and the virtual environment directories.

4. **System Port Conflicts**  
   - If your script binds to a TCP port (e.g., `4567`), confirm it’s free and not used by another service.

5. **Adjust Restart Behavior**  
   - If you encounter a crash loop, increase `RestartSec` or remove `Restart=always` to avoid repeated immediate restarts.

---

### After These Steps

- Your FastAPI application (defined in `test_ros_receive.py`) should be listening on the configured host and port (`0.0.0.0:4567` by default, unless you changed it).
- You can now manage the script using standard systemd commands (`start`, `stop`, `restart`, `status`, etc.).

That’s it! Your Python script is now launched via systemd using the specified virtual environment, ensuring `fastapi` and other dependencies are available at runtime.