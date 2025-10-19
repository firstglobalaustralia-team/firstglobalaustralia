/**
 * ------------------------------------------------------------
 *  FGC Team Australia 2025  |  FIRST Global Challenge
 * ------------------------------------------------------------
 *  Project: FGC101 - Java Demo Series
 *  File:    Demo14_DualGamepad.java
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
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Demo 14: Dual Gamepad Control
 *
 * Knowledge Point: Two-operator control system (driver + operator)
 *
 * This demo demonstrates:
 * - Gamepad 1 (Driver): Drive + flywheel + intake + climb
 * - Gamepad 2 (Operator): Right motor + fast motor + block servo
 * - Division of responsibilities
 * - Multi-person coordination
 *
 * Hardware Required:
 * - bl, br (drive motors)
 * - fly, intake (collection system)
 * - climbleft, climbright (climb motors)
 * - right (operator motor)
 * - fast (operator motor)
 * - block (operator servo)
 *
 * Controls:
 * GAMEPAD 1 (Driver):
 * - Left/Right Stick Y: Tank drive
 * - L1: Flywheel cycle (0%→80%→100%)
 * - R1: Intake forward
 * - R2: Intake reverse
 * - DPad Up/Down: Climb up/down
 *
 * GAMEPAD 2 (Operator):
 * - DPad Left/Right: Right motor control
 * - A/B: Fast motor forward/reverse
 * - X/Y: Block servo forward/reverse
 *
 * How to Demonstrate:
 * 1. Connect TWO gamepads
 * 2. Driver uses gamepad 1 for driving and collection
 * 3. Operator uses gamepad 2 for mechanisms
 * 4. Shows realistic competition setup
 */
@TeleOp(name="Demo14: Dual Gamepad", group="Demo")
public class Demo14_DualGamepad extends LinearOpMode {

    private DcMotor bl, br, fly, intake, climbleft, climbright, fast;
    private DcMotorEx right;
    private CRServo block;

    // Flywheel state
    private int flyState = 0;
    private boolean lastL1 = false;

    @Override
    public void runOpMode() {
        // Initialize all hardware
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fly = hardwareMap.get(DcMotor.class, "fly");
        intake = hardwareMap.get(DcMotor.class, "intake");
        climbleft = hardwareMap.get(DcMotor.class, "climbleft");
        climbright = hardwareMap.get(DcMotor.class, "climbright");
        right = hardwareMap.get(DcMotorEx.class, "right");
        fast = hardwareMap.get(DcMotor.class, "fast");
        block = hardwareMap.get(CRServo.class, "block");

        // Set directions (from v3.java)
        bl.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.REVERSE);
        fly.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
        climbleft.setDirection(DcMotor.Direction.FORWARD);
        climbright.setDirection(DcMotor.Direction.FORWARD);
        right.setDirection(DcMotorEx.Direction.FORWARD);
        fast.setDirection(DcMotor.Direction.FORWARD);

        right.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        fast.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Ready - Dual Gamepad");
        telemetry.addData("GP1", "Driver (drive, fly, intake, climb)");
        telemetry.addData("GP2", "Operator (right, fast, block)");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // ═══ GAMEPAD 1: DRIVER ═══

            // Tank drive
            double leftPower = -gamepad1.left_stick_y;
            double rightPower = -gamepad1.right_stick_y;
            bl.setPower(leftPower);
            br.setPower(rightPower);

            // Flywheel L1 cycle (from v3.java lines 220-238)
            boolean currentL1 = gamepad1.left_bumper;
            if (currentL1 && !lastL1) {
                flyState = (flyState + 1) % 3;
            }
            lastL1 = currentL1;

            switch (flyState) {
                case 0: fly.setPower(0); break;
                case 1: fly.setPower(0.8); break;
                case 2: fly.setPower(1.0); break;
            }

            // Intake R1/R2 (from v3.java lines 207-216)
            boolean r1 = gamepad1.right_bumper;
            boolean r2 = gamepad1.right_trigger > 0.5;

            if (r1 && !r2) {
                intake.setPower(0.8);
            } else if (r2 && !r1) {
                intake.setPower(-1.0);
            } else {
                intake.setPower(0);
            }

            // Climb DPad Up/Down (from v3.java lines 263-281)
            boolean dpadUp = gamepad1.dpad_up;
            boolean dpadDown = gamepad1.dpad_down;

            double climbPower = 0;
            if (dpadUp && !dpadDown) climbPower = 1.0;
            else if (dpadDown && !dpadUp) climbPower = -1.0;

            climbleft.setPower(climbPower);
            climbright.setPower(climbPower);

            // ═══ GAMEPAD 2: OPERATOR ═══

            // Right motor DPad Left/Right (from v3.java lines 284-296)
            boolean dpadLeft2 = gamepad2.dpad_left;
            boolean dpadRight2 = gamepad2.dpad_right;

            if (dpadLeft2 && !dpadRight2) {
                right.setPower(-0.5);
            } else if (dpadRight2 && !dpadLeft2) {
                right.setPower(0.5);
            } else {
                right.setPower(0);
            }

            // Fast motor A/B (from v3.java lines 241-249)
            boolean a2 = gamepad2.a;
            boolean b2 = gamepad2.b;

            if (a2 && !b2) {
                fast.setPower(1.0);
            } else if (b2 && !a2) {
                fast.setPower(-1.0);
            } else {
                fast.setPower(0);
            }

            // Block servo X/Y (from v3.java lines 252-260)
            boolean x2 = gamepad2.x;
            boolean y2 = gamepad2.y;

            double blockPower = 0;
            if (x2 && !y2) blockPower = 1.0;
            else if (y2 && !x2) blockPower = -1.0;

            block.setPower(blockPower);

            // Display status
            telemetry.addData("=== GAMEPAD 1 (Driver) ===", "");
            telemetry.addData("Drive", "L:%.1f R:%.1f", leftPower, rightPower);
            telemetry.addData("Flywheel", flyState == 0 ? "OFF" : (flyState == 1 ? "80%" : "100%"));
            telemetry.addData("Intake", r1 ? "FWD" : (r2 ? "REV" : "OFF"));
            telemetry.addData("Climb", dpadUp ? "UP" : (dpadDown ? "DOWN" : "OFF"));
            telemetry.addData("", "");
            telemetry.addData("=== GAMEPAD 2 (Operator) ===", "");
            telemetry.addData("Right Motor", dpadLeft2 ? "REV" : (dpadRight2 ? "FWD" : "OFF"));
            telemetry.addData("Fast Motor", a2 ? "FWD" : (b2 ? "REV" : "OFF"));
            telemetry.addData("Block Servo", x2 ? "FWD" : (y2 ? "REV" : "OFF"));
            telemetry.addData("", "");
            telemetry.addData("Next Demo", "Demo15 combines everything!");
            telemetry.update();
        }
    }
}

