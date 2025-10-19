package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Demo 06: Fast Motor Control (Gamepad 2)
 *
 * Knowledge Point: Dual gamepad control and hold-to-run pattern
 *
 * This demo demonstrates:
 * - Gamepad 2 (operator) control
 * - A/B button hold pattern
 * - Full speed forward/reverse control
 * - Priority logic (prevent simultaneous press)
 *
 * Hardware Required:
 * - fast (DC motor controlled by gamepad 2)
 *
 * Controls (GAMEPAD 2):
 * - A Button: Hold for FORWARD at full speed (1.0)
 * - B Button: Hold for REVERSE at full speed (-1.0)
 * - Release: Motor STOPS
 *
 * How to Demonstrate:
 * 1. Connect TWO gamepads (or use gamepad 2)
 * 2. Press INIT and START
 * 3. Hold A on gamepad2 - motor runs forward at full speed
 * 4. Release A - motor stops
 * 5. Hold B on gamepad2 - motor runs reverse at full speed
 * 6. Try pressing A+B together - priority logic prevents conflict
 */
@TeleOp(name="Demo06: Fast Motor", group="Demo")
public class Demo06_FastMotor extends LinearOpMode {

    private DcMotor fast;

    @Override
    public void runOpMode() {
        // Initialize fast motor
        fast = hardwareMap.get(DcMotor.class, "fast");

        // Set motor direction and brake behavior (from v3.java)
        fast.setDirection(DcMotor.Direction.FORWARD);
        fast.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "Gamepad 2 hold-to-run control");
        telemetry.addData("Tip", "Use GAMEPAD 2 (operator)");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Gamepad 2 A/B control (from v3.java lines 240-249)
            boolean a2Pressed = gamepad2.a;
            boolean b2Pressed = gamepad2.b;

            double fastPower;

            if (a2Pressed && !b2Pressed) {
                // A pressed: forward at full speed
                fastPower = 1.0;
            } else if (b2Pressed && !a2Pressed) {
                // B pressed: reverse at full speed
                fastPower = -1.0;
            } else {
                // Neither or both pressed: stop
                fastPower = 0.0;
            }

            fast.setPower(fastPower);

            // Display status
            String status;
            if (a2Pressed && !b2Pressed) {
                status = "FORWARD (Full Speed)";
            } else if (b2Pressed && !a2Pressed) {
                status = "REVERSE (Full Speed)";
            } else if (a2Pressed && b2Pressed) {
                status = "STOPPED (Both pressed)";
            } else {
                status = "STOPPED";
            }

            telemetry.addData("=== FAST MOTOR ===", "");
            telemetry.addData("Status", status);
            telemetry.addData("Power", "%.2f", fastPower);
            telemetry.addData("", "");
            telemetry.addData("Gamepad 2 A", a2Pressed ? "PRESSED" : "Released");
            telemetry.addData("Gamepad 2 B", b2Pressed ? "PRESSED" : "Released");
            telemetry.addData("", "");
            telemetry.addData("Controls (GP2)", "A: Forward | B: Reverse");
            telemetry.addData("Note", "Hold to run, release to stop");
            telemetry.update();
        }
    }
}
