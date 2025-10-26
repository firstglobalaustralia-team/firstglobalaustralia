package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.ArrayList;

/**
 * Demo 19: Combined Score Counter with Timer Alert
 *
 * Knowledge Point: Event recording, undo mechanism, timer alerts, display alerts
 *
 * This demo demonstrates:
 * - Three basket score counters (A, B, C)
 * - Gamepad 2 button controls for scoring
 * - Undo mechanism with operation history stack
 * - Timer with vibration and display alert at 75 seconds
 * - Real-time "n more" display after 75 seconds
 * - Real-time score display
 *
 * Hardware Required: None (uses only gamepad)
 *
 * Controls (Gamepad 2):
 * - A Button: Basket A +1
 * - B Button: Basket B +1
 * - X Button: Basket C +1
 * - Y Button: Undo last operation (can undo multiple times)
 * - Start Button: Reset all scores and timer
 *
 * How to Demonstrate:
 * 1. Press INIT and START
 * 2. Use gamepad 2 A/B/X buttons to score points
 * 3. Watch real-time score updates
 * 4. Press Y to undo mistakes (can undo multiple times)
 * 5. Wait for 1:15 - gamepad will vibrate and show "Last 45 S"
 * 6. After 1:15, display shows "n more" for each basket
 * 7. Continue scoring - display updates in real-time
 * 8. Press Start to reset and start new round
 */
@TeleOp(name="Demo19: Combined Score Timer", group="Demo")
public class Demo19_CombinedScoreTimer extends LinearOpMode {

    // Score tracking
    private int basketA = 0;
    private int basketB = 0;
    private int basketC = 0;

    // Operation history for undo
    private ArrayList<String> operationHistory = new ArrayList<>();

    // Button state tracking (for edge detection)
    private boolean lastAState = false;
    private boolean lastBState = false;
    private boolean lastXState = false;
    private boolean lastYState = false;
    private boolean lastStartState = false;

    // Timer
    private ElapsedTime gameTimer = new ElapsedTime();
    private boolean alertTriggered = false;

    // Alert threshold (1 minute 15 seconds = 75 seconds)
    private final double ALERT_TIME = 75.0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Ready - Combined Score Counter with Timer");
        telemetry.addData("Controls", "Gamepad 2: A/B/X to score, Y to undo");
        telemetry.addData("Alert", "Vibration + Display at 1:15");
        telemetry.update();

        waitForStart();
        gameTimer.reset();

        while (opModeIsActive()) {
            // Read gamepad 2 button states
            boolean currentAState = gamepad2.a;
            boolean currentBState = gamepad2.b;
            boolean currentXState = gamepad2.x;
            boolean currentYState = gamepad2.y;
            boolean currentStartState = gamepad2.start;

            // A button: Basket A +1 (edge detection)
            if (currentAState && !lastAState) {
                basketA++;
                operationHistory.add("A");
            }
            lastAState = currentAState;

            // B button: Basket B +1 (edge detection)
            if (currentBState && !lastBState) {
                basketB++;
                operationHistory.add("B");
            }
            lastBState = currentBState;

            // X button: Basket C +1 (edge detection)
            if (currentXState && !lastXState) {
                basketC++;
                operationHistory.add("C");
            }
            lastXState = currentXState;

            // Y button: Undo last operation (edge detection)
            if (currentYState && !lastYState) {
                undoLastOperation();
            }
            lastYState = currentYState;

            // Start button: Reset all (edge detection)
            if (currentStartState && !lastStartState) {
                resetAll();
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
     * Undo the last scoring operation
     */
    private void undoLastOperation() {
        if (operationHistory.isEmpty()) {
            // No operations to undo
            telemetry.speak("No operations to undo");
            return;
        }

        // Get the last operation
        String lastOp = operationHistory.remove(operationHistory.size() - 1);

        // Reverse the operation
        switch (lastOp) {
            case "A":
                basketA--;
                break;
            case "B":
                basketB--;
                break;
            case "C":
                basketC--;
                break;
        }
    }

    /**
     * Reset all scores and timer
     */
    private void resetAll() {
        basketA = 0;
        basketB = 0;
        basketC = 0;
        operationHistory.clear();
        gameTimer.reset();
        alertTriggered = false;
    }

    /**
     * Trigger vibration and display alert at 1:15
     */
    private void triggerAlert() {
        alertTriggered = true;

        // Vibrate gamepad 2 for 500ms
        gamepad2.rumble(500);

        // Display alert message
        telemetry.speak("Last 45 seconds!");
    }

    /**
     * Calculate how many more points each basket needs
     */
    private int[] calculateMorePoints() {
        int maxScore = Math.max(Math.max(basketA, basketB), basketC);
        return new int[]{
            maxScore - basketA,  // A needs this many more
            maxScore - basketB,  // B needs this many more
            maxScore - basketC   // C needs this many more
        };
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

        // Calculate total score
        int totalScore = basketA + basketB + basketC;

        // Determine last operation
        String lastOperation = "None";
        if (!operationHistory.isEmpty()) {
            String lastOp = operationHistory.get(operationHistory.size() - 1);
            lastOperation = "Basket " + lastOp + " +1";
        }

        // Display
        telemetry.addData("=== COMBINED SCORE TIMER ===", "");
        telemetry.addData("Timer", timerDisplay);

        // Alert indicator
        if (alertTriggered) {
            telemetry.addData("Alert", "⚠ Last 45 S");
            // ASCII art for "45" similar to Demo18
            telemetry.addData("", "*    * *****");
            telemetry.addData("", "*    * *    ");
            telemetry.addData("", "***** ****");
            telemetry.addData("", "      *        *");
            telemetry.addData("", "      *        *");
            telemetry.addData("", "      * *****");
        } else if (gameTimer.seconds() > ALERT_TIME - 10) {
            telemetry.addData("Alert", "Approaching 1:15...");
        }

        telemetry.addData("", "");

        // Display scores based on alert status
        if (alertTriggered) {
            // After 75 seconds: show "n more" format
            int[] morePoints = calculateMorePoints();
            telemetry.addData("A", "%dmore", morePoints[0]);
            telemetry.addData("B", "%dmore", morePoints[1]);
            telemetry.addData("C", "%dmore", morePoints[2]);
        } else {
            // Before 75 seconds: show normal scores
            telemetry.addData("Basket A", "%d", basketA);
            telemetry.addData("Basket B", "%d", basketB);
            telemetry.addData("Basket C", "%d", basketC);
        }

        telemetry.addData("Total Score", "%d", totalScore);
        telemetry.addData("", "");
        telemetry.addData("Last Operation", lastOperation);
        telemetry.addData("Undo Available", operationHistory.isEmpty() ? "No" : "Yes (" + operationHistory.size() + " operations)");
        telemetry.addData("", "");
        telemetry.addData("Controls (GP2)", "A/B/X: Score | Y: Undo | Start: Reset");
        telemetry.update();
    }
}
