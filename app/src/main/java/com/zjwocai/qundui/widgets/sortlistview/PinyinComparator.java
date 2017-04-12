package com.zjwocai.qundui.widgets.sortlistview;

import java.util.Comparator;

/**
 * 
 * @author hihiwjc
 *
 */
public class PinyinComparator implements Comparator<PinyinSortModel> {

	public int compare(PinyinSortModel o1, PinyinSortModel o2) {
		if (o1.getPinyinSortLetter().equals("@")
				|| o2.getPinyinSortLetter().equals("#")) {
			return -1;
		} else if (o1.getPinyinSortLetter().equals("#")
				|| o2.getPinyinSortLetter().equals("@")) {
			return 1;
		} else {
			return o1.getPinyinSortLetter().compareTo(o2.getPinyinSortLetter());
		}
	}

}
