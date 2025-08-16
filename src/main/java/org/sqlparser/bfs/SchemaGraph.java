package org.sqlparser.bfs;

import org.sqlparser.domain.graph.Graph;
import org.sqlparser.domain.graph.Node;
import org.sqlparser.domain.sql.Schema;
import org.sqlparser.domain.sql.Table;
import org.sqlparser.domain.sql.constraints.ForeignKey;

import java.util.*;

public class SchemaGraph {
	
	public Graph buildGraphFromSchema(Schema schema) {
		Graph graph = new Graph();
		
		for (Table table : schema.getTablesValues()) {
			graph.addNode(new Node(table));
		}
		
		for (Table table : schema.getTablesValues()) {
			Node source = new Node(table);
			for (ForeignKey fk : table.getForeignKeys()) {
				Node target = new Node(fk.getTargetTable());
				graph.addEdge(source, target);
			}
		}
		return graph;
	}
	
	public List<String> findFullRoutes(Graph graph, List<String> tableNames) {
		List<String> fullRoute = new ArrayList<>();
		List<Set<Node>> components = getConnectedComponents(graph, tableNames);
		
		for (Set<Node> component : components) {
			List<String> compNames = component.stream().map(Node::toString).toList();
			
			if (compNames.size() == 1) {
				fullRoute.add(compNames.get(0));
			} else {
				List<String> extremes = findExtremes(graph, compNames);
				if (extremes.size() == 2) {
					List<String> path = findPathBfs(graph, extremes.get(0), extremes.get(1));
					if (path != null) {
						fullRoute.addAll(path);
					} else {
						fullRoute.addAll(extremes);
					}
				} else {
					fullRoute.addAll(compNames);
				}
			}
		}
		
		return fullRoute;
	}
	
	private List<Set<Node>> getConnectedComponents(Graph graph, List<String> tableNames) {
		Set<Node> visited = new HashSet<>();
		List<Set<Node>> components = new ArrayList<>();
		
		for (String tableName : tableNames) {
			Node node = graph.findNodeByName(tableName);
			if (node != null && !visited.contains(node)) {
				Set<Node> component = new HashSet<>();
				exploreComponent(graph, node, tableNames, visited, component);
				components.add(component);
			}
		}
		return components;
	}
	
	private void exploreComponent(Graph graph, Node node, List<String> allowed,
								  Set<Node> visited, Set<Node> component) {
		Queue<Node> queue = new LinkedList<>();
		queue.add(node);
		visited.add(node);
		while (!queue.isEmpty()) {
			Node current = queue.poll();
			component.add(current);
			for (Node neighbor : graph.getNeighbors(current)) {
				if (!visited.contains(neighbor) && allowed.contains(neighbor.toString())) {
					visited.add(neighbor);
					queue.add(neighbor);
				}
			}
		}
	}
	
	public List<String> findExtremes(Graph graph, List<String> tableNames) {
		int maxDistance = -1;
		List<String> extremes = new ArrayList<>();
		
		if (tableNames == null || tableNames.size() < 2) {
			return Collections.emptyList();
		}
		
		for (int i = 0; i < tableNames.size(); i++) {
			for (int j = i + 1; j < tableNames.size(); j++) {
				String start = tableNames.get(i);
				String end = tableNames.get(j);
				
				List<String> path = findPathBfs(graph, start, end);
				if (path != null && path.size() > maxDistance) {
					maxDistance = path.size();
					extremes.clear();
					extremes.add(start);
					extremes.add(end);
				}
			}
		}
		return extremes;
	}
	
	public List<String> findPathBfs(Graph graph, String startTableName, String endTableName) {
		Node startNode = graph.findNodeByName(startTableName);
		Node endNode = graph.findNodeByName(endTableName);
		
		if (startNode == null || endNode == null) {
			return null;
		}
		
		Queue<List<Node>> queue = new LinkedList<>();
		Set<Node> visited = new HashSet<>();
		
		List<Node> initialPath = new ArrayList<>();
		initialPath.add(startNode);
		queue.add(initialPath);
		visited.add(startNode);
		
		while (!queue.isEmpty()) {
			List<Node> currentPath = queue.poll();
			Node currentNode = currentPath.get(currentPath.size() - 1);
			
			if (currentNode.equals(endNode)) {
				List<String> pathNames = new ArrayList<>();
				for (Node node : currentPath) {
					pathNames.add(node.toString());
				}
				return pathNames;
			}
			
			for (Node neighbor : graph.getNeighbors(currentNode)) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					List<Node> newPath = new ArrayList<>(currentPath);
					newPath.add(neighbor);
					queue.add(newPath);
				}
			}
		}
		
		return null;
	}
}
