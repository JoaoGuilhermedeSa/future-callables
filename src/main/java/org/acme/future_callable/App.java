package org.acme.future_callable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.acme.future_callable.model.Entity;

public class App {

	public static void main(String[] args) {
		boolean heroesLost = false;
		ExecutorService executor = Executors.newFixedThreadPool(4);
		List<Future<Double>> attackResults;
		List<Future<Double>> enemyResults;
		Random random = new Random();

		List<Entity> heroes = new ArrayList<>();
		heroes.add(new Entity("Dregoth", 10, 200));
		heroes.add(new Entity("Drigas", 20, 100));
		heroes.add(new Entity("Quiz", 15, 150));

		Entity enemy = new Entity("Dragon", 3, 1000);

		while (enemy.getHealth() > 0 || heroes.size() > 0) {
			attackResults = new ArrayList<>();
			enemyResults = new ArrayList<>();

			for (Entity hero : heroes) {
				attackResults.add(executor.submit(hero.attack(enemy)));
			}

			if (heroes.size() == 0) {
				heroesLost = true;
				System.out.println("Todos os heróis morreram.");
				break;
			}

			Entity targetedHero = heroes.get(random.nextInt(heroes.size()));

			enemyResults.add(executor.submit(enemy.attack(targetedHero)));
			enemyResults.add(executor.submit(enemy.attack(targetedHero)));

			int totalDamage = 0;
			for (Future<Double> result : attackResults) {
				try {
					totalDamage += result.get();
				} catch (InterruptedException | ExecutionException e) {
					System.out.println(e.getCause().getMessage());
				}
			}

			int enemyDealtDamage = 0;

			for (Future<Double> result : enemyResults) {
				try {
					enemyDealtDamage += result.get();
				} catch (InterruptedException | ExecutionException e) {
					System.out.println(e.getCause().getMessage());
				}
			}

			targetedHero.setHealth(targetedHero.getHealth() - enemyDealtDamage);
			if (targetedHero.getHealth() <= 0) {
				System.out.println("O herói " + targetedHero.getEntityName() + " morreu.");
				heroes.remove(targetedHero);
			}

			enemy.setHealth(enemy.getHealth() - totalDamage);

			System.out.println("O " + enemy.getEntityName() + " sofreu " + totalDamage + " de dano. Ainda permanecem "
					+ Math.max(enemy.getHealth(), 0) + " pontos de vida.");

		}
		if (heroesLost && enemy.getHealth() < 0) {
			System.out.println("Stalemate.");
		} else
		if (!heroesLost) {
			System.out.println("O " + enemy.getEntityName() + " foi eliminado.");
		}
		executor.shutdown();
	}
}
