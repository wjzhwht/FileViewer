package com.xerofox.fileviewer.ui.part;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class SegStringParser {

    private SegStringParser() {
    }

    public static List<Integer> parse(String segStr) throws RuntimeException {
        List<Integer> list = new ArrayList<>();
        if (TextUtils.isEmpty(segStr)) {
            return list;
        }
        String[] split = segStr.split(",");
        for (String aSplit : split) {
            try {
                int seg = Integer.parseInt(aSplit, 16);
                list.add(seg);
            } catch (NumberFormatException e) {
                try {
                    String[] split1 = aSplit.split("-");
                    int min = Integer.parseInt(split1[0], 16);
                    int max = Integer.parseInt(split1[1], 16);
                    for (int j = min; j <= max; j++) {
                        list.add(j);
                    }
                } catch (NumberFormatException e1) {
                    throw new RuntimeException("输入格式不合法，请重新输入！");
                }
            }
        }
        return list;
    }
}
