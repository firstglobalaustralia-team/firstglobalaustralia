package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Demo 15: Complete Robot Control (from v3.java)
 *
 * Knowledge Point: Full integration of ALL features from v3.java
 *
 * This demo is the COMPLETE v3.java control system, including:
 * - Tank drive with speed toggle (X button)
 * - Flywheel 3-state control (L1)
 * - Intake hold controls (R1/R2)
 * - Climb motors (DPad Up/Down)
 * - Right motor with SMART PROTECTION (3s timeout, dead zone, adaptive target)
 * - L2 special mode (asymmetric drive)
 * - Fast motor control (gamepad 2)
 * - Block servo control (gamepad 2)
 * - Dual gamepad support
 *
 * This is v3.java as a teaching demo - all features from previous 14 demos combined!
 *
 * Hardware Required: ALL motors and servos
 *
 * Controls: See v3.java documentation (lines 79-101)
 */
@TeleOp(name="Demo15: Full Control ⭐", group="Demo")
public class Demo15_FullControl extends LinearOpMode {

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

    private DcMotor bl, br, fly, intake, climbleft, climbright, fast;
    private DcMotorEx right;
    private CRServo block;

    // Right motor PID
    private PIDController rightPIDController = new PIDController(0.1, 0, 0.001);
    private int rightTargetPosition = 0;
    private boolean rightPIDEnabled = false;
    private double rightPIDOutput = 0;
    private ElapsedTime rightPIDTimer = new ElapsedTime();
    private boolean rightPIDTimeout = false;

    // Left wheel hold for L2 mode
    private PIDController blPIDController = new PIDController(0.1, 0, 0.001);
    private int blTargetPosition = 0;
    private boolean blHoldEnabled = false;

    // Speed configuration (from v3.java lines 39-56)
    double flyPowerLow = 0.8;
    double flyPowerHigh = 1.0;
    double intakePowerForward = 0.8;
    double intakePowerReverse = 1.0;
    double rightMotorPower = 0.5;
    double climbPowerUp = 1.0;
    double climbPowerDown = -1.0;
    double fastPowerFull = 1.0;

    double driveSpeedScale = 1.0;
    boolean lastXState = false;

    // Flywheel state
    int flyState = 0;
    boolean lastL1FlyState = false;

    @Override
    public void runOpMode() {
        // Initialize all hardware (from v3.java lines 107-136)
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fly = hardwareMap.get(DcMotor.class, "fly");
        intake = hardwareMap.get(DcMotor.class, "intake");
        climbleft = hardwareMap.get(DcMotor.class, "climbleft");
        climbright = hardwareMap.get(DcMotor.class, "climbright");
        right = hardwareMap.get(DcMotorEx.class, "right");
        fast = hardwareMap.get(DcMotor.class, "fast");
        block = hardwareMap.get(CRServo.class, "block");

        bl.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.FORWARD);
        fly.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
        climbleft.setDirection(DcMotor.Direction.FORWARD);
        climbright.setDirection(DcMotor.Direction.FORWARD);
        right.setDirection(DcMotorEx.Direction.FORWARD);
        fast.setDirection(DcMotor.Direction.FORWARD);

        right.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        fast.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        right.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "⭐ Full v3.java Control");
        telemetry.addData("Info", "All 14 demos combined!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Tank drive (from v3.java lines 155-171)
            double leftPower = -gamepad1.left_stick_y;
            double rightPower = -gamepad1.right_stick_y;

            // Speed toggle X button (from v3.java lines 163-167)
            boolean currentXState = gamepad1.x;
            if (currentXState && !lastXState) {
                driveSpeedScale = (driveSpeedScale == 1.0) ? 0.5 : 1.0;
            }
            lastXState = currentXState;

            bl.setPower(leftPower * driveSpeedScale);
            br.setPower(rightPower * driveSpeedScale);

            // L2 special mode (from v3.java lines 173-204)
            boolean l2HoldPressed = gamepad1.left_trigger > 0.5;
            if (l2HoldPressed) {
                if (!blHoldEnabled) {
                    blTargetPosition = bl.getCurrentPosition();
                    blHoldEnabled = true;
                    blPIDController.resetIntegral();
                }

                int blCurrentPosition = bl.getCurrentPosition();
                double blError = blTargetPosition - blCurrentPosition;
                double raw = blPIDController.update(blTargetPosition, blCurrentPosition);

                double blPower;
                if (Math.abs(blError) > 5) {
                    blPower = Math.max(-1, Math.min(1, raw));
                } else if (Math.abs(blError) > 0) {
                    blPower = blError > 0 ? 0.1 : -0.1;
                } else {
                    blPower = 0;
                }

                bl.setPower(blPower);
                br.setPower(-0.7);
            } else {
                blHoldEnabled = false;
            }

            // Intake (from v3.java lines 207-216)
            boolean r1Pressed = gamepad1.right_bumper;
            boolean r2Pressed = gamepad1.right_trigger > 0.5;

            if (r1Pressed && !r2Pressed) {
                intake.setPower(intakePowerForward);
            } else if (r2Pressed && !r1Pressed) {
                intake.setPower(-intakePowerReverse);
            } else {
                intake.setPower(0);
            }

            // Flywheel L1 (from v3.java lines 220-238)
            boolean currentL1FlyState = gamepad1.left_bumper;
            if (currentL1FlyState && !lastL1FlyState) {
                flyState = (flyState + 1) % 3;
            }
            lastL1FlyState = currentL1FlyState;

            switch (flyState) {
                case 0: fly.setPower(0); break;
                case 1: fly.setPower(flyPowerLow); break;
                case 2: fly.setPower(flyPowerHigh); break;
            }

            // Gamepad 2 fast motor (from v3.java lines 241-249)
            boolean a2Pressed = gamepad2.a;
            boolean b2Pressed = gamepad2.b;
            if (a2Pressed && !b2Pressed) {
                fast.setPower(fastPowerFull);
            } else if (b2Pressed && !a2Pressed) {
                fast.setPower(-fastPowerFull);
            } else {
                fast.setPower(0);
            }

            // Gamepad 2 block servo (from v3.java lines 252-260)
            double blockPower = 0.0;
            if (gamepad2.x && !gamepad2.y) {
                blockPower = 1.0;
            } else if (gamepad2.y && !gamepad2.x) {
                blockPower = -1.0;
            }
            block.setPower(blockPower);

            // Climb motors (from v3.java lines 263-281)
            boolean dpadUp = gamepad1.dpad_up;
            boolean dpadDown = gamepad1.dpad_down;

            double climbPower = 0.0;
            if (dpadUp && !dpadDown) {
                climbPower = climbPowerUp;
            } else if (dpadDown && !dpadUp) {
                climbPower = climbPowerDown;
            }
            climbleft.setPower(climbPower);
            climbright.setPower(climbPower);

            // Right motor with SMART PROTECTION (from v3.java lines 283-359)
            boolean dpadLeft = gamepad2.dpad_left;
            boolean dpadRight = gamepad2.dpad_right;

            if (dpadLeft && !dpadRight) {
                rightPIDEnabled = false;
                rightPIDTimeout = false;
                right.setPower(-rightMotorPower);
            } else if (dpadRight && !dpadLeft) {
                rightPIDEnabled = false;
                rightPIDTimeout = false;
                right.setPower(rightMotorPower);
            } else {
                if (!rightPIDEnabled) {
                    rightTargetPosition = right.getCurrentPosition();
                    rightPIDEnabled = true;
                    rightPIDTimeout = false;
                    rightPIDController.resetIntegral();
                    rightPIDTimer.reset();
                }

                int currentPosition = right.getCurrentPosition();
                double error = rightTargetPosition - currentPosition;

                if (!rightPIDTimeout && rightPIDTimer.seconds() > 3.0) {
                    rightTargetPosition = currentPosition;
                    rightPIDTimeout = true;
                    rightPIDController.resetIntegral();
                    rightPIDTimer.reset();
                }

                double power = 0;

                if (rightPIDTimeout) {
                    if (Math.abs(error) > 3) {
                        rightPIDTimeout = false;
                        rightPIDController.resetIntegral();
                        rightPIDTimer.reset();
                    } else {
                        power = 0;
                    }
                }

                if (!rightPIDTimeout) {
                    power = rightPIDController.update(rightTargetPosition, currentPosition);

                    if (Math.abs(error) > 5) {
                        power = Math.max(-1, Math.min(1, power));
                    } else if (Math.abs(error) > 0) {
                        power = error > 0 ? 0.1 : -0.1;
                    } else {
                        power = 0;
                    }
                }

                rightPIDOutput = power;
                right.setPower(power);
            }

            // Compact telemetry
            telemetry.addData("=== FULL v3.java CONTROL ===", "");
            telemetry.addData("Speed", (int)(driveSpeedScale * 100) + "%");
            telemetry.addData("Fly", flyState == 0 ? "OFF" : (flyState == 1 ? "80%" : "100%"));
            telemetry.addData("L2 Mode", l2HoldPressed ? "ACTIVE" : "Off");
            telemetry.addData("Right PID", rightPIDEnabled ? (rightPIDTimeout ? "TIMEOUT" : "HOLD") : "Manual");
            telemetry.addData("", "");
            telemetry.addData("This is v3.java", "All features combined!");
            telemetry.addData("Learned from", "Demo01-14");
            telemetry.update();
        }
    }
}
