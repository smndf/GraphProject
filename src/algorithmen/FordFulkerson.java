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
		int floxMax = 0;
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

		ArrayList<Integer> path = DFS(residualGraph.getCapacity(), start, target);
		//while there exists a path p from s to t in the residual Network Gf
		while(path != null && !pathList.contains(path)){
			for (int i = 0; i < n; i++){
				for (int j = 0; j < n; j++){
					flow[i][j] = 0;
				}	
			}

			int minCapacityPath = Integer.MAX_VALUE;
			int capacity = 0;

			for (int i = 0; i < path.size() - 1; i++){
				capacity = residualGraph.getCapacity()[path.get(i)][path.get(i+1)];
				if (capacity < minCapacityPath){
					minCapacityPath = capacity;
				}
			}

			floxMax += minCapacityPath;

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

			path = DFS(residualGraph.getCapacity(), start, target);
		}
		System.out.println("flow Max Ford-Fulkerson : " + floxMax);
		residualGraph.drawGraph("Graph Visualisierung Ford-Fulkerson");
	}


	public ArrayList<Integer> DFS(int[][] graph, int start, int target) {
		
		int n = graph.length;
		Stack<Integer> stack = new Stack<Integer>();
		int[] isSeen = new int[n];
		int[] nbAdj = new int[n];
		int current = start;

		stack.push(start);

		for (int i=0; i<n; i++){
			isSeen[i] = 0;
			nbAdj[i] = 0;
		}
		
		isSeen[start]=1;
		
		if (start != target){

			while (!stack.isEmpty()){
				current = stack.peek();

				ArrayList<Integer> adjacents = getAdjacent(graph, current); 
				int i = nbAdj[current];
				int nextAdj;
				if (i==adjacents.size() ){
					isSeen[current]=2;
					stack.pop();
				} else {
					nextAdj = adjacents.get(i);
					if (isSeen[nextAdj]==0){
						isSeen[nextAdj] = 1;
						stack.push(nextAdj);
						if (nextAdj==target){
							return  new ArrayList<Integer>(stack);
						}
					}
					nbAdj[current] ++;
				}
			}
		}
	return null;
}	

public ArrayList<Integer> getAdjacent(int[][] graph, int node){
	ArrayList<Integer> result = new ArrayList<Integer>();
	for (int i = 0; i < graph.length; i++){
		if (graph[node][i] > 0){
			result.add(i);
		}			
	}
	return result;
}

public int getParent(int[][] graph, int node){
	for (int i = graph.length - 1; i >= 0; i--){
		if (graph[i][node] > 0){
			return i;
		}			
	}
	return -1;
}

}
