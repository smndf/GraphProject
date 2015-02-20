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

	public int dinic(Graph graph, int start, int target){
		int n = graph.getKnotenPosition().size();
		int[][] flow = new int[n][n];
		int totalFlow = 0;
		ArrayList<Integer> paths = new ArrayList<Integer>();

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

		// graphe de niveau
		Graph acyclicSubgraph;

		while (BFS((acyclicSubgraph = getAcyclicSubgraph(residualGraph, start, target)),start,target) != null){
			//System.out.println("nouveau acyclic graph :");
			//acyclicSubgraph.printGraph();

			int[] path;
			while ((path = BFS(acyclicSubgraph,start,target)) != null){
				//acyclicSubgraph.printGraph();

				// on calcule la longueur du path
				int u = target;
				int pathLength = 0;
				while (path[u]!=-1){
					pathLength++;
					u = path[u];
				}

				// on trouve le flux max du path
				int minCapacityPath = Integer.MAX_VALUE;
				int capacity = 0;
				u = target;
				for (int i = 0; i < pathLength; i++){
					//System.out.println("u : " + u + "    path[u] : " + path[u]);
					capacity = acyclicSubgraph.getCapacity()[path[u]][u];
					u = path[u];
					if (capacity < minCapacityPath){
						minCapacityPath = capacity;
					}
				}
				//System.out.println("flux :" + minCapacityPath);
				totalFlow += minCapacityPath;
				
				// on met à jour les graphes
				u = target;
				for (int i = 0; i < pathLength; i++){
					residualGraph.getCapacity()[path[u]][u] -= minCapacityPath;
					residualGraph.getCapacity()[u][path[u]] += minCapacityPath;
					acyclicSubgraph.getCapacity()[path[u]][u] -= minCapacityPath;
					acyclicSubgraph.getCapacity()[u][path[u]] += minCapacityPath;
					u = path[u];
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
				residualGraphDraw.drawGraph("Graph Visualisierung Dinic");

			}

		}
		System.out.println("flow Max Dinic : " + totalFlow);
		return totalFlow;
	}

	// graphe de niveau (= graphe de départ sans les arcs qui créent des chemins plus longs que ceux existant)
	private Graph getAcyclicSubgraph(Graph graph, int start, int target) {
		int n = graph.getKnotenPosition().size();
		int[][] acyclicGraphCapacity = new int[n][n];

		Graph acyclicGraph = new Graph();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				acyclicGraphCapacity[i][j] = graph.getCapacity()[i][j];
			}
		}
		acyclicGraph.setCapacity(acyclicGraphCapacity);
		acyclicGraph.setKnotenPosition(graph.getKnotenPosition());

		int[] isSeen = new int[n];
		int[] d = new int[n];
		int[] path = new int[n];
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
			//System.out.println("file : " + queue.toString());
			u = queue.poll();
			for (int v : graph.getAdjacent(u)){ 
				if (d[v] >= d[u] + 1){
					d[v] = d[u] + 1;
					path[v] = u;
					/*if (v == target){
							if (d[v] == bestDistance){
								pathlist.add(path);
								int t = target;
								while (t!=start){
									t = path[t];
								}
							}
							else if (d[v] < bestDistance){
								pathlist.clear();
								pathlist.add(path);
								int t = target;
								while (t!=start){
									t = path[t];
								}
								bestDistance = d[v];
							}
						} else {
							queue.add(v);
						}*/
					queue.add(v);
				} else {
					// on enleve l'arc
					acyclicGraph.getCapacity()[u][v] = 0;
					//System.out.println("arc "+u+","+v+" enlevé");
				}
			}
		}
		//System.out.println("fin getAcyclicSubgraph");
		return acyclicGraph;
	}
	
	public int[] BFS(Graph graph, int start, int target) {

		int n = graph.getCapacity().length;
		int[][] graphCapacity = new int[n][n];
		graphCapacity = graph.getCapacity();
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
			u = target;
			//System.out.print("path : " + target + " , ");
			while (u!=start){
				//System.out.print( path[u] + " , ");
				u = path[u];
			}
			//System.out.println("");
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
		return result;
	}

}
