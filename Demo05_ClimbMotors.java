/**
 * ------------------------------------------------------------
 *  FGC Team Australia 2025  |  FIRST Global Challenge
 * ------------------------------------------------------------
 *  Project: FGC101 - Java Demo Series
 *  File:    Demo05_ClimbMotors.java
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
 * Demo 05: Climb Motors Control
 *
 * Knowledge Point: Independent control of two climb motors with DPad
 *
 * This demo demonstrates:
 * - DPad Up/Down for simultaneous control
 * - Separate motor power settings
 * - Hold-to-run button pattern
 *
 * Hardware Required:
 * - climbleft (left climb motor)
 * - climbright (right climb motor)
 *
 * Controls:
 * - DPad Up: Both motors climb UP (power: 1.0)
 * - DPad Down: Both motors climb DOWN (power: -1.0)
 * - Release: Motors STOP
 *
 * How to Demonstrate:
 * 1. Press INIT and START
 * 2. Press and HOLD DPad Up - motors run upward
 * 3. Release - motors stop immediately
 * 4. Press and HOLD DPad Down - motors run downward
 * 5. Observe: Simple hold-to-run pattern, no toggle
 */
@TeleOp(name="Demo05: Climb Motors", group="Demo")
public class Demo05_ClimbMotors extends LinearOpMode {

    private DcMotor climbleft, climbright;

    @Override
    public void runOpMode() {
        // Initialize climb motors
        climbleft = hardwareMap.get(DcMotor.class, "climbleft");
        climbright = hardwareMap.get(DcMotor.class, "climbright");

        // Set motor directions (from v3.java)
        climbleft.setDirection(DcMotor.Direction.FORWARD);
        climbright.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "Climb motor DPad control");
        telemetry.addData("Tip", "Hold DPad Up/Down to climb");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double climbLeftPower = 0.0;
            double climbRightPower = 0.0;

            // DPad control (from v3.java lines 263-278)
            boolean dpadUp = gamepad1.dpad_up;
            boolean dpadDown = gamepad1.dpad_down;

            if (dpadUp && !dpadDown) {
                // DPad Up: climb upward (positive power)
                climbLeftPower = 1.0;
                climbRightPower = 1.0;
            } else if (dpadDown && !dpadUp) {
                // DPad Down: climb downward (negative power)
                climbLeftPower = -1.0;
                climbRightPower = -1.0;
            } else {
                // Released or both pressed: stop
                climbLeftPower = 0.0;
                climbRightPower = 0.0;
            }

            // Apply power to motors
            climbleft.setPower(climbLeftPower);
            climbright.setPower(climbRightPower);

            // Display status
            String status;
            if (dpadUp && !dpadDown) {
                status = "CLIMBING UP";
            } else if (dpadDown && !dpadUp) {
                status = "CLIMBING DOWN";
            } else {
                status = "STOPPED";
            }

            telemetry.addData("=== CLIMB STATUS ===", "");
            telemetry.addData("Status", status);
            telemetry.addData("Left Power", "%.2f", climbLeftPower);
            telemetry.addData("Right Power", "%.2f", climbRightPower);
            telemetry.addData("", "");
            telemetry.addData("Controls", "DPad Up: Climb | DPad Down: Descend");
            telemetry.addData("Note", "Hold to run, release to stop");
            telemetry.update();
        }
    }
}

