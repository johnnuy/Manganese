package org.johnnuy.manganese.aoc2015;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.Direction;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Position;

public class SolutionDay3 {

	private static final Direction NORTH = new Direction(0, 1);
	private static final Direction EAST = new Direction(1, 0);
	private static final Direction SOUTH = new Direction(0, -1);
	private static final Direction WEST = new Direction(-1, 0);

	public static void main(String[] args) throws IOException {
		System.out.println("Total Houses Visited by Santa: %d"
				.formatted(calculateHousesVisitedBySanta(new ClassPathReader("day3/input.txt"))));
		System.out.println("Total Houses Visited by Santa and RoboSanta: %d"
				.formatted(calculateHousesVisitedBySantaAndRoboSanta(new ClassPathReader("day3/input.txt"))));
	}

	public static int calculateHousesVisitedBySanta(Reader reader) throws IOException {
		AtomicReference<Position> santaPosition = new AtomicReference<>(new Position());
		HashSet<Position> housesVisited = new HashSet<>(Set.of(santaPosition.get()));

		new LineHandler((line, index) -> {
			// start at 1, and increment over each character in our instructions
			for (int c = 1; c <= line.length(); c += 1) {
				char currentInstruction = line.charAt(c - 1);
				if (currentInstruction == '^') {
					santaPosition.set(santaPosition.get().move(NORTH));
				} else if (currentInstruction == '>') {
					santaPosition.set(santaPosition.get().move(EAST));
				} else if (currentInstruction == 'v') {
					santaPosition.set(santaPosition.get().move(SOUTH));
				} else if (currentInstruction == '<') {
					santaPosition.set(santaPosition.get().move(WEST));
				}
				housesVisited.add(santaPosition.get());
			}
			return true;
		}).handle(reader);

		return housesVisited.size();
	}

	public static int calculateHousesVisitedBySantaAndRoboSanta(Reader reader) throws IOException {
		AtomicReference<Position> santaPosition = new AtomicReference<>(new Position());
		AtomicReference<Position> roboSantaPosition = new AtomicReference<>(new Position());
		HashSet<Position> housesVisited = new HashSet<>(Set.of(santaPosition.get()));

		new LineHandler((line, index) -> {
			// start at 1, and increment over each character in our instructions
			for (int c = 1; c <= line.length(); c += 1) {
				boolean even = (c % 2) == 0;

				char currentInstruction = line.charAt(c - 1);
				if (currentInstruction == '^') {
					if (even) {
						santaPosition.set(santaPosition.get().move(NORTH));
					} else {
						roboSantaPosition.set(roboSantaPosition.get().move(NORTH));
					}
				} else if (currentInstruction == '>') {
					if (even) {
						santaPosition.set(santaPosition.get().move(EAST));
					} else {
						roboSantaPosition.set(roboSantaPosition.get().move(EAST));
					}
				} else if (currentInstruction == 'v') {
					if (even) {
						santaPosition.set(santaPosition.get().move(SOUTH));
					} else {
						roboSantaPosition.set(roboSantaPosition.get().move(SOUTH));
					}
				} else if (currentInstruction == '<') {
					if (even) {
						santaPosition.set(santaPosition.get().move(WEST));
					} else {
						roboSantaPosition.set(roboSantaPosition.get().move(WEST));
					}
				}
				if (even) {
					housesVisited.add(santaPosition.get());
				} else {
					housesVisited.add(roboSantaPosition.get());
				}
			}
			return true;
		}).handle(reader);

		return housesVisited.size();
	}
}