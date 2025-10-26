package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Demo 17: Timer Vibration Alert
 *
 * Knowledge Point: Timer management, vibration alerts, time formatting
 *
 * This demo demonstrates:
 * - Simple timer that counts up from start
 * - Vibration alert at 75 seconds (1:15)
 * - Real-time timer display
 * - Reset functionality
 *
 * Hardware Required: None (uses only gamepad)
 *
 * Controls (Gamepad 2):
 * - Start Button: Reset timer
 *
 * How to Demonstrate:
 * 1. Press INIT and START
 * 2. Watch timer count up in real-time
 * 3. Wait for 1:15 - gamepad will vibrate as alert
 * 4. Press Start to reset timer and start new round
 */
@TeleOp(name="Demo17: Timer Vibration", group="Demo")
public class Demo17_TimerVibration extends LinearOpMode {

    // Timer
    private ElapsedTime gameTimer = new ElapsedTime();
    private boolean alertTriggered = false;

    // Alert threshold (1 minute 15 seconds = 75 seconds)
    private final double ALERT_TIME = 75.0;

    // Button state tracking (for edge detection)
    private boolean lastStartState = false;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Ready - Timer Vibration Alert");
        telemetry.addData("Controls", "Gamepad 2: Start to reset timer");
        telemetry.addData("Alert", "Vibration at 1:15");
        telemetry.update();

        waitForStart();
        gameTimer.reset();

        while (opModeIsActive()) {
            // Read gamepad 2 start button state
            boolean currentStartState = gamepad2.start;

            // Start button: Reset timer (edge detection)
            if (currentStartState && !lastStartState) {
                resetTimer();
            }
            lastStartState = currentStartState;

            // Timer alert at 1:15
            if (!alertTriggered && gameTimer.seconds() >= ALERT_TIME) {
                triggerAlert();
            }

            // Display telemetry
            displayStatus();
        }
    }

    /**
     * Reset timer and alert state
     */
    private void resetTimer() {
        gameTimer.reset();
        alertTriggered = false;
        telemetry.speak("Timer reset");
    }

    /**
     * Trigger vibration alert at 1:15
     */
    private void triggerAlert() {
        alertTriggered = true;

        // Vibrate gamepad 2 for 500ms
        gamepad2.rumble(500);

        // Optional: also vibrate gamepad 1
        // gamepad1.rumble(500);
    }

    /**
     * Display current status on telemetry
     */
    private void displayStatus() {
        // Format timer as MM:SS
        int totalSeconds = (int) gameTimer.seconds();
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String timerDisplay = String.format("%d:%02d", minutes, seconds);

        // Display
        telemetry.addData("=== TIMER VIBRATION ===", "");
        telemetry.addData("Timer", timerDisplay);

        // Alert indicator
        if (alertTriggered) {
            telemetry.addData("Alert", "⚠ 1:15 REACHED!");
        } else if (gameTimer.seconds() > ALERT_TIME - 10) {
            telemetry.addData("Alert", "Approaching 1:15...");
        } else {
            telemetry.addData("Alert", "Waiting for 1:15");
        }

        telemetry.addData("", "");
        telemetry.addData("Alert Time", "75 seconds (1:15)");
        telemetry.addData("Alert Status", alertTriggered ? "Triggered" : "Not triggered");
        telemetry.addData("", "");
        telemetry.addData("Controls (GP2)", "Start: Reset Timer");
        telemetry.update();
    }
}
