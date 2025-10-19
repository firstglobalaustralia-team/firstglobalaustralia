/**
 * ------------------------------------------------------------
 *  FGC Team Australia 2025  |  FIRST Global Challenge
 * ------------------------------------------------------------
 *  Project: FGC101 - Java Demo Series
 *  File:    Demo07_BlockServo.java
 *
 *  Description:
 *  This demo is part of the FGC101 video series created by
 *  FGC Team Australia 2025. Our goal is to help rookie teams
 *  understand programming logic and competition strategy
 *  through short, simple examples.
 *
 *  Playlist: https://www.youtube.com/playlist?list=PL-29lId0PJrVEo0yJyprGheyRtR-ReDET
 *  Author:   Muyao Zhang (from FGC Team Australia)
 *  Date:     October 2025
 *
 *  Notes:
 *  - We currently provide around 15 demo programs.
 *  - These Java demos are not continuously updated.
 *  - For detailed explanations, please refer to the FGC101 videos.
 *
 *  © FGC Team Australia 2025 | Educational Use Only
 * ------------------------------------------------------------
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Demo 07: Block Continuous Rotation Servo
 *
 * Knowledge Point: CRServo control (continuous rotation servo)
 *
 * This demo demonstrates:
 * - CRServo vs regular Servo
 * - Continuous rotation control
 * - X/Y button hold pattern
 * - Power-based control (like DC motor)
 *
 * Hardware Required:
 * - block (CRServo - continuous rotation servo)
 *
 * Controls (GAMEPAD 2):
 * - X Button: Hold for FORWARD rotation (power: 1.0)
 * - Y Button: Hold for REVERSE rotation (power: -1.0)
 * - Release: Servo STOPS
 *
 * How to Demonstrate:
 * 1. Connect gamepad 2
 * 2. Press INIT and START
 * 3. Hold X on gamepad2 - servo rotates forward continuously
 * 4. Release X - servo stops
 * 5. Hold Y on gamepad2 - servo rotates reverse continuously
 * 6. Observe: CRServo behaves like a motor, not position-based
 */
@TeleOp(name="Demo07: Block Servo", group="Demo")
public class Demo07_BlockServo extends LinearOpMode {

    private CRServo block;

    @Override
    public void runOpMode() {
        // Initialize CRServo
        block = hardwareMap.get(CRServo.class, "block");

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "CRServo continuous rotation");
        telemetry.addData("Tip", "Use GAMEPAD 2 X/Y");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Gamepad 2 X/Y control for CRServo (from v3.java lines 251-260)
            boolean x2Pressed = gamepad2.x;
            boolean y2Pressed = gamepad2.y;

            double blockPower;

            if (x2Pressed && !y2Pressed) {
                // X pressed: forward rotation
                blockPower = 1.0;
            } else if (y2Pressed && !x2Pressed) {
                // Y pressed: reverse rotation
                blockPower = -1.0;
            } else {
                // Neither or both pressed: stop
                blockPower = 0.0;
            }

            block.setPower(blockPower);

            // Display status
            String status;
            if (x2Pressed && !y2Pressed) {
                status = "ROTATING FORWARD";
            } else if (y2Pressed && !x2Pressed) {
                status = "ROTATING REVERSE";
            } else if (x2Pressed && y2Pressed) {
                status = "STOPPED (Both pressed)";
            } else {
                status = "STOPPED";
            }

            telemetry.addData("=== BLOCK SERVO (CRServo) ===", "");
            telemetry.addData("Status", status);
            telemetry.addData("Power", "%.2f", blockPower);
            telemetry.addData("", "");
            telemetry.addData("Gamepad 2 X", x2Pressed ? "PRESSED" : "Released");
            telemetry.addData("Gamepad 2 Y", y2Pressed ? "PRESSED" : "Released");
            telemetry.addData("", "");
            telemetry.addData("Difference", "CRServo rotates continuously");
            telemetry.addData("vs Regular Servo", "Regular servo moves to position (0-1)");
            telemetry.addData("", "");
            telemetry.addData("Controls (GP2)", "X: Forward | Y: Reverse");
            telemetry.addData("Note", "Hold to rotate, release to stop");
            telemetry.update();
        }
    }
}

