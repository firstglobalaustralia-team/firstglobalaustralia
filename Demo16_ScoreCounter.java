package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.ArrayList;

/**
 * Demo 16: Score Counter with Undo
 *
 * Knowledge Point: Event recording, undo mechanism
 *
 * This demo demonstrates:
 * - Three basket score counters (A, B, C)
 * - Gamepad 2 button controls for scoring
 * - Undo mechanism with operation history stack
 * - Real-time score display
 *
 * Hardware Required: None (uses only gamepad)
 *
 * Controls (Gamepad 2):
 * - A Button: Basket A +1
 * - B Button: Basket B +1
 * - X Button: Basket C +1
 * - Y Button: Undo last operation (can undo multiple times)
 * - Start Button: Reset all scores
 *
 * How to Demonstrate:
 * 1. Press INIT and START
 * 2. Use gamepad 2 A/B/X buttons to score points
 * 3. Watch real-time score updates
 * 4. Press Y to undo mistakes (can undo multiple times)
 * 5. Press Start to reset and start new round
 */
@TeleOp(name="Demo16: Score Counter", group="Demo")
public class Demo16_ScoreCounter extends LinearOpMode {

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

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Ready - Score Counter");
        telemetry.addData("Controls", "Gamepad 2: A/B/X to score, Y to undo");
        telemetry.update();

        waitForStart();

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
     * Reset all scores
     */
    private void resetAll() {
        basketA = 0;
        basketB = 0;
        basketC = 0;
        operationHistory.clear();
    }


    /**
     * Display current status on telemetry
     */
    private void displayStatus() {
        // Calculate total score
        int totalScore = basketA + basketB + basketC;

        // Determine last operation
        String lastOperation = "None";
        if (!operationHistory.isEmpty()) {
            String lastOp = operationHistory.get(operationHistory.size() - 1);
            lastOperation = "Basket " + lastOp + " +1";
        }

        // Display
        telemetry.addData("=== SCORE COUNTER ===", "");
        telemetry.addData("", "");
        telemetry.addData("Basket A", "%d", basketA);
        telemetry.addData("Basket B", "%d", basketB);
        telemetry.addData("Basket C", "%d", basketC);
        telemetry.addData("Total Score", "%d", totalScore);
        telemetry.addData("", "");
        telemetry.addData("Last Operation", lastOperation);
        telemetry.addData("Undo Available", operationHistory.isEmpty() ? "No" : "Yes (" + operationHistory.size() + " operations)");
        telemetry.addData("", "");
        telemetry.addData("Controls (GP2)", "A/B/X: Score | Y: Undo | Start: Reset");
        telemetry.update();
    }
}
