package cub.sdd.oneclick;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DataCache {

	private List<List<String>> list;
	
	public DataCache() {
		this.list = new ArrayList<List<String>>();
	}

	public List<List<String>> getList() {
		return list;
	}

	public void setList(List<List<String>> list) {
		this.list = list;
	}

	public void clearCache() {
		this.list = new ArrayList<List<String>>();;
	}

}
