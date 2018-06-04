package vn.com.epi.gate.rule;

import vn.com.epi.gate.OrthoMatcherRule;

public class CallByLastName implements OrthoMatcherRule {

	@Override
	public boolean value(String s1, String s2) {
		String[] chunks1 = s1.split(" +");
		String[] chunks2 = s2.split(" +");
		if (chunks1[0].equals(chunks2[0])) {
			for (int i = 1; i < chunks2.length; i++) {
				if (Character.isUpperCase(chunks2[i].charAt(0))) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Gọi theo họ, vd: Hồ chủ tịch";
	}
	
}
