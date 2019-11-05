package fxQuick.iViews;

import java.util.HashMap;
import java.util.Map;

public class Props {
     
	private Map<String,Object> dic = new HashMap<String, Object>();
	
	public interface Handler<T>{
		public void handle(T object);
	}
	
	public class ElseHanlder {

		private boolean isElse = false;
		
		public ElseHanlder(boolean isElse) {
			this.isElse = isElse;
		}
		
		public void otherwise(Runnable run) {
			if(isElse) {
				run.run();
			}
			
		}
	}
	
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
	
	public <T>ElseHanlder ifContains(String key,Handler<T> handler){
		
		T o = get(key);
		if(o!=null) {
			handler.handle(o);
			return new ElseHanlder(false);
		}else {
			return new ElseHanlder(true);
		}
		
		
		
	}
	

}
