/**
 * ------------------------------------------------------------
 *  FGC Team Australia 2025  |  FIRST Global Challenge
 * ------------------------------------------------------------
 *  Project: FGC101 - Java Demo Series
 *  File:    Demo04_IntakeHold.java
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
 * Demo 04: Intake Hold Control
 *
 * Knowledge Point: Toggle vs Hold button control patterns
 *
 * This demo demonstrates:
 * - R1 toggle: Turn reverse on/off with button press
 * - R2 hold: Active only while trigger is pressed
 * - Priority logic between two control modes
 *
 * Hardware Required:
 * - intake (intake motor)
 *
 * Controls:
 * - R1 (Right Bumper): Toggle reverse mode on/off
 * - R2 (Right Trigger): Forward (hold to activate)
 */
@TeleOp(name="Demo04: Intake Hold", group="Demo")
public class Demo04_IntakeHold extends LinearOpMode {

    private DcMotor intake;
    private boolean reverseMode = false;
    private boolean lastR1 = false;

    @Override
    public void runOpMode() {
        // Initialize intake motor
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "Toggle vs Hold control");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // R1 toggles reverse mode
            boolean currentR1 = gamepad1.right_bumper;
            if (currentR1 && !lastR1) {
                reverseMode = !reverseMode;
            }
            lastR1 = currentR1;

            // R2 triggers forward (priority over reverse)
            double intakePower;
            String mode;

            if (gamepad1.right_trigger > 0.5) {
                intakePower = 1.0;
                mode = "FORWARD (R2 held)";
            } else if (reverseMode) {
                intakePower = -1.0;
                mode = "REVERSE (R1 toggled)";
            } else {
                intakePower = 0.0;
                mode = "OFF";
            }

            intake.setPower(intakePower);

            // Display status
            telemetry.addData("Intake Mode", mode);
            telemetry.addData("Reverse Toggle", reverseMode ? "ON" : "OFF");
            telemetry.addData("Power", "%.2f", intakePower);
            telemetry.addData("", "");
            telemetry.addData("Controls", "R1: Toggle | R2: Hold Forward");
            telemetry.update();
        }
    }
}

