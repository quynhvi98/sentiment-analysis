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

import java.util.LinkedList;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Lê Ngọc Minh
 *
 */
public class SimpleGraphTest {

    @Test
    public void simple() {
        SimpleGraph graph = new SimpleGraph(5, true);
        graph.insertEdge(0, 1, 1);
        graph.insertEdge(0, 2, 1);
        graph.insertEdge(0, 3, 1);
        graph.insertEdge(1, 4, 1);
        graph.insertEdge(2, 4, 1);
        graph.insertEdge(3, 4, 1);
        LinkedList<LinkedList<Integer>> shortestPaths = graph.getAllShortestPaths(0, 4);
        for (LinkedList<Integer> shortestPath : shortestPaths) {
            for (int i : shortestPath) {
                System.out.print(i);
                System.out.print(" ");
            }
            System.out.println();
        }
        assertTrue(graph.isConnected(0, 4));
    }
    
}
