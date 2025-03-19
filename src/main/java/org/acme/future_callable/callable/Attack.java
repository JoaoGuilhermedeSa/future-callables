package org.acme.future_callable.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import org.acme.future_callable.model.Entity;

public class Attack implements Callable<Double> {

	private String character;
	private double damage;
	private Entity target;

	public Attack(String character, int baseDamage, Entity target) {
		this.character = character;
		this.damage = baseDamage + ThreadLocalRandom.current().nextInt(5) * 5;
		this.target = target;
	}

	@Override
	public Double call() {
		if (ThreadLocalRandom.current().nextInt(10) <= 1) {
			throw new RuntimeException("Erro ao atacar! " + character + " errou o ataque à " + target.getEntityName());
		}
		System.out.println(character + " atacou causando " + damage + " de dano à " + target.getEntityName() + ".");
		return damage;
	}
}
