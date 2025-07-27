package org.johnnuy.manganese.aoc2015;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;

public class SolutionDay2 {

	public static void main(String[] args) throws IOException {
		System.out.println("Total Square Feet of Wrapping Paper: %d".formatted(calculateSquareFeetOfWrappingPaper(new ClassPathReader("day2/input.txt"))));
		System.out.println("Total Feet of Ribbon %d".formatted(calculateLengthOfRibbon(new ClassPathReader("day2/input.txt"))));
	} 
	
	public static int calculateSquareFeetOfWrappingPaper(Reader reader) throws IOException {
		AtomicInteger totalSquareFeet = new AtomicInteger(0);
		
		new LineHandler((line, index) -> {
			if (StringUtils.isBlank(line)) {
				return true;
			}
			String[] dimension = line.split("x");
			int length = Integer.parseInt(dimension[0]);
			int width = Integer.parseInt(dimension[1]);
			int height = Integer.parseInt(dimension[2]);
			
			int area1 = length * width;
			int area2 = width * height;
			int area3 = height * length;
			
			int smallest = Stream
					.of(area1, area2, area3)
					.reduce(Integer::min)
					.get();
			
			totalSquareFeet.addAndGet((2 * area1) + (2 * area2) + (2 * area3) + smallest);
			
			return true;
		}).handle(reader);
		
		return totalSquareFeet.get();
	}	
	
	public static int calculateLengthOfRibbon(Reader reader) throws IOException {
		AtomicInteger totalLength = new AtomicInteger(0);
		
		new LineHandler((line, index) -> {
			if (StringUtils.isBlank(line)) {
				return true;
			}
			String[] dimension = line.split("x");
			int length = Integer.parseInt(dimension[0]);
			int width = Integer.parseInt(dimension[1]);
			int height = Integer.parseInt(dimension[2]);
			
			// bow ribbon length
			totalLength.addAndGet(length * width * height);
			
			int perimeter1 = (2 * length) + (2 * width);
			int perimeter2 = (2 * width) + (2 * height);
			int perimeter3 = (2 * height) + (2 * length);
			
			int smallest = Stream
					.of(perimeter1, perimeter2, perimeter3)
					.reduce(Integer::min)
					.get();
			// smallest perimeter
			totalLength.addAndGet(smallest);			
			
			return true;
		}).handle(reader);
		
		return totalLength.get();
	}
}