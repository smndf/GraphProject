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
		Graph acyclicSubgraph = getAcyclicSubgraph(residualGraph, start, target);

		while (BFS(acyclicSubgraph.getCapacity(),start,target) != null){
			// on ajoute les nouveaux chemins
			paths.addAll(BlockingFlow(acyclicSubgraph.getCapacity(), start, target));
			acyclicSubgraph = getAcyclicSubgraph(residualGraph, start, target);
		}
	}

	// graphe de niveau (= graphe de départ sans les arcs qui créent des chemins plus longs que ceux existant)
	// on retire les valeurs des chemins trouvés pour en trouver d'autres avec bfs, 
	// surement pas comme ça qu'il faut faire
	// il faudrait créer une nouvelle BFS qui renvoie pathlist contenant tous les chemins de longueur minimale
	// => BFSDinic
	private Graph getAcyclicSubgraph(Graph graph, int start, int target) {
		int n = graph.getKnotenPosition().size();
		ArrayList<int[]> pathlist = new ArrayList<int[]>();
		int[][] acyclicGraphCapacity = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				acyclicGraphCapacity[i][j] = 0;
			}
		}
		Graph acyclicGraph = new Graph(graph.getKnotenPosition(),acyclicGraphCapacity);
		System.out.println("acyclicGraph :");
		acyclicGraph.printGraph();

		pathlist = DFSDinic(graph.getCapacity(), start, target);
		/*int[] path = BFS(graph.getCapacity(), start, target);

		while (path != null){
			pathlist.add(path);

			// MAJ graph avant de relancer bfs
			int capacityPath = getCapacityPath(graph, path, target, start);
			int v = target;
			int u;
			while (v != start) {
				u = path[v];
				System.out.println("flow : " + u + " , " + v + " : " + capacityPath );
				graph.getCapacity()[u][v] -= capacityPath;
				graph.getCapacity()[v][u] += capacityPath;
				acyclicGraph.getCapacity()[u][v] += capacityPath;
				v = u;
			}
			System.out.println("acyclicGraph :");
			acyclicGraph.printGraph();

			path = BFS(graph.getCapacity(), start, target);
		}*/
		return acyclicGraph;
	}


	public ArrayList<int[]> DFSDinic(int[][] graph, int start, int target) {

		ArrayList<int[]> pathlist = new ArrayList<int[]>();
		int n = graph.length;
		//int[] isSeen = new int[n];
		int[] d = new int[n];
		int[] path = new int[n];
		int bestDistance = Integer.MAX_VALUE;
		//boolean exist = false;
		int u;
		//LinkedList<Integer> queue = new LinkedList<Integer>();
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < n; i++){
			//isSeen[i] = 0;
			d[i] = Integer.MAX_VALUE;
			path[i] = -1; 
		}
		//isSeen[start] = 1;
		d[start] = 0;

		stack.push(start);
		System.out.println("uktifuolyjk");
		while (!stack.isEmpty()){
			u = stack.pop();
			for (int v : getAdjacent(graph, u)){ 
				System.out.println("bestDistance = " + bestDistance);
				if (d[v] > d[u] + 1){
					System.out.println("gfc");
					d[v] = d[u] + 1;
					path[v] = u;
					if (v == target){
						//exist = true;
						System.out.println("d[v] + " + d[v] + " , bestdistance :" + bestDistance);
						if (d[v] == bestDistance){
							pathlist.add(path);
							int t = target;
							System.out.print("path : " + target + " , ");
							while (t!=start){
								System.out.print( path[t] + " , ");
								t = path[t];
							}
							System.out.println("");
						}
						else if (d[v] < bestDistance){
							pathlist.clear();
							pathlist.add(path);
							int t = target;
							System.out.print("path : " + target + " , ");
							while (t!=start){
								System.out.print( path[t] + " , ");
								t = path[t];
							}
							System.out.println("");
							bestDistance = d[v];
						}
						
						// quand on a trouvé un chemin, on vide la pile et on recommence une nouvelle recherche
						stack.clear();
						stack.push(start);
					} else {
						stack.push(v);
					}
				}	
			}
		}
		return pathlist;
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

	private int getCapacityPath(Graph graph, int[] path, int target, int start) {
		int capacityPath = 0;
		int minCapa = Integer.MAX_VALUE;
		int current = target;
		while (current != start){
			capacityPath = graph.getCapacity()[path[current]][current];
			current = path[current];
			if (capacityPath < minCapa){
				minCapa = capacityPath;
			}
		}
		return minCapa;
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
