package algorithmen;

import graph.Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Dinic {

	private ArrayList<ArrayList<Integer>> pathList;

	public Dinic(){
		pathList = new ArrayList<ArrayList<Integer>>();
	}

	public Dinic(ArrayList<ArrayList<Integer>> list){
		pathList = list;
	}

	public void dinic(Graph graph, int start, int target){
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

		Graph acyclicSubgraph = getAcyclicSubgraph(graph, start, target);
		ArrayList<Integer> path = BlockingFlow(acyclicSubgraph.getCapacity(), start, target);
		
	}
	
	
	private Graph getAcyclicSubgraph(Graph graph, int start, int target) {
		ArrayList<int[]> pathlist = new ArrayList<int[]>();
		int[] path = BFS(graph.getCapacity(), start, target);
		
		while (path != null){
			pathlist.add(path);
			path = BFS(graph.getCapacity(), start, target);
		}
		return graph;
	}

	
	public int[] BFS(int[][] graph, int start, int target) {
		
		int n = graph.length;
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
			for (int v : getAdjacent(graph, u)){ 
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
			u = target;
			while (u!=start){
				u = path[u];
			}
			return path;			
		}else {
			return null;
		}
	}
	
	public ArrayList<Integer> BlockingFlow(int[][] graph, int start, int target) {
		int n = graph.length;
		Stack<Integer> stack = new Stack<Integer>();
		int[] isSeen = new int[n];
		int[] nbAdj = new int[n];
		int current = start;
		int[][] newGraph;

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
					newGraph = removeIncident(graph, current);
					graph = newGraph;
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

	private int[][] removeIncident(int[][] graph, int current) {
		int n = graph.length;
		int[][] newGraph = new int[n-1][n-1];
		for (int i = 0; i < graph.length; i++){
			for (int j = 0; j < graph.length; j++){
				if (i==current || j==current)
					newGraph[i][j] = 0;				
			}
		}
		return newGraph;
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

}
