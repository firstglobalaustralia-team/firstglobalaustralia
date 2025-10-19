/**
 * ------------------------------------------------------------
 *  FGC Team Australia 2025  |  FIRST Global Challenge
 * ------------------------------------------------------------
 *  Project: FGC101 - Java Demo Series
 *  File:    Demo02_SpeedToggle.java
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
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Demo 02: Speed Toggle Control
 *
 * Knowledge Point: Toggle between normal/fast speed modes
 *
 * This demo demonstrates:
 * - Speed multiplier toggle using button press
 * - Button state detection (edge trigger)
 * - Variable speed control
 *
 * Hardware Required:
 * - bl (back left motor)
 * - br (back right motor)
 *
 * Controls:
 * - Left Stick Y: Control left wheel
 * - Right Stick Y: Control right wheel
 * - X Button: Toggle between Normal (60%) and Fast (100%) speed
 */
@TeleOp(name="Demo02: Speed Toggle", group="Demo")
public class Demo02_SpeedToggle extends LinearOpMode {

    private DcMotor bl, br;
    private boolean fast = false;
    private boolean lastX = false;

    @Override
    public void runOpMode() {
        // Initialize motors
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");

        bl.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "Speed toggle control");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // X button toggles speed
            boolean currentX = gamepad1.x;
            if (currentX && !lastX) {
                fast = !fast;
            }
            lastX = currentX;

            // Apply speed multiplier
            double speedMultiplier = fast ? 1.0 : 0.6;
            double leftPower = -gamepad1.left_stick_y * speedMultiplier;
            double rightPower = -gamepad1.right_stick_y * speedMultiplier;

            bl.setPower(leftPower);
            br.setPower(rightPower);

            // Display status
            telemetry.addData("Speed Mode", fast ? "FAST (100%)" : "NORMAL (60%)");
            telemetry.addData("Left Power", "%.2f", leftPower);
            telemetry.addData("Right Power", "%.2f", rightPower);
            telemetry.addData("", "");
            telemetry.addData("Controls", "X: Toggle Speed");
            telemetry.update();
        }
    }
}

