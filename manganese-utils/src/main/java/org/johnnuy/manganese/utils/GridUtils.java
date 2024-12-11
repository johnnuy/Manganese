package org.johnnuy.manganese.utils;

import java.util.List;

public class GridUtils {

	/**
	 * Generate a grid of chars
	 * 
	 * @param data
	 * @return
	 */
	public static char[][] gridify(List<String> data) {
		char[][] grid = new char[data.size()][];
		for (int i = 0; i < data.size(); i++) {
			String row = data.get(i);
			grid[i] = new char[row.length()];
			for (int j = 0; j < row.length(); j++) {
				grid[i][j] = row.charAt(j);
			}
		}
		return grid;
	}
	
	/**
	 * Generate a grid of ints
	 * 
	 * @param data
	 * @return
	 */
	public static int[][] gridifyInt(List<String> data) {
		int[][] grid = new int[data.size()][];
		for (int i = 0; i < data.size(); i++) {
			String row = data.get(i);
			grid[i] = new int[row.length()];
			for (int j = 0; j < row.length(); j++) {
				grid[i][j] = Integer.parseInt(Character.toString(row.charAt(j)));
			}
		}
		return grid;
	}
}
