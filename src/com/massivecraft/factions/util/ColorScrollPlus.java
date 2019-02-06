package com.massivecraft.factions.util;

public class ColorScrollPlus {
	
	private int position;
	private String str;
	private String colorBefore;
	private String colorAfter;
	private String colorMid;
	private String textColor;

	public ColorScrollPlus(String textColor, String str, String colorMid, String colorBefore, String colorAfter) {
		this.str = str;
		this.colorMid = colorMid;
		this.colorBefore = colorBefore;
		this.colorAfter = colorAfter;
		this.textColor = textColor;
		this.position = -1;
	}

	public String next() {

		if (position >= str.length()) {
			String one = str.substring(position - 1, str.length() - 1);
			String two = colorMid + one;
			String fin = textColor + str.substring(0, str.length() - 1) + colorBefore + str.substring(str.length() - 1, str.length()) + two;
			position = -1;
			return fin;
		}

		if (position <= -1) {
			position++;
			return colorBefore + str.substring(0, 1) + textColor + str.substring(1, str.length());
		}

		if (position == 0) {
			String one = str.substring(0, 1);
			String two = colorMid + one;
			String fin = two + colorAfter + str.substring(1, 2) + textColor + str.substring(2, str.length());
			position++;
			return fin;
		}

		if (position >= 1) {
			String one = str.substring(0, position);
			String two = str.substring(position + 1, str.length());
			String three = colorMid + str.substring(position, position + 1);

			String fin = null;

			int m = one.length();
			int l = two.length();

			String first = m <= 1 ? 
					colorBefore + one
					: one.substring(0, one.length() - 1) + colorBefore + one.substring(one.length() - 1, one.length());

			String second = l <= 1 ? 
					colorAfter + two
					: colorAfter + two.substring(0, 1) + textColor + two.substring(1, two.length());

			fin = textColor + first + three + second;

			position++;
			return fin;
		}
		return null;
	}

}