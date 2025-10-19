/**
 * ------------------------------------------------------------
 *  FGC Team Australia 2025  |  FIRST Global Challenge
 * ------------------------------------------------------------
 *  Project: FGC101 - Java Demo Series
 *  File:    Demo12_RightProtection.java
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
 * Demo 12: Right Motor Smart Protection (Complete)
 *
 * Knowledge Point: Timeout + Adaptive Target = Motor Protection
 *
 * This demo demonstrates the COMPLETE smart protection system:
 * 1. Manual control → PID hold (from Demo10)
 * 2. Dead zone optimization (from Demo11)
 * 3. ⭐ NEW: 3-second timeout detection
 * 4. ⭐ NEW: Adaptive target (gives up unreachable target)
 * 5. ⭐ NEW: Automatic recovery after timeout
 *
 * Why This Matters:
 * - Prevents motor burnout from blocked/stuck situations
 * - Adapts to physical limitations automatically
 * - Real-world robust control system
 *
 * Hardware Required:
 * - right (DcMotorEx motor with encoder)
 *
 * Controls (GAMEPAD 2):
 * - DPad Left/Right: Manual control
 * - Release: Smart PID hold with protection
 *
 * How to Demonstrate:
 * 1. Hold DPad Right to move motor
 * 2. While motor is moving, BLOCK it with your hand
 * 3. Watch PID timer count up: 0s → 1s → 2s → 3s
 * 4. At 3 seconds: "⚠ TIMEOUT! Adaptive target activated"
 * 5. Motor gives up original target, accepts current position
 * 6. Remove hand - motor stays at new position (protected!)
 * 7. This prevents motor from fighting obstacles indefinitely
 */
@TeleOp(name="Demo12: Right Protection ⭐", group="Demo")
public class Demo12_RightProtection extends LinearOpMode {

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

    private DcMotorEx right;
    private PIDController pidController;
    private int targetPosition = 0;
    private boolean pidEnabled = false;
    private boolean pidTimeout = false;
    private ElapsedTime pidTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Initialize motor
        right = hardwareMap.get(DcMotorEx.class, "right");
        right.setDirection(DcMotorEx.Direction.FORWARD);
        right.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        right.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        pidController = new PIDController(0.1, 0, 0.001);

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "⭐ Smart motor protection");
        telemetry.addData("Tip", "Block motor to trigger timeout!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            boolean dpadLeft = gamepad2.dpad_left;
            boolean dpadRight = gamepad2.dpad_right;

            int currentPosition = right.getCurrentPosition();
            int error = targetPosition - currentPosition;

            if (dpadLeft && !dpadRight) {
                // Manual control: disable PID and timeout (from v3.java lines 288-291)
                pidEnabled = false;
                pidTimeout = false;
                right.setPower(-0.5);

            } else if (dpadRight && !dpadLeft) {
                // Manual control: disable PID and timeout (from v3.java lines 293-296)
                pidEnabled = false;
                pidTimeout = false;
                right.setPower(0.5);

            } else {
                // Released: enable smart PID with protection
                if (!pidEnabled) {
                    // First time: record target and reset (from v3.java lines 300-307)
                    targetPosition = currentPosition;
                    pidEnabled = true;
                    pidTimeout = false;
                    pidController.resetIntegral();
                    pidTimer.reset();
                }

                // ⭐ Timeout detection (from v3.java lines 313-320)
                if (!pidTimeout && pidTimer.seconds() > 3.0) {
                    // 3 seconds passed, can't reach target → ADAPTIVE TARGET
                    targetPosition = currentPosition;  // Give up, accept current position
                    pidTimeout = true;
                    pidController.resetIntegral();
                    pidTimer.reset();
                }

                double power = 0;

                // ⭐ Timeout recovery logic (from v3.java lines 325-337)
                if (pidTimeout) {
                    // After timeout, only move if error becomes large again
                    if (Math.abs(error) > 3) {
                        // Re-enable PID if pushed away from timeout position
                        pidTimeout = false;
                        pidController.resetIntegral();
                        pidTimer.reset();
                    } else {
                        // Stay stopped (mission accomplished)
                        power = 0;
                    }
                }

                // Normal PID with dead zone (from v3.java lines 340-352)
                if (!pidTimeout) {
                    if (Math.abs(error) > 5) {
                        double rawPID = pidController.update(targetPosition, currentPosition);
                        power = Math.max(-1, Math.min(1, rawPID));
                    } else if (Math.abs(error) > 0) {
                        power = error > 0 ? 0.1 : -0.1;
                    } else {
                        power = 0;
                    }
                }

                right.setPower(power);
            }

            // Display complete status
            telemetry.addData("=== SMART PROTECTION ===", "");

            if (pidEnabled) {
                telemetry.addData("PID Status", pidTimeout ? "⚠ TIMEOUT" : "Active");
                telemetry.addData("PID Timer", "%.1fs / 3.0s", pidTimer.seconds());
            } else {
                telemetry.addData("PID Status", "Manual Control");
                telemetry.addData("PID Timer", "N/A");
            }

            telemetry.addData("", "");
            telemetry.addData("Current Position", "%d ticks", currentPosition);
            telemetry.addData("Target Position", "%d ticks", targetPosition);
            telemetry.addData("Error", "%d ticks", error);
            telemetry.addData("Motor Power", "%.2f", right.getPower());
            telemetry.addData("", "");

            if (pidTimeout) {
                telemetry.addData("⚠ PROTECTION ACTIVE", "");
                telemetry.addData("Action Taken", "Target adapted to current position");
                telemetry.addData("Reason", "Could not reach original target in 3s");
                telemetry.addData("Result", "Motor protected from burnout!");
            } else if (pidEnabled && pidTimer.seconds() > 2.0) {
                telemetry.addData("⚠ WARNING", "Approaching timeout...");
                telemetry.addData("Tip", "Block motor to trigger protection");
            }

            telemetry.addData("", "");
            telemetry.addData("Protection Features", "");
            telemetry.addData("  ✓ Dead zone", "Prevents oscillation");
            telemetry.addData("  ✓ 3s timeout", "Detects stuck motor");
            telemetry.addData("  ✓ Adaptive target", "Gives up unreachable goals");
            telemetry.addData("  ✓ Auto recovery", "Resumes if error > 3");
            telemetry.addData("", "");
            telemetry.addData("Controls (GP2)", "DPad: Manual | Release: Protected PID");
            telemetry.addData("Next Demo", "Demo13 shows L2 special mode");
            telemetry.update();
        }
    }
}

