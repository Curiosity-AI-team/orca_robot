1. Force Lower Quality Settings via Command Options
You can combine several options to push the connection toward lower quality:

```bash
xtigervncviewer -SecurityTypes VncAuth \
  -QualityLevel=3 \
  -CompressLevel=9 \
  -PreferredEncoding=Tight \
  -LowColorLevel=1 \
  -FullColor=0 \
  192.168.1.102:2
```


The error you're encountering is because the default startup script `/etc/X11/Xvnc-session` is either failing or causing a deadlock when `vncserver` tries to launch the desktop environment. Here's a breakdown of what happened and how to fix it:

---

### 🔧 What Happened

- `vncserver` starts correctly and assigns `:1` to the session.
- The `Xtigervnc` server itself starts and listens on port `5901`.
- However, the session startup script (`/etc/X11/Xvnc-session`) failed with exit code `256`, possibly due to a misconfiguration or missing dependencies (like no desktop environment installed).
- The VNC session is killed shortly after with SIGKILL due to being unresponsive or stuck.

---

### ✅ What You Should Do

Start a minimal, working VNC session manually to test it.

Try this command:

```bash
vncserver -xstartup /usr/bin/xterm
```

This tells VNC to skip the usual full desktop session and instead start a simple terminal (`xterm`). If this works, you'll know the issue is with your full session startup (like GNOME, XFCE, etc.).

---

### 🧠 If That Works and You Want a Real Desktop (e.g. XFCE)

1. **Install XFCE desktop (or another lightweight desktop)** if not already installed:

```bash
sudo apt update
sudo apt install xfce4 xfce4-goodies
```

2. **Create or edit your `~/.vnc/xstartup` file**:

```bash
nano ~/.vnc/xstartup
```

Replace its content with:

```bash
#!/bin/sh
xrdb $HOME/.Xresources
startxfce4 &
```

3. **Make it executable**:

```bash
chmod +x ~/.vnc/xstartup
```

4. **Start the VNC server again**:

```bash
vncserver
```

---

### 🔍 To Check Logs

If anything goes wrong again, always check the latest log:

```bash
tail -n 50 ~/.vnc/ubuntu:1.log
```

---

The issue is that your VNC server is only listening on the loopback interface (127.0.0.1), so remote connections are refused. You need to change it so that it listens on all interfaces.

You have two main options:

---

### Option 1: Start VNC Server with the "-localhost no" Flag

1. **Kill the existing session:**

   ```bash
   vncserver -kill :2
   ```

2. **Restart the VNC server allowing remote connections:**

   ```bash
   vncserver -localhost no :2
   ```

3. **Verify it's listening on all interfaces:**

   ```bash
   ss -tuln | grep 5902
   ```

   You should now see an entry like:

   ```
   tcp    LISTEN  0       5   0.0.0.0:5902   0.0.0.0:*
   ```

4. **Connect from your client (gumich):**

   ```bash
   xtigervncviewer -SecurityTypes VncAuth -passwd ~/.vnc/passwd 192.168.1.102:2
   ```

---

### Option 2: Edit the VNC Configuration File

1. **Create or edit the VNC config file:**

   ```bash
   nano ~/.vnc/config
   ```

2. **Add the following line to force VNC to listen on all interfaces:**

   ```
   localhost=no
   ```

3. **Save the file and exit the editor.**

4. **Kill and restart your VNC session:**

   ```bash
   vncserver -kill :2
   vncserver :2
   ```

5. **Verify with:**

   ```bash
   ss -tuln | grep 5902
   ```

   You should see it listening on `0.0.0.0:5902` instead of `127.0.0.1:5902`.

6. **Now try connecting from your client again:**

   ```bash
   xtigervncviewer -SecurityTypes VncAuth -passwd ~/.vnc/passwd 192.168.1.102:2
   ```

---

### Recommendation

For security reasons, many prefer to leave VNC bound to localhost and use an SSH tunnel to access it remotely. However, if you need to connect directly, use one of the options above to disable localhost-only mode.

Let me know if you run into any further issues or need additional help!