package lt.walrus.utils;

public class Equaler {
	
	public static boolean equals(String s1, String s2, boolean caseSensitive){
		if(null == s1 && null != s2){
			return false;
		}
		
		if(null != s1 && null == s2){
			return false;
		}
		
		if(null != s1 && null != s2){
			if(caseSensitive){
				return s1.equals(s2);
			}else{
				return s1.equalsIgnoreCase(s2);
			}
		}else{
			return true;
		}
	}

}
