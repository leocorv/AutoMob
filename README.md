# AutoMob

> Experimental adaptive mob AI for Minecraft, built as a playground for reinforcement-learning concepts.

## Project status

**Paused experimental prototype.**

I have not worked on AutoMob for a while and the project is **not currently considered functional or maintained**. The repository is published as a technical experiment and as a record of the architecture explored around adaptive Minecraft entities.

Do not expect the current codebase to work out of the box without fixes or dependency/version adjustments.

AutoMob is **not** a finished reinforcement-learning system. The current repository mainly contains the Minecraft/Fabric-side foundations: controlled mobs, feature extraction, reward/stat tracking and a policy abstraction. The external learning backend described below was planned but is not implemented in this version.

## What is in the repository

AutoMob is a **server-side Fabric mod** targeting **Minecraft 1.21.4 / Java 21**.

Current prototype components include:

- controlled mob spawning around players;
- basic adaptive spawning logic;
- extraction of gameplay/world features;
- reward-oriented statistics for damage, kills, survival and inactivity;
- live metrics exposed through `/neuro stats`;
- a `Policy` abstraction for future AI decision engines;
- a local `DummyPolicy` used as the current placeholder decision system.

## Current architecture

```text
Minecraft Server (Fabric)
        |
        +--> ControlledSpawner
        |
        +--> FeatureExtractor
        |
        +--> LiveStats / reward signals
        |
        +--> Policy
              |
              +--> DummyPolicy (current prototype)
```

The longer-term architecture I was exploring was:

```text
Minecraft Server (Fabric)
        |
        +--> Feature extraction
        |
        +--> External Python learning backend
        |       |
        |       +--> training / inference
        |
        +<-- predicted action
        |
        +--> action applied to the controlled mob
```

The Python/gRPC learning backend is **not part of the current working implementation**.

## Main classes

```text
src/main/java/org/nerix/automob/
├── Automob.java
├── ControlledSpawner.java
├── ControlledMobRegistry.java
├── ControllerTick.java
├── FeatureExtractor.java
├── LiveStats.java
├── DamageHooks.java
├── Policy.java
├── DummyPolicy.java
└── NeuroCommands.java
```

## Reward experimentation

The project contains early reward-shaping ideas based on gameplay events such as:

- damage dealt to players;
- player kills;
- assists;
- damage received;
- inactivity;
- difficulty/progression phases.

These values are experimental and should not be interpreted as a tuned or validated RL reward model.

## Build

Prerequisites used during development:

- Java 21
- Minecraft 1.21.4
- Fabric Loader / Fabric API
- Gradle wrapper included in the repository

```bash
./gradlew build
```

A local development server can normally be launched through Fabric Loom with:

```bash
./gradlew runServer
```

Because the project is currently paused, compatibility with the latest dependency versions is **not guaranteed**.

## Original roadmap

- [x] Controlled mob spawning
- [x] Gameplay feature extraction
- [x] Live statistics / reward signals
- [x] Policy abstraction
- [x] Placeholder local policy
- [ ] External Python inference backend
- [ ] gRPC communication
- [ ] PPO/DQN or another learning algorithm
- [ ] Persistent training pipeline
- [ ] AI-vs-AI experimentation
- [ ] Proper testing and production-ready balancing

## Why this repository is public

AutoMob is mainly a personal experiment around the intersection of **Minecraft modding, game telemetry and machine learning**. It is public to document the approach and the code explored, not as a ready-to-install mod.

## License

All Rights Reserved.

## Author

**Léo Corvaisier-Palluy (Nerix)**  
GitHub: [leocorv](https://github.com/leocorv)
