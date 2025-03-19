package org.acme.future_callable.model;

import org.acme.future_callable.callable.Attack;

public class Entity {

	private String entityName;
	private int baseDamage;
	private int health;

	public Entity(String entityName, int baseDamage, int health) {
		this.entityName = entityName;
		this.baseDamage = baseDamage;
		this.health = health;
	}

	public Attack attack(Entity target) {
		return new Attack(entityName, baseDamage, target);
	}

	public String getEntityName() {
		return entityName;
	}

	public int getBaseDamage() {
		return baseDamage;
	}

	public int getHealth() {
		return health;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setBaseDamage(int baseDamage) {
		this.baseDamage = baseDamage;
	}

	public void setHealth(int health) {
		this.health = health;
	}

}
