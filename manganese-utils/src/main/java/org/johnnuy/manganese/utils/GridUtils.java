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
}
