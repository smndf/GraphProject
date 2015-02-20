package algorithmen;

import graph.DrawGraph;
import graph.Graph;
import graph.Position;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JFrame;

public class EdmondsKarp {

	private Graph graph;
	private ArrayList<int[]> pathList;

	public EdmondsKarp(Graph g) {
		graph = g;
		pathList = new ArrayList<int[]>();
	}

	public EdmondsKarp(Graph g, ArrayList<int[]> list) {
		graph = g;
		pathList = list;
	}

	public int edmondsKarp(int start, int target) {
		int n = graph.getKnotenPosition().size();
		int[][] flow = new int[n][n];
		int floxMax = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				flow[i][j] = 0;
			}
		}

		// while there exists a path p from s to t in the residual Network Gf
		Graph residualGraph = new Graph();
		int[][] residualGraphCapacity = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				residualGraphCapacity[i][j] = graph.getCapacity()[i][j];
			}
		}
		residualGraph.setCapacity(residualGraphCapacity);
		residualGraph.setKnotenPosition(graph.getKnotenPosition());
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				residualGraphCapacity[i][j] = graph.getCapacity()[i][j] - flow[i][j];
			}
		}
		//System.out.println("Graph :");
		ArrayList<Position> pos = new ArrayList<Position>();
		Graph g = new Graph(pos, residualGraphCapacity);
		//g.printGraph();

		int[] path = BFS(residualGraphCapacity, start, target);

		while (path != null && !pathList.contains(path)) {
			int capacityPath = getCapacityPath(residualGraphCapacity, path, target, start);
			//System.out.println("capacity path : " + capacityPath );
			floxMax += capacityPath;

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					flow[i][j] = 0;
				}
			}
			
			int v = target;
			int u;
			while (v != start) {
				u = path[v];
				//System.out.println("flow : " + u + " , " + v + " : " + capacityPath );
				flow[u][v] = /*flow[u][v] +*/ capacityPath;
				flow[v][u] = /*flow[v][u] */- capacityPath;
				residualGraphCapacity[u][v] = residualGraphCapacity[u][v] - flow[u][v];
				residualGraphCapacity[v][u] -= flow[v][u];
				v = u;
			}

			Graph residualGraphDraw = new Graph();
			int[][] residualGraphCapacityDraw = new int[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					residualGraphCapacityDraw[i][j] = residualGraph.getCapacity()[i][j];
				}
			}
			residualGraphDraw.setCapacity(residualGraphCapacityDraw);
			residualGraphDraw.setKnotenPosition(graph.getKnotenPosition());
			residualGraphDraw.drawGraph("Graph Visualisierung EdmondsKarp");
			
			pathList.add(path);
			//drawGraph(residualGraph, path);

			pos = new ArrayList<Position>();
			g = new Graph(pos, residualGraphCapacity);
			//g.printGraph();
			
			path = BFS(residualGraphCapacity, start, target);
		}
		System.out.println("flow Max Edmonds-Karp: " + floxMax);
		return floxMax;
	}

	private int getCapacityPath(int[][] residualGraphCapacity, int[] path, int target, int start) {
				int capacityPath = 0;
				int minCapa = Integer.MAX_VALUE;
				int current = target;
				while (current != start){
					capacityPath = residualGraphCapacity[path[current]][current];
					current = path[current];
					if (capacityPath < minCapa){
						minCapa = capacityPath;
					}
				}
				return minCapa;
	}

	public int[] BFS(int[][] graphCapacity, int start, int target) {
		
		int n = graphCapacity.length;
		int[] isSeen = new int[n];
		int[] d = new int[n];
		int[] path = new int[n];
		boolean exist = false;
		int u;
		LinkedList<Integer> queue = new LinkedList<Integer>();
		for (int i = 0; i < n; i++){
			isSeen[i] = 0;
			d[i] = Integer.MAX_VALUE;
			path[i] = -1; 
		}
		isSeen[start] = 1;
		d[start] = 0;

		queue.add(start);

		while (!queue.isEmpty()){
			u = queue.poll();
			for (int v : getAdjacent(graphCapacity, u)){ 
				if (isSeen[v]==0){
					isSeen[v] = 1;
					d[v] = d[u] + 1;
					path[v] = u;
					if (v == target){
						exist = true;
						break;
					}
					queue.add(v);
				}
			}
			isSeen[u] = 2;
		}
		
		if (exist){
			//System.out.print("path : " + target + " , ");
			u = target;
			while (u!=start){
				//System.out.print( path[u] + " , ");
				u = path[u];
			}
			return path;			
		}else {
			return null;
		}
	}

	public ArrayList<Integer> getAdjacent(int[][] graphCapacity, int node){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < graphCapacity.length; i++){
			if (graphCapacity[node][i] > 0){
				result.add(i);
			}			
		}
		//System.out.println("adjacent a " + node + " : " + result);
		return result;
	}
	
}
