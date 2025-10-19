/**
 * ------------------------------------------------------------
 *  FGC Team Australia 2025  |  FIRST Global Challenge
 * ------------------------------------------------------------
 *  Project: FGC101 - Java Demo Series
 *  File:    Demo10_RightPID.java
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
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Demo 10: Right Motor PID Position Hold
 *
 * Knowledge Point: Basic PID position control (P + D only)
 *
 * This demo demonstrates:
 * - PID controller usage (Kp=0.1, Ki=0, Kd=0.001)
 * - Automatic position holding after manual control
 * - PID enable/disable switching
 * - resetIntegral() when re-enabling PID
 *
 * Hardware Required:
 * - right (DcMotorEx motor with encoder)
 *
 * Controls (GAMEPAD 2):
 * - DPad Left/Right: Manual control (disables PID)
 * - Release: PID automatically HOLDS current position
 *
 * How to Demonstrate:
 * 1. Press INIT and START
 * 2. Hold DPad Right - motor moves forward (PID disabled)
 * 3. Release - PID ENABLED, motor holds position
 * 4. Try to push motor manually - PID fights back!
 * 5. Observe telemetry: PID Output adjusts to maintain position
 * 6. Foundation for smart protection (next demos)
 */
@TeleOp(name="Demo10: Right PID Hold", group="Demo")
public class Demo10_RightPID extends LinearOpMode {

    // PID Controller class (from PIDController.java)
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

    private DcMotorEx right;
    private PIDController pidController;
    private int targetPosition = 0;
    private boolean pidEnabled = false;
    private double pidOutput = 0;

    @Override
    public void runOpMode() {
        // Initialize motor
        right = hardwareMap.get(DcMotorEx.class, "right");
        right.setDirection(DcMotorEx.Direction.FORWARD);
        right.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        right.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        // Initialize PID controller (from v3.java line 25: Kp=0.1, Ki=0, Kd=0.001)
        pidController = new PIDController(0.1, 0, 0.001);

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "PID position hold");
        telemetry.addData("Tip", "Release DPad to activate PID hold");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            boolean dpadLeft = gamepad2.dpad_left;
            boolean dpadRight = gamepad2.dpad_right;

            int currentPosition = right.getCurrentPosition();

            if (dpadLeft && !dpadRight) {
                // Manual control: disable PID (from v3.java lines 288-291)
                pidEnabled = false;
                right.setPower(-0.5);

            } else if (dpadRight && !dpadLeft) {
                // Manual control: disable PID (from v3.java lines 293-296)
                pidEnabled = false;
                right.setPower(0.5);

            } else {
                // Released: enable PID to hold position (from v3.java lines 298-307)
                if (!pidEnabled) {
                    // First time enabling: record target and reset PID
                    targetPosition = currentPosition;
                    pidEnabled = true;
                    pidController.resetIntegral();
                }

                // Use PID to hold position (from v3.java line 341)
                pidOutput = pidController.update(targetPosition, currentPosition);
                double power = Math.max(-1, Math.min(1, pidOutput));
                right.setPower(power);
            }

            int error = targetPosition - currentPosition;

            // Display information
            telemetry.addData("=== PID POSITION HOLD ===", "");
            telemetry.addData("PID Status", pidEnabled ? "ENABLED (Holding)" : "DISABLED (Manual)");
            telemetry.addData("", "");
            telemetry.addData("Current Position", "%d ticks", currentPosition);
            telemetry.addData("Target Position", "%d ticks", targetPosition);
            telemetry.addData("Error", "%d ticks", error);
            telemetry.addData("", "");

            if (pidEnabled) {
                telemetry.addData("PID Output", "%.3f", pidOutput);
                telemetry.addData("Motor Power", "%.3f", Math.max(-1, Math.min(1, pidOutput)));
                telemetry.addData("", "");
                telemetry.addData("Try This", "Push motor manually - PID fights back!");
            } else {
                telemetry.addData("Mode", "Manual Control");
            }

            telemetry.addData("", "");
            telemetry.addData("PID Params", "Kp=0.1, Ki=0, Kd=0.001");
            telemetry.addData("Controls (GP2)", "DPad: Manual | Release: PID Hold");
            telemetry.addData("Next Demo", "Demo11 adds dead zone optimization");
            telemetry.update();
        }
    }
}

