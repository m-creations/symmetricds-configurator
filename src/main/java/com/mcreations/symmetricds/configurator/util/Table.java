package com.mcreations.symmetricds.configurator.util;

public class Table {
	public static final Table[] EMPTY_DEPENDENCIES = {};
	private String name;
	private boolean visiting;
	private boolean visited;
	private Table[] dependencies;

	public Table() {
		super();
	}

	public Table(String name, Table[] dependencies) {
		this.name = name;
		this.dependencies = dependencies;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isVisiting() {
		return visiting;
	}

	public void setVisiting(boolean visiting) {
		this.visiting = visiting;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public Table[] getDependencies() {
		return dependencies;
	}

	public void setDependencies(Table[] dependencies) {
		this.dependencies = dependencies;
	}

	@Override
	public String toString() {
		return "Table [name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if(name == null) {
			if(other.name != null)
				return false;
		} else if(!name.equals(other.name))
			return false;
		return true;
	}

}
