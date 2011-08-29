package net.mauhiz.contest.codejam.qual2011;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mauhiz.contest.codejam.CodejamLineChunkSolver;

public class BotTrust extends CodejamLineChunkSolver {
	static enum Robot {
		B, O;
		public Robot other() {
			return this == B ? O : B;
		}
	}

	static enum Action {
		STAY, MOVE, PUSH;
	}

	static class Step {
		Robot robot;
		int buttonIndex;
	}

	public static void main(String... args) throws IOException {
		new BotTrust().run(args);
	}
	@Override
	protected String doJamProblem(String[] chunks) {
		List<Step> steps = readAll(chunks);
		int time = 0;
		Map<Robot, Integer> pos = new HashMap<Robot, Integer>();
		pos.put(Robot.O, 1);
		pos.put(Robot.B, 1);

		for (int stepIndex = 0; stepIndex < steps.size(); stepIndex++) {
			// find out what steps to do next
			Step next = steps.get(stepIndex);
			Step nextOther = null;
			for (int otherStepIndex = stepIndex; otherStepIndex < steps.size(); otherStepIndex++) {
				Step candidate = steps.get(otherStepIndex);
				if (candidate.robot != next.robot) {
					nextOther = candidate;
					break;
				}
			}

			while (true) {

				time++;
				Action main;
				Action other;
				// actually do the steps
				int robotPos = pos.get(next.robot);

				if (next.buttonIndex == robotPos) {
					// push
					main = Action.PUSH;

				} else {
					// move
					pos.put(next.robot, (int) (robotPos + Math.signum(next.buttonIndex - robotPos)));
					main = Action.MOVE;
				}
				if (nextOther != null) {
					int otherRobotPos = pos.get(next.robot.other());
					if (nextOther.buttonIndex == otherRobotPos) {
						// stay
						other = Action.STAY;
					} else {
						// move
						pos.put(next.robot.other(),
								(int) (otherRobotPos + Math.signum(nextOther.buttonIndex - otherRobotPos)));
						other = Action.MOVE;
					}
				}
				if (main == Action.PUSH) {
					break;
				}
			}
		}
		return Integer.toString(time);
	}

	private List<Step> readAll(String[] chunks) {

		int chunkIndex = 0;
		int numButtons = Integer.parseInt(chunks[chunkIndex++]);
		List<Step> steps = new ArrayList<Step>(numButtons);
		while (chunkIndex < chunks.length) {
			Step step = new Step();
			step.robot = Robot.valueOf(chunks[chunkIndex++]);
			step.buttonIndex = Integer.parseInt(chunks[chunkIndex++]);
			steps.add(step);
		}
		return steps;
	}

	@Override
	public String getName() {
		return "bottrust";
	}

}
