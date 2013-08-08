package com.games4stuul.pinballguardian.comparator;

import java.util.Comparator;

import com.games4stuul.pinballguardian.gameobject.enemy.Enemy;

public class EnemyReserveTimeComparator implements Comparator<Enemy> {

	@Override
	public int compare(Enemy enemy1, Enemy enemy2) {
		float diff = enemy1.getReserveTime() - enemy2.getReserveTime();
		if (diff < 0) return -1;
		if (diff > 0) return 1;
		return 0;
	}

}
