package org.acme.future_callable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.acme.future_callable.model.Entity;

public class App {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(4);
		Random random = new Random();

		List<Entity> heroes = new ArrayList<>();
		heroes.add(new Entity("Dregoth", 10, 200));
		heroes.add(new Entity("Drigas", 20, 100));
		heroes.add(new Entity("Quiz", 15, 150));

		Entity enemy = new Entity("Dragon", 3, 1000);

		while (enemy.getHealth() > 0 && !heroes.isEmpty()) {
			List<Callable<Double>> heroAttacks = new ArrayList<>();
			List<Callable<Double>> enemyAttacks = new ArrayList<>();

			for (Entity hero : heroes) {
				heroAttacks.add(hero.attack(enemy));
			}
			int totalDamage = processAttacks(executor, heroAttacks);
			enemy.setHealth(enemy.getHealth() - totalDamage);
			System.out.println("O " + enemy.getEntityName() + " sofreu " + totalDamage + " de dano. Ainda tem "
					+ Math.max(enemy.getHealth(), 0) + " pontos de vida.");
			if (enemy.getHealth() < 1) {
				break;
			}

			Entity targetedHero = heroes.get(random.nextInt(heroes.size()));
			enemyAttacks.add(enemy.attack(targetedHero));
			enemyAttacks.add(enemy.attack(targetedHero));

			int enemyDealtDamage = processAttacks(executor, enemyAttacks);

			targetedHero.setHealth(targetedHero.getHealth() - enemyDealtDamage);
			if (targetedHero.getHealth() <= 0) {
				System.out.println("O herói " + targetedHero.getEntityName() + " morreu.");
				heroes.remove(targetedHero);
			}

		}

		if (enemy.getHealth() > 0) {
			System.out.println("Todos os heróis morreram. O inimigo venceu!");
		} else {
			System.out.println("O " + enemy.getEntityName() + " foi eliminado!");
		}

		executor.shutdown();
	}

	private static int processAttacks(ExecutorService executor, List<Callable<Double>> attacks) {
		int totalDamage = 0;
		try {
			List<Future<Double>> results = executor.invokeAll(attacks);
			for (Future<Double> result : results) {
				try {
					if (result.isDone()) {
						totalDamage += result.get(2, TimeUnit.SECONDS);
					}
				} catch (InterruptedException | ExecutionException | TimeoutException e) {
					System.out.println("Erro no ataque: " + e.getMessage());
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return totalDamage;
	}
}
