package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Demo 11: Right Motor with Dead Zone
 *
 * Knowledge Point: Dead zone processing for precise position control
 *
 * This demo demonstrates:
 * - Dead zone (dead band) optimization
 * - Three-tier error handling:
 *   * Error > 5: Full PID output
 *   * Error 1-5: Minimum power (±0.1)
 *   * Error = 0: Stop
 * - Why dead zone improves stability
 * - Prevents oscillation near target
 *
 * Hardware Required:
 * - right (DcMotorEx motor with encoder)
 *
 * Controls (GAMEPAD 2):
 * - DPad Left/Right: Manual control
 * - Release: PID hold with dead zone processing
 *
 * How to Demonstrate:
 * 1. Hold DPad Right to move forward
 * 2. Release - observe three zones:
 *    - Far from target (error > 5): Full PID, motor moves fast
 *    - Near target (error 1-5): Minimum power 0.1, gentle approach
 *    - At target (error = 0): Stop completely
 * 3. Compare to Demo10: smoother, no jitter at target
 * 4. Dead zone prevents motor "hunting" around target
 */
@TeleOp(name="Demo11: Right Dead Zone", group="Demo")
public class Demo11_RightDeadzone extends LinearOpMode {

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
        telemetry.addData("Knowledge Point", "Dead zone optimization");
        telemetry.addData("Tip", "Watch error zones change");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            boolean dpadLeft = gamepad2.dpad_left;
            boolean dpadRight = gamepad2.dpad_right;

            int currentPosition = right.getCurrentPosition();
            int error = targetPosition - currentPosition;

            if (dpadLeft && !dpadRight) {
                pidEnabled = false;
                right.setPower(-0.5);

            } else if (dpadRight && !dpadLeft) {
                pidEnabled = false;
                right.setPower(0.5);

            } else {
                if (!pidEnabled) {
                    targetPosition = currentPosition;
                    pidEnabled = true;
                    pidController.resetIntegral();
                }

                // Dead zone processing (from v3.java lines 343-351)
                double power;

                if (Math.abs(error) > 5) {
                    // Large error: use full PID output
                    double rawPID = pidController.update(targetPosition, currentPosition);
                    power = Math.max(-1, Math.min(1, rawPID));
                } else if (Math.abs(error) > 0) {
                    // Small error (1-5): use minimum power for gentle approach
                    power = error > 0 ? 0.1 : -0.1;
                } else {
                    // At target: stop completely
                    power = 0;
                }

                right.setPower(power);
            }

            // Determine current zone
            String zone;
            String zoneExplain;

            if (Math.abs(error) > 5) {
                zone = "ZONE 1: Large Error";
                zoneExplain = "Full PID (fast movement)";
            } else if (Math.abs(error) > 0) {
                zone = "ZONE 2: Small Error";
                zoneExplain = "Minimum power ±0.1 (gentle)";
            } else {
                zone = "ZONE 3: At Target";
                zoneExplain = "Motor stopped (0.0)";
            }

            // Display information
            telemetry.addData("=== DEAD ZONE CONTROL ===", "");
            telemetry.addData("Current Zone", zone);
            telemetry.addData("Explanation", zoneExplain);
            telemetry.addData("", "");
            telemetry.addData("Current Position", "%d ticks", currentPosition);
            telemetry.addData("Target Position", "%d ticks", targetPosition);
            telemetry.addData("Error", "%d ticks", error);
            telemetry.addData("Motor Power", "%.2f", right.getPower());
            telemetry.addData("", "");
            telemetry.addData("Dead Zone Logic", "");
            telemetry.addData("  |Error| > 5", "Full PID output");
            telemetry.addData("  |Error| 1-5", "Min power ±0.1");
            telemetry.addData("  |Error| = 0", "Stop (0.0)");
            telemetry.addData("", "");
            telemetry.addData("Benefit", "Prevents oscillation at target");
            telemetry.addData("Controls (GP2)", "DPad: Manual | Release: PID");
            telemetry.addData("Next Demo", "Demo12 adds timeout protection!");
            telemetry.update();
        }
    }
}
