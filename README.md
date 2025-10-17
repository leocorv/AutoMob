


# 🧠 AutoMob

> Adaptive AI mobs for Minecraft – built for live learning.

AutoMob est un mod **Fabric (serveur uniquement)** pour **Minecraft 1.21.4 (Java 21)**.  
Il fait apparaître et contrôle des mobs à l’aide d’un système d’**apprentissage par renforcement en direct (RL)**.

---

## ⚙️ Fonctionnalités principales

- Spawns automatiques de mobs “IA” autour des joueurs.
- Difficulté dynamique selon les performances du joueur.
- Système de *reward shaping* : dégâts, kills, survie, inactivité, etc.
- Commande `/neuro stats` pour suivre les stats en live :
```

[AutoMob] RPM=2.45 | DPM=0.53 | K/D=6/8

```

---

## 🧩 Structure du projet

```

src/main/java/org/nerix/automob/
├─ Automob.java               → Entrypoint principal
├─ ControlledSpawner.java     → Spawn adaptatif des mobs
├─ ControllerTick.java        → Application des actions IA
├─ FeatureExtractor.java      → Extraction des features du monde
├─ LiveStats.java             → Statistiques et rewards
├─ DamageHooks.java           → Gestion des dégâts & rewards
├─ Policy.java / DummyPolicy  → Interface de décision IA
└─ ControlledMobRegistry.java → Liste des mobs IA actifs

````

---

## 🧮 Système de Rewards (exemple)

| Action                    | Reward  |
|---------------------------|----------|
| Dégâts au joueur (½ cœur) | +1       |
| Kill du joueur            | +50      |
| Assist (>20% dmg)         | +10      |
| Dégâts subis              | −0.5     |
| Inactivité                | −0.01    |
| Multiplicateur de phase   | × (1 + 0.25 × phase) |

---

## 🏗️ Build & Lancement

### Prérequis
- **Java 21**
- **Gradle** (wrapper inclus)
- **Minecraft 1.21.4**
- **Fabric API**

### Build le mod
```bash
./gradlew build
````

→ JAR dispo dans `build/libs/`

### Lancer un serveur local

```bash
./gradlew runServer
```

---

## 📜 Commandes disponibles

| Commande       | Description                            |
| -------------- | -------------------------------------- |
| `/neuro stats` | Affiche les métriques IA en temps réel |
| `/neuro reset` | Réinitialise les stats locales         |

---

## 🧠 Vision du projet

L’objectif est de créer une IA Minecraft **vivante et évolutive**, capable d’apprendre de ses erreurs,
d’analyser les comportements des joueurs, et d’adapter sa stratégie au fil du temps.

### Architecture prévue :

```
Minecraft Server (Fabric)
│
├─ Collecte de données → FeatureExtractor
│
├─ Transmission → Backend Python (gRPC)
│
├─ Prédiction d’action ← Policy / NeuroClient
│
└─ Application en jeu → ControllerTick
```

---

## 🧰 Stack technique

| Élément       | Version | Description                          |
| ------------- | ------- | ------------------------------------ |
| Minecraft     | 1.21.4  | Base du serveur                      |
| Fabric Loader | 0.17.3  | Gestion du mod                       |
| Fabric API    | 0.119.4 | Outils serveurs                      |
| Yarn Mappings | build.8 | Mappings 1.21.4                      |
| Java          | 21      | Support moderne (records, var, etc.) |

---

## 🧩 Roadmap

* [x] Système de spawn adaptatif
* [x] Suivi live des stats / rewards
* [x] Policy locale (Dummy)
* [ ] gRPC ↔ backend Python
* [ ] Entraînement PPO/DQN
* [ ] Interface visuelle IA
* [ ] Mode “AI vs AI”

---

## 📜 Licence

`All Rights Reserved`
Projet privé – utilisation ou redistribution non autorisée sans permission explicite.

---

## 👤 Auteur

**Leo Corvaisier-Palluy**
Développeur Minecraft & Machine Learning – 🇫🇷 France
🔗 [github.com/leocorv](https://github.com/leocorv)

> “The goal isn’t to make smarter mobs, it’s to make the world fight back.”

```


