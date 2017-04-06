package com.zl.biz;

import java.io.IOException;

public interface IHistoryBiz {
	public void saveHistory(int uid, String data) throws IOException;
	public Object[] getHistory(int uid);
}
