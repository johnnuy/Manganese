package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay13 {
	
	private static Pattern INPUT = Pattern.compile("(.*):\\sX[\\+=]([0-9]+),\\sY[\\+=]([0-9]+)");

	private static Timer<Long> timer = new Timer<>();

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> process(new ClassPathReader("day13/sample_1.txt"), 0L))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> process(new ClassPathReader("day13/input_1.txt"), 0L))));

		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> process(new ClassPathReader("day13/sample_2.txt"), 10000000000000L))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> process(new ClassPathReader("day13/input_2.txt"), 10000000000000L))));
	}

	public static Long process(Reader reader, long delta) {
		List<Game> games = new ArrayList<>();
		
		new LineHandler((line, index) -> {
			if (StringUtils.isBlank(line)) {
				return true;
			}
			Matcher m = INPUT.matcher(line);
			if (m.matches()) {
				switch(m.group(1)) {
				case "Button A":
					Game game = new Game();
					game.buttonA = Pair.of(Long.parseLong(m.group(2)), Long.parseLong(m.group(3)));
					games.add(game);
					break;
				case "Button B":
					game = games.get(games.size() - 1);
					game.buttonB = Pair.of(Long.parseLong(m.group(2)), Long.parseLong(m.group(3)));
					break;
				case "Prize":
					game = games.get(games.size() - 1);
					game.prize = Pair.of(Long.parseLong(m.group(2)) + delta, Long.parseLong(m.group(3)) + delta);
					break;
				}				
			}
			else {
				throw new IllegalArgumentException(line);
			}
			
			return true;
		}).handle(reader);
		
		AtomicLong tokens = new AtomicLong();

		for (int i=0; i<games.size(); i++) {
			Game game = games.get(i);
			Optional<Pair<Long, Long>> solution = solvePart1(game);
			if (solution.isPresent()) {
				tokens.addAndGet(solution.get().getLeft() * 3 + solution.get().getRight());
			}
		};
		
		return tokens.get();
	}
	
	private static class Game {
		
		public Pair<Long, Long> buttonA;
		public Pair<Long, Long> buttonB;
		public Pair<Long, Long> prize;
	}
	
	/**
	 * ax + by = c
	 * dx + ey = f
	 * 
	 * y = (cd - fa) / (bd - ea)
	 * x = (f - ey) / d
	 * 
	 * @param game
	 * @return
	 */
	private static Optional<Pair<Long, Long>> solvePart1(Game game) {
		long y1 = (game.prize.getLeft() * game.buttonA.getRight()) - (game.prize.getRight() * game.buttonA.getLeft());
		long y2 = (game.buttonB.getLeft() * game.buttonA.getRight()) - (game.buttonB.getRight() * game.buttonA.getLeft());
		if (y1 % y2 == 0) {
			long y = y1 / y2;
			long x = (game.prize.getRight() - (game.buttonB.getRight() * y)) / game.buttonA.getRight();
			return Optional.of(Pair.of(x, y));
		}		
		return Optional.empty();
	}
}