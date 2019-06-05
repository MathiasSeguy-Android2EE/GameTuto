/**<ul>
 * <li>GameTuto1</li>
 * <li>com.android2ee.android.tuto.game.game.one.generator</li>
 * <li>3 déc. 2011</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage but can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.android2ee.android.tuto.game.game.one.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class LabyrinthGenerator {
	private static final int North = 0, West = 1, South = 2, East = 3;
	private static int width;
	private static int height;

	public static void generateLabyrinth(int[][] level) {
		/*
		 * Cet algorithme utilise une propriété des labyrinthes parfaits précédemment énoncée telle
		 * quelle : Chaque cellule est reliée à toutes les autres, et ce, de manière unique. Il
		 * fonctionne en fusionnant progressivement des chemins depuis la simple cellule jusqu'à
		 * l'obtention d'un chemin unique, il suit donc une approche ascendante.
		 * L'algorithme associe une valeur unique à chaque cellule (leur numéro de séquence, par
		 * exemple) et part d'un labyrinthe où tous les murs sont fermés.
		 * À chaque itération, on choisit un mur à ouvrir de manière aléatoire.
		 * Lorsqu'un mur est ouvert entre deux cellules adjacentes, les deux cellules sont liées
		 * entre elles et forment un chemin. L'identifiant de la première cellule est recopié dans
		 * la seconde.
		 * À chaque fois que l'on tente d'ouvrir un mur entre deux cellules, on vérifie que ces deux
		 * cellules ont des identifiants différents.
		 * Si les identifiants sont identiques, c'est que les deux cellules sont déjà reliées et
		 * appartiennent donc au même chemin. On ne peut donc pas ouvrir le mur.
		 * Si les identifiants sont différents, le mur est ouvert, et l'identifiant de la première
		 * cellule est affecté à toutes les cellules du second chemin.
		 * Au final, on obtient un chemin unique lorsque le nombre de murs ouverts atteint mn − 1,
		 * ce qui donne 19 étapes dans l'exemple ci-contre.
		 */
		/**
		 * Ici on part du principe q"au départ les murs sont les cellules dont l'absisse ou
		 * l'ordonnée est paire
		 */
		Map<Integer, List<int[]>> valueToCells = new HashMap<Integer, List<int[]>>();
		width = level[0].length;
		height = level.length;
		
		// la definition de currentCell est
		// [0]=absisse (numero de colonne)
		// [1]=ordonné (numero de ligne)
		// [2]=identifiant de la cellule
		int[] currentCell = new int[3];
		int k = 0;
		List<int[]> currentCells;
		// remplir la liste des cellules non connectés
		for (int i = 0; i < width; i = i + 2) {
			for (int j = 0; j < height; j = j + 2) {
				currentCells = new ArrayList<int[]>();
				currentCell = new int[3];
				currentCell[0] = i;
				currentCell[1] = j;
				currentCell[2] = -1 * k;
				level[j][i] = -1 * k;
				currentCells.add(currentCell);
				valueToCells.put(new Integer(-1 * k), currentCells);
				k++;
			}
		}

		// la direction
		int direction;
		// le point choisit comme but quand on tombe le mur
		int[] target = new int[3];
		// le trou dans le mur
		int[] hole = new int[3];
		// la liste des points appartenant à la composante connexe du point target
		List<int[]> linked = new ArrayList<int[]>();
		// la liste des points appartenant à la composante connexe du point current
		List<int[]> current = new ArrayList<int[]>();
		// les valeurs del a case target et de la case courante
		int targetValue, currentValue;

		// Parcourir les points et ouvrir les murs
		for (int j = 0; j < height; j = j + 2) {
			for (int i = 0; i < width; i = i + 2) {
				target = new int[3];
				hole = new int[3];
				// choix du points
				// la valeur de la case courante
				currentValue = level[j][i];
				// currentValue = findPoint(level, notYetManaged);
				// Trouver la direction du mur à ouvrir
				direction = chooseDirection(level, i, j, target, hole);
				// si cette direction existe
				if (direction != -1) {
					// On trouve la valeur de la case target
					targetValue = level[target[1]][target[0]];
					// on modifie en fonction ValueToPoint
					linked = valueToCells.get(targetValue);
					current = valueToCells.get(currentValue);
					// ajout de tous les points de linked a courant
					// Mise à jour de level
					for (int[] cell : linked) {
						current.add(cell);
						level[cell[1]][cell[0]] = currentValue;
					}
					// la composante connexe de target est vide donc
					linked.clear();
					// on s'occupe du point
					level[hole[1]][hole[0]] = currentValue;
					current.add(hole);
				}

			}
		}
		// print(valueToCells);
	}


	/**
	 * @param level the level
	 * @param i the current absissa (column number so)
	 * @param j the current ordinate (line number so)
	 * @param target the output parameter target (choosen cell to connect)
	 * @param hole the output parameter hole (the hole in the wall)
	 * @return the direction choose (-1 no direction found)
	 */
	private static int chooseDirection(int[][] level, int i, int j, int[] target, int[] hole) {
		List<Integer> directions = new ArrayList<Integer>();
		int direction;
		direction = -1;
		// La direction est possible si le point appartient au plateau et si la valeur de la celules
		// derrière le mur est différentes de la cellule currente
		if (i - 2 >= 0) {
			// dans le plateau
			if (level[j][i - 2] != level[j][i]) {
				directions.add(West);
			}
		}
		// cas du East
		if (i + 2 < width) {
			// dans le plateau
			if (level[j][i + 2] != level[j][i]) {
				directions.add(East);
			}
		}
		// cas du Sud
		if (j + 2 < height) {
			// dans le plateau
			if (level[j + 2][i] != level[j][i]) {
				directions.add(South);
			}
		}
		// cas du nord
		if (j - 2 >= 0) {
			// dans le plateau
			if (level[j - 2][i] != level[j][i]) {
				directions.add(North);
			}
		}
		if (directions.size() != 0) {
			direction = directions.get((int) Math.floor(Math.random() * directions.size()));
			switch (direction) {

			case 0:
				// north
				target[0] = i;
				target[1] = j - 2;
				hole[0] = i;
				hole[1] = j - 1;
				break;
			case 1:
				// west
				target[0] = i - 2;
				target[1] = j;
				hole[0] = i - 1;
				hole[1] = j;
				break;
			case 2:
				// south
				target[0] = i;
				target[1] = j + 2;
				hole[0] = i;
				hole[1] = j + 1;
				break;
			case 3:
				// east
				target[0] = i + 2;
				target[1] = j;
				hole[0] = i + 1;
				hole[1] = j;
				break;
			}
		}
		return direction;
	}

	/**
	 * @param valueToCells print that map
	 */
	private static void print(Map<Integer, List<int[]>> valueToCells) {
		StringBuilder strb = new StringBuilder();

		for (Entry<Integer, List<int[]>> entry : valueToCells.entrySet()) {
			Integer cle = entry.getKey();
			strb.append("clef :" + cle + "(taille: " + valueToCells.get(cle).size() + ") ");
			for (int[] point : valueToCells.get(cle)) {
				strb.append("(" + point[1] + "," + point[0] + "); ");
			}
			strb.append("\r\n");
		}
//		Log.e("LabyrinthGenerator", strb.toString());
	}
	
	
	
}
