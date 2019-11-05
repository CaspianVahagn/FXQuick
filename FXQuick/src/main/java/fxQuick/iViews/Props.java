package fxQuick.iViews;

import java.util.HashMap;
import java.util.Map;

public class Props {
     
	private Map<String,Object> dic = new HashMap<String, Object>();
	
	private Props props;
	
	public void add(String key,Object value) {
		dic.put(key, value);
	}
	
	public <T>T get(String key) {
		if(dic.containsKey(key)) {
			return (T) dic.get(key);
		}
		return null;
	}
	
	public Map<String,Object> getDic(){
		
		return dic;
	}

	public Props getProps() {
		
		return props;
	}

	public void setProps(Props props) {
		dic = props.getDic();
		this.props = props;
	}
	

}
