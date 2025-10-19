package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

/**
 * Demo 03: Flywheel 3-State Control
 *
 * Knowledge Point: State machine for motor speed cycling
 *
 * This demo demonstrates:
 * - State cycling through multiple speeds (0% → 80% → 100%)
 * - DcMotorEx for velocity control
 * - Edge-triggered button detection
 *
 * Hardware Required:
 * - fly (flywheel motor, must be DcMotorEx)
 *
 * Controls:
 * - L1 (Left Bumper): Cycle through states (0% → 80% → 100% → 0%)
 */
@TeleOp(name="Demo03: Flywheel States", group="Demo")
public class Demo03_FlywheelStates extends LinearOpMode {

    private DcMotorEx fly;
    private int flyState = 0; // 0=OFF, 1=80%, 2=100%
    private boolean lastL1 = false;

    @Override
    public void runOpMode() {
        // Initialize flywheel motor
        fly = hardwareMap.get(DcMotorEx.class, "fly");
        fly.setDirection(DcMotorEx.Direction.FORWARD);

        telemetry.addData("Status", "Ready");
        telemetry.addData("Knowledge Point", "State machine cycling");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // L1 cycles through states
            boolean currentL1 = gamepad1.left_bumper;
            if (currentL1 && !lastL1) {
                flyState = (flyState + 1) % 3;
            }
            lastL1 = currentL1;

            // Apply power based on state
            double flyPower = 0.0;
            String stateName = "";

            switch (flyState) {
                case 0:
                    flyPower = 0.0;
                    stateName = "OFF";
                    break;
                case 1:
                    flyPower = 0.8;
                    stateName = "80%";
                    break;
                case 2:
                    flyPower = 1.0;
                    stateName = "100%";
                    break;
            }

            fly.setPower(flyPower);

            // Display status
            telemetry.addData("Flywheel State", stateName);
            telemetry.addData("Power", "%.0f%%", flyPower * 100);
            telemetry.addData("Velocity", "%.0f ticks/sec", fly.getVelocity());
            telemetry.addData("", "");
            telemetry.addData("Controls", "L1: Cycle (0%→80%→100%)");
            telemetry.update();
        }
    }
}
