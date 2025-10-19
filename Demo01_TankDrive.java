/**
 * ------------------------------------------------------------
 *  FGC Team Australia 2025  |  FIRST Global Challenge
 * ------------------------------------------------------------
 *  Project: FGC101 - Java Demo Series
 *  File:    Demo01_TankDrive.java
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
 * Demo 01: Basic Tank Drive
 *
 * Knowledge Point: Dual joystick tank-style driving control
 *
 * This demo demonstrates:
 * - Independent left/right wheel control using dual joysticks
 * - Motor direction setup
 * - Basic gamepad input reading
 *
 * Hardware Required:
 * - bl (back left motor)
 * - br (back right motor)
 *
 * Controls:
 * - Left Stick Y: Control left wheel
 * - Right Stick Y: Control right wheel
 */
@TeleOp(name="Demo01: Tank Drive", group="Demo")
public class Demo01_TankDrive extends LinearOpMode {

    private DcMotor bl, br;

    @Override
    public void runOpMode() {
        // Initialize motors
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");

        bl.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "Tank drive with dual joysticks");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Tank drive control
            double leftPower = -gamepad1.left_stick_y;
            double rightPower = -gamepad1.right_stick_y;

            bl.setPower(leftPower);
            br.setPower(rightPower);

            // Display status
            telemetry.addData("Left Power", "%.2f", leftPower);
            telemetry.addData("Right Power", "%.2f", rightPower);
            telemetry.addData("", "");
            telemetry.addData("Controls", "Left/Right Stick Y");
            telemetry.update();
        }
    }
}

