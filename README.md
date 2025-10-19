# FTC Robot Demo Code Library - v3.java Teaching Edition (English)

This directory contains **15 demonstration programs** extracted from v3.java, progressing from simple to complex robot control techniques. Each demo focuses on one knowledge point, is ready to run, and suitable for teaching and video recording.

---

## 📚 Demo Categories

### 🟢 Basic Control (Demo 01-04)

| No. | Name | Knowledge Point | Description |
|-----|------|-----------------|-------------|
| **Demo01** | Tank Drive | Dual joystick control | Independent left/right wheel control |
| **Demo02** | Speed Toggle | X button speed modes | 50%/100% speed switching |
| **Demo03** | Flywheel States | L1 state cycling | 0%→80%→100% loop |
| **Demo04** | Intake Hold | R1/R2 hold pattern | R1 forward / R2 reverse |

### 🟡 Motors and Servos (Demo 05-07)

| No. | Name | Knowledge Point | Description |
|-----|------|-----------------|-------------|
| **Demo05** | Climb Motors | DPad control | Dual climb motors up/down |
| **Demo06** | Fast Motor | Gamepad 2 control | A/B hold for full speed forward/reverse |
| **Demo07** | Block Servo | CRServo | Continuous rotation servo control |

### 🔵 Right Motor Advanced (Demo 08-12) ⭐Core

| No. | Name | Knowledge Point | Description |
|-----|------|-----------------|-------------|
| **Demo08** | Right Manual | Basic control | DPad left/right control |
| **Demo09** | Right Encoder | Encoder reading | Position recording and display |
| **Demo10** | Right PID Hold | PID basics | Automatic position holding |
| **Demo11** | Right Dead Zone | Dead zone optimization | Three-tier error handling |
| **Demo12** | Right Smart Protection ⭐ | Timeout adaptive | 3s timeout + adaptive target |

### 🟣 Advanced Applications (Demo 13-15)

| No. | Name | Knowledge Point | Description |
|-----|------|-----------------|-------------|
| **Demo13** | L2 Special Mode | Asymmetric drive | Right wheel advances + left holds |
| **Demo14** | Dual Gamepad | Two-operator cooperation | Driver + operator |
| **Demo15** | Full Control ⭐ | Complete v3.java | All features integrated |

---

## 🎮 Gamepad Controls

### Demo01 - Tank Drive
- **Left Stick Y**: Left wheel
- **Right Stick Y**: Right wheel

### Demo02 - Speed Toggle
- **Left Stick Y**: Left wheel
- **Right Stick Y**: Right wheel
- **X Button**: Toggle speed (50%/100%)

### Demo03 - Flywheel States
- **L1 (Left Bumper)**: Cycle states (0%→80%→100%→0%)

### Demo04 - Intake Hold
- **R1 (Right Bumper)**: Forward (hold)
- **R2 (Right Trigger)**: Reverse (hold)

### Demo05 - Climb Motors
- **DPad Up**: Both climb up
- **DPad Down**: Both climb down

### Demo06 - Fast Motor (Gamepad 2)
- **A Button**: Forward full speed (hold)
- **B Button**: Reverse full speed (hold)

### Demo07 - Block Servo (Gamepad 2)
- **X Button**: Forward rotation (hold)
- **Y Button**: Reverse rotation (hold)

### Demo08 - Right Manual (Gamepad 2)
- **DPad Left**: Reverse rotation (hold)
- **DPad Right**: Forward rotation (hold)

### Demo09 - Right Encoder (Gamepad 2)
- **DPad Left/Right**: Move motor
- **Release**: Record current position as target

### Demo10 - Right PID Hold (Gamepad 2)
- **DPad Left/Right**: Manual control
- **Release**: PID auto holds position

### Demo11 - Right Dead Zone (Gamepad 2)
- **DPad Left/Right**: Manual control
- **Release**: PID hold + dead zone optimization

### Demo12 - Right Smart Protection ⭐ (Gamepad 2)
- **DPad Left/Right**: Manual control
- **Release**: Smart protection mode
  - 3-second timeout detection
  - Adaptive target position
  - Dead zone processing

### Demo13 - L2 Special Mode
- **L2 (Left Trigger)**: Activate special mode
  - Right wheel: 0.7 forward
  - Left wheel: PID hold
- **Left/Right Stick Y**: Normal tank drive

### Demo14 - Dual Gamepad
**Gamepad 1 (Driver)**:
- Left/Right Stick Y: Tank drive
- L1: Flywheel cycle
- R1: Intake forward
- R2: Intake reverse
- DPad Up/Down: Climb up/down

**Gamepad 2 (Operator)**:
- DPad Left/Right: Right motor
- A/B: Fast motor
- X/Y: Block servo

### Demo15 - Full Control ⭐
Complete v3.java control with all features above

---

## 📖 How to Use

### Running Demos
1. Select demo from **TeleOp** menu on Driver Station
2. Press **INIT** to initialize hardware
3. Press **START** to run
4. Follow gamepad instructions above

### Recommended Learning Sequence

**Stage 1: Basic Control (Demo01-07)**
- Understand basic motor control
- Learn button patterns (hold/toggle)
- Explore different motor types

**Stage 2: Right Motor Advanced (Demo08-12)** ⭐Focus
- Demo08: Manual control basics
- Demo09: How to use encoders
- Demo10: How PID holds position
- Demo11: How dead zone optimizes
- Demo12: How smart protection works

**Stage 3: Advanced Applications (Demo13-15)**
- Understand complex feature combinations
- Learn multi-person coordination
- Master complete system

---

## 🎬 How to Demonstrate - Right Motor Smart Protection (Demo12)

### Demonstration Goal
Show automatic motor protection when encountering obstacles

### Demonstration Steps

1. **Normal Operation Test**:
   - Hold DPad Right (gamepad 2) to move motor
   - Release button
   - Observe: Motor holds position with PID
   - Telemetry shows: "PID Status: Active"

2. **Obstacle Protection Demo** ⭐Core:
   - Hold DPad Right
   - **Block motor with your hand** (simulate obstacle)
   - Watch PID Timer count: 0s → 1s → 2s → 3s
   - At 3 seconds, Telemetry shows:
     ```
     ⚠ PROTECTION ACTIVE
     Action Taken: Target adapted to current position
     Reason: Could not reach original target in 3s
     Result: Motor protected from burnout!
     ```
   - Remove hand - motor no longer tries to advance, holds new position

3. **Key Observation Points**:
   - **Timeout Detection**: 3-second timer
   - **Adaptive Target**: Gives up original target, accepts current position
   - **Motor Protection**: Prevents prolonged stall

### Why This Matters?

❌ **Without Protection**: Motor fights obstacle continuously → overheats → burns out
✅ **Smart Protection**: Gives up after 3s → accepts reality → motor safe

---

## 🔬 Right Motor Knowledge Progression

### Demo08 → Demo09 → Demo10 → Demo11 → Demo12

```
Demo08: Manual Control
  ↓ Add encoder
Demo09: Position Recording
  ↓ Add PID
Demo10: Auto Hold
  ↓ Add dead zone
Demo11: Optimize Stability
  ↓ Add timeout protection
Demo12: Complete Smart Protection ⭐
```

| Demo | Manual | Encoder | PID | Dead Zone | Timeout |
|------|--------|---------|-----|-----------|---------|
| Demo08 | ✓ | ✓ | ✗ | ✗ | ✗ |
| Demo09 | ✓ | ✓ | ✗ | ✗ | ✗ |
| Demo10 | ✓ | ✓ | ✓ | ✗ | ✗ |
| Demo11 | ✓ | ✓ | ✓ | ✓ | ✗ |
| Demo12 | ✓ | ✓ | ✓ | ✓ | ✓ |

---

## 🔧 Hardware Configuration

### Motors
- **bl** - Back left drive motor
- **br** - Back right drive motor
- **fly** - Flywheel motor
- **intake** - Collection motor
- **climbleft** - Left climb motor
- **climbright** - Right climb motor
- **right** - Right motor (requires DcMotorEx, for PID demos)
- **fast** - Fast motor (gamepad 2 control)

### Servos
- **block** - Block continuous rotation servo (CRServo)

---

## 💡 Demo Selection Guide

### Want to learn basic control?
→ **Demo01-04**

### Want to understand different motor types?
→ **Demo05-07**

### Want to deep dive into PID control and protection? ⭐
→ **Demo08-12** (learn in sequence)

### Want to see advanced applications?
→ **Demo13-15**

### Want to record teaching videos?
→ Each demo is independent, can be recorded separately

---

## 🎯 Knowledge Point Index

| Knowledge Point | Related Demos |
|-----------------|---------------|
| Tank Drive | Demo01, Demo13, Demo14, Demo15 |
| Speed Toggle | Demo02, Demo15 |
| State Machine | Demo03, Demo14, Demo15 |
| Hold Control Pattern | Demo04, Demo05, Demo06, Demo07, Demo08 |
| Climb Motors | Demo05, Demo14, Demo15 |
| Gamepad 2 Control | Demo06, Demo07, Demo08-12, Demo14, Demo15 |
| CRServo | Demo07, Demo14, Demo15 |
| Encoder | Demo09, Demo10, Demo11, Demo12, Demo13, Demo15 |
| PID Control | Demo10, Demo11, Demo12, Demo13, Demo15 |
| Dead Zone | Demo11, Demo12, Demo13, Demo15 |
| Timeout Protection | Demo12, Demo15 |
| Adaptive Target | Demo12, Demo15 |
| L2 Special Mode | Demo13, Demo15 |
| Dual Gamepad | Demo14, Demo15 |
| DcMotorEx | Demo08-Demo12, Demo14, Demo15 |

---

## 📝 Code Features

✅ **All English** - Code, comments, variable names in English
✅ **Based on v3.java** - All features extracted from v3.java
✅ **Progressive Learning** - Simple to complex
✅ **Ready to Run** - Each demo runs independently
✅ **Video-Friendly** - Clear knowledge point demonstration
✅ **Detailed Comments** - "How to Demonstrate" instructions
✅ **Integrated PID Class** - PIDController built-in

---

## 🆚 Demo Group Comparison

| Group | Demo Count | Difficulty | Core Content |
|-------|-----------|-----------|--------------|
| Basic Control | 4 demos | ⭐ | Drive, state, buttons |
| Motors & Servos | 3 demos | ⭐⭐ | Different hardware types |
| Right Advanced | 5 demos | ⭐⭐⭐ | PID protection mechanism |
| Advanced Apps | 3 demos | ⭐⭐⭐⭐ | System integration |

---

## 📂 File List

```
演示代码/
├── README_CN.md (Chinese documentation)
├── README_EN.md (This file)
├── Demo01_TankDrive.java
├── Demo02_SpeedToggle.java
├── Demo03_FlywheelStates.java
├── Demo04_IntakeHold.java
├── Demo05_ClimbMotors.java
├── Demo06_FastMotor.java
├── Demo07_BlockServo.java
├── Demo08_RightManual.java
├── Demo09_RightEncoder.java
├── Demo10_RightPID.java
├── Demo11_RightDeadzone.java
├── Demo12_RightProtection.java ⭐
├── Demo13_L2Mode.java
├── Demo14_DualGamepad.java
└── Demo15_FullControl.java ⭐
```

**Total**: 15 demonstration programs
**Lines of Code**: ~2800 lines
**Difficulty**: Beginner → Intermediate → Advanced
**Language**: All English
**Source Files**: v3.java + PIDController.java

---

*Extracted from v3.java, designed for FTC robot control teaching* 🤖
