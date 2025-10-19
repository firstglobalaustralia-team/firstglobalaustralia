/**
 * ------------------------------------------------------------
 *  FGC Team Australia 2025  |  FIRST Global Challenge
 * ------------------------------------------------------------
 *  Project: FGC101 - Java Demo Series
 *  File:    Demo09_RightEncoder.java
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
 * Demo 09: Right Motor Encoder Position
 *
 * Knowledge Point: Encoder position reading and target position recording
 *
 * This demo demonstrates:
 * - Reading encoder position (getCurrentPosition)
 * - Recording target position when button released
 * - Understanding encoder ticks
 * - Foundation for PID control
 *
 * Hardware Required:
 * - right (DcMotorEx motor with encoder)
 *
 * Controls (GAMEPAD 2):
 * - DPad Left: Reverse rotation
 * - DPad Right: Forward rotation
 * - Release: Motor stops and RECORDS current position as target
 *
 * How to Demonstrate:
 * 1. Press INIT and START (encoder resets to 0)
 * 2. Hold DPad Right - motor rotates forward
 * 3. Watch "Current Position" increase
 * 4. Release - "Target Position" is recorded
 * 5. Hold DPad Left - motor rotates back
 * 6. Observe: Target position stays at last release point
 * 7. This target will be used for PID in next demo!
 */
@TeleOp(name="Demo09: Right Encoder", group="Demo")
public class Demo09_RightEncoder extends LinearOpMode {

    private DcMotorEx right;
    private int targetPosition = 0;  // Records position when button released

    @Override
    public void runOpMode() {
        // Initialize right motor
        right = hardwareMap.get(DcMotorEx.class, "right");
        right.setDirection(DcMotorEx.Direction.FORWARD);
        right.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // Reset encoder to 0
        right.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "Encoder position tracking");
        telemetry.addData("Tip", "Watch target position update on release");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            boolean dpadLeft = gamepad2.dpad_left;
            boolean dpadRight = gamepad2.dpad_right;

            int currentPosition = right.getCurrentPosition();

            if (dpadLeft && !dpadRight) {
                // Manual control: reverse
                right.setPower(-0.5);
            } else if (dpadRight && !dpadLeft) {
                // Manual control: forward
                right.setPower(0.5);
            } else {
                // Released: stop and RECORD current position as target
                // (from v3.java lines 299-301)
                targetPosition = currentPosition;
                right.setPower(0.0);
            }

            // Calculate error (will be used in next demos)
            int error = targetPosition - currentPosition;

            // Display information
            telemetry.addData("=== ENCODER POSITION ===", "");
            telemetry.addData("Current Position", "%d ticks", currentPosition);
            telemetry.addData("Target Position", "%d ticks", targetPosition);
            telemetry.addData("Error", "%d ticks", error);
            telemetry.addData("", "");

            if (dpadLeft || dpadRight) {
                telemetry.addData("Status", "MANUAL CONTROL");
                telemetry.addData("Note", "Target will update on release");
            } else {
                telemetry.addData("Status", "STOPPED");
                telemetry.addData("Note", "Target recorded at " + targetPosition);
            }

            telemetry.addData("", "");
            telemetry.addData("Controls (GP2)", "DPad Left/Right to move");
            telemetry.addData("Next Demo", "Demo10 will use PID to hold target!");
            telemetry.update();
        }
    }
}

