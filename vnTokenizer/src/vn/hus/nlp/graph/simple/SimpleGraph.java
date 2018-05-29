/*******************************************************************************
 * Copyright (c) 2012 ePi Technologies.
 * 
 * This file is part of VNLP: a Natural Language Processing framework 
 * for Vietnamese.
 * 
 * VNLP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * VNLP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with VNLP.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package vn.hus.nlp.graph.simple;

import java.util.HashSet;
import java.util.LinkedList;

public class SimpleGraph {
	private int nV;
	private double[][] cost;
	private boolean[][] adj;
	private boolean directed;
	private int[] order;
	private int count;
	private double[] distance;
	private LinkedList<LinkedList<Integer>> shortestPaths;
	private LinkedList<Integer> path;

	public SimpleGraph(int n, boolean directed) {
		nV = n;
		this.directed = directed;
		this.adj = new boolean[n][n];
		this.cost = new double[n][n];
		this.order = new int[n];
		count = 0;
	}

	public int getNumberOfVertices() {
		return nV;
	}

	public void insertEdge(int u, int v, double cost) {
		this.adj[u][v] = true;
		this.cost[u][v] = cost;
		if (!directed) {
			this.adj[v][u] = true;
			this.cost[v][u] = cost;
		}
	}

	private void dfs(int u) {
		order[u] = ++count;
		for (int v = 0; v < nV; v++) {
			if (adj[u][v] && order[v] == 0) {
				dfs(v);
			}
		}
	}

	public boolean isConnected(int u, int d) {
		order[u] = ++count;
		for (int v = 0; v < nV; v++) {
			if (adj[u][v] && order[v] == 0) {
				if (v == d) {
					return true;
				}
				return isConnected(v, d);
			}
		}
		return false;
	}

	// Perform dijkstra from a vertex
	private void dijkstra(int startVertex) {
		// Initial
		HashSet<Integer> set = new HashSet<Integer>();
		distance = new double[nV];
		for (int u = 0; u < nV; u++) {
			set.add(u);
			if (adj[startVertex][u]) {
				distance[u] = cost[startVertex][u];
			}
			else {
				distance[u] = Double.MAX_VALUE;
			}
		}
		distance[startVertex] = 0;
		set.remove(startVertex);

		// Loop)
		while (set.size() != 0) {
			// Find vertex with min distance
			double min = Double.MAX_VALUE;
			int v = 0;
			for (int u : set) {
				if (distance[u] <= min) {
					min = distance[u];
					v = u;
				}
			}
			set.remove(v);
			// Update distance
			for (int u : set) {
				if (adj[v][u] && distance[u] > distance[v] + cost[v][u]) {
					distance[u] = distance[v] + cost[v][u];
				}
			}
		}
	}

	public LinkedList<LinkedList<Integer>> getAllShortestPaths(int startVertex, int endVertex) {
		dijkstra(startVertex);
		shortestPaths = new LinkedList<LinkedList<Integer>>();
		path = new LinkedList<Integer>();
		path.addFirst(endVertex);
		backTrack(startVertex, endVertex);
		return shortestPaths;
	}

	private void backTrack(int startVertex, int u) {
		if (u == startVertex) {
			shortestPaths.addLast(new LinkedList<Integer>(path));
		}
		else {
			for (int v = 0; v < nV; v++) {
				if (adj[v][u] && distance[u] == distance[v] + cost[v][u]) {
					path.addFirst(v);
					backTrack(startVertex, v);
				}
			}
		}
		path.removeFirst();
	}

	public int[] getIsolatedVertices() {
		int[] vertices = new int[nV];

		int n = 0;
		for (int u = 0; u < nV; u++) {
			// Is u isolated?
			boolean isolated = true;
			for (int v = 0; v < nV; v++)
				if (adj[v][u]) {
					isolated = false;
				}
			if (isolated) {
				vertices[n++] = u;
			}
		}
		int[] isolatedVertices = new int[n];
		for (int i = 0; i < n; i++) {
			isolatedVertices[i] = vertices[i];
		}
		return isolatedVertices;
	}

}
