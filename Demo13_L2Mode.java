/**
 * ------------------------------------------------------------
 *  FGC Team Australia 2025  |  FIRST Global Challenge
 * ------------------------------------------------------------
 *  Project: FGC101 - Java Demo Series
 *  File:    Demo13_L2Mode.java
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
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Demo 13: L2 Special Mode
 *
 * Knowledge Point: Asymmetric drive - one wheel advances, other holds position
 *
 * This demo demonstrates:
 * - L2 trigger activates special mode
 * - Right wheel (br): Fixed 0.7 forward power
 * - Left wheel (bl): PID holds position (resists rotation)
 * - Result: Robot pivots around left wheel
 * - Practical use: Tight turns, precise positioning
 *
 * Hardware Required:
 * - bl (back left drive motor with encoder)
 * - br (back right drive motor)
 *
 * Controls:
 * - L2 (Left Trigger): Activate special mode
 *   → Right wheel: 0.7 forward
 *   → Left wheel: PID hold (locked)
 * - Release L2: Normal tank drive
 * - Left/Right Stick Y: Tank drive (when L2 not pressed)
 *
 * How to Demonstrate:
 * 1. Press INIT and START
 * 2. Use tank drive normally (left/right sticks)
 * 3. Press and HOLD L2 trigger
 * 4. Observe: Right wheel advances, left wheel locks
 * 5. Robot pivots around left wheel axis
 * 6. Release L2 - return to normal tank drive
 * 7. This is useful for precise positioning!
 */
@TeleOp(name="Demo13: L2 Special Mode", group="Demo")
public class Demo13_L2Mode extends LinearOpMode {

    // PID Controller class
    private class PIDController {
        private double Kp, Ki, Kd;
        private double integralSum = 0;
        private double lastError = 0;
        private ElapsedTime timer = new ElapsedTime();

        public PIDController(double Kp, double Ki, double Kd) {
            this.Kp = Kp;
            this.Ki = Ki;
            this.Kd = Kd;
        }

        public double update(double target, double current) {
            double error = target - current;
            double derivative = (error - lastError) / timer.seconds();
            integralSum += error * timer.seconds();
            double output = (Kp * error) + (Ki * integralSum) + (Kd * derivative);
            lastError = error;
            timer.reset();
            return output;
        }

        public void resetIntegral() {
            integralSum = 0;
            lastError = 0;
            timer.reset();
        }
    }

    private DcMotor bl, br;
    private PIDController blPIDController;
    private int blTargetPosition = 0;
    private boolean blHoldEnabled = false;

    @Override
    public void runOpMode() {
        // Initialize motors
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");

        // Set directions (from v3.java lines 125-126)
        bl.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.FORWARD);

        // Initialize encoder for bl
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Initialize PID for left wheel (from v3.java line 33)
        blPIDController = new PIDController(0.1, 0, 0.001);

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "L2 asymmetric drive mode");
        telemetry.addData("Tip", "Hold L2 to activate pivot mode");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Check L2 trigger (from v3.java line 174)
            boolean l2Pressed = gamepad1.left_trigger > 0.5;

            if (l2Pressed) {
                // ═══ L2 SPECIAL MODE ═══ (from v3.java lines 175-200)

                // Enable left wheel hold on first press
                if (!blHoldEnabled) {
                    blTargetPosition = bl.getCurrentPosition();
                    blHoldEnabled = true;
                    blPIDController.resetIntegral();
                }

                // Calculate PID for left wheel position hold
                int blCurrentPosition = bl.getCurrentPosition();
                double blError = blTargetPosition - blCurrentPosition;

                double blPower;
                double raw = blPIDController.update(blTargetPosition, blCurrentPosition);

                // Dead zone processing
                if (Math.abs(blError) > 5) {
                    blPower = Math.max(-1, Math.min(1, raw));
                } else if (Math.abs(blError) > 0) {
                    blPower = blError > 0 ? 0.1 : -0.1;
                } else {
                    blPower = 0;
                }

                // Apply L2 mode: left holds, right advances
                bl.setPower(blPower);
                br.setPower(-0.7);  // Note: direction corrected in v3.java

            } else {
                // ═══ NORMAL TANK DRIVE ═══
                blHoldEnabled = false;

                double leftPower = -gamepad1.left_stick_y;
                double rightPower = -gamepad1.right_stick_y;

                bl.setPower(leftPower);
                br.setPower(rightPower);
            }

            // Display status
            telemetry.addData("=== L2 SPECIAL MODE ===", "");
            telemetry.addData("L2 Status", l2Pressed ? "⚡ ACTIVE" : "Inactive");
            telemetry.addData("", "");

            if (l2Pressed) {
                telemetry.addData("Left Wheel", "PID HOLD (locked)");
                telemetry.addData("  Target Pos", "%d ticks", blTargetPosition);
                telemetry.addData("  Current Pos", "%d ticks", bl.getCurrentPosition());
                telemetry.addData("  Error", "%d ticks", blTargetPosition - bl.getCurrentPosition());
                telemetry.addData("", "");
                telemetry.addData("Right Wheel", "FORWARD 0.7");
                telemetry.addData("", "");
                telemetry.addData("Result", "Robot pivots around left wheel!");
            } else {
                telemetry.addData("Mode", "Normal Tank Drive");
                telemetry.addData("Left Power", "%.2f", bl.getPower());
                telemetry.addData("Right Power", "%.2f", br.getPower());
            }

            telemetry.addData("", "");
            telemetry.addData("Controls", "L2: Special Mode | Sticks: Tank Drive");
            telemetry.addData("Use Case", "Precise positioning, tight turns");
            telemetry.addData("Next Demo", "Demo14 shows dual gamepad");
            telemetry.update();
        }
    }
}

