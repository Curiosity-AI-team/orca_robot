# Orca Robot Project

Welcome to the **Orca Robot Project**! This repository brings together a complete and versatile robotics platform, featuring multiple sub-packages designed to handle robotic reinforcement learning, multimodal interaction, and smart home integration. Below is a detailed guide to help you get started with each component of the project.

## Package Overview

The project consists of the following main packages:

1. **OrcaRL** - A reinforcement learning package for ROS2-based robots.
2. **OrcaVA** - A multimodal voice assistant designed to interact with users in a natural way.
3. **OrcaHACS** - A collection of HACS (Home Assistant Community Store) integrations for seamless smart home control.

## Folder Structure

```plaintext
orca_robot/
├── colcon_ws/
│   └── src/
│       └── OrcaRL2/          # OrcaRL package for ROS2
├── OrcaVA/                   # Multimodal assistant package
├── HACS/                     # HACS integrations for Home Assistant
└── docs/                     # Documentation and images
```

## Installation Guide

### Prerequisites

Before you begin, ensure you have the following installed on your system:

- **ROS2 Humble** or later
- **Docker** (for containerized components)
- **Python 3.8+**
- **Home Assistant** (for HACS integration)

### Setting Up the Workspace

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/orca_robot.git
   cd orca_robot
   ```

2. Build the ROS2 workspace:
   ```bash
   cd colcon_ws
   colcon build
   ```

3. Source the setup file:
   ```bash
   source install/setup.bash
   ```

### Running OrcaRL

OrcaRL provides tools and environments for training reinforcement learning models on robots.

1. Navigate to the OrcaRL package:
   ```bash
   cd colcon_ws/src/OrcaRL2
   ```
2. Launch the OrcaRL node:
   ```bash
   ros2 launch orca_rl orca_rl.launch.py
   ```

For detailed usage, see the [OrcaRL Documentation](docs/orca_rl_docs.md).

### Running OrcaVA

OrcaVA is a multimodal assistant capable of handling voice commands, text inputs, and visual prompts.

1. Navigate to the OrcaVA directory:
   ```bash
   cd OrcaVA
   ```
2. Run the assistant:
   ```bash
   python3 main.py
   ```

Refer to the [OrcaVA User Guide](docs/orca_va_docs.md) for more information on setup and configuration.

### Setting Up OrcaHACS

OrcaHACS contains custom components and integrations for Home Assistant.

1. Copy the contents of the `HACS` folder to your Home Assistant `custom_components` directory.
2. Restart Home Assistant.
3. Add the new integrations via the Home Assistant UI.

For details on available integrations, see the [OrcaHACS Documentation](docs/orca_hacs_docs.md).

## Documentation

Comprehensive documentation for each package can be found in the `docs` folder:

- [OrcaRL Documentation](docs/orca_rl_docs.md)
- [OrcaVA Documentation](docs/orca_va_docs.md)
- [OrcaHACS Documentation](docs/orca_hacs_docs.md)

## Screenshots

Below are some screenshots showing various components of the project in action:

### OrcaRL Interface
![OrcaRL Interface](docs/images/orca_rl_interface.png)

### OrcaVA in Action
![OrcaVA Interaction](docs/images/orca_va_interaction.png)

### OrcaHACS Integration
![OrcaHACS Home Assistant](docs/images/orca_hacs_integration.png)

## Contributing

We welcome contributions to the Orca Robot Project! If you'd like to contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Commit your changes and push them to your fork.
4. Submit a pull request with a detailed description of your changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## Contact

If you have any questions or need further assistance, feel free to contact us:

- **Email:** support@orca-robot.com
- **GitHub Issues:** [Issue Tracker](https://github.com/your-repo/orca_robot/issues)

Thank you for using the Orca Robot Project! We hope it enhances your robotics and smart home experience.