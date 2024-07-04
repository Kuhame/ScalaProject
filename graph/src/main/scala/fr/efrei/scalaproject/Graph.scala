package fr.efrei.scalaproject.graph

trait Graph[V] {
  def vertices: Set[V]
  def edges: Set[(V, V)]
  def neighbors(vertex: V): Set[V]
  def addEdge(from: V, to: V): Graph[V]
  def removeEdge(from: V, to: V): Graph[V]
}
