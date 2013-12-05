
public class Stats {
	
	public String[][] dataGrid;
	int totalObjects, maxObjectTypes = 16;

	public Stats() {
		dataGrid = new String[maxObjectTypes][7];
		totalObjects = 0;
	}
	
	private int returnPosition(String unit) {
		unit = unit.toLowerCase();
		for(int i = 0; i < 7; i++) {
			if(dataGrid[i][0].equals(unit))
					return i;
		}
		System.err.print("Could not find unit in stats file");
		return -1;
	}
	
	public void addStats(String[] line) {
		addObject(line[0]);
		addImage(line[0], line[1]);
		addKind(line[0], line[2]);
		addShield(line[0], line[3]);
		addDamage(line[0], line[4]);
		addFirepower(line[0], line[5]);
		addSpeed(line[0], line[6]);
		
	}
	
	private void addObject(String type) {
		dataGrid[totalObjects][0] = type;
		totalObjects++;
	}
	
	private void addImage(String unit, String path) {
		dataGrid[returnPosition(unit)][1] = path;
	}
	
	private void addKind(String unit, String kind) {
		dataGrid[returnPosition(unit)][2] = kind;
	}
	
	private void addShield(String unit, String shield) {
		dataGrid[returnPosition(unit)][3] = shield;
	}

	private void addDamage(String unit, String damage) {
		dataGrid[returnPosition(unit)][4] = damage;
	}
	
	private void addFirepower(String unit, String firepower) {
		dataGrid[returnPosition(unit)][5] = firepower;
	}
	
	private void addSpeed(String unit, String speed) {
		dataGrid[returnPosition(unit)][6] = speed;
	}
	
	public String getImage(String unit) {
		return dataGrid[returnPosition(unit)][1];
	}
	
	public String getKind(String unit) {
		return dataGrid[returnPosition(unit)][2];
	}
	
	public int getShield(String unit) {
		return Integer.parseInt(dataGrid[returnPosition(unit)][3]);
	}
	
	public double getDamage(String unit) {
		return Double.parseDouble(dataGrid[returnPosition(unit)][4]);
	}
	
	public int getFirepower(String unit) {
		return Integer.parseInt(dataGrid[returnPosition(unit)][5]);
	}
	
	public double getSpeed(String unit) {
		return Double.parseDouble(dataGrid[returnPosition(unit)][6]);
	}
}
