package algorithmen;

import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JFrame;

import graph.DrawGraph;
import graph.Graph;
import graph.Position;

public class FordFulkerson {

	//private Graph graph;
	private ArrayList<ArrayList<Integer>> pathList;
	public int i = 0;

	public FordFulkerson(){
		pathList = new ArrayList<ArrayList<Integer>>();
	}

	public FordFulkerson(ArrayList<ArrayList<Integer>> list){
		pathList = list;
	}

	public void fordFulkerson(Graph graph, int start, int target){

		int n = graph.getKnotenPosition().size();
		int[][] flow = new int[n][n];
		int flowMax = 0;
		for (int i=0; i<n; i++){
			for (int j=0; j<n; j++){
				flow[i][j] = 0;
			}	
		}

		Graph residualGraph = new Graph();
		int[][] residualGraphCapacity = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				residualGraphCapacity[i][j] = graph.getCapacity()[i][j];
			}
		}
		residualGraph.setCapacity(residualGraphCapacity);
		residualGraph.setKnotenPosition(graph.getKnotenPosition());

		ArrayList<Integer> path = residualGraph.DFS(start, target);
		//while there exists a path p from s to t in the residual Network Gf
		while(path != null && !pathList.contains(path)){
			for (int i = 0; i < n; i++){
				for (int j = 0; j < n; j++){
					flow[i][j] = 0;
				}	
			}

			int minCapacityPath = residualGraph.getCapacityPath(path, target, start);//Integer.MAX_VALUE;

			flowMax += minCapacityPath;

			// for each edge (u,v) in p 
			for (int i = 0; i < path.size() - 1; i++){
				flow[path.get(i)][path.get(i+1)] = minCapacityPath;
			}

			for (int i = 0; i < n; i++){
				for (int j = 0; j < n; j++){
					residualGraph.getCapacity()[i][j] -= flow[i][j];
					residualGraph.getCapacity()[j][i] += flow[i][j];
				}	
			}

			pathList.add(path);

			path = residualGraph.DFS(start, target);
		}
		System.out.println("flow Max Ford-Fulkerson : " + flowMax);
		residualGraph.drawGraph("Graph Visualisierung Ford-Fulkerson");
	}

}
