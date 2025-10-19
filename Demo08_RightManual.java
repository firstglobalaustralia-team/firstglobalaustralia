/**
 * ------------------------------------------------------------
 *  FGC Team Australia 2025  |  FIRST Global Challenge
 * ------------------------------------------------------------
 *  Project: FGC101 - Java Demo Series
 *  File:    Demo08_RightManual.java
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
import com.qualcomm.robotcore.hardware.DcMotorEx;

/**
 * Demo 08: Right Motor Manual Control
 *
 * Knowledge Point: Basic motor control with DPad Left/Right
 *
 * This demo demonstrates:
 * - DcMotorEx motor type
 * - DPad Left/Right control
 * - Forward/Reverse rotation
 * - Hold-to-run pattern
 * - BRAKE behavior when stopped
 *
 * Hardware Required:
 * - right (DcMotorEx motor with encoder)
 *
 * Controls (GAMEPAD 2):
 * - DPad Left: Hold for REVERSE rotation (power: -0.5)
 * - DPad Right: Hold for FORWARD rotation (power: 0.5)
 * - Release: Motor STOPS and BRAKES
 *
 * How to Demonstrate:
 * 1. Press INIT and START
 * 2. Hold DPad Right (gamepad2) - motor rotates forward
 * 3. Release - motor stops and holds position (BRAKE mode)
 * 4. Hold DPad Left - motor rotates reverse
 * 5. Observe: Simple manual control, foundation for next demos
 */
@TeleOp(name="Demo08: Right Manual", group="Demo")
public class Demo08_RightManual extends LinearOpMode {

    private DcMotorEx right;

    @Override
    public void runOpMode() {
        // Initialize right motor
        right = hardwareMap.get(DcMotorEx.class, "right");

        // Set motor direction and brake behavior (from v3.java lines 131-136)
        right.setDirection(DcMotorEx.Direction.FORWARD);
        right.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // Initialize encoder (required for future PID demos)
        right.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "Right motor manual control");
        telemetry.addData("Tip", "Use GAMEPAD 2 DPad Left/Right");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // DPad control for right motor (from v3.java lines 284-296)
            boolean dpadLeft = gamepad2.dpad_left;
            boolean dpadRight = gamepad2.dpad_right;

            double rightPower;
            String status;

            if (dpadLeft && !dpadRight) {
                // DPad Left: reverse rotation
                rightPower = -0.5;
                status = "REVERSE";
            } else if (dpadRight && !dpadLeft) {
                // DPad Right: forward rotation
                rightPower = 0.5;
                status = "FORWARD";
            } else {
                // Released or both pressed: stop and brake
                rightPower = 0.0;
                status = "STOPPED (BRAKE)";
            }

            right.setPower(rightPower);

            // Display status
            telemetry.addData("=== RIGHT MOTOR ===", "");
            telemetry.addData("Status", status);
            telemetry.addData("Power", "%.2f", rightPower);
            telemetry.addData("Current Position", "%d ticks", right.getCurrentPosition());
            telemetry.addData("", "");
            telemetry.addData("GP2 DPad Left", dpadLeft ? "PRESSED" : "Released");
            telemetry.addData("GP2 DPad Right", dpadRight ? "PRESSED" : "Released");
            telemetry.addData("", "");
            telemetry.addData("Brake Mode", "ENABLED (holds position when stopped)");
            telemetry.addData("", "");
            telemetry.addData("Controls (GP2)", "DPad Left: Reverse | DPad Right: Forward");
            telemetry.addData("Next Demo", "Demo09 will show encoder usage");
            telemetry.update();
        }
    }
}

